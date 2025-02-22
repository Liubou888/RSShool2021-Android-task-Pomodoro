package com.rsschool.pomodoro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.rsschool.pomodoro.StopwatchListener
import com.rsschool.pomodoro.StopwatchViewHolder
import com.rsschool.pomodoro.databinding.StopwatchItemBinding
import com.rsschool.pomodoro.modelka.Stopwatch


class StopwatchAdapter(
    private val listener: StopwatchListener
) : ListAdapter<Stopwatch, StopwatchViewHolder>(itemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopwatchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = StopwatchItemBinding.inflate(layoutInflater, parent, false)
        return StopwatchViewHolder(binding, listener, binding.root.context.resources)
    }

    override fun onBindViewHolder(holder: StopwatchViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
    private companion object {
        private val itemComparator = object : DiffUtil.ItemCallback<Stopwatch>() {
            override fun areItemsTheSame(oldItem: Stopwatch, newItem: Stopwatch): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Stopwatch, newItem: Stopwatch): Boolean {
                return oldItem.isStarted == newItem.isStarted && oldItem.currentMs == newItem.currentMs
            }

            override fun getChangePayload(oldItem: Stopwatch, newItem: Stopwatch) = Any()

        }
    }
}