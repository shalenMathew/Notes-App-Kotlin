package com.example.notesapp.room.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "notes_table")

@Parcelize
data class NotesModel(

@PrimaryKey(autoGenerate = true)
@ColumnInfo("notes_id")
    var id:Int?=null,

@ColumnInfo("notes_title")
     var title:String?,

@ColumnInfo("notes_description")
     var description:String?,

@ColumnInfo("notes_img")
var img:String?=null,

@ColumnInfo("notes_starred")
     var isStarred:Boolean?
) : Parcelable{


    // earlier we needed to overide and decalre our logic but thanks to to kotlin we dont need to do that

//    override fun describeContents(): Int {
//        TODO("Not yet implemented")
//    }

//    override fun writeToParcel(dest: Parcel, flags: Int) {
//        TODO("Not yet implemented")
//    }

}
