package com.example.aaaaaac

import android.app.Application
import com.example.aaaaaac.ui.chat.ChatRepository
import com.example.aaaaaac.ui.session.SessionRepository

class MainApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        SessionRepository.initialize(this)
        ChatRepository.initialize(this)
    }
}