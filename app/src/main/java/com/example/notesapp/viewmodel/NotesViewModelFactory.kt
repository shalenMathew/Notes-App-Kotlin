package com.example.notesapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.repository.NotesRepository
import com.example.notesapp.room.NotesDataBase
import java.lang.IllegalArgumentException
import javax.inject.Inject

// HILT WIL CREATE THE FACTORY NO NEED FOR THIS CLASS

//class NotesViewModelFactory @Inject constructor(private val application: Application, private var notesRepository: NotesRepository)
//    :ViewModelProvider.Factory
//
//{
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//
//        if(modelClass.isAssignableFrom(NotesViewModel::class.java)) {
//            return NotesViewModel(application,notesRepository)  as T
//        }
//        throw IllegalArgumentException("error")
//    }
//}