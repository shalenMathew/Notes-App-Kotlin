package com.example.notesapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.notesapp.adapter.CustomAdapter
import com.example.notesapp.adapter.ViewPagerAdapter
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.db_backup.DatabaseBackup
import com.example.notesapp.room.model.NotesModel
import com.example.notesapp.viewmodel.NotesViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.material.tabs.TabLayoutMediator
import com.google.api.services.drive.DriveScopes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// this backup and restore branch
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val titleTab = arrayOf("Notes","Starred")

    lateinit var notesViewModel: NotesViewModel

    lateinit var databaseBackup:DatabaseBackup

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val EXTERNAL_STORAGE_REQUSET_CODE = 100

//    @Inject
//    lateinit var notesViewModelFactory: NotesViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope("https://www.googleapis.com/auth/drive.appdata"))  // Request access to App Folder
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        signIn()

        toolBarSetUp()

        if(!isPermissionGranted()){
            requestPermission()
        }


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

        binding.viewPager.currentItem = 0

        TabLayoutMediator(binding.tabLayout,binding.viewPager) { tab, position ->
            tab.text = titleTab[position]
        }.attach()


//        binding.addButton.setOnClickListener(){
//            val i = Intent(this,AddNoteActivity::class.java)
//            noteActionLauncher.launch(i)
//        }

    }

     fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
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
              CoroutineScope(Dispatchers.IO).launch {
                  databaseBackup.upload()
              }
            }

            R.id.restore->{
                CoroutineScope(Dispatchers.IO).launch {
                    databaseBackup.download()
                }
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
}else if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                databaseBackup = DatabaseBackup(this)
                databaseBackup.googleSignIn(account)
            } catch (e: ApiException) {
                Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            }
        }
    }
    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "MainActivity"
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                EXTERNAL_STORAGE_REQUSET_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                EXTERNAL_STORAGE_REQUSET_CODE
            )
        }
    }

    private fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

}