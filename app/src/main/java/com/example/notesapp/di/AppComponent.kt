package com.example.notesapp.di

import com.example.notesapp.fragments.NotesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(notesFragment: NotesFragment)
}