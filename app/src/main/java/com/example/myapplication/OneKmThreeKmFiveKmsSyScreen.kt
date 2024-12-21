package com.example.myapplication



import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.os.CountDownTimer
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.draw.shadow
import com.example.myapplication.startTimerForegroundService
import kotlinx.coroutines.flow.MutableStateFlow


var liveDataTimeLeft = MutableLiveData<Long>()
var mTimerManage : TimeManage? = TimeManage(initialTime = 8 * 60 * 1000L) // 10秒倒计时
var liveDataonFinish = MutableLiveData<Boolean>()
var liveDataTip = MutableLiveData<String>()

@Composable
fun OneKmThreeKmFiveKmsSyScreen(modifier: Modifier = Modifier,
                                onNextButtonClicked: () -> Unit,) {
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//    ) { //innerPadding ->

        var timeLeft: Long by remember { mutableLongStateOf(8 * 60 * 1000L) } // State for minutes left
        var tip: String by remember { mutableStateOf("Look 1 KM") }

        val context = LocalContext.current
        var amountInput by remember { mutableStateOf("") }

        var isPlaying by remember { mutableStateOf(false) }
        val imagePlayResource = when (isPlaying) {
            true -> R.drawable.pause
            false -> R.drawable.start
            else -> R.drawable.start
        }

//        SoundsList.scenario = "back"
        val characterName = "ai"
        val lifecycleOwner = LocalLifecycleOwner.current // Get the current lifecycle owner

        liveDataTimeLeft.observe(lifecycleOwner, { value ->
            timeLeft = value.toLong()
            if(timeLeft / 1000L >= 7 * 60 )      tip = "Look 1 KM"
            else if(timeLeft / 1000L >= 6 * 60 ) tip = "Look 3 KM"
            else if(timeLeft / 1000L >= 5 * 60 ) tip = "Look 5 KM"
            else if(timeLeft / 1000L >= 3 * 60 ) tip = "Look SKY"
            else if(timeLeft / 1000L >= 2 * 60 ) tip = "Look 5 KM"
            else if(timeLeft / 1000L >= 1 * 60 ) tip = "Look 3 KM"
            else if(timeLeft / 1000L >= 0 * 60 ) tip = "Look 1 KM"
        })
//        liveDataTip.observe(lifecycleOwner, { value: String ->
//            tip = value
//        })
        liveDataonFinish.observe(lifecycleOwner, { value: Boolean ->
            if (value == true) {
//                VoiceManager(context, characterName, "stop").start()
                isPlaying = false
                tip = "Look 1 KM"
            }else{
                isPlaying = true
            }
        })





        Surface() {


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // 自动将内容推到两边
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top,
                ) {
                    Settings(onNextButtonClicked = onNextButtonClicked)
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top,
                ) {
                    InfoButtonOneKmThreeKmFiveKmSky()
                }

            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
            ) {
                DoubleClickButton(context, characterName)
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
            ) {



//            DrawColorRing(300.dp, minutesLeft.toFloat() / (8 * 60) * 360f, tip)
                DrawCircleProgressBar(process = timeLeft.toFloat() / (8 * 60 * 1000), tip = tip)


                Spacer(modifier = Modifier.height(200.dp))

//                Row(
//                    horizontalArrangement = Arrangement.Center,//.SpaceBetween // 自动将内容推到两边
//                ) {



//                    Button( // restart
//                        modifier = Modifier.weight(1f),
//                        onClick = {
//
////                        if(!playing) {
//                            minutesLeft = 8 * 60L
//                            VoiceManager(context, "ai", "look1km").start()
//                            tip = "1 KM"
//                            mTimerManage?.restart()
//                            playing = true
//
////                        }
//
//                        },
//                        // 根据主题状态定义按钮颜色
//                        colors = ButtonDefaults.buttonColors(
////                        containerColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray, // 按钮背景色
////                            containerColor = MaterialTheme.colorScheme.primaryContainer,
//                            containerColor = Color.Transparent,
////                       contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black // 按钮文本色
////                       contentColor =  MaterialTheme.colorScheme.primary,
//
//                        )
//
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.restart),
//                            contentDescription = "Restart Icon"
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.padding(24.dp))

                ElevatedButton( //play
//                        modifier = Modifier.weight(1f),
                        shape = CircleShape,
                        border= BorderStroke(1.dp, Color.Transparent),
                        contentPadding = PaddingValues(0.dp),  //avoid the little icon
                        onClick = {
                            if (isPlaying == false) {
                                if (timeLeft == 8 * 60 * 1000L) {
//                                    VoiceManager(context, characterName, "look1km").start()
                                    playMusic(context, characterName, "look1km")
                                }
                                startTimerForegroundService(context)
//                                mTimerManage?.start()
//                                isPlaying = true

                            } else {
                                pauseTimerForegroundService(context)
//                                mTimerManage?.pause()
//                                isPlaying = false

                            }

                        },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.Transparent, // 按钮背景色
//                        contentColor = Color.Black        // 按钮文本色                   )
//                    ),

                    ) {
//                        Spacer(modifier = Modifier.width(8.dp)) // Add space between text and image
                        Image(
//                            painter = painterResource(imagePlayResource),
//                            painter = painterResource(if(isPlaying) R.drawable.pause else R.drawable.start),
                            painter = painterResource(if(isPlaying) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24),
                            contentDescription = "start Icon",
                            modifier = Modifier.size(70.dp) ,// Scale down the image size)
                        )
                    }
