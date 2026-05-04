package com.example.todoapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTask: EditText
    private lateinit var btnAdd: Button
    private var taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        editTask = findViewById(R.id.editTask)
        btnAdd = findViewById(R.id.btnAdd)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        taskList = dbHelper.getAllTasks()

        taskAdapter = TaskAdapter(
            tasks = taskList,
            onDeleteClick = { task ->
                dbHelper.deleteTask(task.id)
                taskAdapter.removeTask(task)  // ✅ instant delete
                Toast.makeText(this, "Task deleted ✅", Toast.LENGTH_SHORT).show()
            },
            onCheckChange = { task, isChecked ->
                task.isCompleted = isChecked
                dbHelper.updateTask(task)
                val msg = if (isChecked) "Marked as done ✔️" else "Marked as pending ⏳"
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter = taskAdapter

        btnAdd.setOnClickListener {
            val taskName = editTask.text.toString().trim()
            if (taskName.isNotEmpty()) {
                val newTask = Task(name = taskName, isCompleted = false)
                val id = dbHelper.insertTask(newTask)
                newTask.id = id.toInt()
                taskList.add(newTask)
                taskAdapter.notifyItemInserted(taskList.size - 1)
                Toast.makeText(this, "Task Added Succesfully ✅", Toast.LENGTH_SHORT).show()
                editTask.text.clear()
                recyclerView.scrollToPosition(taskList.size - 1)
            }
        }
    }
}
