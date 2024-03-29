package com.example.notesapp.fragments

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.adapter.CustomAdapter
import com.example.notesapp.databinding.FragmentStarredBinding
import com.example.notesapp.di.MyApplication
import com.example.notesapp.repository.NotesRepository
import com.example.notesapp.room.NotesDataBase
import com.example.notesapp.room.model.NotesModel
import com.example.notesapp.viewmodel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint

import javax.inject.Inject


@AndroidEntryPoint
class StarredFragment : Fragment() {


    lateinit var binding: FragmentStarredBinding
    lateinit var customAdapter: CustomAdapter
    lateinit var notesViewModel: NotesViewModel

//    @Inject
//    lateinit var notesViewModelFactory: NotesViewModelFactory

  private lateinit  var starredList:ArrayList<NotesModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStarredBinding.inflate(inflater,container,false)
        starredList=ArrayList()

//        val application = requireContext().applicationContext as Application
//        var notesDatabase = NotesDataBase.getDataBaseInstance(requireContext())
//        var notesDao  = notesDatabase.getNoteDao()
//        var notesRepository = NotesRepository(notesDao)
//
//        notesViewModelFactory = NotesViewModelFactory(application,notesRepository)

//        val application = requireContext().applicationContext as MyApplication
//        application.appComponent.inject(this)


        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        observeList()

        binding.starredRv.layoutManager = LinearLayoutManager(context)
        customAdapter = CustomAdapter(requireActivity(),notesViewModel)
        binding.starredRv.adapter=customAdapter
        return  binding.root
    }

    private fun observeList() {

        notesViewModel.getAllNotes().observe(viewLifecycleOwner){ notesList ->

            starredList.clear()

            for(notesModelObj in notesList){

                if(notesModelObj.isStarred){
                    starredList.add(notesModelObj)
                }
            }
            customAdapter.setNotes2(starredList)
        }

    }

}