package com.exalture.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.exalture.todolist.Constants.FIREBASE_ITEM
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), ItemRowListener {
    private var toDoItemList = ArrayList<ToDoItem>()
    private lateinit var toDoAdapter: ToDoAdapter

    //Get Access to Firebase database, no need of any URL, Firebase
    //identifies the connection via the package name of the app
    private lateinit var mDatabase: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.task_recyclerview)
        toDoAdapter = ToDoAdapter(this, toDoItemList)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = toDoAdapter
        mDatabase = FirebaseDatabase.getInstance().reference

        //reference for FAB
        val fab: FloatingActionButton = findViewById(R.id.fab)

        //Adding click listener for FAB
        fab.setOnClickListener {
            //Show Dialog here to add new Item
            addNewItemDialog()
        }

        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                toDoItemList.clear()
                addDataToList(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                // Getting Item failed, log a message
                Log.w("MainActivity", "loadItem:onCancelled", error.toException())            }
        })
    }

    private fun addDataToList(dataSnapshot: DataSnapshot) {
        val items = dataSnapshot.children.iterator()
        //Check if current database contains any collection
        if (items.hasNext()) {
            val toDoListIndex = items.next()
            val itemsIterator = toDoListIndex.children.iterator()

            //check if the collection has any to do items or not
            while (itemsIterator.hasNext()) {
                //get current item
                val currentItem = itemsIterator.next()
                //calling companion object
                val todoItem = ToDoItem.create()
                //get current data in a map
                val map = currentItem.value as HashMap<*, *>
                //key will return Firebase ID
                todoItem.itemId = currentItem.key
                todoItem.done = map["done"] as Boolean?
                todoItem.itemText = map["itemText"] as String?
                toDoItemList.add(todoItem)
            }
        }
        //alert adapter that has changed
        toDoAdapter.notifyDataSetChanged()
    }

    /**
     * This method will show a dialog box where user can enter new item
     * to be added
     */
    private fun addNewItemDialog() {
        val alert = AlertDialog.Builder(this)
        val itemEditText = EditText(this)
        alert.setMessage(getString(R.string.add_new_item))
        alert.setTitle(getString(R.string.enter_to_do_text))
        alert.setView(itemEditText)
        alert.setPositiveButton(getString(R.string.submit)) { dialog, _ ->
            val todoItem = ToDoItem.create()
            todoItem.itemText = itemEditText.text.toString()
            todoItem.done = false
            //We first make a push so that a new item is made with a unique ID
            val newItem = mDatabase.child(FIREBASE_ITEM).push()
            todoItem.itemId = newItem.key
            //then, we used the reference to set the value on that ID
            newItem.setValue(todoItem)

            dialog.dismiss()
            Toast.makeText(this, getString(R.string.saved_with_id) + todoItem.itemId, Toast.LENGTH_SHORT).show()
        }
        alert.show()
    }

    override fun modifyItemState(itemObjectId: String, isDone: Boolean) {
        val itemReference = mDatabase.child(FIREBASE_ITEM).child(itemObjectId)
        itemReference.child("done").setValue(isDone)
    }

    override fun onItemDelete(itemObjectId: String) {
        //get child reference in database via the ObjectID
        val itemReference = mDatabase.child(FIREBASE_ITEM).child(itemObjectId)
        //deletion can be done via removeValue() method
        itemReference.removeValue()    }
}