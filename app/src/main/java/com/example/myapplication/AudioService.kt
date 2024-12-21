package com.example.myapplication

//import android.annotation.SuppressLint
import android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Notification
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import com.example.myapplication.SoundsList

//// 定义单个声音文件的数据模型
//data class VoiceClip(val resourceId: Int, val description: String)
//// 定义一个角色的数据模型，该角色可以有多个声音片段
//data class VoiceCharacter(val name: String, val clips: Map<String, VoiceClip>)

//class AudioService ( private val context: Context,
//    val nameVoice: String,
//    val scenario :String ): Service() {
//class SoundsListo(){
//    // 初始化一些示例语音角色及其对应的多个声音文件
//    private val characters = listOf(
//        VoiceCharacter(
//            "ai",
//            mapOf(
//                "2m" to VoiceClip(R.raw.ai_2m, "2m"),
//                "1m5" to VoiceClip(R.raw.ai_1m5, "1m5"),
//                "1m" to VoiceClip(R.raw.ai_1m, "1m"),
//                "45s" to VoiceClip(R.raw.ai_45s, "45s"),
//                "30s" to VoiceClip(R.raw.ai_30s, "30s"),
//                "15s" to VoiceClip(R.raw.ai_15s, "15s"),
//                "10to0" to VoiceClip(R.raw.ai_10to0, "10to0"),
//                "stop" to VoiceClip(R.raw.ai_stop, "stop"),
//                "look1km" to VoiceClip(R.raw.ai_look1km, "look1km"),
//                "look3km" to VoiceClip(R.raw.ai_look3km, "look3km"),
//                "look5km" to VoiceClip(R.raw.ai_look5km, "look5km"),
//                "look_sky" to VoiceClip(R.raw.ai_look_sky, "look_sky"),
//            )
//        ),
//
//// Add more voice characters as needed
//    )
//}



class AudioService (
//    private var context: Context
): Service(){



//    private val currentTrack = MutableStateFlow<SoundsList>(SoundsList())
    private val musicList = SoundsList.scenario

    private val binder = MusicBinder()
    inner class MusicBinder : Binder(){
        fun getService(): AudioService = this@AudioService
    }

    private var mediaPlayer: MediaPlayer? = null

    inner class LocalBinder : Binder() {
        fun getService(): AudioService = this@AudioService

    }

    override fun onCreate() {
        super.onCreate()
        // 初始化MediaPlayer
//        mediaPlayer = MediaPlayer()
//        mediaPlayer.setDataSource(this,getRawUrl(currentTack.value.id) )

//        createNotificationChannel()
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    enum class Actions{
        START, STOP, Prev, Next, Pause, Restart, Play, RePlay, Guard
    }
    val next = "next"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        intent?.let{
            when(intent.action){
                Actions.START.toString()->{
//                    sendNotification()
                    startPlaying()
                }
                Actions.STOP.toString() ->{
                    stopPlaying()
                }
                Actions.Play.toString() ->{
                    startPlaying()
                }
                Actions.Prev.toString() ->{

                }
                Actions.Next.toString() ->{
                    stopPlaying()
                }
                Actions.Pause.toString() ->{
                    stopPlaying()
                }
            }
        }
        flags.let {
            when(flags.toString()){
                Actions.Prev.toString() ->{

                }
                Actions.Next.toString() ->{
                    stopPlaying()
                }

            }

        }

        return START_STICKY
//        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        stopPlaying()
    }


//    private var CHANNEL_ID : String = "vitality"

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(R.string.CHANNEL_ID.toString(), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }



    private fun sendNotification(){
//        val mediaSession = MediaSessionCompat(this, "music")
        var isPlaying : Boolean = mediaPlayer?.isPlaying == true
        var notifacation = NotificationCompat.Builder(this, "vitality")
            .setSmallIcon(R.drawable.baseline_baby_changing_station_24)
            .setContentTitle("原身")
            .setContentText("Elapsed time : 50")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
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

    fun pausePendingIntent(): PendingIntent{
        val intent_ = Intent(this, AudioService::class.java).apply {
            action = Actions.Pause.toString()
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent : PendingIntent =   PendingIntent.getService(
            this,
            0,
            intent_,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        return pendingIntent



        val intent = Intent(this, CounterNotificationReceiver::class.java).apply {
            action = Actions.Pause.toString()
        }
//        sendBroadcast(intent)
        val incrementIntent = PendingIntent.getBroadcast(
            this,
            2,
//            Intent(this, CounterNotificationReceiver::class.java),
            Intent(this, CounterNotificationReceiver::class.java).apply {
                action = Actions.Pause.toString()
            },

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        return incrementIntent

    }

    fun playPendingIntent(): PendingIntent {
        val intent_ = Intent(this, AudioService::class.java).apply {
            action = Actions.Play.toString()
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getService(
            this,
            0,
            intent_,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        return pendingIntent
    }

    private fun getRawUri(id:Int){
        Uri.parse("android.resource://${packageName}/${id}")
    }

    fun startPlaying() {
        val character = SoundsList.characters.find{ it.name == SoundsList.characterName }
        character?.clips?.get(SoundsList.scenario)?.let { clip ->
            mediaPlayer = MediaPlayer.create(this, clip.resourceId)
        }

//        mediaPlayer= MediaPlayer.create(this, R.raw.videoplayback1)
        mediaPlayer?.isLooping = false // 设置是否循环播放

        // Set an OnCompletionListener to release resources when playback ends
        mediaPlayer?.setOnCompletionListener {
//            stopSelf()
            mediaPlayer?.release() // Release MediaPlayer resources
            mediaPlayer = null // Set the MediaPlayer reference to null
        }

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








}






