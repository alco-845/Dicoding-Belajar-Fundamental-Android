package com.alcorp.githubuser

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_profile.*
import kotlin.properties.Delegates

class ActivityProfile : AppCompatActivity(), View.OnClickListener {

    companion object{
        const val EXTRA_USER = "user"
        const val EXTRA_NAMEPREF = "name"
    }

    private lateinit var namePref: String
    private lateinit var sharedPreferences: SharedPreferences
    private val follow: String = "follow"
    private lateinit var value: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setToolbar()
        setValue()

        namePref = intent.getStringExtra(EXTRA_NAMEPREF)
        sharedPreferences = this.getSharedPreferences("follow", Context.MODE_PRIVATE)
        value = sharedPreferences.getString(namePref + follow, "unfollow").toString()

        if (value.equals("unfollow")){
            btnFollow.setText(R.string.txt_follow)
            btnFollow.setBackgroundResource(R.drawable.btn_follow)
        } else {
            btnFollow.setText(R.string.txt_unfollow)
            btnFollow.setBackgroundResource(R.drawable.btn_unfollow)
        }

        btnFollow.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
            if (value.equals("unfollow")){
                editor.putString(namePref + follow, "following")
                btnFollow.setText(R.string.txt_follow)
                btnFollow.setBackgroundResource(R.drawable.btn_follow)
                reload()
            } else {
                editor.putString(namePref + follow, "unfollow")
                btnFollow.setText(R.string.txt_unfollow)
                btnFollow.setBackgroundResource(R.drawable.btn_unfollow)
                reload()
            }
            editor.apply()
            editor.commit()
    }

    private fun reload(){
        finish()
        overridePendingTransition(0, 0)
        startActivity(getIntent())
        overridePendingTransition(0, 0)
    }

    private fun setValue() {
        val user = intent.getParcelableExtra(EXTRA_USER) as User

        tvName.text = user.name
        tvUsername.text = user.username
        tvLocation.text = user.location
        tvCompany.text = user.company
        tvRepository.text = user.repository
        tvFollowers.text = user.followers
        tvFollowing.text = user.following

        Glide.with(this)
            .load(user.avatar)
            .apply(RequestOptions().override(120, 120))
            .into(profile_img)
    }

    @SuppressLint("RestrictedApi")
    private fun setToolbar(){
        supportActionBar?.title = "Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val user = intent.getParcelableExtra(EXTRA_USER) as User

        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.menu_share){
            val share = Intent(Intent.ACTION_SEND)
            share.setType("text/plain")
            share.putExtra(Intent.EXTRA_TEXT,
                "Name : ${user.name}" +
                       "\nUsername : ${user.username}" +
                       "\nLocation : ${user.location}" +
                       "\nCompany : ${user.company}")
            startActivity(Intent.createChooser(share, "Share Profile..."))

        }
        return super.onOptionsItemSelected(item)
    }
}
