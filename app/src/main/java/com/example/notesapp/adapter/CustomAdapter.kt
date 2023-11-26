package com.example.notesapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notesapp.MainActivity
import com.example.notesapp.R
import com.example.notesapp.UpdateActivity
import com.example.notesapp.databinding.CardItemBinding
import com.example.notesapp.databinding.CardItemWithImgBinding
import com.example.notesapp.room.model.NotesModel
import com.example.notesapp.viewmodel.NotesViewModel

class CustomAdapter(private val context: Context,private val notesViewModel: NotesViewModel): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val LAYOUT_ITEM_WITH_IMG = 0

    private val LAYOUT_ITEM_WITHOUT_IMG =1

    private var list: ArrayList<NotesModel> = ArrayList()


    fun setNotes(newData:ArrayList<NotesModel>){
        list.clear() // Clear the existing list
        list = newData
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view:View? = null

        return if(viewType==LAYOUT_ITEM_WITH_IMG){
            view = LayoutInflater.from(parent.context).inflate(R.layout.card_item_with_img,parent,false)
            ViewHolderWithImg(view)
        }else{
            view = LayoutInflater.from(parent.context).inflate(R.layout.card_item,parent,false)
            ViewHolderWithoutImg(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val notesModel = list?.get(position)

     if(notesModel!=null){


         if(holder.itemViewType==LAYOUT_ITEM_WITH_IMG){

             val viewHolderWithImg = holder as ViewHolderWithImg
             viewHolderWithImg.binding.cardItemImgTitle.text = notesModel.title
             viewHolderWithImg.binding.cardItemImgDescription.text = notesModel.description
             Glide.with(context).load(notesModel.img).into(viewHolderWithImg.binding.cardItemImg)


             viewHolderWithImg.binding.cardItemImgStar.setOnClickListener(){

                 val starSelected = !notesModel.isStarred!!
                 notesModel.isStarred=starSelected

                 notesViewModel.updateNote(notesModel)

                 Toast.makeText(context, "Title ${notesModel.title} , star = ${notesModel.isStarred}", Toast.LENGTH_SHORT).show()
                 // Update the star icon drawable based on the updated starSelected value
                 if (starSelected) {
                     viewHolderWithImg.binding.cardItemImgStar.setImageResource(R.drawable.star)
                 } else {
                     viewHolderWithImg.binding.cardItemImgStar.setImageResource(R.drawable.star_outline)
                 }
                 notifyDataSetChanged()
             }

         }else{


             val viewHolderWithoutImg = holder as ViewHolderWithoutImg
             viewHolderWithoutImg.binding.cardItemTitle.text = notesModel.title
             viewHolderWithoutImg.binding.cardItemDescription.text = notesModel.description


             viewHolderWithoutImg.binding.cardItemStar.setOnClickListener(){

                 val starSelected = !notesModel.isStarred!!
                 notesModel.isStarred=starSelected

                 notesViewModel.updateNote(notesModel)

                 Toast.makeText(context, "Title ${notesModel.title} , star = ${notesModel.isStarred}", Toast.LENGTH_SHORT).show()
                 // Update the star icon drawable based on the updated starSelected value
                 if (starSelected) {
                     viewHolderWithoutImg.binding.cardItemStar.setImageResource(R.drawable.star)
                 } else {
                     viewHolderWithoutImg.binding.cardItemStar.setImageResource(R.drawable.star_outline)
                 }

                 notifyDataSetChanged()

             }

         }

     }

        holder.itemView.setOnClickListener(){
            var selectedNote = list[position]
            val i = Intent(context,UpdateActivity::class.java)
            i.putExtra("note",selectedNote)
            (context as MainActivity).startActivityForResult(i, 49)
        }

       holder.itemView.setOnLongClickListener(){

           deleteDialogBox(position)

           return@setOnLongClickListener true
       }


    }

    private fun deleteDialogBox(position: Int) {

       val alert = AlertDialog.Builder(context)

        alert.setTitle("Delete note!!!")
        alert.setMessage("Do u want to delete the Note ?")

        alert.setPositiveButton("Yes"){
            dialog, which ->
            deleteNote(position)
        }

        alert.setNegativeButton("No",null)

        alert.create().show()

    }

    private fun deleteNote(position: Int) {
        notesViewModel.deleteNote(list.get(position))
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    public class ViewHolderWithoutImg(itemView: View) : RecyclerView.ViewHolder(itemView){

         var binding:CardItemBinding

        init {
            binding = CardItemBinding.bind(itemView)
        }
    }

    public class ViewHolderWithImg(itemView: View) : RecyclerView.ViewHolder(itemView){

        var binding:CardItemWithImgBinding

        init {
            binding= CardItemWithImgBinding.bind(itemView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val notesModel = list.get(position)
        return if (notesModel.img != null) LAYOUT_ITEM_WITH_IMG else LAYOUT_ITEM_WITHOUT_IMG
    }

}