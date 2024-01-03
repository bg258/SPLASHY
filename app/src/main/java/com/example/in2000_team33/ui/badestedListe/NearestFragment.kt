package com.example.in2000_team33.ui.badestedListe
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.in2000_team33.ui.BadeAdapter
import com.example.in2000_team33.R
import com.example.in2000_team33.ui.HjemViewModel


class NearestFragment: Fragment(){
    private var recyclerView: RecyclerView? = null
    private var listAdapter: BadeAdapter? = null
    private val viewModel: HjemViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nearest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val instillinger = requireContext().getSharedPreferences("shared", Context.MODE_PRIVATE)
        val nattmodus = instillinger.getBoolean("night", false)
        val fahrenheit = instillinger.getBoolean("temperature", false)

        viewModel.badesteder.observe(viewLifecycleOwner, { badeliste ->
            listAdapter?.badeliste = badeliste.sortedBy { it.avstand }
            listAdapter?.notifyDataSetChanged()
        })

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            listAdapter = BadeAdapter(nattmodus, fahrenheit)
            recyclerView!!.adapter = listAdapter
            //adapter.changeSortType(Order.TempOrder())
            Log.d(null,"Nearest apply")
        }
    }
}


