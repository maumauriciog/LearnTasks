package com.devspace.taskbeats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CategoryListAdapter : ListAdapter<CategoryUiData, CategoryListAdapter.CategoryViewHolder>(CategoryListAdapter) {
    private lateinit var onClicked: (CategoryUiData) -> Unit

    fun setOnClickListener(onClick: (CategoryUiData) -> Unit) {
        this.onClicked = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categories = getItem(position)
        holder.bind(categories, onClicked)
    }

    class CategoryViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private var rvCategory = view.findViewById<TextView>(R.id.tv_itemCategory)

        fun bind(categories: CategoryUiData, onClick: (CategoryUiData) -> Unit) {
            rvCategory.text = categories.name
            rvCategory.isSelected = categories.isSelected

            view.setOnClickListener {
                onClick.invoke(categories)
            }
        }
    }

    companion object : DiffUtil.ItemCallback<CategoryUiData>() {
        override fun areItemsTheSame(oldItem: CategoryUiData, newItem: CategoryUiData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CategoryUiData, newItem: CategoryUiData): Boolean {
            return oldItem.name == newItem.name
        }
    }

}