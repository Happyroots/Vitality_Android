package com.example.myapplication

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder

class GuardService  : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (intent.action) {

                AudioService.Actions.Guard.toString() -> {
                    startTimer()
                }

            }
        }

        return  START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()

        val intent = Intent(this, CounterNotificationReceiver::class.java).apply {
            action = AudioService.Actions.Guard.toString()
        }
        sendBroadcast(intent)

    }

    private var timer: CountDownTimer? = null
    private val initialTime: Long = 8*60*1000L // 初始时间（毫秒）
    private val tickInterval: Long = 1000L // 每次更新的间隔（毫秒）
    private var timeRamin: Long = 8*60*1000L // 初始时间（毫秒）

    fun startTimer() {
        timer?.cancel() // Cancel any existing timer before starting a new one

        timer = object : CountDownTimer(timeRamin, tickInterval) {
            override fun onTick(millisUntilFinished: Long) {
                println("剩余时间: ${millisUntilFinished / 1000} 秒")

//                val intent = Intent(this, CounterNotificationReceiver::class.java)
//                val intent = Intent(this, CounterNotificationReceiver::class.java).apply {
//                    action = AudioService.Actions.START.toString()
//                }
//                sendBroadcast(intent)


            }



            override fun onFinish() { //only called when initialTime to zero

            }
        }
        timer?.start()
    }


    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }



}