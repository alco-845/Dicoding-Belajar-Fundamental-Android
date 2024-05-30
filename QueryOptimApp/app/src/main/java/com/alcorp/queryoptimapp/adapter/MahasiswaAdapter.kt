package com.alcorp.queryoptimapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.queryoptimapp.R
import com.alcorp.queryoptimapp.model.MahasiswaModel
import kotlinx.android.synthetic.main.item_mahasiswa.view.*

class MahasiswaAdapter : RecyclerView.Adapter<MahasiswaAdapter.MahasiswaHolder>() {

    private val listMahasiswa = ArrayList<MahasiswaModel>()

    fun setData(listMahasiswa: ArrayList<MahasiswaModel>) {
        if (listMahasiswa.size > 0) {
            this.listMahasiswa.clear()
        }

        this.listMahasiswa.addAll(listMahasiswa)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_mahasiswa, parent, false)
        return MahasiswaHolder(view)
    }

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount(): Int {
        return listMahasiswa.size
    }

    override fun onBindViewHolder(holder: MahasiswaHolder, position: Int) {
        holder.bind(listMahasiswa[position])
    }


    class MahasiswaHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mahasiswaModel: MahasiswaModel) {
            with(itemView) {
                txt_name.text = mahasiswaModel.name
                txt_nim.text = mahasiswaModel.nim
            }
        }
    }
}