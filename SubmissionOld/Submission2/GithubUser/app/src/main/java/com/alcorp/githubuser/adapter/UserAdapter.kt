package com.alcorp.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.githubuser.R
import com.alcorp.githubuser.model.User
import com.alcorp.githubuser.view.ProfileActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_list.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private val listUser = ArrayList<User>()

    fun setData(items: java.util.ArrayList<User>) {
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(user: User){
            with(itemView){
                tv_item_name.text = user.login
                Glide.with(itemView.context)
                    .load(user.avatar_url)
                    .apply(RequestOptions().override(55, 55))
                    .into(item_img)

                itemView.setOnClickListener {
                    val intent = Intent(context, ProfileActivity::class.java)
                    intent.putExtra(ProfileActivity.EXTRA_USER, user)
                    context?.startActivity(intent)
                }
            }
        }
    }

}