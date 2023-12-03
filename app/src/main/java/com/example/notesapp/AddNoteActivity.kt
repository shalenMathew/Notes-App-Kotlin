package com.example.notesapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.notesapp.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddNoteBinding

    private  var imgUri: Uri? = null

    private val EXTERNAL_STORAGE_REQUSET_CODE = 100

    lateinit var galleryLauncher:ActivityResultLauncher<Intent>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.activityAddNoteTrue.setOnClickListener(){

            var title = binding.activityAddNoteTitle.text.toString()
            var desc = binding.activityAddNoteDescription.text.toString()


            if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) ) {
                insertData(title, desc)
            }else{
                Toast.makeText(this, "fields cannot be empty", Toast.LENGTH_SHORT).show()
            }

        }

        binding.activityAddNoteFalse.setOnClickListener(){
            finish()
        }

        binding.activityAddNotesImg.setOnClickListener(){

//            openGallery()
            if (isPermissionGranted()) {
                openGallery()
            } else {
                requestPermission()
            }
        }


        galleryLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

                result ->

            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.also { uri ->
                    contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    binding.activityAddNotesImg.setImageURI(uri)
                    imgUri = uri
                }
            }

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

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        galleryLauncher.launch(intent)
    }


    private fun insertData(title: String, desc: String) {

        val i = Intent(this,MainActivity::class.java)

        i.putExtra("resultCode",1)
        i.putExtra("title",title)
        i.putExtra("desc",desc)

        if(imgUri!=null){
            i.putExtra("img",imgUri.toString())
        }

        setResult(RESULT_OK, i)

        finish()
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == EXTERNAL_STORAGE_REQUSET_CODE && grantResults.isNotEmpty() && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED ) {
            // Permission is granted, launch the gallery picker.

            openGallery()

        }else{
            // Permission was denied after the request.
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }


}