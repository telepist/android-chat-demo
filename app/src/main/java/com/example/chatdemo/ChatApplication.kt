package com.example.chatdemo

import android.app.Application
import com.example.chatdemo.repository.FirebaseRepository
import com.example.chatdemo.repository.Repository
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ChatApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ChatApplication)
            modules(
                module {
                    single<Repository> { FirebaseRepository() }
                }
            )
        }
    }
}