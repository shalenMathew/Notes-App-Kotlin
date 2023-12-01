package com.example.notesapp.di

import android.app.Application
import android.content.Context
import com.example.notesapp.repository.NotesRepository
import com.example.notesapp.room.NotesDao
import com.example.notesapp.room.NotesDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule{

    // a module should have empty constructor so that hilt can use it

    // we don't need this method anymore as Hilt even creates Application context for us when its creating component and all that thing
//    @Provides
//    @Singleton
//    fun providesApplication(@ApplicationContext context: Context): Application {
//        return context.applicationContext as Application
//    }

    // return context will creates error as there are now duplicate context in hilt

    @Singleton
    @Provides
    // here @ApplicationContext provides the context that the hilt has created
    fun providesNotesDataBase(@ApplicationContext context: Context):NotesDataBase{
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

