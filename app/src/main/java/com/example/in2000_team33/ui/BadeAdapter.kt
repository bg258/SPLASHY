package com.example.in2000_team33.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.in2000_team33.R
import com.example.in2000_team33.db.BadestedEntity

class BadeAdapter(night: Boolean, tempVaer: Boolean) :
    RecyclerView.Adapter<BadeAdapter.ViewHolder>() {

    var badeliste: List<BadestedEntity> = mutableListOf()
    var night_mode = night
    var temperatur = tempVaer

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val badestedN = itemView.findViewById<TextView>(R.id.bade_sted_navn)
        val bydel = itemView.findViewById<TextView>(R.id.bydel)
        val luftTemp = itemView.findViewById<TextView>(R.id.luftTemp)
        val temp = itemView.findViewById<TextView>(R.id.temp)
        val distanse = itemView.findViewById<TextView>(R.id.distanse)
        val icon = itemView.findViewById<ImageView>(R.id.icon_vaer)


        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(badested: BadestedEntity) {

            val badeforhold = badested.badeforhold
            badeforhold?.let {
                if (temperatur){
                    val temperaturLuft = (it[0].luftTemperatur?.times((9/5))?.plus(32))
                    val temperaturHav = (it[0].havTemperatur?.times((9/5))?.plus(32))

                    luftTemp.text = itemView.context.getString(R.string.temp_far, temperaturLuft.toString())
                    temp.text = itemView.context.getString(R.string.temp_far, temperaturHav.toString())
                    icon.setImageDrawable(itemView.context.getDrawable(itemView.context.resources.getIdentifier(
                        it[0].weather, "drawable", this.itemView.context.packageName)))

                } else {
                    luftTemp.text = itemView.context.getString(R.string.temperature, it[0].luftTemperatur.toString())
                    temp.text = itemView.context.getString(R.string.temperature, it[0].havTemperatur.toString())
                    icon.setImageDrawable(itemView.context.getDrawable(itemView.context.resources.getIdentifier(
                        it[0].weather, "drawable", this.itemView.context.packageName)))
                }
            }


            badestedN.text = badested.navn
            bydel.text = badested.sted


            val avstand = badested.avstand
            if (avstand != null) {
                if (avstand >= 1000){
                    distanse.text = itemView.context.getString(R.string.distance_km, (avstand/1000.0).toString())
                }else{
                    distanse.text = itemView.context.getString(R.string.distance_m, (avstand).toString())
                }
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item

        if(night_mode){
            val night = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.element_dark, viewGroup, false)
            return ViewHolder(night)
        }

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.element, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d("BadeAdapter", "BindView: $position")
        when (viewHolder) {
            is ViewHolder -> {
                viewHolder.bind(badeliste[position])
            }

        }

        viewHolder.itemView.setOnClickListener{
            val intent = Intent(it.context, BadestedActivity::class.java)
            intent.putExtra("stedId", badeliste[position].id)
            it.context.startActivity(intent)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        Log.d("BadeAdapter", "Antall: " + badeliste.size)
        return badeliste.size
    }
}