//                    Spacer(modifier = Modifier.padding(24.dp))
//                }
             }

//            Column {
//                ServicesUI(context)
//            }

        }




//    }
}





//@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        OneKmThreeKmFiveKmsSyScreen(onNextButtonClicked = {},)
    }
}


@Preview
@Composable
fun DrawColorRingView()
{
    DrawColorRing(300.dp,240f, "")
}

@Composable
fun DrawCircleProgressBar(
    modifier: Modifier = Modifier,
    process: Float, tip: String) {
    Box(//Box
//        modifier= modifier.padding()
//            .pointerInput(Unit) {
//            detectTapGestures {
//                log = "Tap!"
//            }
//        }

    ){
        CircularProgressIndicator(
            progress = { process },
            modifier = Modifier.width(280.dp).align(Alignment.TopCenter) ,// 修改为顶部中心对齐
                //.padding(top = 16.dp),       // 可选：添加一些顶部内边距//.align(Alignment.Center),
            color = Color(0xFF6200EE),
//            color = Color(0xFF85703e),
            strokeWidth = 5.dp
        )


            Text(
                text = tip,
                modifier = Modifier.padding(top= 120.dp)
                    .align(Alignment.Center) ,// 将文本居中对齐
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )


    }


}

@Composable
fun DoubleClickButton(context: Context, characterName: String){
    OutlinedButton(
        onClick = {
            vibrate(context)

            playMusic(context, characterName, "look1km")

            restartTimerForegroundService(context)
        },
        modifier= Modifier.size(280.dp),  //avoid the oval shape
        shape = CircleShape,
        border= BorderStroke(1.dp, Color.Transparent),
//        border= BorderStroke(1.dp, Color(0xFF85703e)),
        contentPadding = PaddingValues(0.dp),  //avoid the little icon
        colors = ButtonDefaults.outlinedButtonColors(contentColor =  Color.Blue)
    ) {
//        Icon(Icons.Default.Add, contentDescription = "content description")
    }

}
/**
 * 环形进度条描绘
 * @param boxWithDp 控件大小
 * @param process 进度所占角度大小
 */
@Composable
fun DrawColorRing(boxWithDp:Dp,process: Float, tip: String) {
    Box(
        modifier = Modifier.size(boxWithDp+30.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(boxWithDp)) {
            //背景
            drawArc(
                Color.LightGray,
                startAngle = 270f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(80f, cap = StrokeCap.Round),
            )

            drawArc(
                brush = Brush.sweepGradient(listOf(Color.Cyan, Color.Yellow, Color.Cyan, ), Offset(300.dp.toPx() / 2f, 300.dp.toPx() / 2f)),
                startAngle = 270f,
                sweepAngle = process,
                useCenter = false,
                style = Stroke(
                    width = 81f,
//                    cap = StrokeCap.Round
                )
            )
        }

        // Centered text
        Text(
            text = tip,
            modifier = Modifier.align(Alignment.Center),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black // Change color as needed
        )
    }
}


