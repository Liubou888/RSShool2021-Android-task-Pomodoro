package com.rsschool.pomodoro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsschool.pomodoro.utilities.COMMAND_ID
import com.rsschool.pomodoro.utilities.COMMAND_START
import com.rsschool.pomodoro.utilities.COMMAND_STOP
import com.rsschool.pomodoro.utilities.STARTED_TIMER_TIME_MS
import com.rsschool.pomodoro.adapters.StopwatchAdapter
import com.rsschool.pomodoro.databinding.ActivityMainBinding
import com.rsschool.pomodoro.modelka.Stopwatch


class MainActivity : AppCompatActivity(), StopwatchListener, LifecycleObserver {
    private lateinit var binding: ActivityMainBinding
    private val stopwatchAdapter = StopwatchAdapter(this)
    private val stopwatches = mutableListOf<Stopwatch>()
    private var nextId = 0
    private var currentId= -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stopwatchAdapter
        }
        binding.addNewStopwatchButton.setOnClickListener {
            val startTime= binding.editTime.text.toString().toLongOrNull()?.times(60000L) ?: 0L
            stopwatches.add(Stopwatch(nextId++, startTime, currentMs = startTime, isStarted = false,false,null))
            stopwatchAdapter.submitList(stopwatches.toList())
        }
    }
    override fun start(id: Int) {
        currentId=id
        changeStopwatch(id,null, true,null)
    }
    override fun stop(id: Int, currentMs: Long,isFinish:Boolean?) {
        if(id==currentId)currentId=-1
        changeStopwatch(id, currentMs, false,isFinish)
    }
    override fun delete(id: Int) {
        if(id==currentId)currentId=-1
        stopwatches.remove(stopwatches.find { it.id == id })
        stopwatchAdapter.submitList(stopwatches.toList())
    }
    private fun changeStopwatch(id: Int, currentMs: Long?, isStarted: Boolean,isFinish:Boolean?) {
        stopwatches.replaceAll{
            when {
                it.id == id -> Stopwatch(it.id, it.startTime,currentMs ?: it.currentMs, isStarted,isFinish?:it.isFinish,it.timer)
                it.isStarted && isFinish==null->{
                    it.timer?.cancel()
                    Stopwatch(it.id,  it.startTime,currentMs ?: it.currentMs, false,isFinish?:it.isFinish,it.timer)
                    }
                else -> {it}
            }
        }
        stopwatchAdapter.submitList(stopwatches.toList())
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
            val startTime = stopwatchAdapter.currentList.find { it.id == currentId }?.currentMs ?: 0L
        if(startTime>0) {
            val startIntent = Intent(this, ForegroundService::class.java)
            startIntent.putExtra(COMMAND_ID, COMMAND_START)
            startIntent.putExtra(STARTED_TIMER_TIME_MS, startTime)
            startService(startIntent)
        }
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)
    }
}