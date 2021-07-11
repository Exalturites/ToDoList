package com.exalture.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class ToDoAdapter(context: Context, private var todoList: List<ToDoItem>) :
    RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindToDoAdapter(toDoItem: ToDoItem) {
            itemView.item_text.text = toDoItem.itemText
            itemView.checkbox.isChecked = toDoItem.done!!

            itemView.checkbox.setOnClickListener {
                itemRowListener.modifyItemState(toDoItem.itemId!!, !toDoItem.done!!)
            }

            itemView.close.setOnClickListener {
                itemRowListener.onItemDelete(toDoItem.itemId!!)
            }

        }
    }

    private var itemRowListener: ItemRowListener = context as ItemRowListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindToDoAdapter(todoList[position])
    }

}