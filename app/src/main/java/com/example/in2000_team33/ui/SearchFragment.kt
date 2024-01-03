package com.example.in2000_team33.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.in2000_team33.R

class SearchFragment : Fragment(), SearchView.OnQueryTextListener {
    private var night = false
    private var temp = false
    private val viewModel: SearchViewModel by activityViewModels()
    private lateinit var badeAdapter: BadeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val instillinger = requireContext().getSharedPreferences("shared", Context.MODE_PRIVATE)
        night = instillinger.getBoolean("night", false)
        temp = instillinger.getBoolean("temperature", false)

        if (night){
            return inflater.inflate(R.layout.search_fragment_dark, container, false)
        }
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = requireActivity().findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(this)
        searchView.setOnClickListener { searchView.isIconified = false }

        //Konfigurerer recyclerview.
        val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.search_recyclerview)
        recyclerView.apply {
            adapter = BadeAdapter(night, temp).also { badeAdapter = it }
            layoutManager = LinearLayoutManager(requireContext())
        }

        //Observer søkeresultater
        viewModel.badesteder.observe(viewLifecycleOwner, {
            println("Oppdaterer liste")
            badeAdapter.badeliste = it
            badeAdapter.notifyDataSetChanged()
        })
    }

    //Lytt etter endringer i søkefeltet.
    override fun onQueryTextSubmit(tekst: String?): Boolean {
        tekst?.let { viewModel.sok(it) }
        return true
    }

    override fun onQueryTextChange(tekst: String?): Boolean {
        tekst?.let { viewModel.sok(it) }
        return true
    }
}