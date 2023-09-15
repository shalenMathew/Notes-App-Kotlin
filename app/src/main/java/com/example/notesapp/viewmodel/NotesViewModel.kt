package com.example.notesapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.repository.NotesRepository
import com.example.notesapp.room.NotesDataBase
import com.example.notesapp.room.model.NotesModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NotesViewModel(private val application: Application,private val notesRepository: NotesRepository)
    :AndroidViewModel(application) {


fun insertNote(notes:NotesModel){

viewModelScope.launch {
    notesRepository.insertNotes(notes)
}

}

    fun updateNote(notes:NotesModel){


        viewModelScope.launch {
            notesRepository.updateNotes(notes)
        }

    }

    fun deleteNote(notes:NotesModel){

        viewModelScope.launch {
            notesRepository.deleteNotes(notes)
        }
    }

    fun getAllNotes() = notesRepository.getAllNotes()

    fun searchNote(query:String?) = notesRepository.searchNote(query)
}