package com.example.myapplication

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import android.Manifest.permission.POST_NOTIFICATIONS
import android.os.CountDownTimer
import android.os.PowerManager
import android.support.v4.media.session.MediaSessionCompat
import androidx.compose.runtime.Composable
import androidx.core.app.NotificationManagerCompat
import androidx.media3.session.MediaSessionService
import java.util.logging.Handler


class TimerForegroundService : Service(){ //

//    private val binder = LocalBinder()
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var startTime: Long = 0L
    private var elapsedPausedTime: Long = 0L
    private var isRunning = false
    private var isPaused = false

    companion object {
        const val CHANNEL_ID = "TimerChannel"
        const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
//        createNotificationChannel()
//        handler = Handler(Looper.getMainLooper())
        sendNotification()
    }

    private val binder = MusicBinder()
    inner class MusicBinder : Binder(){
        fun getService(): TimerForegroundService = this@TimerForegroundService
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    private val initialTime: Long = 8*60*1000L // 初始时间（毫秒）
    private val tickInterval: Long = 1000L // 每次更新的间隔（毫秒）

    private var timer: CountDownTimer? = null
    private var isPause = false
    var timeRamin = initialTime
    enum class Actions{
        START, STOP, Prev, Next, Pause, Restart, Play, RePlay, Close
    }

    override fun onDestroy() {
//        if(isRunning == true){
//            pauseTimer()
//            SoundsList.timeLeft = timeRamin
//            val intent = Intent(this, CounterNotificationReceiver::class.java).apply {
//                action = AudioService.Actions.START.toString()
//            }
//            sendBroadcast(intent)
//            return
//        }

        super.onDestroy()
        stopPlaying()
        stopForeground(Service.STOP_FOREGROUND_REMOVE)
        wakeLock?.release()
        wakeLock = null
        stopSelf()

    }

    var wakeLock: PowerManager.WakeLock? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

//        val intent = Intent(this, CounterNotificationReceiver::class.java).apply {
//                action = AudioService.Actions.Guard.toString()
//            }
//        sendBroadcast(intent)

        intent?.let {
            when (intent.action) {
                Actions.START.toString() -> {
                    startTimer()

                    sendNotification()
                }
                Actions.Pause.toString() -> {
                    pauseTimer()

                    sendNotification()
                }
                Actions.Restart.toString() -> {
                    restartTimer()

//                    sendNotification()
                }
                Actions.Close.toString() -> {
                    closeTimer()
                    onDestroy()

//                    sendNotification()
                }

            }
        }

        return  START_STICKY
    }


