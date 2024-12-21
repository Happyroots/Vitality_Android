package com.example.myapplication

object SoundsList {
    var timeLeft : Long = 8 * 60 *1000
//    var nameVoice = "ai"
    var characterName = "ai"
    var scenario = "10to0"

    // 定义单个声音文件的数据模型
//    data class VoiceClip(val resourceId: Int, val description: String)

//    class SoundsList(){
        // 初始化一些示例语音角色及其对应的多个声音文件
        val characters = listOf(
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
                    "back" to VoiceClip(R.raw.videoplayback1, "back"),
                )
            ),

            // Add more voice characters as needed
            VoiceCharacter(
                "tongtong~",
                mapOf(
                    "2m" to VoiceClip(R.raw.ai_2m, "2m"),
                    "1m5" to VoiceClip(R.raw.ai_1m5, "1m5"),
                    "1m" to VoiceClip(R.raw.ai_1m, "1m")
                )
            ),


        )
//    }

}