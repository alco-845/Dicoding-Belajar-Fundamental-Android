package com.alcorp.queryoptimapp.database

import android.provider.BaseColumns

internal object DatabaseContract {

    var TABLE_NAME = "table_mahasiswa"

    internal class MahasiswaColumns : BaseColumns {
        companion object {
            const val NAME = "nama"
            const val NIM = "nim"
        }
    }
}