package com.rsschool.pomodoro.utilities

import android.os.CountDownTimer


fun getTimer(startTime: Long, interval:Long, tick: (Long) -> Unit, finish: () -> Unit): CountDownTimer {
    return object : CountDownTimer(startTime, interval) {
        override fun onTick(millisUntilFinished: Long) {
            tick(millisUntilFinished)
        }
        override fun onFinish() {
            finish()
        }

    }
}



