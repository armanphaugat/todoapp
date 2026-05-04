package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onDeleteClick: (Task) -> Unit,
    private val onCheckChange: (Task, Boolean) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkboxTask: CheckBox = itemView.findViewById(R.id.checkboxTask)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.checkboxTask.text = task.name
        holder.checkboxTask.isChecked = task.isCompleted
        holder.checkboxTask.setOnCheckedChangeListener { _, isChecked ->
            onCheckChange(task, isChecked)
        }
        holder.btnDelete.setOnClickListener {
            onDeleteClick(task)
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: MutableList<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
    fun removeTask(task: Task) {
        val position = tasks.indexOf(task)
        if (position != -1) {
            tasks.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