@Composable
fun InfoDialogOneKmThreeKmFiveKmSky(
    shouldShowDialog: MutableState<Boolean>) {
    if (shouldShowDialog.value) { // 2
        AlertDialog( // 3
            onDismissRequest = { // 4
                shouldShowDialog.value = false
            },
            // 5
            title = { Text(text = "Why do this?") },
            text = { Text(text = "    「20-20-20 護眼法則」是指：在看近（看書／做功課／使用電話／Ipad/ 電腦) 時，每隔20分鐘休息 20 秒，期間望向 20 尺 (～ 6 m ) 外的景物，讓眼睛得到適當放鬆。這是由一位美國的視光師 Jeffrey Anshel 提出，舒緩「視疲勞」的方法。「20-20-20 護眼法則」易於記憶 ，有研究顯示在長時間使用電腦時，期間看看遠的東西可有效舒緩「視疲勞」。*\n" +
                    "不過有些研究指出，看遠 20 秒對放鬆眼睛的效果有限。要完全放鬆眼睛，最少需要看遠 3 分鐘以上。" +
                    "眼睛交替性斜视：手指前后动，球左右动。\n   " +
                    " 缓解视疲劳：从1KM到3KM到5KM，每个停留20～60s，等待2-3分钟再看回来. \n    " +
                    "The doctor tells me this method and After my practicing, and it worked. The science source has not been found from my knowledge.") },
            confirmButton = { // 6
                Button(
                    onClick = {
                        shouldShowDialog.value = false
                    }
                ) {
                    Text(
                        text = "Confirm",
                        color = Color.White
                    )
                }
            }
        )
    }


}

fun vibrate(context: Context){
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if(Build.VERSION.SDK_INT >= 26){
        vibrator.vibrate(
            VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
        )
    }
    else{
        vibrator.vibrate(200)
    }

}

fun playMusic(context: Context, characterName: String, scenario :String){
    SoundsList.characterName = characterName
    SoundsList.scenario = scenario

    Intent (context, AudioService::class.java).also {
        it.action = AudioService.Actions.Play.toString()
        context.startService(it)
    }

}

fun startTimerForegroundService(context: Context){
    Intent (context, TimerForegroundService::class.java).also {
        it.action = TimerForegroundService.Actions.START.toString()
        context.startService(it)
    }
}

fun pauseTimerForegroundService(context: Context){
    Intent (context, TimerForegroundService::class.java).also {
        it.action = TimerForegroundService.Actions.Pause.toString()
        context.startService(it)
    }
}

fun restartTimerForegroundService(context: Context){
    Intent (context, TimerForegroundService::class.java).also {
        it.action = TimerForegroundService.Actions.Restart.toString()
        context.startService(it)
    }
}

