package com.example.notesapp.di

import android.app.Application

class MyApplication:Application() {

 lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent=DaggerAppComponent.builder().
        appModule(AppModule(this)) // used when creating paramaterized constructor
            .build()
    }

}