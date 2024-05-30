package com.alcorp.consumerapp.helper

import android.database.Cursor
import com.alcorp.consumerapp.db.DatabaseContract
import com.alcorp.consumerapp.entity.Note

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Note>{
        val noteList = ArrayList<Note>()

        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID))
                val title = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE))
                val description = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE))

                noteList.add(Note(id, title, description, date))
            }
        }
        return noteList
    }

    fun mapCursorToObject(notesCursor: Cursor?) : Note{
        var note = Note()
        notesCursor?.apply {
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID))
            val title = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE))
            val desc = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION))
            val date = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE))

            note = Note(id, title, desc, date)
        }

        return note
    }
}