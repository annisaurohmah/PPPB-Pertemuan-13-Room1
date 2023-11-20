package com.example.room1.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.room1.R
import com.example.room1.database.Note
import com.example.room1.database.NoteDao
import com.example.room1.database.NoteRoomDatabase
import com.example.room1.databinding.ActivityMain2Binding
import com.example.room1.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity2 : AppCompatActivity() {
    private lateinit var mNotesDao: NoteDao
    private lateinit var binding: ActivityMain2Binding
    private lateinit var executorService: ExecutorService
    private var updateId: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.nodeDao()!!

        with(binding){

            val title = intent.getStringExtra(ListActivity.EXTRA_TITLE)
            val desc = intent.getStringExtra(ListActivity.EXTRA_DESC)
            val date = intent.getStringExtra(ListActivity.EXTRA_DATE)
            val id = intent.getIntExtra(ListActivity.EXTRA_ID, 3)

            txtTitle.setText(title)
            txtDesc.setText(desc)
            txtDate.setText(date)

            btnUpdate.setOnClickListener {
                update(
                    Note(
                        id = id,
                        title = txtTitle.getText().toString(),
                        description = txtDesc.getText().toString(),
                        date = txtDate.getText().toString()
                    )
                )
                updateId = 0
                startActivity(Intent(this@MainActivity2, ListActivity::class.java))
            }

            btnDelete.setOnClickListener {
                val noteToDelete = Note(id = id, "","","")
                delete(noteToDelete)
                startActivity(Intent(this@MainActivity2, ListActivity::class.java))
            }
            btnBack.setOnClickListener {
                startActivity(Intent(this@MainActivity2, ListActivity::class.java))
            }


        }


    }
//    private fun getAllNotes() {
//        mNotesDao.allNotes.observe(this) { notes ->
//            val adapter: ArrayAdapter<Note> = ArrayAdapter<Note>(
//                this,
//                android.R.layout.simple_list_item_1, notes
//            )
//            binding.listView.adapter = adapter
//        }
//    }

    private fun insert(note: Note) {
        executorService.execute { mNotesDao.insert(note) }
    }

    private fun delete(note: Note) {
        executorService.execute { mNotesDao.delete(note) }
    }

    private fun update(note: Note) {
        executorService.execute { mNotesDao.update(note) }
    }
    //    override fun onResume() {
//        super.onResume()
//        getAllNotes()
//    }
    private fun setEmptyField() {
        with(binding){
            txtTitle.setText("")
            txtDesc.setText("")
            txtDate.setText("")
        }
    }
}