package com.example.notesapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.notesapp.databinding.ActivityUpdateBinding
import com.example.notesapp.room.model.NotesModel

class UpdateActivity : AppCompatActivity() {

    lateinit var binding:ActivityUpdateBinding

     var imgUri: Uri? = null

    var notesModel:NotesModel? = null

    lateinit var galleryLauncher:ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val i = intent

        if(i!=null){
             notesModel = i.getParcelableExtra("note")

            if(notesModel!=null) {
                binding.activityUpdateTitle.setText(notesModel!!.title)
                binding.activityUpdateDescription.setText(notesModel!!.description)

                if(notesModel!!.img!=null){

                    val uri:Uri = Uri.parse(notesModel!!.img)
                    binding.activityUpdateImg.setImageURI(uri)
                }

            }else{
                Toast.makeText(this, "notesModel is null", Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(this, "i is null", Toast.LENGTH_SHORT).show()
        }

        binding.activityUpdateCancel.setOnClickListener(){
            finish()
        }

        binding.activityUpdateImg.setOnClickListener(){
            openGallery()
        }

        galleryLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.also { uri ->
                    contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    binding.activityUpdateImg.setImageURI(uri)
                    imgUri = uri
                }
            }
        }

        binding.activityUpdateAdd.setOnClickListener(){
            var updatedNotes = notesModel
            if(!TextUtils.isEmpty(binding.activityUpdateTitle.text)  && (!TextUtils.isEmpty(binding.activityUpdateDescription.text))){
               if(updatedNotes!=null){

                   updatedNotes.title =  binding.activityUpdateTitle.text.toString()

                   updatedNotes.description=binding.activityUpdateDescription.text.toString()

                   if(imgUri!=null){
                       updatedNotes.img = imgUri.toString()
                   }

                   val setIntent = Intent()
                   setIntent.putExtra("updatedNote",updatedNotes)
                   setResult(RESULT_OK,setIntent)

                   Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show()
                   finish()
               }else{
                   Toast.makeText(this, "The updatedNotes is empty", Toast.LENGTH_SHORT).show()
               }
            }
            else{
                Toast.makeText(this, "fields are empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        galleryLauncher.launch(intent)
    }
}