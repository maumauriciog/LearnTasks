@file:OptIn(DelicateCoroutinesApi::class)

package com.devspace.taskbeats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var categories = listOf<CategoryUiData>()
    private var tasks = listOf<TaskUiData>()

    private val categoryAdapter = CategoryListAdapter()
    private val taskAdapter = TaskListAdapter()

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
        val btnFab = findViewById<FloatingActionButton>(R.id.fab)

        btnFab.setOnClickListener {
            val taskButSheet = TaskBotSheet(
                categories
            ) { taskToBeCreated ->
                val newTask = TaskEntity(
                    nameTask = taskToBeCreated.name,
                    nameCategory = taskToBeCreated.category
                )
                insertTaskDb(newTask)
            }

            taskButSheet.show(supportFragmentManager, "taskBoo")
        }

        categoryAdapter.setOnClickListener { selected ->
            if (selected.name == "Add Category...") {
                val categoryBottSheet = CategoryBottSheet { categoryCreated ->
                    val insFromDB = CategoryEntity(
                        name = categoryCreated,
                        isSelected = false
                    )
                    insertCategory(insFromDB)
                }
                categoryBottSheet.show(supportFragmentManager, "categoryBott")
            } else {
                val categoryTemp = categories.map { item ->
                    when {
                        item.name == selected.name && !item.isSelected -> item.copy(isSelected = true)
                        item.name == selected.name && item.isSelected -> item.copy(isSelected = false)
                        else -> item
                    }
                }
                val taskTemp =
                    if (selected.name != "All") {
                        tasks.filter { it.category == selected.name }
                    } else {
                        tasks
                    }
                categoryAdapter.submitList(categoryTemp)
                taskAdapter.submitList(taskTemp)
            }
        }

        rvCategory.adapter = categoryAdapter
        getCategories()
        rvTask.adapter = taskAdapter
        getTasks()
    }

    //get categories
    private fun getCategories() {
        GlobalScope.launch(Dispatchers.IO) {
            val getCategories: List<CategoryEntity> = categoryDao.getAll()
            val uICategories = getCategories.map {
                CategoryUiData(
                    name = it.name, isSelected = it.isSelected
                )
            }
                .toMutableList()
            uICategories.add(
                CategoryUiData(
                    name = "Add Category...",
                    isSelected = false
                )
            )
            GlobalScope.launch(Dispatchers.Main) {
                categories = uICategories
                categoryAdapter.submitList(uICategories)
            }
        }
    }

    //get all tasks from database
    private fun getTasks() {
        GlobalScope.launch(Dispatchers.IO) {
            val getFromDbTasks: List<TaskEntity> = taskDao.getAll()
            val insAdpTasks = getFromDbTasks.map {
                TaskUiData(
                    name = it.nameTask,
                    category = it.nameCategory
                )
            }
            GlobalScope.launch(Dispatchers.Main) {
                tasks = insAdpTasks
                taskAdapter.submitList(insAdpTasks)
            }
        }
    }

    //insert in database
    private fun insertCategory(categoryEntity: CategoryEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            categoryDao.insert(categoryEntity)
            getCategories()
        }
    }

    private fun insertTaskDb (taskEntity: TaskEntity){
        GlobalScope.launch(Dispatchers.IO){
            taskDao.insert(taskEntity)
            getTasks()
        }
    }
}