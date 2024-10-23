@file:OptIn(DelicateCoroutinesApi::class)

package com.devspace.taskbeats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val categories: List<CategoryUiData> = listOf()
    private val tasks: List<TaskUiData> = listOf()

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            TaskBeatsDataBase::class.java, "data-base-task"
        ).build()
    }
    private val categoryDao: CategoryDAO by lazy {
        database.getCategoryDao()
    }
    private val taskDao: TaskDAO by lazy {
        database.getTaskDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvCategory = findViewById<RecyclerView>(R.id.rv_categories)
        val rvTask = findViewById<RecyclerView>(R.id.rv_tasks)

        val taskAdapter = TaskListAdapter()
        val categoryAdapter = CategoryListAdapter()

        categoryAdapter.setOnClickListener { selected ->
            val categoryTemp = categories.map { item ->
                when {
                    item.name == selected.name && !item.isSelected -> item.copy(isSelected = true)
                    item.name == selected.name && item.isSelected -> item.copy(isSelected = false)
                    else -> item
                }
            }
            val taskTemp = if (selected.name != "All") {
                tasks.filter { it.category == selected.name }
            } else {
                tasks
            }
            taskAdapter.submitList(taskTemp)
            categoryAdapter.submitList(categoryTemp)
        }

        rvCategory.adapter = categoryAdapter
        getCategories(categoryAdapter)
        rvTask.adapter = taskAdapter
        getTasks(taskAdapter)
    }

    //get categories
    private fun getCategories(adapter: CategoryListAdapter) {
        GlobalScope.launch(Dispatchers.IO) {
            val getCategories: List<CategoryEntity> = categoryDao.getAll()
            val uICategories = getCategories.map {
                CategoryUiData(
                    name = it.name, isSelected = it.isSelected
                )
            }
            GlobalScope.launch(Dispatchers.Main) {
                adapter.submitList(uICategories)
            }
        }
    }

    //get all tasks from database
    private fun getTasks(adapter: TaskListAdapter) {
        GlobalScope.launch(Dispatchers.IO) {
            val getFromDbTasks: List<TaskEntity> = taskDao.getAll()
            val insAdpTasks = getFromDbTasks.map {
                TaskUiData(
                    name = it.nameTask,
                    category = it.nameCategory
                )
            }
            GlobalScope.launch(Dispatchers.Main) {
                adapter.submitList(insAdpTasks)
            }
        }
    }
}
