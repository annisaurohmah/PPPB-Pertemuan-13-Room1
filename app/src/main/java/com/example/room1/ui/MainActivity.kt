package com.example.room1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.room1.R
import com.example.room1.database.Note
import com.example.room1.database.NoteDao
import com.example.room1.database.NoteRoomDatabase
import com.example.room1.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var mNotesDao: NoteDao
    private lateinit var binding: ActivityMainBinding
    private lateinit var executorService: ExecutorService
    private var updateId: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.nodeDao()!!

        with(binding){
            btnAdd.setOnClickListener(View.OnClickListener {
                insert(
                    Note(
                        id = updateId,
                        title = txtTitle.text.toString(),
                        description = txtDesc.text.toString(),
                        date = txtDate.text.toString()
                    )
                )
                setEmptyField()
            })

            btnUpdate.setOnClickListener {
                update(
                    Note(
                        id = updateId,
                        title = txtTitle.getText().toString(),
                        description = txtDesc.getText().toString(),
                        date = txtDate.getText().toString()
                    )
                )
                updateId = 0
                setEmptyField()
            }

            listView.setOnItemClickListener { adapterView, view, i,id ->
                val item = adapterView.adapter.getItem(i) as Note
                updateId = item.id
                txtTitle.setText(item.title)
                txtDesc.setText(item.description)
                txtDate.setText(item.date)
            }

            listView.onItemLongClickListener =
                AdapterView.OnItemLongClickListener { adapterView, view, i, id ->
                    val item = adapterView.adapter.getItem(i) as Note
                    delete(item)
                    true
                }
        }


    }
    private fun getAllNotes() {
        mNotesDao.allNotes.observe(this) { notes ->
            val adapter: ArrayAdapter<Note> = ArrayAdapter<Note>(
                this,
                android.R.layout.simple_list_item_1, notes
            )
            binding.listView.adapter = adapter
        }
    }

    private fun insert(note: Note) {
        executorService.execute { mNotesDao.insert(note) }
    }

    private fun delete(note: Note) {
        executorService.execute { mNotesDao.delete(note) }
    }

    private fun update(note: Note) {
        executorService.execute { mNotesDao.update(note) }
    }
    override fun onResume() {
        super.onResume()
        getAllNotes()
    }
    private fun setEmptyField() {
        with(binding){
            txtTitle.setText("")
            txtDesc.setText("")
            txtDate.setText("")
        }
    }



}