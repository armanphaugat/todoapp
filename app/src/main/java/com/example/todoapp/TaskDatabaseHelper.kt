package com.example.todoapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "TodoList.db", null,1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable="""
            Create table tasks(
            id integer primary key autoincrement,
            name text,
            completed integer
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("Drop Table if exists tasks")
        onCreate(db)
    }
    fun insertTask(task: Task): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", task.name)
            put("completed", if (task.isCompleted) 1 else 0)
        }
        val id = db.insert("tasks", null, values)
        db.close()
        return id
    }
    fun getAllTasks(): MutableList<Task>{
        val taskList = mutableListOf<Task>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks", null)
        if(cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val completed = cursor.getInt(cursor.getColumnIndexOrThrow("completed"))==1
                taskList.add(Task(id, name, completed))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return taskList
    }
    fun updateTask(task: Task){
        val db=writableDatabase
        db.execSQL(
            "UPDATE tasks SET name = ?, completed = ? WHERE id = ?",
            arrayOf(task.name, if (task.isCompleted) 1 else 0, task.id)
        )
        db.close()
    }
    fun deleteTask(id: Int) {
        val db = writableDatabase
        db.execSQL("DELETE FROM tasks WHERE id = ?", arrayOf(id))
        db.close()
    }
}
