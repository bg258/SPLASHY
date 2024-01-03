package com.example.in2000_team33.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.in2000_team33.R
import com.example.in2000_team33.databinding.TextSizeBinding

class TextSize: AppCompatActivity() {
    //Viewbinding
    private lateinit var binding: TextSizeBinding

    // SETTINGS - SIDE
    private lateinit var textView: TextView
    private lateinit var use: TextView
    private lateinit var useInfo: TextView
    private lateinit var textCard: TextView
    private lateinit var increase: Button
    private lateinit var reset: Button

    private var visited = false

    private val smallText = 16F
    private val bigText = 24F
    private var small = smallText
    private var big = bigText

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TextSizeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // These are all the text views and buttons we have used in this activity.
        textView = findViewById(R.id.text_size_info)
        use = findViewById(R.id.use)
        useInfo = findViewById(R.id.use_info)
        textCard = findViewById(R.id.text_card)
        increase = findViewById(R.id.increase)
        reset = findViewById(R.id.reset)

        val back = findViewById<ImageButton>(R.id.back_button_about)
        back.setOnClickListener{
            val data = Intent()
            data.putExtra("SMALL_TEXT_SIZE", small)
            data.putExtra("BIG_TEXT_SIZE", big)
            setResult(RESULT_OK, data)
            finish()
        }



        // The settings activity will send this activity information about whether or not
        // the switch night mode is enabled or not. In this case, if the switch is clicked, we
        // change the background to our own made "night mode" background. If not, nothing
        // will happen.
        val switchMode = intent.extras!!.getBoolean("night_mode")
        if (switchMode){
            val background = findViewById<ScrollView>(R.id.background_settings_size)
            val useCard = findViewById<LinearLayout>(R.id.use_cardview)
            val nighCard = findViewById<LinearLayout>(R.id.night_card)

            useCard.setBackgroundResource(R.color.cardview_dark)
            background.setBackgroundResource(R.drawable.header_settings_dark_mode)
            nighCard.setBackgroundResource(R.color.cardview_dark)

            textView.setTextColor(Color.parseColor("#FFFFFF"))
            increase.setBackgroundColor(Color.parseColor("#D8DBE2"))
            reset.setBackgroundColor(Color.parseColor("#D8DBE2"))

        }

        val languageMode = intent.extras!!.getBoolean("language_state")
        if (languageMode){
            textView.setText(R.string.text_size)
            use.setText(R.string.text_size_title)
            useInfo.setText(R.string.text_size_info)
            textCard.setText(R.string.night_mode)

            increase.setText(R.string.increase)
            reset.setText(R.string.reset)
        }


        val intentVisited = loadData()
        //Toast.makeText(this, visited_intent.toString(), Toast.LENGTH_SHORT).show()
        if (intentVisited){
            val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            val smallSize = sharedPreferences.getFloat("SMALL_TEXT_SIZE", smallText)
            val bigSize = sharedPreferences.getFloat("BIG_TEXT_SIZE", bigText)

            binding.textSizeInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, bigSize)
            use.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
            useInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
            textCard.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)

        } else{
            Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()
        }

        increase.setOnClickListener{
            if (small <= 22F){
                small += 2F
                big += 2F

                binding.textSizeInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, big)
                use.setTextSize(TypedValue.COMPLEX_UNIT_SP, small)
                useInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, small)
                textCard.setTextSize(TypedValue.COMPLEX_UNIT_SP, small)
                visited = true

                val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.apply {
                    putFloat("SMALL_TEXT_SIZE", small)
                    putFloat("BIG_TEXT_SIZE", big)
                    putBoolean("VISITED", true)
                }.apply()

                //val visited = sharedPreferences.getBoolean("visited", visited)
                //Toast.makeText(this, "SAVED", Toast.LENGTH_SHORT).show()

            } else {
                val info = "The system can not do it bigger!"
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
            }
        }

        reset.setOnClickListener{
            binding.textSizeInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, bigText)
            use.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallText)
            useInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallText)
            textCard.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallText)
            visited = true

            small = smallText
            big = bigText

            val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply {
                putFloat("SMALL_TEXT_SIZE", smallText)
                putFloat("BIG_TEXT_SIZE", bigText)
                putBoolean("VISITED", true)
            }.apply()
        }
    }


    private fun loadData(): Boolean {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("VISITED", false)
    }
}