package com.example.myapplication

import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class MediaService: MediaSessionService()  {


    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return null
    }



}