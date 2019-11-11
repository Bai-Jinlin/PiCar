package com.picar.controller

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Message
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.concurrent.thread
import java.net.InetAddress.getAllByName
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import java.net.InetAddress


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RemoteService : Service() {
    lateinit var socket: Socket

    private val mBinder = sendBinder()

    inner class sendBinder : Binder() {
        @ExperimentalStdlibApi
        fun sendtoCar(command: String): Boolean {
            thread {
                val outputStream = socket.getOutputStream()
                val date = command.encodeToByteArray()
                outputStream.write(date, 0, date.size)
                outputStream.flush()
            }
            return true
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }


    fun sendMessage(id: Int, msg: String) {
        val message = Message()
        message.what = id
        message.obj = msg
        MainActivity.handle.sendMessage(message)
    }

    private fun getIpAddr(intent: Intent): String {
        val ip: String = intent.getStringExtra("IP")
        return if (ip.matches(Regex("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"))) {
            ip
        } else {
            val resolve = InetAddress.getAllByName(ip)[0].hostAddress
            resolve
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        thread {
            socket = Socket()
            while (true)
                try {
                    socket.connect(
                        InetSocketAddress(
                            getIpAddr(intent!!),
                            intent.getIntExtra("PORT", 8008)
                        )
                    )
                    break
                } catch (e: Exception) {
                    Thread.sleep(1);
                    continue
                }
            sendMessage(MainActivity.TOAST_TEXT, "connect successfully")
        }

        return super.onStartCommand(intent, flags, startId)
    }

    @ExperimentalStdlibApi
    private fun closeSocket() {
        val date = "shutdown".encodeToByteArray()
        thread {
            val outputStream = socket.getOutputStream()
            outputStream.write(date, 0, date.size)
            outputStream.flush()
            socket.close()
        }
    }

    @ExperimentalStdlibApi
    override fun onDestroy() {
        sendMessage(MainActivity.TOAST_TEXT, "onDestroy executed")
        closeSocket()
    }


}