    fun startTimer() {
        timer?.cancel() // Cancel any existing timer before starting a new one
        wakeLock?.release()
        wakeLock = null
        timer = object : CountDownTimer(timeRamin, tickInterval) {
            override fun onTick(millisUntilFinished: Long) {
                println("剩余时间: ${millisUntilFinished / 1000} 秒")
                timeRamin = millisUntilFinished
                liveDataTimeLeft.postValue(millisUntilFinished)
                liveDataonFinish.postValue(false)
                musicScheme(millisUntilFinished / 1000L)
//                updateNotification()
//                sendNotification() //will crash soon
            }



            override fun onFinish() { //only called when initialTime to zero
                timeRamin = initialTime
                SoundsList.timeLeft = initialTime
                liveDataTimeLeft.postValue(initialTime)
                liveDataonFinish.postValue(true)
                play(characterName, "stop")
                isRunning = false
                wakeLock?.release()
                wakeLock = null
                stopForeground(true)
                stopForeground(Service.STOP_FOREGROUND_REMOVE)
            }
        }

        timer?.start()
        isRunning = true
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag").apply {
                    acquire()
                }
            }

    }

    fun pauseTimer() {
        timer?.cancel()
        wakeLock?.release()
        wakeLock = null
        liveDataTimeLeft.postValue(timeRamin)
        liveDataonFinish.postValue(true)
        isPause = true
        isRunning = false
    }

    fun restartTimer() {
        isPause = false
        isRunning = true
        timeRamin = initialTime
        wakeLock?.release()
        wakeLock = null
        startTimer()
    }

    fun closeTimer() {
        timer?.cancel()
        wakeLock?.release()
        wakeLock = null
        liveDataTimeLeft.postValue(initialTime)
        liveDataonFinish.postValue(true)
        isRunning = false
    }

    val characterName = SoundsList.characterName
    fun musicScheme(timeLeft : Long){

        when (timeLeft.toDouble()) {
            7.0 * 60 -> {
                play( characterName, "look3km")
                liveDataTip.postValue("3 KM")

            }

            6.0 * 60 -> {
                play(characterName, "look5km")
                liveDataTip.postValue("5 KM")

            }

            5.0 * 60 -> {
                play(characterName, "look_sky")
                liveDataTip.postValue("SKY")

            }

            4.5 * 60 -> {
                play(characterName, "1m5")
                liveDataTip.postValue("SKY")

            }

            4.0 * 60 -> {
                play(characterName, "1m")
                liveDataTip.postValue("SKY")

            }

            3.0 * 60 -> {
                play(characterName, "look5km")
                liveDataTip.postValue("5 KM")

            }

            2.0 * 60 -> {
                play( characterName, "look3km")
                liveDataTip.postValue("3 KM")

            }

            1.0 * 60 -> {
                play(characterName, "look1km")
                liveDataTip.postValue("1 KM")

            }
        }

//        val minutesLeft = timeLeft / 60L
//        if (minutesLeft != 4L) {
//            val secondsLeft = timeLeft - minutesLeft * 60
//            when (secondsLeft) {
//                45L -> play(characterName, "45s")
//                30L -> play(characterName, "30s")
//                15L -> play(characterName, "15s")
//                10L -> play(characterName, "10to0")
//            }
//        }
    }

    private var mediaPlayer: MediaPlayer? = null

    fun play(characterName: String, scenario :String) {

        val character = SoundsList.characters.find{ it.name == characterName }
        character?.clips?.get(scenario)?.let { clip ->
            mediaPlayer = MediaPlayer.create(this, clip.resourceId)
        }

        mediaPlayer?.isLooping = false // 设置是否循环播放

        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.release() // Release MediaPlayer resources
            mediaPlayer = null // Set the MediaPlayer reference to null
        }

//        Async
//        mediaPlayer?.setOnPreparedListener{
//            it.start() // 准备完成后开始播放
//        }
//
//        try {
//            mediaPlayer?.prepareAsync() // 异步准备资源
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

        if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
        }

    }


    fun stopPlaying() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }


    private fun sendNotification(){
        val mediaSession = MediaSessionCompat(this, "music")
        val style = androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(1)

//        var isRunning : Boolean = mediaPlayer?.isPlaying == true
        var notifacation = NotificationCompat.Builder(this, R.string.CHANNEL_ID.toString())
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.baseline_baby_changing_station_24)
            .setContentTitle("祝你有个好身体")
//            .setContentText("$")//
                .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSilent(true)
            .setAutoCancel(true)

            .addAction(
                R.drawable.vector_restart,
                "restart",
                restartPendingIntent()
            )
            .addAction(
//                R.drawable.baseline_pause_24,
//                "Pause",
//                pausePendingIntent()
                if (isRunning) R.drawable.vector_pause else R.drawable.vector_play_1,
                if (isRunning) "pause" else "play",
                if (isRunning) pausePendingIntent() else playPendingIntent()
            )
            .addAction(
                R.drawable.vector_close,
                "close",
                closePendingIntent()
            )
            .setStyle(
//                androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0,1,2)
                androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(2)
            )

            .build()

