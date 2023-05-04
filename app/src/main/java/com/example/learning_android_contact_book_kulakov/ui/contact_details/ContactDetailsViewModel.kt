package com.example.learning_android_contact_book_kulakov.ui.contact_details

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.example.learning_android_contact_book_kulakov.Contact
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ContactDetailsViewModel(
    private val app: Application,
    savedStateHandle: SavedStateHandle
): AndroidViewModel(app) {

    private val _contact = MutableLiveData<Contact>()
    val contact : LiveData<Contact> = _contact

    private val gson = Gson()

    init {
        val created = savedStateHandle.get<Long>(ContactDetailsActivity.CREATED)
        viewModelScope.launch(Dispatchers.IO) {
            val file = File(app.filesDir, "$created.json")
            val contact = gson.fromJson(file.readText(), Contact::class.java)
            _contact.postValue(contact)
        }
    }

    fun saveImage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedContact = contact.value?.copy(imageUri = uri.toString()) ?: return@launch
            val json = gson.toJson(updatedContact)
            File(app.filesDir, "${updatedContact.created}.json").writeText(json)
        }
    }

}