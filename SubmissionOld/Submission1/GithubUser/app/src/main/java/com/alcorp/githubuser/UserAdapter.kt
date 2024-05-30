package com.alcorp.githubuser

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UserAdapter(val context: Context, val listUser: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]

        holder.tvName.text = user.name
        holder.tvLocation.text = user.location
        holder.tvCompany.text = user.company


        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .apply(RequestOptions().override(55, 55))
            .into(holder.img)

        holder.itemView.setOnClickListener {
            val userData = User(
                user.username,
                user.name,
                user.location,
                user.company,
                user.repository,
                user.followers,
                user.following,
                user.avatar
            )

            val intent = Intent(context, ActivityProfile::class.java)
            intent.putExtra(ActivityProfile.EXTRA_USER, userData)
            intent.putExtra(ActivityProfile.EXTRA_NAMEPREF, user.name)
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvLocation: TextView = itemView.findViewById(R.id.tv_item_location)
        var tvCompany: TextView = itemView.findViewById(R.id.tv_item_company)
        var img: ImageView = itemView.findViewById(R.id.item_img)
    }

}