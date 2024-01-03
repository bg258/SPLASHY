package com.example.in2000_team33.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.in2000_team33.R

class About: AppCompatActivity() {
    // SETTINGS + CARDS
    private lateinit var background: ScrollView
    private lateinit var aboutCard: LinearLayout

    private lateinit var mortenCard: RelativeLayout
    private lateinit var charlotteCard: RelativeLayout
    private lateinit var ingeCard: RelativeLayout
    private lateinit var preCard: RelativeLayout
    private lateinit var markCard: RelativeLayout
    private lateinit var ingerCard: RelativeLayout

    private lateinit var question: RelativeLayout
    private lateinit var question_en: RelativeLayout
    private lateinit var question_to: RelativeLayout
    private lateinit var question_tre: RelativeLayout
    private lateinit var question_fire: RelativeLayout


    // TEXT VIEWS
    private lateinit var aboutText: TextView
    private lateinit var infoText: TextView
    private lateinit var aboutUs: TextView
    private lateinit var aboutIntro: TextView

    private lateinit var mortenText: TextView
    private lateinit var charlotteText: TextView
    private lateinit var ingeText: TextView
    private lateinit var preText: TextView
    private lateinit var markText: TextView
    private lateinit var ingerText: TextView

    private lateinit var morten: TextView
    private lateinit var charlotte: TextView
    private lateinit var inge: TextView
    private lateinit var pre: TextView
    private lateinit var mark: TextView
    private lateinit var inger: TextView

    private lateinit var faq: TextView
    private lateinit var faq_info: TextView

    private lateinit var questions: TextView
    private lateinit var questions_answer: TextView

    private lateinit var questions_1: TextView
    private lateinit var questions_1_answer: TextView

    private lateinit var questions_2: TextView
    private lateinit var questions_2_answer: TextView

    private lateinit var questions_3: TextView
    private lateinit var questions_3_answer: TextView

