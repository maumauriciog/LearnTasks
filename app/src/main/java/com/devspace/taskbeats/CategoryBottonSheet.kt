package com.devspace.taskbeats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CategoryBottSheet(
    private val onClicked: (String) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.category_botton_sheet, container, false)

        val tvNewCategory = view.findViewById<TextView>(R.id.tiv_New_Category)
        val btnCreateCategory = view.findViewById<Button>(R.id.btn_CreateCategory)

        btnCreateCategory.setOnClickListener {
            val nameCategory = tvNewCategory.text.toString()
            onClicked.invoke(nameCategory)
            dismiss()
        }
        return view
    }
}