// for update
//        val notificationManager: NotificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(2, notifacation)


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
                startForeground(1, notifacation)
            }
        }else{
            startForeground(1, notifacation)
        }

    }

    private fun restartPendingIntent(): PendingIntent? {
        val intent_ = Intent(this, TimerForegroundService::class.java).apply {
            action = Actions.Restart.toString()
        }
        val pendingIntent : PendingIntent =   PendingIntent.getService(
            this,
            0,
            intent_,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        return pendingIntent
    }

    private fun closePendingIntent(): PendingIntent? {
        val intent_ = Intent(this, TimerForegroundService::class.java).apply {
            action = Actions.Close.toString()
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent : PendingIntent =   PendingIntent.getService(
            this,
            0,
            intent_,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        return pendingIntent
    }


    fun pausePendingIntent(): PendingIntent{
        val intent_ = Intent(this, TimerForegroundService::class.java).apply {
            action = TimerForegroundService.Actions.Pause.toString()
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent : PendingIntent =   PendingIntent.getService(
            this,
            0,
            intent_,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        return pendingIntent
    }

    fun playPendingIntent(): PendingIntent{
        val intent_ = Intent(this, TimerForegroundService::class.java).apply {
            action = Actions.START.toString()
        }
        val pendingIntent : PendingIntent =   PendingIntent.getService(
            this,
            0,
            intent_,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        return pendingIntent
    }

    val PROGRESS_MAX = 100
    var PROGRESS_CURRENT = 0
    fun updateNotification(){
        val mediaSession = MediaSessionCompat(this, "music")
        val style = androidx.media.app.NotificationCompat.MediaStyle()
        PROGRESS_CURRENT = timeRamin.toInt() * 100 / 8*60*1000
        var isPlaying : Boolean = mediaPlayer?.isPlaying == true
        var notifacation = NotificationCompat.Builder(this, "vitality")
            .setSmallIcon(R.drawable.baseline_baby_changing_station_24)
//            .setContentTitle("原身")
//            .setContentText("Elapsed time : $timeRamin")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
//            .setProgress(PROGRESS_MAX, PROGRESS_CURRENT, true)
            .setStyle(style)
//            .setStyle(MediaStyleNotificationHelper.MediaStyle(mediaSession)
//                .setShowActionsInCompactView(1 /* #1: pause button \*/))
            .addAction(
                R.drawable.baseline_pause_24,
                "Pause",
                pausePendingIntent()
//                if (isPlaying) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24,
//                if (isPlaying) "Pause" else "Play",
//                if (isPlaying) pausePendingIntent() else playPendingIntent()

            )
//            .build()

        NotificationManagerCompat.from(this).apply {
            // Issue the initial notification with zero progress.
            notifacation.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
            getSystemService(Context.NOTIFICATION_SERVICE)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(2, notifacation.build());

            // Do the job that tracks the progress here.
            // Usually, this is in a worker thread.
            // To show progress, update PROGRESS_CURRENT and update the notification with:
            // builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
            // notificationManager.notify(notificationId, builder.build());

            // When done, update the notification once more to remove the progress bar.
//            notifacation.setContentText("Download complete")
//                .setProgress(0, 0, false)
//            notify(2, notifacation.build())
        }

        // for update
//        val notificationManager: NotificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(2, notifacation)
    }

    @Composable //use jecpack compose
    fun annimation(){

    }

//    private fun buildNotification(elapsedTime: Long): Notification {
//        return NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle("计时器运行中")
//            .setContentText("已运行时间: ${elapsedTime / 1000} 秒")
//            .setSmallIcon(android.R.drawable.ic_media_play)
//            .setOngoing(true)
//            .build()
//    }
//
//    private fun updateNotification(elapsedTime: Long) {
//        val notification = buildNotification(elapsedTime)
//        val notificationManager = getSystemService(NotificationManager::class.java)
//        notificationManager.notify(NOTIFICATION_ID, notification)
//    }

    inner class LocalBinder : Binder() {
        fun getService(): TimerForegroundService = this@TimerForegroundService
    }
}
