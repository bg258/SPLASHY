package com.example.in2000_team33.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.in2000_team33.api.BadestedRepository
import com.example.in2000_team33.db.BadestedEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BadestedViewModel(application: Application, stedId: Int) : AndroidViewModel(application){
    val stedId = stedId
    private val badested: MutableLiveData<BadestedEntity> by lazy {
        MutableLiveData<BadestedEntity>().also {
            hentBadested()
        }
    }
    private val repo = BadestedRepository(application)

    class InstanceCreator(val application: Application, val stedId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BadestedViewModel(application, stedId) as T
        }
    }

    private fun hentBadested() {
        CoroutineScope(Dispatchers.IO).launch {
            badested.postValue(repo.hentBadested(stedId))
        }
    }

    fun getBadested() : LiveData<BadestedEntity> {
        return badested
    }


    fun leggTilFavoritt() {
        repo.leggTilFavoritt(stedId)
    }


    fun fjernFavoritt() {
        repo.fjernFavoritt(stedId)
    }
}