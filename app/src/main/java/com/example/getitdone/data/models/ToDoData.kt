package com.example.getitdone.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.getitdone.data.models.Priority
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "todo_table")
@Parcelize
data class ToDoData(

    var title: String,
    // we have used Converter to convert Priority enum class object to string so
    // that it can be stored in the database
    var priority: Priority,
    var description: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int
) : Parcelable