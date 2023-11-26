package com.example.notesapp.di

import android.app.Application
import android.content.Context
import com.example.notesapp.repository.NotesRepository
import com.example.notesapp.room.NotesDao
import com.example.notesapp.room.NotesDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context:Context){

    @Provides
    @Singleton
    fun providesApplication(): Application {
        return context.applicationContext as Application
    }

    @Singleton
    @Provides
    fun providesNotesDataBase():NotesDataBase{
        return NotesDataBase.getDataBaseInstance(context)
    }

    @Provides
    @Singleton
    fun providesNotesDao(notesDataBase: NotesDataBase):NotesDao{
        return notesDataBase.getNoteDao()
    }

    @Provides
    @Singleton
    fun providesRepository(notesDao: NotesDao):NotesRepository{
        return NotesRepository(notesDao)
    }
    
}

