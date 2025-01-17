package com.alcorp.queryoptimapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.alcorp.queryoptimapp.database.DatabaseContract.MahasiswaColumns.Companion.NAME
import com.alcorp.queryoptimapp.database.DatabaseContract.MahasiswaColumns.Companion.NIM
import com.alcorp.queryoptimapp.database.DatabaseContract.TABLE_NAME
import com.alcorp.queryoptimapp.model.MahasiswaModel

class MahasiswaHelper(context: Context) {

    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private var INSTANCE: MahasiswaHelper? = null

        fun getInstance(context: Context): MahasiswaHelper{
            if (INSTANCE == null){
                synchronized(SQLiteOpenHelper::class.java){
                    if (INSTANCE == null){
                        INSTANCE = MahasiswaHelper(context)
                    }
                }
            }
            return INSTANCE as MahasiswaHelper
        }

    }

    @Throws(SQLiteException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }

    fun close(){
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    /**
     * Gunakan method ini untuk mendapatkan semua data mahasiswa
     *
     * @return hasil query mahasiswa model di dalam arraylist
     */
    fun getAllData(): ArrayList<MahasiswaModel>{
        val cursor = database.query(TABLE_NAME, null, null, null, null, null, "$_ID ASC", null)
        cursor.moveToFirst()
        val arrayList = ArrayList<MahasiswaModel>()
        var mahasiswaModel: MahasiswaModel
        if (cursor.count > 0){
            do {
                mahasiswaModel = MahasiswaModel()
                mahasiswaModel.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                mahasiswaModel.name = cursor.getString(cursor.getColumnIndexOrThrow(NAME))
                mahasiswaModel.nim = cursor.getString(cursor.getColumnIndexOrThrow(NIM))

                arrayList.add(mahasiswaModel)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    /**
     * Gunakan method ini untuk query insert
     *
     * @param mahasiswaModel inputan model mahasiswa
     * @return id dari data mahasiswa yang baru saja dimasukkan
     */
    fun insert(mahasiswaModel: MahasiswaModel): Long{
        val initialValues = ContentValues()
        initialValues.put(NAME, mahasiswaModel.name)
        initialValues.put(NIM, mahasiswaModel.nim)
        return database.insert(TABLE_NAME, null, initialValues)
    }

    /**
     * Gunakan method ini untuk cari NIM berdasarkan nama mahasiswa
     *
     * @param nama nama yang dicari
     * @return NIM dari mahasiswa
     */
    fun getDataByName(name: String): ArrayList<MahasiswaModel>{
        val cursor = database.query(TABLE_NAME, null, "$NAME LIKE ?", arrayOf(name), null, null, "$_ID ASC", null)
        cursor.moveToFirst()
        val arrayList = ArrayList<MahasiswaModel>()
        var mahasiswaModel: MahasiswaModel
        if (cursor.count > 0){
            do {
                mahasiswaModel = MahasiswaModel()
                mahasiswaModel.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                mahasiswaModel.name = cursor.getString(cursor.getColumnIndexOrThrow(NAME))
                mahasiswaModel.nim = cursor.getString(cursor.getColumnIndexOrThrow(NIM))

                arrayList.add(mahasiswaModel)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    /**
     * Gunakan method ini untuk memulai sesi query transaction
     */
    fun beginTransaction(){
        database.beginTransaction()
    }

    /**
     * Gunakan method ini jika query transaction berhasil, jika error jangan panggil method ini
     */
    fun setTransactionSuccess(){
        database.setTransactionSuccessful()
    }

    /**
     * Gunakan method ini untuk mengakhiri sesi query transaction
     */
    fun endTransaction(){
        database.endTransaction()
    }

    /**
     * Gunakan method ini untuk query insert di dalam transaction
     *
     * @param mahasiswaModel inputan model mahasiswa
     */
    fun insertTransaction(mahasiswaModel: MahasiswaModel){
        val sql = ("INSERT INTO $TABLE_NAME ($NAME, $NIM) VALUES (?, ?)")
        val stmt = database.compileStatement(sql)
        stmt.bindString(1, mahasiswaModel.name)
        stmt.bindString(2, mahasiswaModel.nim)
        stmt.execute()
        stmt.clearBindings()
    }

    /**
     * Gunakan method ini untuk update data mahasiswa yang ada di dalam database
     *
     * @param mahasiswaModel inputan model mahasiswa
     * @return jumlah mahasiswa yang ter-update
     */
    fun update(mahasiswaModel: MahasiswaModel): Int {
        val initialValues = ContentValues()
        initialValues.put(NAME, mahasiswaModel.name)
        initialValues.put(NIM, mahasiswaModel.nim)
        return database.update(TABLE_NAME, initialValues, _ID + "= '" + mahasiswaModel.id + "'", null)
    }

    /**
     * Gunakan method ini untuk menghapus data mahasiswa yang ada di dalam database
     *
     * @param id id mahasiswa yang akan di hapus
     * @return jumlah mahasiswa yang di-delete
     */
    fun delete(id: Int): Int {
        return database.delete(TABLE_NAME, "$_ID = '$id'", null)
    }
}