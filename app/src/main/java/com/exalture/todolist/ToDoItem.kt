package com.exalture.todolist

class ToDoItem {

    companion object Factory {
        fun create(): ToDoItem = ToDoItem()
    }

    var itemId: String? = null
    var itemText: String? = null
    var done: Boolean? = false
}