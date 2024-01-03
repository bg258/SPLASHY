package com.example.in2000_team33.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.in2000_team33.R

class FavoritterFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var listAdapter: BadeAdapter? = null
    private val viewModel: HjemViewModel by activityViewModels()
    var night = false
    var temp = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val instillinger = requireContext().getSharedPreferences("shared", Context.MODE_PRIVATE)
        night = instillinger.getBoolean("night", false)
        temp = instillinger.getBoolean("temperature", false)

        if (night){
            return inflater.inflate(R.layout.favoritter_fragment_dark, container, false)
        }
        return inflater.inflate(R.layout.favoritter_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.favorittSteder.observe(viewLifecycleOwner) { badeliste ->
            listAdapter?.badeliste = badeliste
            listAdapter?.notifyDataSetChanged()
        }

        recyclerView = view.findViewById(R.id.favoritter)
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            listAdapter = BadeAdapter(night, temp)
            recyclerView!!.adapter = listAdapter
        }
    }

}