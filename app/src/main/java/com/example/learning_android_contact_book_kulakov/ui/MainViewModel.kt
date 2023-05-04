package com.example.learning_android_contact_book_kulakov.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learning_android_contact_book_kulakov.Contact
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val app: Application): AndroidViewModel(app) {

    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts : LiveData<List<Contact>> = _contacts

    private val gson = Gson()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val files = app.filesDir.listFiles { filter -> filter.extension == "json" }
            val contacts = files?.map {
                val json = it.readText()
                gson.fromJson(json, Contact::class.java)
            }
            _contacts.postValue(contacts)
        }
    }

}