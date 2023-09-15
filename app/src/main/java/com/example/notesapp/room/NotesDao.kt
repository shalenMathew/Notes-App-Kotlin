package com.example.notesapp.room

import android.widget.ListView
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notesapp.room.model.NotesModel


@Dao
interface NotesDao {


    //OnConflictStrategy.REPLACE is used, which means that if there is a conflict (e.g., if a record with the same
    // primary key already exists), the new data will replace the existing data. This strategy is useful for ensuring
    // that the database contains the most up-to-date information.

  //  By default, if you don't specify an onConflict strategy, Room will use the OnConflictStrategy.ABORT strategy,
    //  which means that if there is a conflict (e.g., if a record with the same primary key already exists), the
    //  insert operation will be aborted, and the new data will not be inserted.

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertNote(notes :NotesModel) // sus[pend means this fun can be paused and resumed by coroutine

    @Update
   suspend fun updateNote(notes: NotesModel)

    @Delete
  suspend  fun deleteNote(notesModel: NotesModel)

    @Query(" DELETE FROM notes_table ")
     fun deleteAll()

    @Query(" SELECT * FROM notes_table ")
     fun getAllNotes():LiveData<List<NotesModel>>

     @Query("SELECT * FROM notes_table WHERE notes_title LIKE:query OR notes_description LIKE:query")
     fun searchNote(query:String?):LiveData<List<NotesModel>>

//    The query parameter is of type String?, which means it can accept a regular non-null String or it can accept null


     // When working with Room database queries and LiveData in Android, you often do not need to explicitly use
// suspend functions because Room and LiveData are designed to handle background threading and asynchronous data updates
// for you

}