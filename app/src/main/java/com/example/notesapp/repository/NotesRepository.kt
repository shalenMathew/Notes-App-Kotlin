package com.example.notesapp.repository

import androidx.lifecycle.LiveData
import com.example.notesapp.room.NotesDao
import com.example.notesapp.room.model.NotesModel

class NotesRepository(private val notesDao:NotesDao) {


     suspend fun insertNotes(notesModel: NotesModel){
        // a suspend function only can be called in another suspend function or coroutine
        notesDao.insertNote(notesModel)
    }

    suspend fun updateNotes(notesModel: NotesModel){
        notesDao.updateNote(notesModel)
    }

    suspend fun deleteNotes(notesModel: NotesModel){
        notesDao.deleteNote(notesModel)
    }

    fun getAllNotes():LiveData<List<NotesModel>>{
        return notesDao.getAllNotes()
    }

    fun searchNote(query:String?) = notesDao.searchNote(query)


}