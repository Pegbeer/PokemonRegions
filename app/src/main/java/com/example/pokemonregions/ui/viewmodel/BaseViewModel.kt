package com.example.pokemonregions.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokemonregions.utils.Event
import javax.inject.Inject


abstract class BaseViewModel : ViewModel(){

    private val _errorDialog = MutableLiveData<Event<String?>>()
    val errorDialog:LiveData<Event<String?>> get() = _errorDialog


    fun showErrorDialog(message:String? = null){
        _errorDialog.value = Event(message)
    }

}