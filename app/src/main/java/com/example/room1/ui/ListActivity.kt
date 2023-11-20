package com.example.room1.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.room1.R
import com.example.room1.database.Note
import com.example.room1.database.NoteDao
import com.example.room1.database.NoteRoomDatabase
import com.example.room1.databinding.ActivityListBinding
import com.example.room1.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ListActivity : AppCompatActivity() {
    private lateinit var mNotesDao: NoteDao
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var binding: ActivityListBinding
    private lateinit var itemAdapter: ListAdapter
    private lateinit var executorService: ExecutorService
    private val listViewData = ArrayList<Note>()
    private var updateId: Int=0

    companion object{
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_ID = "extra_id"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manager = LinearLayoutManager(this)

        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.nodeDao()!!


        itemAdapter = ListAdapter(this, listViewData) { item ->
            // Handle item click event
            // Misalnya, buka detail catatan atau lakukan tindakan lain
            updateId = item.id
            val IntentToForm = Intent(this, MainActivity2::class.java)
                .apply {
                    putExtra(EXTRA_TITLE, item.title)
                    putExtra(EXTRA_DESC, item.description)
                    putExtra(EXTRA_DATE, item.title)
                    putExtra(EXTRA_ID, item.id)
                }
            startActivity(IntentToForm)
        }

        with(binding){
            btnAdd.setOnClickListener {
                val IntentToForm = Intent(this@ListActivity, MainActivity::class.java)
                startActivity(IntentToForm)

            }
//            listView.setOnClickListener {
//                val adapter = ListAdapter(this@ListActivity, listViewData) { item ->
//                    // Handle item click event
//                    // Misalnya, buka detail catatan atau lakukan tindakan lain
//                    updateId = item.id
//                    val IntentToForm = Intent(this@ListActivity, MainActivity2::class.java)
//                        .apply {
//                            putExtra(EXTRA_TITLE, item.title)
//                            putExtra(EXTRA_DESC, item.description)
//                            putExtra(EXTRA_DATE, item.title)
//                            putExtra(EXTRA_ID, item.id)
//
//                        }
//                    startActivity(IntentToForm)
//                }

                listView.layoutManager = LinearLayoutManager(this@ListActivity)
                listView.adapter = itemAdapter



            }

//            listView.onItemLongClickListener =
//                AdapterView.OnItemLongClickListener { adapterView, view, i, id ->
//                    val item = adapterView.adapter.getItem(i) as Note
//                    delete(item)
//                    true
//                }
            }

    private fun getAllNotes() {
        mNotesDao.allNotes.observe(this) { notes ->
            listViewData.clear()
            listViewData.addAll(notes)
            itemAdapter.notifyDataSetChanged()

            Log.d("ListActivity", "Number of notes: ${notes.size}")
        }


    }

    override fun onResume() {
        super.onResume()
        getAllNotes()
    }
}


