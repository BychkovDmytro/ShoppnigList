package com.lokilinks.shoppniglist.db

import android.content.SharedPreferences
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lokilinks.shoppniglist.R
import com.lokilinks.shoppniglist.databinding.NoteListItemBinding
import com.lokilinks.shoppniglist.entities.NoteItem
import com.lokilinks.shoppniglist.utils.HtmlManager
import com.lokilinks.shoppniglist.utils.TimeManager

class NoteAdapter(private val listener: Listener, private val defPref: SharedPreferences): ListAdapter<NoteItem, NoteAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
       holder.setData(getItem(position), listener, defPref)

    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = NoteListItemBinding.bind(view)

        @RequiresApi(Build.VERSION_CODES.N)
        fun setData(note: NoteItem, listener: Listener, defPref: SharedPreferences) = with(binding){
            tvTitle.text = note.title
            tvDescription.text = HtmlManager.getFromHtml(note.content).trim()
            tvTime.text = TimeManager.getTimeFormat(note.time, defPref )
            imDelete.setOnClickListener {
                listener.deleteItem(note.id!!)
            }
            itemView.setOnClickListener {
                listener.onClickItem(note)
            }
        }

        companion object{
            fun create (parent: ViewGroup):ItemHolder{
                return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<NoteItem>(){

        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id ==newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem
        }
    }


    interface Listener{
        fun deleteItem(id: Int)
        fun onClickItem(note: NoteItem)
    }
}