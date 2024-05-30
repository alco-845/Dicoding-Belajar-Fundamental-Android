package com.alcorp.githubuser

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class ActivityMain : AppCompatActivity() {

    private var list = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        setData()
    }

    @SuppressLint("Recycle")
    private fun setData(){
        val getUserName = resources.getStringArray(R.array.username)
        val getName = resources.getStringArray(R.array.name)
        val getLocation = resources.getStringArray(R.array.location)
        val getCompany = resources.getStringArray(R.array.company)
        val getRepository = resources.getStringArray(R.array.repository)
        val getFollowers = resources.getStringArray(R.array.followers)
        val getFollowings = resources.getStringArray(R.array.following)
        val getAvatar = resources.obtainTypedArray(R.array.avatar)

        for (position in getName.indices){
            val user = User(
                getUserName[position],
                getName[position],
                getLocation[position],
                getCompany[position],
                getRepository[position],
                getFollowers[position],
                getFollowings[position],
                getAvatar.getResourceId(position, -1)
            )
            list.add(user)
        }
        loadList(list)
    }

    private fun loadList(user: ArrayList<User>) {
        rec_list.setHasFixedSize(true)
        rec_list.layoutManager = LinearLayoutManager(this)

        val userAdapter = UserAdapter(this, user)
        rec_list.adapter = userAdapter
    }
}
