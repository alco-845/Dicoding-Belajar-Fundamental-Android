package com.alcorp.githubuser.ui.favorite

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.githubuser.data.local.entity.FavoriteEntity
import com.alcorp.githubuser.databinding.ItemListBinding
import com.alcorp.githubuser.ui.detail.DetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FavoriteAdapter(private val context: Context, private val listFavorite: List<FavoriteEntity>): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = ItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        with(holder) {
            with(listFavorite[position]){
                binding.tvItemName.text = login
                Glide.with(itemView.context)
                    .load(avatarUrl)
                    .apply(RequestOptions().override(55, 55))
                    .into(binding.imgItem)

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_DETAIL, login)
                    context.startActivity(intent)
                }
            }
        }
    }

    class FavoriteViewHolder(val binding: ItemListBinding): RecyclerView.ViewHolder(binding.root)
}