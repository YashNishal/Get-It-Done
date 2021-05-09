package com.example.getitdone.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.getitdone.data.models.ToDoData

@Dao
interface ToDoDao {

    @Query("Select * from todo_table order by id asc")
    fun getAllData(): LiveData<List<ToDoData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoData)

    @Update
    suspend fun updateData(toDoData: ToDoData)

    @Delete
    suspend fun deleteData(toDoData: ToDoData)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()
}