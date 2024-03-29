package com.example.notesapp.fragments

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.END
import androidx.recyclerview.widget.ItemTouchHelper.START
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.adapter.CustomAdapter
import com.example.notesapp.databinding.FragmentNotesBinding
import com.example.notesapp.di.MyApplication
import com.example.notesapp.repository.NotesRepository
import com.example.notesapp.room.NotesDataBase
import com.example.notesapp.viewmodel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint

import javax.inject.Inject


@AndroidEntryPoint
class NotesFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding
    private lateinit var customAdapter: CustomAdapter
    private lateinit var notesViewModel: NotesViewModel


//    @Inject
//    lateinit var notesViewModelFactory: NotesViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNotesBinding.inflate(inflater,container,false)

//        var notesDatabase = NotesDataBase.getDataBaseInstance(requireContext())
//        var notesDao  = notesDatabase.getNoteDao()
//        var notesRepository = NotesRepository(notesDao)


        // here  view model is not dependent on any objects its getting all the objects read-made from outside
        // this is know as manual dependency , manually adding dependency

        // replacing it with dagger dependency

//        notesViewModelFactory = NotesViewModelFactory(application,notesRepository)

//        val application = requireActivity().application as MyApplication
//        application.appComponent.inject(this)

        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

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
        customAdapter = CustomAdapter(requireActivity(),notesViewModel)
        binding.notesRv.adapter= customAdapter
    }


}