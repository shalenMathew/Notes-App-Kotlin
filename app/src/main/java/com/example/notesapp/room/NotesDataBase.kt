package com.example.notesapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesapp.room.model.NotesModel


@Database(entities = [NotesModel::class], version = 2)
abstract class NotesDataBase : RoomDatabase() {

    abstract fun getNoteDao():NotesDao

    // there 2 ways to create singleton pattern

//    companion object{
////        In Kotlin, the companion object is a way to define static members and methods within a class.
//
////        private val lock = Any(): This is a synchronization lock used to prevent multiple threads from creating
////        multiple instances of the NotesDataBase class.
//
//        // Code inside this block is synchronized
//        // Only one thread can execute this code at a time
//        // Other threads trying to enter this block will be blocked until it's available
//        // 'lock' is the object used for synchronization
//        // This block ensures that critical sections of code are executed atomically
//
//
//        @Volatile // volatile make the fields immediately visible to other threads
//        private  var instance:NotesDataBase? = null
//        private val lock = Any()
//
//        operator fun invoke(context: Context) = instance?:
//        synchronized(lock){
//            instance?:createDatabase(context).also{
//                instance = it
//            }
//        }
//
//        private fun createDatabase(context: Context)=
//            Room.databaseBuilder(
//                context.applicationContext,
//                NotesDataBase::class.java,
//                "notes_db")
//                .fallbackToDestructiveMigration()
//                .build()
//
//    }


    companion object{

        @Volatile
        private var INSTANCE:NotesDataBase?=null

        fun getDataBaseInstance(context: Context):NotesDataBase{

            synchronized(this){

                if (INSTANCE==null){
                    INSTANCE=Room.databaseBuilder(context.applicationContext,NotesDataBase::class.java,"notes_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return INSTANCE!!

                // here we can see w3e gave declared INSTANCE as a nullable value i.e a value that can hold null
                // so if u dont put '!!' when returning INSTANCE it may give warning coz the INSTANCE ur retutning
                // may provide null value by adding INSTANCE!! u declare that the value i am returning is not null,
                // if its null it will throw a null pointer exception , so use '!!' only when ur confident that u have
                // added appropriate logic to make sure that it will not return a null instance
            }
        }

        // u should write return inside syncronize block

//        Here's what could happen if you move the return statement outside the synchronized block:
//
//        Thread A enters the synchronized block, checks that instance is null, and proceeds to create a new instance.
//        Before Thread A can set the INSTANCE variable and return the instance, it gets preempted by the operating system,
//        allowing Thread B to enter the synchronized block.
//        Thread B checks that instance is still null (because Thread A hasn't set it yet) and proceeds to create another new
//        instance.
//        Now, you have two instances of the database being created, which is not the intended behavior of a singleton pattern.
//        To avoid this situation, it's essential to keep the return statement inside the synchronized block. This ensures that
//        only one thread can create and initialize the database instance while other threads wait, and it guarantees that the
//        initialized instance is returned.

        // in short syncronize only let one thread at a time



    }






}