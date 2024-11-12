package com.devspace.taskbeats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TaskListAdapter : ListAdapter<TaskUiData, TaskListAdapter.TaskViewHolder>(TaskListAdapter) {
    private lateinit var callBack: (TaskUiData) -> Unit

    fun setOnClick (onClick: (TaskUiData) -> Unit){
        callBack = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task, callBack)
    }

    class TaskViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tvTask = view.findViewById<TextView>(R.id.tv_task_name)
        private val tvCategoryTask = view.findViewById<TextView>(R.id.tv_category_name_noTask)

        fun bind(uiTask: TaskUiData, callback: (TaskUiData) -> Unit) {
            tvTask.text = uiTask.name
            tvCategoryTask.text = uiTask.category

            view.rootView.setOnClickListener {
                callback.invoke(uiTask)
            }
        }
    }

    companion object : DiffUtil.ItemCallback<TaskUiData>() {
        override fun areItemsTheSame(oldItem: TaskUiData, newItem: TaskUiData): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: TaskUiData, newItem: TaskUiData): Boolean {
         return oldItem.name == newItem.name
        }
    }
}