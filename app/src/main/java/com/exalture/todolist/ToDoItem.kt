package com.exalture.todolist

class ToDoItem {

    // Same as static in java
    companion object {
        fun create(): ToDoItem = ToDoItem()
    }

    var itemId: String? = null
    var itemText: String? = null
    var done: Boolean? = false
}