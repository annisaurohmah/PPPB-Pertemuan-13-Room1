package com.example.room1.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.room1.database.Note
import com.example.room1.databinding.ListItemBinding

class ListAdapter(private val context: Context, private val listData: List<Note>, private val onClickData: (Note) -> Unit) :
    RecyclerView.Adapter<ListAdapter.ItemDataViewHolder>() {

    inner class ItemDataViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Note) {
            with(binding) {
                // Lakukan binding data ke tampilan
                noteTitle.text = data.title
                noteDesc.text = data.description
                noteDate.text = data.date

                // Atur listener untuk onClick
                itemView.setOnClickListener {
                    onClickData(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDataViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemDataViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size
}
