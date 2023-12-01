package com.example.notesapp.di

import com.example.notesapp.MainActivity
import com.example.notesapp.fragments.NotesFragment
import com.example.notesapp.fragments.StarredFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(notesFragment: NotesFragment)

    fun inject(mainActivity: MainActivity)

    fun inject(starredFragment: StarredFragment)

}