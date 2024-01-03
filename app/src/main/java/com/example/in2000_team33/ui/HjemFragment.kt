package com.example.in2000_team33.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.in2000_team33.R
import com.example.in2000_team33.api.ForecastMapAPI
import com.example.in2000_team33.util.sjekkPosisjonsinstillinger
import com.example.in2000_team33.util.sjekkPosisjonstillatelse
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HjemFragment: Fragment(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener,
    GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,
SearchView.OnQueryTextListener{
    private lateinit var kart: GoogleMap
    private lateinit var minPosisjonKnapp: FloatingActionButton
    private var posisjonOppdateres = false
    private var sistePosisjon: Location? = null
    private lateinit var clusterManager: ClusterManager<MarkerItem>

    var nattmodus = false
    var temperature = false

    //Kobler kartet til en viewmodel.
    private val viewModel: HjemViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val shared = requireContext().getSharedPreferences("shared", Context.MODE_PRIVATE)
        nattmodus = shared.getBoolean("night", false)
        temperature = shared.getBoolean("temperature", false)

        if (nattmodus){
            return inflater.inflate(R.layout.main_fragment_dark, container, false)
        }
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Gi brukeren et tips om hvordan man legger til et badested.
        if (viewModel.visTips) {
            Toast.makeText(requireContext(), R.string.tipsNyttBadested, Toast.LENGTH_LONG).show()
            viewModel.visTips = false
        }

        val badeAdapter = ListAdapter(this)

        val viewpager = view.findViewById<ViewPager2>(R.id.pager)
        viewpager.adapter = badeAdapter

        val bottomSheet = view.findViewById<LinearLayout>(R.id.bottomSheet)
        val tabLayout = view.findViewById<TabLayout>(R.id.tablayout)


        TabLayoutMediator(tabLayout, viewpager) {tab, position ->
            if (position == 0) {
                tab.text = "Varmeste"
            } else if (position == 1) {
                tab.text = "Nærmeste"
            }
        }.attach()


        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                /*if (newState == STATE_EXPANDED)
                    viewModel.oppdaterVarslinger()*/
            }
        })

        //Henter kartkomponentet og laster inn kartet.
        val kartFragment = childFragmentManager.findFragmentById(R.id.kart) as SupportMapFragment?
        kartFragment?.getMapAsync(this)

        //Gi posisjonsknappen en lytter
        minPosisjonKnapp = view.findViewById(R.id.posisjonsKnapp)
        minPosisjonKnapp.setOnClickListener { onLocationButtonClick() }
    }

    //Callback som kjøres når kartet har lastet inn.
    override fun onMapReady(googleMap: GoogleMap) {
        kart = googleMap
        if (nattmodus){
            kart.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.dark_mode))
        } else {
            kart.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.normal_mode))
        }
        viewModel.oppdaterAvstand(kart.cameraPosition.target)

        //Oppretter en clustermanager for markører
        clusterManager = ClusterManager(context, kart)

        //Setter mimimun klyngestørrelse til 3.
        clusterManager.renderer = DefaultClusterRenderer(context, kart, clusterManager)
                .also { it.minClusterSize = 3;}

        //Håndter klikk på en cluster.
        clusterManager.setOnClusterClickListener{
            kart.animateCamera(CameraUpdateFactory.newLatLng(it.position))
            true
        }

        //Håndter klikk på en markør
        clusterManager.setOnClusterItemClickListener {
            //Vis informasjon om badestedet.
            val intent = Intent(requireContext(), BadestedActivity::class.java)
            intent.putExtra("stedId", it.id)
            requireContext().startActivity(intent)

            true
        }

        //Lytt etter kamerabevegelser
        kart.setOnCameraIdleListener(this)
        kart.setOnCameraMoveStartedListener(this)

        //Lytt etter klikk på kartet
        kart.setOnMapClickListener(this)
        kart.setOnMapLongClickListener(this)

        //Zoom kameraet
        kart.moveCamera(CameraUpdateFactory.zoomTo(15f))

        //Starter observasjon av livedata fra KartViewModel
        observerMarkorer()

        if (viewModel.hentKameraPosisjon() != null) {
            //Bruk eksisterende kameraposisjon
            startPosisjonsoppdateringer()
            kart.moveCamera(CameraUpdateFactory.newCameraPosition(viewModel.hentKameraPosisjon()))
        }
        else if (sjekkPosisjonstillatelse()) {
            sjekkInstillinger()
        }
        else {
            //Vis standardsted
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(61.2, 8.8), 5.5f))
        }
    }

    /**
     * Starter observasjon av markørlivedata fra KartViewModel
     */
    private fun observerMarkorer() {
        //Observer markørlisten
        viewModel.hentMarkorer().observe(viewLifecycleOwner) {

            //Oppdater avstanden til hvert badested når
            viewModel.oppdaterAvstand(kart.cameraPosition.target)

            //Fjern eksisterende markører og legg til de oppdaterte markørene.
            clusterManager.clearItems()
            for (item in it)
                clusterManager.addItem(item)

            clusterManager.cluster()
        }
    }

    /**
     * Starter observasjon av posisjonslivedata fra KartViewModel
     */
    @SuppressLint("MissingPermission")
    private fun observerPosisjon() {
        if (!sjekkPosisjonstillatelse()) return
        viewModel.hentPosisjon().observe(viewLifecycleOwner) {
            if (sistePosisjon == null) {
                //Hopp rett til første posisjon uten animasjon
                val posisjon = LatLng(it.latitude, it.longitude)
                kart.moveCamera(CameraUpdateFactory.newLatLngZoom(posisjon, 13f))
            } else if (folgerPosisjon)
                visPosisjon(it)

            sistePosisjon = it
        }
    }

    /**
     * Sjekker at systeminstillingene er optimale for å finne brukerens posisjon
     */
    private fun sjekkInstillinger() {
        //Sjekk at systemlokasjon er slått på, og at lokasjonsmodusen er satt til "High accuracy"
        sjekkPosisjonsinstillinger { exception ->
            if (exception == null)
                startPosisjonsoppdateringer()
            else
                if (exception is ResolvableApiException)
                    systeminstillingsrequest.launch(IntentSenderRequest.Builder(exception.resolution.intentSender).build())
        }
    }

    @SuppressLint("MissingPermission")
    private fun startPosisjonsoppdateringer() {
        if (sjekkPosisjonstillatelse()) {
            posisjonOppdateres = true
            kart.isMyLocationEnabled = true
            kart.uiSettings.isMyLocationButtonEnabled = false

            //Starter posisjonsoppdateringer
            observerPosisjon()
        }
    }

    //Håndterer klikk på lokasjonsknappen
    private fun onLocationButtonClick() {
        //Hvis posisjonen allerede følges, stopp å følge posisjonen.
        if (folgerPosisjon) {
            folgerPosisjon = false
            return
        }

        if (sjekkPosisjonstillatelse()) {
            //Hvis posisjonen oppdateres følg minposisjon
            if (posisjonOppdateres) {
                visPosisjon(sistePosisjon)
                folgerPosisjon = true
            }

            else {
                //Sjekk at instillingene er optimale og start posisjonsoppdateringer
                sjekkInstillinger()
            }
        }
        else {
            //Send en forespørsel om posisjonstillatelse
            posisjonsrequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    //Viser brukerens posisjon i kartet
    private fun visPosisjon(location: Location?) {
        if (location == null) return
        val posisjon = LatLng(location.latitude, location.longitude)
        kart.animateCamera(CameraUpdateFactory.newLatLng(posisjon))

    }

    override fun onCameraMoveStarted(grunn: Int) {
        //Hvis brukeren flytter kameraet, stopp å følge brukerens posisjon
        if (grunn == 1) {
            folgerPosisjon = false
        }
    }

    override fun onCameraIdle() {
        //Lagre siste kameraposisjon i ViewModel
        viewModel.settKameraPosisjon(kart.cameraPosition)
        viewModel.oppdaterAvstand(kart.cameraPosition.target)
        clusterManager.onCameraIdle()
    }

    override fun onMapClick(posisjon: LatLng) {
        Log.d("onMapClick", posisjon.toString())
    }

    override fun onMapLongClick(posisjon: LatLng) {
        CoroutineScope(Dispatchers.IO).launch {
            val oceanForecast = Retrofit.Builder()
                .baseUrl("https://in2000-apiproxy.ifi.uio.no/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ForecastMapAPI::class.java)

            try {
                val forecast = oceanForecast.fetchForecastOcean(posisjon.latitude.toString(), posisjon.longitude.toString())
                if (forecast.properties.timeseries.isEmpty()) {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(requireContext(), "Ugyldig plassering!", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }


                CoroutineScope(Dispatchers.Main).launch {
                    val dialog = NyttBadestedDialogFragment()
                    dialog.show(parentFragmentManager, "nyttBadested")
                    viewModel.klikkPosisjon = posisjon
                }
            }
            catch (err: HttpException) {
                Log.d("onMapLongClick", "Klarte ikke å hente forecast.")
            }
        }
    }

    //Håndter resultatet av lokasjonsforespørselen
    val posisjonsrequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { godkjent ->
        if (godkjent) {
            folgerPosisjon = true
            sjekkInstillinger()
        }
    }

    //Håndter resultatet av forespørselen om å endre systeminstillinger.
    val systeminstillingsrequest = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { resultat ->
        if (resultat.resultCode == Activity.RESULT_OK) {
            folgerPosisjon = true
            sjekkInstillinger()
        }
    }

    private var folgerPosisjon = false
        set(folger) {
            if (folger)
                minPosisjonKnapp.imageTintList = ColorStateList.valueOf(Color.parseColor("#0096FF"))
            else
                minPosisjonKnapp.imageTintList = ColorStateList.valueOf(Color.BLACK)
            field = folger
        }

    override fun onQueryTextSubmit(query: String?): Boolean {
       query?.let {  viewModel.oppdaterSok(it) }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {  viewModel.oppdaterSok(it) }
        return true
    }
}