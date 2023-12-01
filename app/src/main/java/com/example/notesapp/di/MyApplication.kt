package com.example.notesapp.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication:Application() {

// lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        // don't need this anymore

//        appComponent=DaggerAppComponent.builder().
//        appModule(AppModule(this)) // used when creating paramaterized constructor
//            .build()
    }

}