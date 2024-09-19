package com.example.th3

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var timePicker: TimePicker
    private lateinit var numberPicker: NumberPicker
    private lateinit var setAlarmButton: Button
    private lateinit var stopAlarmButton: Button
    private var vibrator: Vibrator? = null
    private var handler = Handler(Looper.getMainLooper())
    private var alarmRunnable: Runnable? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        timePicker = findViewById(R.id.timePicker)
        numberPicker = findViewById(R.id.numberPicker)
        setAlarmButton = findViewById(R.id.setAlarmButton)
        stopAlarmButton = findViewById(R.id.stopAlarmButton)
        vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        numberPicker.minValue = 1
        numberPicker.maxValue = 5
        numberPicker.wrapSelectorWheel = true

        numberPicker.displayedValues = arrayOf("1", "2", "3", "4", "5")

        setAlarmButton.setOnClickListener {
            setAlarm()
        }

        stopAlarmButton.setOnClickListener {
            stopAlarm()
        }
    }


    private fun setAlarm() {
        val calendar = Calendar.getInstance()

        // Lấy thời gian hiện tại
        val currentTime = calendar.timeInMillis

        // Đặt thời gian báo thức
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
        calendar.set(Calendar.MINUTE, timePicker.minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // Lấy thời gian báo thức
        val alarmTime = calendar.timeInMillis

        // Nếu thời gian báo thức đã qua trong ngày, thì đặt vào ngày hôm sau
        if (alarmTime < currentTime) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val delay = calendar.timeInMillis - currentTime

        // Chạy báo thức sau thời gian tính toán
        alarmRunnable = Runnable { startAlarm() }
        handler.postDelayed(alarmRunnable!!, delay)

        stopAlarmButton.visibility = Button.VISIBLE
    }

    private fun startAlarm() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Rung theo chu kỳ với các khoảng tắt và bật
            val vibrationEffect = VibrationEffect.createWaveform(longArrayOf(0, 500, 1000), 0) // Lặp vô hạn
            vibrator?.vibrate(vibrationEffect)
        } else {
            vibrator?.vibrate(longArrayOf(0, 500, 1000), 0) // Deprecated nhưng vẫn dùng ở các API cũ hơn
        }
    }

    private fun stopAlarm() {
        vibrator?.cancel()
        handler.removeCallbacks(alarmRunnable!!)
        stopAlarmButton.visibility = Button.GONE
    }
}