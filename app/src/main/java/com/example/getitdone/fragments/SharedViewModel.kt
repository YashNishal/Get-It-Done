package com.example.getitdone.fragments

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.getitdone.R
import com.example.getitdone.data.models.Priority
import com.example.getitdone.data.models.ToDoData


class SharedViewModel(application: Application): AndroidViewModel(application) {
// we needed these  functions in AddFragment and update fragment
    // so we made a SharedViewModel class extending AndroidViewModel
    // and got private val mSharedViewModel : SharedViewModel by viewModels() respectively
    // in each class

    val emptyDatabase : MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDatabaseEmpty(toDoData : List<ToDoData>) {
        emptyDatabase.value = toDoData.isEmpty()
    }


    // when spinner item is selected by user , spinner object receives an on-item-selected even
    val listener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener{
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long)
        {
           when(position) {
               0 -> { (parent?.getChildAt(0) as TextView).setTextColor(
                   ContextCompat.getColor(application, R.color.red)) }
               1 -> { (parent?.getChildAt(0) as TextView).setTextColor(
                   ContextCompat.getColor(application, R.color.yellow)) }
               2 -> { (parent?.getChildAt(0) as TextView).setTextColor(
                   ContextCompat.getColor(application, R.color.green)) }
           }

        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}

    }

    fun verifyDataFromUser(title: String,description: String): Boolean {
        return !(title.isEmpty() || description.isEmpty())
    }

    fun parsePriority(priority: String): Priority {
        return when(priority) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            "Low Priority" -> Priority.LOW
            else -> Priority.LOW
        }
    }

    fun parsePriorityToInt(priority: Priority) : Int {
        return when(priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }
}