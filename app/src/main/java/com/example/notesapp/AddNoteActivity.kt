package com.example.notesapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.notesapp.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddNoteBinding

    private  var imgUri: Uri? = null

    private val EXTERNAL_STORAGE_REQUSET_CODE = 100

    lateinit var galleryLauncher:ActivityResultLauncher<String>

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
            openGallery()
        }


        galleryLauncher=registerForActivityResult(ActivityResultContracts.GetContent()){
                result->

            binding.activityAddNotesImg.setImageURI(result)
            imgUri=result

        }


    }

    private fun openGallery() {

        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        val granted = PackageManager.PERMISSION_GRANTED

        if (ContextCompat.checkSelfPermission(this, permission) != granted) {
            // Request the necessary permission if not granted
            ActivityCompat.requestPermissions(this, arrayOf(permission), EXTERNAL_STORAGE_REQUSET_CODE)
        } else {
            // Permission is already granted, you can proceed with launching the gallery.
            galleryLauncher.launch("image/*")
        }


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
        if (requestCode == EXTERNAL_STORAGE_REQUSET_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, launch the gallery picker.
            openGallery()
        } else {
           Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }


}