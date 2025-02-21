package br.com.alfatek.indikey

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication(): Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        Firebase.initialize(this)
    }
}