package com.example.th1

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textView = findViewById<TextView>(R.id.TextView01)
    }

    fun readWebpage(view: View) {
        // Start a coroutine for background work
        CoroutineScope(Dispatchers.IO).launch {
            val response = downloadWebPage("https://www.msn.com/vi-vn/news/national/c%E1%BB%B1u-b%C3%AD-th%C6%B0-h%C3%A0-giang-%C4%91%E1%BA%B7ng-qu%E1%BB%91c-kh%C3%A1nh-b%E1%BB%8B-%C4%91%E1%BB%81-ngh%E1%BB%8B-k%E1%BB%B7-lu%E1%BA%ADt/ar-AA1qnLgd?ocid=msedgntp&pc=U531&cvid=508fe19a9a8242cf92f6cd995265ca4b&ei=4")
            // Update UI on the main thread
            withContext(Dispatchers.Main) {
                textView.text = response
            }
        }
    }

    private fun downloadWebPage(urlString: String): String {
        var result = ""
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                result += line
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }
}