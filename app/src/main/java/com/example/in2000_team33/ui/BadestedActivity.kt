
package com.example.in2000_team33.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.in2000_team33.R
import com.example.in2000_team33.db.BadestedEntity
import kotlinx.android.synthetic.main.activity_badested.*


class BadestedActivity() : AppCompatActivity() {

    private var temperature: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_badested)

        val instillinger = getSharedPreferences("shared", Context.MODE_PRIVATE)
        val night = instillinger.getBoolean("night", false)
        temperature = instillinger.getBoolean("temperature", false)

        if (night){
            setContentView(R.layout.activity_badested_dark)
        }

        val stedID = intent.extras?.getInt("stedId")
        if(stedID != null) {
            val viewModel: BadestedViewModel by viewModels { BadestedViewModel.InstanceCreator(application, stedID) }
            viewModel.getBadested().observe(this, { badested ->
                if(badested == null){
                    Toast.makeText(this, getString(R.string.no_place), Toast.LENGTH_LONG).show()
                    finish()
                }
                settOppSide(badested)


            })

            toggleFavoritt.setOnClickListener {
                if(toggleFavoritt.isChecked) viewModel.leggTilFavoritt()
                else viewModel.fjernFavoritt()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "StringFormatMatches")
    private fun settOppSide(badested: BadestedEntity) {
        tilbake_knapp.setOnClickListener {
            finish()
        }
        badested_navn.text = badested.navn
        val avstand = badested.avstand

        if (avstand != null) {
            if (avstand >= 1000){
                distanse.text = getString(R.string.distanse_km,(avstand /1000).toString())
            }else{
                distanse.text = getString(R.string.distanse_m,(avstand /1000).toString())
            }
        }


        if (temperature){
            vear_temp.text = getString(R.string.temp_F, badested.badeforhold?.get(0)?.luftTemperatur?.times((9/5))?.plus(32).toString())
            bade_temp.text = getString(R.string.temp_F, badested.badeforhold?.get(0)?.havTemperatur?.times((9/5))?.plus(32).toString())

            badested.observert?.let {
                textview3.visibility = View.VISIBLE
                observert.visibility = View.VISIBLE
                observert.text = getString(R.string.temp_F, it.times((9/5)).plus(32).toString())
            }


            // Timevarsel
            text_temp1.text = getString(R.string.blanda_temp_F, badested.badeforhold?.get(0)?.luftTemperatur?.times((9/5))?.plus(32).toString(),
                badested.badeforhold?.get(0)?.havTemperatur?.times((9/5))?.plus(32).toString())
            text_temp2.text = getString(R.string.blanda_temp_F, badested.badeforhold?.get(1)?.luftTemperatur?.times((9/5))?.plus(32).toString(),
                badested.badeforhold?.get(0)?.havTemperatur?.times((9/5))?.plus(32).toString())
            text_temp3.text = getString(R.string.blanda_temp_F, badested.badeforhold?.get(2)?.luftTemperatur?.times((9/5))?.plus(32).toString(),
                badested.badeforhold?.get(0)?.havTemperatur?.times((9/5))?.plus(32).toString())
            text_temp4.text = getString(R.string.blanda_temp_F, badested.badeforhold?.get(3)?.luftTemperatur?.times((9/5))?.plus(32).toString(),
                badested.badeforhold?.get(0)?.havTemperatur?.times((9/5))?.plus(32).toString())
        }  else {
            vear_temp.text = getString(R.string.temp_C, badested.badeforhold?.get(0)?.luftTemperatur.toString())
            bade_temp.text = getString(R.string.temp_C, badested.badeforhold?.get(0)?.havTemperatur.toString())

            badested.observert?.let {
                textview3.visibility = View.VISIBLE
                observert.visibility = View.VISIBLE
                observert.text = getString(R.string.temp_C, it.toString())
            }

            // Timevarsel
            text_temp1.text = getString(R.string.blanda_temp_C, badested.badeforhold?.get(0)?.luftTemperatur.toString(),
                badested.badeforhold?.get(0)?.havTemperatur.toString())
            text_temp2.text = getString(R.string.blanda_temp_C, badested.badeforhold?.get(1)?.luftTemperatur.toString(),
                badested.badeforhold?.get(0)?.havTemperatur.toString())
            text_temp3.text = getString(R.string.blanda_temp_C, badested.badeforhold?.get(2)?.luftTemperatur.toString(),
                badested.badeforhold?.get(0)?.havTemperatur.toString())
            text_temp4.text = getString(R.string.blanda_temp_C, badested.badeforhold?.get(3)?.luftTemperatur.toString(),
                badested.badeforhold?.get(0)?.havTemperatur.toString())

        }

        vear_icon.setImageDrawable(getDrawable(resources.getIdentifier(badested.badeforhold?.get(0)?.weather, "drawable",this.packageName)))
        vindstyrke.text = getString(R.string.vindhastighet,badested.badeforhold?.get(0)?.wind)

        // Timevarsel
        text_time1.text = badested.badeforhold?.get(0)?.time?.substring(11, 16)
        icon1.setImageDrawable(getDrawable(resources.getIdentifier(badested.badeforhold?.get(0)?.weather, "drawable",this.packageName)))

        text_time2.text = badested.badeforhold?.get(1)?.time?.substring(11, 16)
        icon2.setImageDrawable(getDrawable(resources.getIdentifier(badested.badeforhold?.get(1)?.weather, "drawable",this.packageName)))

        text_time3.text = badested.badeforhold?.get(2)?.time?.substring(11, 16)
        icon3.setImageDrawable(getDrawable(resources.getIdentifier(badested.badeforhold?.get(2)?.weather, "drawable",this.packageName)))

        text_time4.text = badested.badeforhold?.get(3)?.time?.substring(11, 16)
        icon4.setImageDrawable(getDrawable(resources.getIdentifier(badested.badeforhold?.get(3)?.weather, "drawable",this.packageName)))


        // solvarsel
        badested.soloppgang?.let {
            val timeSunrise = it.substring(11,16)
            val adjustedTimeSunrise = adjustTime(timeSunrise.substring(0,2).toInt())
            kl_solopp.text = adjustedTimeSunrise + timeSunrise.substring(2,5)
        }

        badested.solnedgang?.let {
            val timeSunset = it.substring(11,16)
            val adjustedTimeSunset = adjustTime(timeSunset.substring(0,2).toInt())
            kl_solned.text = adjustedTimeSunset + timeSunset.substring(2,5)
        }

        if(badested.favoritt != toggleFavoritt.isChecked) {
            toggleFavoritt.toggle()

        }

    }

    fun adjustTime(hour : Int) : String{
        var time = hour
        if (time < 9) {
            time++
            return "0$time"
        } else if (time == 23) {
            return "00"
        }
        time++
        return time.toString()
    }
}
