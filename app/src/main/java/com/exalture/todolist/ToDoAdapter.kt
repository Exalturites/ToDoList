package com.exalture.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToDoAdapter(context: Context, private var todoList: List<ToDoItem>) :
    RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
        var itemText: TextView = itemView.findViewById(R.id.item_text)
        var closeButton: ImageButton = itemView.findViewById(R.id.close)
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
        val toDoItem = todoList[position]
        val itemId: String? = toDoItem.itemId
        val itemText: String? = toDoItem.itemText
        val done: Boolean? = toDoItem.done
        holder.itemText.text = itemText
        holder.checkbox.isChecked = done!!

        holder.checkbox.setOnClickListener {
            itemRowListener.modifyItemState(itemId!!, !done)
        }

        holder.closeButton.setOnClickListener {
            itemRowListener.onItemDelete(itemId!!)
        }
    }

}