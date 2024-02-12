package com.example.notesapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.notesapp.fragments.NotesFragment
import com.example.notesapp.fragments.StarredFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
 return  2
    }

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0-> NotesFragment()
            1-> StarredFragment()
            else -> NotesFragment()
        }
    }


}