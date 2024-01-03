package com.example.in2000_team33.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.in2000_team33.api.BadestedRepository
import com.example.in2000_team33.db.BadestedEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    val badesteder = MutableLiveData<List<BadestedEntity>>()
    private val repository = BadestedRepository(application)

    //Oppdaterer badestedlivedataen med alle stedene som matcher søket.
    fun sok(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val resultat = repository.badestedSok(query)
            badesteder.postValue(resultat)
        }
    }

    init {
       sok("")
    }
}