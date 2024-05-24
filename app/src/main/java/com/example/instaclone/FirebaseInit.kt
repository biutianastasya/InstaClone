package com.example.instaclone

import android.app.Application
import com.google.firebase.FirebaseApp

class FirebaseInit : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
    }
}