package com.example.notesapp.fragments

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.R
import com.example.notesapp.adapter.CustomAdapter
import com.example.notesapp.databinding.FragmentNotesBinding
import com.example.notesapp.repository.NotesRepository
import com.example.notesapp.room.NotesDataBase
import com.example.notesapp.viewmodel.NotesViewModel
import com.example.notesapp.viewmodel.NotesViewModelFactory



class NotesFragment : Fragment() {

    // commit

    lateinit var binding: FragmentNotesBinding
    lateinit var customAdapter: CustomAdapter
    lateinit var notesViewModel: NotesViewModel

    lateinit var notesViewModelFactory: NotesViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNotesBinding.inflate(inflater,container,false)

        val application = requireContext().applicationContext as Application

        var notesDatabase = NotesDataBase.getDataBaseInstance(requireContext())
        var notesDao  = notesDatabase.getNoteDao()
        var notesRepository = NotesRepository(notesDao)

        notesViewModelFactory = NotesViewModelFactory(application,notesRepository)
        notesViewModel = ViewModelProvider(this,notesViewModelFactory).get(NotesViewModel::class.java)


        configureRecycleView()
        observeData()

        return binding.root
    }



    private fun observeData() {
        notesViewModel.getAllNotes().observe(viewLifecycleOwner) { notesList->

            // by default our observed list will be stored in variable named 'it'
            // we can change the name of it using 'urListName ->'

            val notesArrayList = ArrayList(notesList)
            customAdapter.setNotes(notesArrayList)

        }
    }



    private fun configureRecycleView() {
        binding.notesRv.layoutManager = LinearLayoutManager(context)
        customAdapter = CustomAdapter(requireContext(),notesViewModel)
        binding.notesRv.adapter= customAdapter
    }

}