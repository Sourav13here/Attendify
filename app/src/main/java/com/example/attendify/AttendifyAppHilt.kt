package com.example.attendify

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import android.util.Log

@HiltAndroidApp
class AttendifyAppHilt : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("FirebaseInit", "Initializing Firebase in App class")
        FirebaseApp.initializeApp(this)
    }
}
