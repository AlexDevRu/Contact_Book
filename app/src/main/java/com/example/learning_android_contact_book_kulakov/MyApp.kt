package com.example.learning_android_contact_book_kulakov

import android.app.Application

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPrefs.init(this)
    }

}