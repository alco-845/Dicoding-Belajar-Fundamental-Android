package com.alcorp.githubuser.ui.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.githubuser.data.remote.response.User
import com.alcorp.githubuser.databinding.ItemListBinding
import com.alcorp.githubuser.ui.detail.DetailActivity
import com.alcorp.githubuser.ui.detail.DetailActivity.Companion.EXTRA_DETAIL
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UserAdapter(private val context: Context, private val listUser: List<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        with(holder) {
            with(listUser[position]){
                binding.tvItemName.text = login
                Glide.with(itemView.context)
                    .load(avatarUrl)
                    .apply(RequestOptions().override(55, 55))
                    .into(binding.imgItem)

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(EXTRA_DETAIL, login)
                    context.startActivity(intent)
                }
            }
        }
    }

    class UserViewHolder(val binding: ItemListBinding): RecyclerView.ViewHolder(binding.root)
}