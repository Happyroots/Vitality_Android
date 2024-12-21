package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CounterNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var service = AudioService()

//        service.showNotification(++SoundsList.value)
//        service.sendNotification(++SoundsList.value)
        when (intent?.action) {
            AudioService.Actions.Pause.toString()-> {
                // Handle previous action
                val pauseIntent = Intent(context, AudioService::class.java).apply {
                    action = AudioService.Actions.Pause.toString()
                }
                context?.startService(pauseIntent)
            }

            AudioService.Actions.Play.toString()-> {
                // Handle previous action
                val pauseIntent = Intent(context, AudioService::class.java).apply {
                    action = AudioService.Actions.Play.toString()
                }
                context?.startService(pauseIntent)
            }

            AudioService.Actions.START.toString()-> {
                // Handle previous action
                val pauseIntent = Intent(context, TimerForegroundService::class.java).apply {
                    action = TimerForegroundService.Actions.START.toString()
                }
                context?.startService(pauseIntent)
            }

            AudioService.Actions.Guard.toString()-> {
                // Handle previous action
                val pauseIntent = Intent(context, GuardService::class.java).apply {
                    action = AudioService.Actions.Guard.toString()
                }
                context?.startService(pauseIntent)
            }

        }

    }
}