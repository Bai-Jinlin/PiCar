package com.picar.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_start.*
import org.jetbrains.anko.startActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        port.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE ->
                    toActivity()
            }
            false
        }
        start.setOnClickListener {
            toActivity()
        }
    }

    private fun toActivity() {
        startActivity<MainActivity>(
            "IP" to ipaddr.text.toString(),
            "PORT" to port.text.toString().toInt()
        )
        finish()
    }
}
