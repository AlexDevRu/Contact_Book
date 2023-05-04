package com.example.learning_android_contact_book_kulakov

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPrefs {

    private const val nameKey = "name"
    private const val surnameKey = "surname"
    private const val patronymicKey = "patronymic"
    private const val phoneNumberKey = "phoneNumber"
    private const val emailKey = "email"
    private const val addressKey = "address"
    private const val uriKey = "uri"

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    var name: String?
        get() = preferences.getString(nameKey, null)
        set(value) = preferences.edit { putString(nameKey, value) }

    var surname: String?
        get() = preferences.getString(surnameKey, null)
        set(value) = preferences.edit { putString(surnameKey, value) }

    var patronymic: String?
        get() = preferences.getString(patronymicKey, null)
        set(value) = preferences.edit { putString(patronymicKey, value) }

    var phone: String?
        get() = preferences.getString(phoneNumberKey, null)
        set(value) = preferences.edit { putString(phoneNumberKey, value) }

    var email: String?
        get() = preferences.getString(emailKey, null)
        set(value) = preferences.edit { putString(emailKey, value) }

    var address: String?
        get() = preferences.getString(addressKey, null)
        set(value) = preferences.edit { putString(addressKey, value) }

    var uri: String?
        get() = preferences.getString(uriKey, null)
        set(value) = preferences.edit { putString(uriKey, value) }

    fun clear() = preferences.edit().clear().apply()
}