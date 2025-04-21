package com.example.tutorm6

import android.app.Application

class App: Application() {
    companion object{
        lateinit var db:AppDatabase
    }
    override fun onCreate() {
        super.onCreate()
//        baseContext.deleteDatabase("prakm6")
        db = AppDatabase.getInstance(baseContext)
    }
}