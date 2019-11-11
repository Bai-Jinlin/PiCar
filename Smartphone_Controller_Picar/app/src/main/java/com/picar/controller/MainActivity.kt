package com.picar.controller

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    @ExperimentalStdlibApi
    val listener=View.OnTouchListener{v,event-> when (event!!.action) {
            MotionEvent.ACTION_DOWN -> mBinder.sendtoCar((v as Button).text.toString())
            MotionEvent.ACTION_UP -> mBinder.sendtoCar("stop")
            else -> false
        }
    }

    companion object {
        const val TOAST_TEXT = 1
        lateinit var context: Context
        val handle = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    TOAST_TEXT -> context.toast(msg.obj as String)
                }
            }
        }
    }

    private val connect = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBinder = service as RemoteService.sendBinder
        }
    }
    private var isServerStart = false
    private var host: String = "localhost";
    private var port: Int = 0;
    private lateinit var mBinder: RemoteService.sendBinder

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = applicationContext
        host = intent.getStringExtra("IP")!!;
        port = intent.getIntExtra("PORT", 8008)
        startButton.setOnClickListener {
            if (!isServerStart) {
                startService<RemoteService>(
                    "IP" to host,
                    "PORT" to port
                )
                bindService(
                    Intent(this, RemoteService::class.java),
                    connect,
                    Context.BIND_AUTO_CREATE
                )
                startButton.text = "stop"
                isServerStart = true
                leftButton.visibility=View.VISIBLE
                rightButton.visibility=View.VISIBLE
                forwordButton.visibility=View.VISIBLE
                backButton.visibility=View.VISIBLE
            } else {
                stopService(Intent(this, RemoteService::class.java))
                startButton.text = "start"
                unbindService(connect)
                isServerStart = false
                finish()
            }
        }
        leftButton.setOnTouchListener(listener)
        rightButton.setOnTouchListener(listener)
        forwordButton.setOnTouchListener(listener)
        backButton.setOnTouchListener(listener)

    }

}

