package com.example.in2000_team33.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.in2000_team33.ui.badestedListe.NearestFragment
import com.example.in2000_team33.ui.badestedListe.VarmestFragment

class ListAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
     override fun createFragment(position: Int): Fragment {

         if (position == 0) {
             return VarmestFragment()
         } else {
             return NearestFragment()
         }
     }

    override fun getItemCount(): Int {
        return 2
    }
}

