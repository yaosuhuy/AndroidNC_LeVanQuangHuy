package com.example.th2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.concurrent.thread
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private var thread1Running = true
    private var thread2Running = true
    private var thread3Running = true
    private var oddNumber = 1
    private var seqNumber = 0

    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textView1 = findViewById(R.id.textView1)
        textView2 = findViewById(R.id.textView2)
        textView3 = findViewById(R.id.textView3)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)

        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    1 -> textView1.text = "Random Number: ${msg.arg1}"
                    2 -> textView2.text = "Odd Number: ${msg.arg1}"
                    3 -> textView3.text = "Sequential Number: ${msg.arg1}"
                }
            }
        }

        button1.setOnClickListener {
            if (thread1Running) {
                thread1Running = false
            } else {
                thread1Running = true
                startThread1()
            }
        }

        button2.setOnClickListener {
            if (thread2Running) {
                thread2Running = false
            } else {
                thread2Running = true
                startThread2()
            }
        }

        button3.setOnClickListener {
            if (thread3Running) {
                thread3Running = false
            } else {
                thread3Running = true
                startThread3()
            }
        }

        // Start threads after a delay of 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("MainActivity", "Starting threads after delay")
            if (thread1Running) startThread1()
            if (thread2Running) startThread2()
            if (thread3Running) startThread3()
        }, 2000)
    }

    private fun startThread1() {
        thread(start = true) {
            while (thread1Running) {
                val randomNumber = Random.nextInt(50, 101)
                handler.sendMessage(handler.obtainMessage(1, randomNumber, 0))
                Thread.sleep(1000)
            }
        }
    }

    private fun startThread2() {
        thread(start = true) {
            while (thread2Running) {
                handler.sendMessage(handler.obtainMessage(2, oddNumber, 0))
                oddNumber += 2
                Thread.sleep(2500)
            }
        }
    }

    private fun startThread3() {
        thread(start = true) {
            while (thread3Running) {
                handler.sendMessage(handler.obtainMessage(3, seqNumber, 0))
                seqNumber += 1
                Thread.sleep(2000)
            }
        }
    }
}