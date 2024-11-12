package com.devspace.taskbeats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class TaskBotSheet(
    private val categories: List<CategoryUiData>,
    private val task: TaskUiData? = null,
    private val onClicked: (TaskUiData) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.task_botton_sheet, container, false)

        val tltTask = view.findViewById<TextView>(R.id.tlt_task)
        val spLstCategory = view.findViewById<Spinner>(R.id.sp_list_categories)
        val tivNewTask = view.findViewById<TextInputEditText>(R.id.tiv_new_task)
        val btnCrTask = view.findViewById<Button>(R.id.btn_CrTask)

        if (task == null) {
            tltTask.setText(R.string.title_new_task)
            btnCrTask.setText(R.string.create_new_task)
        } else {
            tltTask.setText(R.string.title_update_task)

            btnCrTask.setText(R.string.title_update_task)
        }

        var taskCategory: String? = null

        btnCrTask.setOnClickListener {
            val name = tivNewTask.text.toString()
            if (taskCategory != null) {
                onClicked.invoke(
                    TaskUiData(
                        id = 0,
                        name = name,
                        category = requireNotNull(taskCategory)
                    )
                )
                dismiss()
            } else {
                Snackbar.make(
                    btnCrTask,
                    "Please, select categories or write new task...",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        val categories: List<String> = categories.map { it.name }
        ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_item,
            categories.toList()
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            spLstCategory.adapter = adapter
        }

        spLstCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                taskCategory = categories[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        return view
    }
}