package com.example.notesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.adapter.ViewPagerAdapter
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.room.model.NotesModel
import com.example.notesapp.viewmodel.NotesViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

// Adding back up feature hope everything works fine
// this is main branch
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val titleTab = arrayOf("Notes","Starred")

    lateinit var notesViewModel: NotesViewModel


//    @Inject
//    lateinit var notesViewModelFactory: NotesViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolBarSetUp()
        // Room + Mvvm + dependency injection + view binding + kotlin

//        var notesDatabase = NotesDataBase.getDataBaseInstance(this)
//        var notesDao  = notesDatabase.getNoteDao()
//        var notesRepository = NotesRepository(notesDao)
//        notesViewModelFactory = NotesViewModelFactory(getApplication(),notesRepository)

        // dagger im[pl
//        val application = applicationContext as MyApplication
//        application.appComponent.inject(this)

        // hilt will create the factory no need to create one
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        val viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter=viewPagerAdapter

        binding.viewPager.currentItem = 1

        TabLayoutMediator(binding.tabLayout,binding.viewPager) { tab, position ->
            tab.text = titleTab[position]
        }.attach()


//        binding.addButton.setOnClickListener(){
//            val i = Intent(this,AddNoteActivity::class.java)
//            noteActionLauncher.launch(i)
//        }

    }
    private fun toolBarSetUp() {
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    val noteActionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->

        if(result.resultCode== Activity.RESULT_OK){

            val data = result.data

            if(data!=null){

                val requestCode = data.getIntExtra("resultCode",-1)

                when (requestCode){

                    1 ->{
                        var title = data.getStringExtra("title")
                        var desc = data.getStringExtra("desc")
                        var img = data.getStringExtra("img")

                        val notesModel = NotesModel(null,title =title, description = desc, img = img, isStarred = false)
                        notesViewModel.insertNote(notesModel)
                        Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.backup_restore,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.backUp ->{
                Toast.makeText(this, "backup", Toast.LENGTH_SHORT).show()
            }

            R.id.restore->{
                Toast.makeText(this, "restore", Toast.LENGTH_SHORT).show()
            }

            R.id.add_note->{
            val i = Intent(this,AddNoteActivity::class.java)
           noteActionLauncher.launch(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

if(requestCode==49 && data!=null){

    val updatedNotes:NotesModel? = data.getParcelableExtra("updatedNote")
    notesViewModel.updateNote(updatedNotes!!)
}

    }
}