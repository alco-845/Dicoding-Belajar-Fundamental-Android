package com.alcorp.sqliteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alcorp.sqliteapp.adapter.NoteAdapter
import com.alcorp.sqliteapp.db.NoteHelper
import com.alcorp.sqliteapp.entity.Note
import com.alcorp.sqliteapp.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NoteAdapter
    private lateinit var noteHelper: NoteHelper

    companion object{
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Notes"

        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.setHasFixedSize(true)

        adapter = NoteAdapter(this)
        rv_notes.adapter = adapter

        fab_add.setOnClickListener{
            val intent = Intent(this@MainActivity, NoteAddUpdateActivity::class.java)
            startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_ADD)
        }

        noteHelper = NoteHelper.getInstance(applicationContext)
        noteHelper.open()

        /**
        Cek jika savedInstaceState null maka akan melakukan proses asynctask nya
        jika tidak,akan mengambil arraylist nya dari yang sudah di simpan
         */
        if (savedInstanceState == null){
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Note>(EXTRA_STATE)
            if (list != null){
                adapter.listNotes = list
            }
        }
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main){
            progressbar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO){
                val cursor = noteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressbar.visibility = View.INVISIBLE
            val notes = deferredNotes.await()
            if (notes.size > 0){
                adapter.listNotes = notes
            } else {
                adapter.listNotes = ArrayList()
                showSnackBarMessage("Tidak ada data saat ini")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNotes)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){
            when(requestCode){
                // Akan dipanggil jika request codenya ADD
                NoteAddUpdateActivity.REQUEST_ADD -> if (resultCode == NoteAddUpdateActivity.RESULT_ADD){
                    val note = data.getParcelableExtra<Note>(NoteAddUpdateActivity.EXTRA_NOTE)

                    adapter.addItem(note)
                    rv_notes.smoothScrollToPosition(adapter.itemCount - 1)

                    showSnackBarMessage("Satu item berhasil ditambahkan")
                }

                // Update dan Delete memiliki request code sama akan tetapi result codenya berbeda
                NoteAddUpdateActivity.REQUEST_UPDATE -> when(resultCode){
                    /**
                    Akan dipanggil jika result codenya  UPDATE
                    Semua data di load kembali dari awal
                    */
                    NoteAddUpdateActivity.RESULT_UPDATE -> {
                        val note = data.getParcelableExtra<Note>(NoteAddUpdateActivity.EXTRA_NOTE)
                        val position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0)

                        adapter.updateItem(position, note)
                        rv_notes.smoothScrollToPosition(position)

                        showSnackBarMessage("Satu item berhasil diubah")
                    }
                }

                /**
                Akan dipanggil jika result codenya DELETE
                Delete akan menghapus data dari list berdasarkan dari position
                */
                NoteAddUpdateActivity.RESULT_DELETE -> {
                    val position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0)

                    adapter.removeItem(position)

                    showSnackBarMessage("Satu item berhasil dihapus")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        noteHelper.close()
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(rv_notes, message, Snackbar.LENGTH_SHORT).show()
    }
}