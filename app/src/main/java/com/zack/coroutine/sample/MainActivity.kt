package com.zack.coroutine.sample

import android.app.ActivityManager
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.zack.coroutine.sample.ui.theme.CoroutineSampleTheme
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoroutineSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        Greeting("Android")
//                        LastExitReason()
//                        ExitMySelf(0)
//                        ExitMySelf(1)
//                        ExitMySelf(-1)
//                        KillMySelf()
                        HowCoroutineHandleException()
                    }
                }
            }
        }

//        triggerBluetoothApiAccess()
//        findHowCoroutineHandleException()
    }

    private fun triggerBluetoothApiAccess() {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            (getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)
                ?.adapter?.bluetoothLeScanner?.let { adapter ->
                    val scanCallback = object : ScanCallback() {}
                    adapter.startScan(scanCallback)
                    handler.postDelayed({ adapter.stopScan(scanCallback) }, 5000L)
                }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun KillMySelf() {
    Button(onClick = { android.os.Process.killProcess(android.os.Process.myPid()) }) {
        Text(text = "Kill mySelf")
    }
}

@Composable
fun LastExitReason() {
    val context = LocalContext.current
    val lastExitInfo =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            (context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)
                ?.getHistoricalProcessExitReasons(context.packageName, 0, 1)
                ?.getOrNull(0)
        else null
    Text(text = "LastExitReason: $lastExitInfo")
}

@Composable
fun ExitMySelf(status: Int) {
    Button(onClick = { exitProcess(status) }) {
        Text(text = "Exit mySelf with status: $status")
    }
}

private fun findHowCoroutineHandleException() {
//    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
//        printWhereAmI()
//        Log.e("CoroutineExceptionHandling", "handleException, coroutineContext: $${coroutineContext.contentToString()}, throwable: $throwable", Throwable("Where is handleException"))
//    }
//
//    val rootScope = CoroutineScope(Dispatchers.Default)
//    val rootScope = CoroutineScope(Dispatchers.Default + exceptionHandler)

//    rootScope.launch {
//        printWhereAmI()
//        throw Exception("Exception in rootScope launch")
//    }
//    rootScope.launch {
//        printWhereAmI()
//        launch(Dispatchers.Main) {
//            printWhereAmI()
//            throw Exception("Exception in subScope 1 launch")
//        }
//    }

//    var deferred: Deferred<*>? = null

//    deferred = rootScope.async {
//        throw Exception("Exception in rootScope async")
//    }
//    rootScope.launch {
//        deferred = async {
//            throw Exception("Exception in subScope 1 async")
//        }
//    }

//    deferred = rootScope.async {
//        launch {
//            throw Exception("Exception in subScope 1 launch")
//        }
//    }

//    rootScope.launch {
//        try {
//            deferred?.await()
//        } catch (e: Exception) {
//            Log.e("CoroutineExceptionHandling", "catchException, coroutineContext: $coroutineContext, throwable: $e", Throwable("Where is catchException"))
//        }
//    }

//    val supervisorContext = SupervisorJob() + Dispatchers.Default + exceptionHandler
//
//    rootScope.launch(supervisorContext) {
//        Log.i("CoroutineExceptionHandling", "in rootScope launch, context: ${coroutineContext.contentToString()}")
//        launch {
//            Log.i("CoroutineExceptionHandling", "in subScope 1 launch, context: ${coroutineContext.contentToString()}")
//            launch(supervisorContext) {
//                Log.i("CoroutineExceptionHandling", "in subScope 2 launch, context: ${coroutineContext.contentToString()}")
//                throw Exception("Exception in subScope 2 launch")
//            }
//            launch(supervisorContext) {
//                Log.i("CoroutineExceptionHandling", "in subScope 3 launch, context: ${coroutineContext.contentToString()}")
//                throw Exception("Exception in subScope 3 launch")
//            }
//        }
//    }

//    val deferred = GlobalScope.async {
//        throw RuntimeException("Exception in root async")
//    }
//    GlobalScope.launch {
//        try {
//            deferred.await()
//        } catch (e: RuntimeException) {
//            Log.e("CoroutineExceptionHandling", "caught exception on await", e)
//        }
//    }

    var deferred: Deferred<*>? = null
    GlobalScope.launch {
        deferred = async {
            throw RuntimeException("Exception in child async")
        }
    }
    GlobalScope.launch {
        try {
            deferred?.await()
        } catch (e: RuntimeException) {
            Log.e("CoroutineExceptionHandling", "caught exception on await", e)
        }
    }
}

private fun CoroutineContext.contentToString() =
    "[" + fold("") { acc, element ->
        if (acc.isEmpty()) "${element.key}=$element" else "$acc, ${element.key}=$element"
    } + "]"

private fun printWhereAmI() {
    Log.i("CoroutineExceptionHandling", "run in ${Thread.currentThread().name}")
}

@Composable
fun HowCoroutineHandleException() {
    Button(onClick = { findHowCoroutineHandleException() }) {
        Text(text = "How coroutine handle exception")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CoroutineSampleTheme {
        Column {
            Greeting("Android")
//            LastExitReason()
//            ExitMySelf(0)
//            ExitMySelf(1)
//            ExitMySelf(-1)
//            KillMySelf()
            HowCoroutineHandleException()
        }
    }
}