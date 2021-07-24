package com.rsschool.pomodoro.modelka

import android.os.CountDownTimer

data class Stopwatch(
    val id: Int,
    val startTime:Long,
    var currentMs: Long,
    var isStarted: Boolean =false,
    var isFinish: Boolean =false,
    var timer: CountDownTimer?
)