fun soundsSceme(){
//    when (timeLeft.toDouble() / 60 / 1000) {
//        7.0 -> {
//            VoiceManager(context, characterName, "look3km").start()
//            tip = "3 KM"
//        }
//
//        6.0 -> {
//            VoiceManager(context, characterName, "look5km").start()
//            tip = "5 KM"
//        }
//
//        5.0 -> {
//            VoiceManager(context, characterName, "look_sky").start()
//            tip = "SKY"
//        }
//
//        4.5 -> {
//            VoiceManager(context, characterName, "1m5").start()
//            tip = "SKY"
//        }
//
//        4.0 -> {
//            VoiceManager(context, characterName, "1m").start()
//            tip = "SKY"
//        }
//
//        3.0 -> {
//            VoiceManager(context, characterName, "look5km").start()
//            tip = "5 KM"
//        }
//
//        2.0 -> {
//            VoiceManager(context, characterName, "look3km").start()
//            tip = "3KM"
//        }
//
//        1.0 -> {
//            VoiceManager(context, characterName, "look1km").start()
//            tip = "1 KM"
//        }
//    }
//    if (timeLeft / 60L / 1000 != 4L) {
//        when (timeLeft / 1000 - timeLeft / 1000 / 60L * 60) {
//            45L -> playMusic(context, characterName, "45s")
//            30L -> playMusic(context, characterName, "30s")
//            15L -> playMusic(context, characterName, "15s")
//            10L -> playMusic(context, characterName, "10to0")
//        }
//    }


}
@Composable
fun ServicesUI(context: Context) {
    // on below line creating variable
    // for service status and button value.
    val serviceStatus = remember {
        mutableStateOf(false)
    }
    val buttonValue = remember {
        mutableStateOf("Start Service")
    }

    val maxDuration = MutableStateFlow(0f)
    val currentDuration = MutableStateFlow(0f)
    var service: AudioService? = null
    var isBuond = false

    val connection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {

            service = (binder as AudioService.MusicBinder).getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
            isBuond = false
        }

    }

    // on below line we are creating a column,
    Column(
        // on below line we are adding a modifier to it,
        modifier = Modifier
            .fillMaxSize()
            // on below line we are adding a padding.
            .padding(all = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // on the below line we are adding a text for heading.
        Text(
            // on below line we are specifying text
            text = "Services in Android",
            // on below line we are specifying text color,
            // font size and font weight
            color = Color.Green,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            if (serviceStatus.value) {
                // service already running
                // stop the service
                serviceStatus.value = !serviceStatus.value
                buttonValue.value = "Start Service"

                // starting the service
//                val intent = Intent(context, AudioService::class.java)
//                context.stopService(intent)
//                context.unbindService(connection)

                Intent (context, AudioService::class.java).also {
                    it.action = AudioService.Actions.STOP.toString()

                    context.startService(it)
                }




            } else {
                // service not running start service.
                serviceStatus.value = !serviceStatus.value
                buttonValue.value = "Stop Service"

                // starting the service
//                val intent = Intent(context, AudioService::class.java)
//                context.startService(intent)
//                context.startForegroundService(intent)
//                context.bindService(intent, connection, BIND_AUTO_CREATE)

                Intent (context, AudioService::class.java).also {
                    it.action = AudioService.Actions.START.toString()
                    context.startService(it)
                }

            }

        }) {
            // on below line creating a text for our button.
            Text(
                // on below line adding a text,
                // padding, color and font size.
                text = buttonValue.value,
                modifier = Modifier.padding(10.dp),
                color = Color.White,
                fontSize = 20.sp
            )
        }
    }
}



@Composable
fun Settings(modifier: Modifier = Modifier,
             onNextButtonClicked: () -> Unit,
){

//    Row (
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(top = 25.dp), // Add top padding here,
//        horizontalArrangement = Arrangement.Start, // Arrange items to the end (right)
//        verticalAlignment = Alignment.Top // Align items to the top
//
//    ){

        IconButton(onClick =  onNextButtonClicked ) {
            Icon(
                Icons.Filled.Settings,
                contentDescription = "Localized description"
            )
        }




//    }



}

@Composable
fun InfoButtonOneKmThreeKmFiveKmSky(modifier: Modifier = Modifier,
){

        val shouldShowDialog = remember { mutableStateOf(false) } // 1

        if (shouldShowDialog.value) {
            InfoDialogOneKmThreeKmFiveKmSky(shouldShowDialog = shouldShowDialog)
        }

        IconButton(
            onClick = { shouldShowDialog.value = true },
            ) {
            Icon(
                Icons.Filled.Info,
                contentDescription = "Why do this?"
            )
        }

}



class TimeManage(

    private val initialTime: Long, // 初始时间（毫秒）
    private val tickInterval: Long = 1000L // 每次更新的间隔（毫秒）
) {
    private var timer: CountDownTimer? = null
    private var isPause = false
    private var timeRamin = initialTime

    fun start() {
        timer?.cancel() // Cancel any existing timer before starting a new one

        timer = object : CountDownTimer(timeRamin, tickInterval) {
            override fun onTick(millisUntilFinished: Long) {
                println("剩余时间: ${millisUntilFinished / 1000} 秒")
                timeRamin = millisUntilFinished
                liveDataTimeLeft.postValue(millisUntilFinished / 1000)
            }

            override fun onFinish() {
                timeRamin = initialTime
                liveDataTimeLeft.postValue(initialTime)
                liveDataonFinish.postValue(true)
            }

        }
        timer?.start()
    }


    fun pause(){
        timer?.cancel()
        isPause = true
    }

    fun restart(){
        isPause = false
        timeRamin = initialTime
        start()
    }
}
