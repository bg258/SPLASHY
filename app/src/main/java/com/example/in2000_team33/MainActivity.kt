package com.example.in2000_team33

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.in2000_team33.ui.FavoritterFragment
import com.example.in2000_team33.ui.HjemFragment
import com.example.in2000_team33.ui.SearchFragment
import com.example.in2000_team33.ui.Settings
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){
    private var checked : Boolean = false
    private var temperature: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadData()
        if (checked)
            setContentView(R.layout.activity_main_dark)
        else
            setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            replaceFragment(HjemFragment())

        navigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    val intent = Intent(this, Settings::class.java)
                    intent.putExtra("night", checked)
                    intent.putExtra("temperature", temperature)
                    setResult(RESULT_OK, intent)
                    startActivity(intent)
                }
                R.id.home ->replaceFragment(HjemFragment())
                R.id.sok -> replaceFragment(SearchFragment())
                R.id.bookmark -> replaceFragment(FavoritterFragment())
            }
            true

        }
    }

    fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    private fun loadData(){
        val shared = getSharedPreferences("shared", Context.MODE_PRIVATE)
        checked = shared.getBoolean("night", false)
        temperature = shared.getBoolean("temperature", false)
    }
}