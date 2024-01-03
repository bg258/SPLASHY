package com.example.in2000_team33.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.in2000_team33.MainActivity
import com.example.in2000_team33.R


@SuppressLint("WrongViewCast")
class Settings : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode", "ResourceAsColor", "ResourceType")
    var checked : Boolean = false
    var temp: Boolean = false
    var languageChecked = false
    private lateinit var startText: TextView
    private lateinit var startName: TextView

    private lateinit var nightModeText: TextView
    private lateinit var clearCacheText: TextView
    private lateinit var temperatureText: TextView
    private lateinit var textSizeText: TextView
    private lateinit var languageText: TextView
    private lateinit var aboutText: TextView
    private lateinit var notificationsText: TextView

    var smallSize = 16F
    var bigSize = 24F



    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        // BACK - BUTTON
        val back = findViewById<ImageButton>(R.id.back_button)
        back.setOnClickListener{
            saveData()
            val intent = Intent(this, MainActivity::class.java)
            setResult(RESULT_OK, intent)
            startActivity(intent)
            finish()
        }

        val night_mode = findViewById<LinearLayout>(R.id.night_mode_background)
        val cache = findViewById<LinearLayout>(R.id.clearCache_background)
        val temperature = findViewById<LinearLayout>(R.id.temperature_background)
        val size = findViewById<LinearLayout>(R.id.text_size)
        val language = findViewById<LinearLayout>(R.id.language_background)
        val info = findViewById<LinearLayout>(R.id.info_background)
        val notifications = findViewById<LinearLayout>(R.id.notifications_background)


        // STRENGER
        // ALLE VARIABLER SOM VI KOMMER TIL Å BRUKE GJENNOM HELE AKTIVITETEN.
        startText = findViewById(R.id.settings_text)
        startName = findViewById(R.id.settings_name)

        nightModeText = findViewById(R.id.night_mode_text)
        clearCacheText = findViewById(R.id.cache_text)
        temperatureText = findViewById(R.id.temp_text)
        textSizeText = findViewById(R.id.text_size_text)
        languageText = findViewById(R.id.language_text)
        aboutText = findViewById(R.id.info_text)
        notificationsText = findViewById(R.id.notifications_text)


        // SWITCH - FUNKSJONER
        val night = findViewById<Switch>(R.id.night_mode)
        val language_switch = findViewById<Switch>(R.id.language_switch)
        val temperature_switch = findViewById<Switch>(R.id.temperature_switch)

        // CardViews
        val cacheView= findViewById<CardView>(R.id.cache)
        val notification = findViewById<Switch>(R.id.notifications)

        val switchMode = intent.extras!!.getBoolean("night")
        val tempMode = intent.extras!!.getBoolean("temperature")
        val background = findViewById<ScrollView>(R.id.background_settings)

        if (switchMode){
            night.isChecked = true
            // Setter bakgrunn til dark-mode theme. Her har vi valgt å implementere vårt eget
            // metode, altså der vi bare forandrer fargen på selve bakgrunnen.
            background.setBackgroundResource(R.drawable.header_settings_dark_mode)

            // Change color of title and owner name
            startText.setTextColor(Color.parseColor("#FFFFFF"))
            startName.setTextColor(Color.parseColor("#FFFFFF"))

            // The switch is enabled/checked. We change the color of viewCards
            night_mode.setBackgroundResource(R.color.cardview_dark)
            cache.setBackgroundResource(R.color.cardview_dark)
            temperature.setBackgroundResource(R.color.cardview_dark)
            size.setBackgroundResource(R.color.cardview_dark)
            language.setBackgroundResource(R.color.cardview_dark)
            info.setBackgroundResource(R.color.cardview_dark)
            notifications.setBackgroundResource(R.color.cardview_dark)
            checked = true
        }

        if (tempMode){
            temperature_switch.isChecked = true
        }

        cacheView.setOnClickListener(){
            Toast.makeText(this, "NOT IMPLEMENTED YET!", Toast.LENGTH_SHORT).show()
        }

        notification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                Toast.makeText(this, "NOT IMPLEMENTED YET.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "NOT IMPLEMENTED YET.", Toast.LENGTH_SHORT).show()
            }
        }


        night.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                // Setter bakgrunn til dark-mode theme. Her har vi valgt å implementere vårt eget
                // metode, altså der vi bare forandrer fargen på selve bakgrunnen.
                background.setBackgroundResource(R.drawable.header_settings_dark_mode)

                // Change color of title and owner name
                startText.setTextColor(Color.parseColor("#FFFFFF"))
                startName.setTextColor(Color.parseColor("#FFFFFF"))

                // The switch is enabled/checked. We change the color of viewCards
                night_mode.setBackgroundResource(R.color.cardview_dark)
                cache.setBackgroundResource(R.color.cardview_dark)
                temperature.setBackgroundResource(R.color.cardview_dark)
                size.setBackgroundResource(R.color.cardview_dark)
                language.setBackgroundResource(R.color.cardview_dark)
                info.setBackgroundResource(R.color.cardview_dark)
                notifications.setBackgroundResource(R.color.cardview_dark)
                checked = true

            } else {

                // The switch is disabled
                // Setter bakgrunn til dark-mode theme. Her har vi valgt å implementere vårt eget
                // metode, altså der vi bare forandrer fargen på selve bakgrunnen.
                background.setBackgroundResource(R.drawable.header_settings)

                // Change color of title and owner name
                startText.setTextColor(Color.parseColor("#000000"))
                startName.setTextColor(Color.parseColor("#000000"))

                // The switch is enabled/checked. We change the color of viewCards
                night_mode.setBackgroundResource(R.color.white)
                cache.setBackgroundResource(R.color.white)
                temperature.setBackgroundResource(R.color.white)
                size.setBackgroundResource(R.color.white)
                language.setBackgroundResource(R.color.white)
                info.setBackgroundResource(R.color.white)
                notifications.setBackgroundResource(R.color.white)
                checked = false
            }
        }

        language_switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startText.setText(R.string.settings)
                startName.setText(R.string.welcome)
                nightModeText.setText(R.string.night_mode)
                clearCacheText.setText(R.string.cache)
                textSizeText.setText(R.string.text_size)
                aboutText.setText(R.string.info)
                notificationsText.setText(R.string.not)
                languageChecked = true

            } else {
                startText.setText(R.string.settings_nor)
                startName.setText(R.string.welcome_nor)
                nightModeText.setText(R.string.night_mode_nor)
                clearCacheText.setText(R.string.cache_nor)
                textSizeText.setText(R.string.text_size_nor)
                aboutText.setText(R.string.info_nor)
                notificationsText.setText(R.string.not_nor)
                languageChecked = false
            }
        }

        temperature_switch.setOnCheckedChangeListener { _, isChecked ->
            temp = isChecked
        }

        //Toast.makeText(applicationContext, switch_state.toString(), Toast.LENGTH_SHORT).show()


        // INFO - ACTIVITY
        info.setOnClickListener{
            val intent = Intent(applicationContext, About::class.java)
            intent.putExtra("night_mode", checked)
            intent.putExtra("language_state", languageChecked)
            intent.putExtra("small_size", smallSize)
            intent.putExtra("big_size", bigSize)
            activityResult.launch(intent)

        }

        // TEXT SIZE - ACTIVITY
        size.setOnClickListener{
            val intent = Intent(this, TextSize::class.java)
            intent.putExtra("night_mode", checked)
            intent.putExtra("language_state", languageChecked)
            activityResult.launch(intent)
        }
    }

    //Håndter resultatet av lokasjonsforespørselen
    val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultat ->
        if (resultat.resultCode == RESULT_OK) {
            val data = resultat.data
            val smallText = data?.getFloatExtra("SMALL_TEXT_SIZE", 16F)
            val bigText = data?.getFloatExtra("BIG_TEXT_SIZE", 24F)
            //Toast.makeText(applicationContext, strEditText.toString(), Toast.LENGTH_SHORT).show()

            startText.setTextSize(TypedValue.COMPLEX_UNIT_SP, bigText!!)
            startName.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallText!!)
            nightModeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallText)
            clearCacheText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallText)
            temperatureText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallText)
            textSizeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallText)
            languageText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallText)
            aboutText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallText)
            notificationsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallText)

            smallSize = smallText
            bigSize = bigText

        }

    }

    private fun saveData(){
        val shared = getSharedPreferences("shared", Context.MODE_PRIVATE)
        val editor = shared.edit()
        editor.putBoolean("night", checked)
        editor.putBoolean("temperature", temp)
        editor.apply()
    }
}


