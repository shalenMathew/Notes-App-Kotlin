package com.example.notesapp.db_backup

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.room.Room
import com.example.notesapp.Constants
import com.example.notesapp.Constants.dbPath
import com.example.notesapp.Constants.dbPathShm
import com.example.notesapp.Constants.dbPathWal
import com.example.notesapp.MainActivity
import com.example.notesapp.adapter.CustomAdapter
import com.example.notesapp.room.NotesDataBase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.Scopes
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.IOException
import java.util.Collections

class DatabaseBackup(private val context:Context){

    private lateinit var googleDriveService:Drive
    suspend fun upload() {

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "Uploading...Don't close the app", Toast.LENGTH_LONG).show()
        }
        val fileNames = listOf("notes_database", "notes_database-shm", "notes_database-wal")
        val filePaths = listOf(dbPath, dbPathShm, dbPathWal)

        try {
            // Get the list of files in the appDataFolder
            val files = googleDriveService.files().list()
                .setSpaces("appDataFolder")
                .setFields("nextPageToken, files(id, name)")
                .execute()

            // Delete all existing files in the appDataFolder
            for (existingFile in files.files) {
                googleDriveService.files().delete(existingFile.id).execute()
            }

            for (fileName in fileNames) {
                // Create a new file
                val storageFile = File().apply {
                    parents = Collections.singletonList("appDataFolder")
                    name = fileName
                }

                val filePath = java.io.File(filePaths[fileNames.indexOf(fileName)])
                val mediaContent = FileContent("", filePath)

                val file = googleDriveService.files().create(storageFile, mediaContent).execute()
                println("Filename: ${file.name} File ID: ${file.id}")

                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Uploaded ${file.name} successfully", Toast.LENGTH_LONG).show()
                }

            }
        } catch (e: GoogleJsonResponseException) {
            if (e.statusCode == 401) {
                // The token is invalid or expired
                withContext(Dispatchers.Main) {
                    (context as MainActivity).signIn()
                }
            }
        } catch (e: UserRecoverableAuthIOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    suspend fun download() {

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "Downloading...Don't close the app", Toast.LENGTH_LONG).show()
        }

        try {
            val dir = java.io.File("/data/data/com.example.notesapp/databases")
            if (dir.isDirectory) {
                val children = dir.list()
                for (child in children) {
                    java.io.File(dir, child).delete()
                }
            }

            val files = googleDriveService.files().list()
                .setSpaces("appDataFolder")
                .setFields("nextPageToken, files(id, name, createdTime)")
                .setPageSize(10)
                .execute()

            if (files.files.isEmpty()) {
                Log.e(TAG, "No DB file exists in Drive")
            }

            for (file in files.files) {
                println("Found file: ${file.name} (${file.id}) ${file.createdTime}")

                when (file.name) {
                    "notes_database" -> {
                        val outputStream = FileOutputStream(dbPath)
                        googleDriveService.files()[file.id].executeMediaAndDownloadTo(outputStream)
                    }
                    "notes_database-shm" -> {
                        val outputStream = FileOutputStream(dbPathShm)
                        googleDriveService.files()[file.id].executeMediaAndDownloadTo(outputStream)
                    }
                    "notes_database-wal" -> {
                        val outputStream = FileOutputStream(dbPathWal)
                        googleDriveService.files()[file.id].executeMediaAndDownloadTo(outputStream)
                    }
                }
            }

            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Downloaded successfully,Please reopen the app to reflect changes", Toast.LENGTH_LONG).show()
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, ""+e.printStackTrace(), Toast.LENGTH_LONG).show()
        }
    }


    fun googleSignIn(account: GoogleSignInAccount) {
        val credential = GoogleAccountCredential.usingOAuth2(context, setOf(Scopes.DRIVE_FILE))

        credential.selectedAccount = account.account
         googleDriveService = Drive.Builder(
            NetHttpTransport(),
            GsonFactory(),
            credential
        )
            .setApplicationName("Notes App")
            .build()

//        googleDriveServiceSetUp(credential)

    }

//    private fun googleDriveServiceSetUp(credential: GoogleAccountCredential?) {
//        googleDriveService = Drive.Builder(
//            NetHttpTransport(),
//            GsonFactory(),
//            credential
//        )
//            .setApplicationName("Notes App")
//            .build()
//
//
//    }


}