    private lateinit var questions_4: TextView
    private lateinit var questions_4_answer: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_us)

        val smallSize = intent.extras!!.getFloat("small_size")
        val bigSize = intent.extras!!.getFloat("big_size")

        // BACKGROUND SETTINGS + CARD VIEWS
        background = findViewById(R.id.background_settings_about)
        aboutCard = findViewById(R.id.about_card)

        mortenCard = findViewById(R.id.morten_card)
        charlotteCard = findViewById(R.id.charlotte_card)
        ingeCard = findViewById(R.id.inge_card)
        preCard = findViewById(R.id.pre_card)
        markCard = findViewById(R.id.mark_card)
        ingerCard = findViewById(R.id.inger_card)

        question = findViewById(R.id.questions_card)
        question_en = findViewById<RelativeLayout>(R.id.questions_card_1)
        question_to = findViewById<RelativeLayout>(R.id.questions_card_2)
        question_tre = findViewById<RelativeLayout>(R.id.questions_card_3)
        question_fire = findViewById<RelativeLayout>(R.id.questions_card_4)


        // TEXT

        aboutText = findViewById(R.id.about_us_text)
        infoText = findViewById(R.id.about_info)
        aboutUs = findViewById(R.id.about_us_info)
        aboutIntro= findViewById(R.id.about_us_intro)

        mortenText= findViewById(R.id.mortenText)
        charlotteText= findViewById(R.id.charlotteText)
        ingeText = findViewById(R.id.ingeText)
        preText = findViewById(R.id.preText)
        markText = findViewById(R.id.markText)
        ingerText = findViewById(R.id.ingerText)

        morten = findViewById(R.id.morten)
        charlotte = findViewById(R.id.charlotte)
        inge  = findViewById(R.id.inge)
        pre = findViewById(R.id.pre)
        mark = findViewById(R.id.mark)
        inger = findViewById(R.id.inger)

        faq = findViewById(R.id.faq)
        faq_info = findViewById(R.id.faq_info)

        questions = findViewById(R.id.questions)
        questions_answer= findViewById(R.id.questions_answer)

        questions_1 = findViewById(R.id.questions_1)
        questions_1_answer= findViewById(R.id.questions_1_answer)

        questions_2 = findViewById(R.id.questions_2)
        questions_2_answer= findViewById(R.id.questions_2_answer)

        questions_3 = findViewById(R.id.questions_3)
        questions_3_answer= findViewById(R.id.questions_3_answer)

        questions_4 = findViewById(R.id.questions_4)
        questions_4_answer= findViewById(R.id.questions_4_answer)

        // Whenever we enter the about activity, we automatically set the font size
        // that has been sent from settings. This is automatically refreshed every time
        // we enter this activity.
        aboutText.setTextSize(TypedValue.COMPLEX_UNIT_SP, bigSize)
        infoText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        aboutUs.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        aboutIntro.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)

        faq.setTextSize(TypedValue.COMPLEX_UNIT_SP, bigSize)
        faq_info.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)



        // Student cards will also increase their size depending on the size that has been send

        morten.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        mortenText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)

        charlotte.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        charlotteText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)

        inge.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        ingeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)

        pre.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        preText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)

        mark.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        markText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)

        inger.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        ingerText.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)

        questions.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        questions_answer.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        questions_1.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        questions_1_answer.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        questions_2.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        questions_2_answer.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        questions_3.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        questions_3_answer.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        questions_4.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)
        questions_4_answer.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallSize)


        val back = findViewById<ImageButton>(R.id.back_button_about)
        back.setOnClickListener{
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        val switchMode = intent.extras!!.getBoolean("night_mode")
        if (switchMode){

            this.background.setBackgroundResource(R.drawable.header_settings_dark_mode)
            this.aboutCard.setBackgroundResource(R.color.cardview_dark)

            this.mortenCard.setBackgroundResource(R.color.cardview_dark)
            this.charlotteCard.setBackgroundResource(R.color.cardview_dark)
            this.ingeCard.setBackgroundResource(R.color.cardview_dark)
            this.preCard.setBackgroundResource(R.color.cardview_dark)
            this.markCard.setBackgroundResource(R.color.cardview_dark)
            this.ingerCard.setBackgroundResource(R.color.cardview_dark)

            this.question.setBackgroundResource(R.color.cardview_dark)
            this.question_en.setBackgroundResource(R.color.cardview_dark)
            this.question_to.setBackgroundResource(R.color.cardview_dark)
            this.question_tre.setBackgroundResource(R.color.cardview_dark)
            this.question_fire.setBackgroundResource(R.color.cardview_dark)

            aboutText.setTextColor(Color.parseColor("#FFFFFF"))
            infoText.setTextColor(Color.parseColor("#FFFFFF"))

            faq.setTextColor(Color.parseColor("#FFFFFF"))
            faq_info.setTextColor(Color.parseColor("#FFFFFF"))

        } else {
            //Toast.makeText(applicationContext, switch_mode_state.toString(), Toast.LENGTH_SHORT).show()
            this.background.setBackgroundResource(R.drawable.header_settings)
        }

        val languageMode = intent.extras!!.getBoolean("language_state")
        if (languageMode){
            aboutText.setText(R.string.info)
            infoText.setText(R.string.introduction)
            aboutUs.setText(R.string.about_intro_name)
            aboutIntro.setText(R.string.about_intro)

            mortenText.setText(R.string.morten_eng)
            charlotteText.setText(R.string.charlotte_eng)
            ingeText.setText(R.string.inge_eng)
            preText.setText(R.string.pre_eng)
            markText.setText(R.string.mark_eng)
            ingerText.setText(R.string.inger_eng)

            faq.setText(R.string.faq)
            faq_info.setText(R.string.faq_info)

            questions.setText(R.string.question_eng)
            questions_answer.setText(R.string.answer_eng)
            questions_1.setText(R.string.question_eng_1)
            questions_1_answer.setText(R.string.answer_eng_1)
            questions_2.setText(R.string.question_eng_2)
            questions_2_answer.setText(R.string.answer_eng_2)
            questions_3.setText(R.string.question_eng_3)
            questions_3_answer.setText(R.string.answer_eng_3)
            questions_4.setText(R.string.question_eng_4)
            questions_4_answer.setText(R.string.answer_eng_4)

        } else {
            //Toast.makeText(applicationContext, switch_mode_state.toString(), Toast.LENGTH_SHORT).show()
        }

    }
}