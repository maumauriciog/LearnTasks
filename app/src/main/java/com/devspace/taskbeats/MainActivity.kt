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

        insertCategories()
        insertTask()

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

    //insert categories in to database
    private fun insertCategories() {
        GlobalScope.launch(Dispatchers.IO) {
            val insCategory = categories.map {
                CategoryEntity(
                    name = it.name, isSelected = it.isSelected
                )
            }
            categoryDao.insert(insCategory)
        }
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

    //insert tasks in to database
    private fun insertTask() {
        GlobalScope.launch(Dispatchers.IO) {
            val insTasks = tasks.map {
                TaskEntity(
                    nameCategory = it.category, nameTask = it.name
                )
            }
            taskDao.insert(insTasks)
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

val categories = listOf(
    CategoryUiData(
        name = "All", isSelected = false
    ),
    CategoryUiData(
        name = "STUDY", isSelected = false
    ),
    CategoryUiData(
        name = "WORK",
        isSelected = false
    ),
    CategoryUiData(
        name = "WELLNESS",
        isSelected = false
    ),
    CategoryUiData(
        name = "HOME",
        isSelected = false
    ),
    CategoryUiData(
        name = "HEALTH",
        isSelected = false
    )
)

val tasks = listOf(
    TaskUiData(
        "Ler 10 páginas do livro atual", "STUDY"
    ),
    TaskUiData(
        "45 min de treino na academia", "HEALTH"
    ),
    TaskUiData(
        "Correr 5km", "HEALTH"
    ),
    TaskUiData(
        "Meditar por 10 min", "WELLNESS"
    ),
    TaskUiData(
        "Silêncio total por 5 min", "WELLNESS"
    ),
    TaskUiData(
        "Descer o livo", "HOME"
    ),
    TaskUiData(
        "Tirar caixas da garagem", "HOME"
    ),
    TaskUiData(
        "Lavar o carro", "HOME"
    ),
    TaskUiData(
        "Gravar aulas DevSpace", "WORK"
    ),
    TaskUiData(
        "Criar planejamento de vídeos da semana", "WORK"
    ),
    TaskUiData(
        "Soltar reels da semana", "WORK"
    ),
)