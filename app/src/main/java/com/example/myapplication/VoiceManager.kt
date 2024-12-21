package com.example.myapplication

import android.content.Context
import android.media.MediaPlayer


// 定义单个声音文件的数据模型
data class VoiceClip(val resourceId: Int, val description: String)
// 定义一个角色的数据模型，该角色可以有多个声音片段
data class VoiceCharacter(val name: String, val clips: Map<String, VoiceClip>)
class VoiceManager(private val context: Context, val nameVoice: String, val scenario :String) : Thread(){

    // 初始化一些示例语音角色及其对应的多个声音文件
    private val characters = listOf(
        VoiceCharacter(
            "ai",
            mapOf(
                "2m" to VoiceClip(R.raw.ai_2m, "2m"),
                "1m5" to VoiceClip(R.raw.ai_1m5, "1m5"),
                "1m" to VoiceClip(R.raw.ai_1m, "1m"),
                "45s" to VoiceClip(R.raw.ai_45s, "45s"),
                "30s" to VoiceClip(R.raw.ai_30s, "30s"),
                "15s" to VoiceClip(R.raw.ai_15s, "15s"),
                "10to0" to VoiceClip(R.raw.ai_10to0, "10to0"),
                "stop" to VoiceClip(R.raw.ai_stop, "stop"),
                "look1km" to VoiceClip(R.raw.ai_look1km, "look1km"),
                "look3km" to VoiceClip(R.raw.ai_look3km, "look3km"),
                "look5km" to VoiceClip(R.raw.ai_look5km, "look5km"),
                "look_sky" to VoiceClip(R.raw.ai_look_sky, "look_sky"),
            )
        ),
        VoiceCharacter(
            "tongtong~",
            mapOf(
                "2m" to VoiceClip(R.raw.ai_2m, "2m"),
                "1m5" to VoiceClip(R.raw.ai_1m5, "1m5"),
                "1m" to VoiceClip(R.raw.ai_1m, "1m")
            )
        )
// Add more voice characters as needed
    )

    override fun run(){
        playVoice(nameVoice, scenario)
    }

    // 获取某个角色并播放指定情景下的声音文件
    fun playVoice(characterName: String, scenario: String) {
        val character = characters.find { it.name == characterName }
        character?.clips?.get(scenario)?.let { clip ->
            val mediaPlayer = MediaPlayer.create(context, clip.resourceId).apply {
                setOnCompletionListener { it.release() }
                start()
            }

        } ?: run {
            // Handle the case where the character or scenario is not found
            println("Character $characterName or scenario $scenario not found.")
        }
    }

}