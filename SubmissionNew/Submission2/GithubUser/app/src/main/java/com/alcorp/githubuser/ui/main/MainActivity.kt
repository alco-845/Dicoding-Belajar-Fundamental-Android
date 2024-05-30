package com.alcorp.githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.githubuser.R
import com.alcorp.githubuser.databinding.ActivityMainBinding
import com.alcorp.githubuser.ui.favorite.FavoriteActivity
import com.alcorp.githubuser.ui.setting.SettingActivity
import com.alcorp.githubuser.utils.LoadingDialog
import com.alcorp.githubuser.utils.checkNetwork
import com.alcorp.githubuser.viewmodel.MainViewModel
import com.alcorp.githubuser.viewmodel.SettingViewModel
import com.alcorp.githubuser.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var userAdapter: UserAdapter

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this, dataStore)
    }

    private val settingViewModel: SettingViewModel by viewModels {
        ViewModelFactory.getInstance(this, dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        checkTheme()
        binding.refreshMain.setOnRefreshListener(this)

        loadingDialog = LoadingDialog(this)
        showLoading(true)

        if (checkNetwork(this)){
            mainViewModel.getUser()

            mainViewModel.userList.observe(this) {
                userAdapter = UserAdapter(this, it)
                binding.rvMain.setHasFixedSize(true)
                binding.rvMain.layoutManager = LinearLayoutManager(this)
                binding.rvMain.adapter = userAdapter

                mainViewModel.isLoading.observe(this) { status ->
                    showLoading(status)
                }
            }

            mainViewModel.errorMessage.observe(this) {
                if (it.isNotEmpty()) {
                    showLoading(false)
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            showLoading(false)
            Toast.makeText(this, resources.getString(R.string.txt_toast_network), Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkTheme() {
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onRefresh() {
        reload()
        binding.refreshMain.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        menu.findItem(R.id.menuShare).isVisible = false

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menuSearch).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.hint_search)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (checkNetwork(this@MainActivity)){
                    if (query.isEmpty()) {
                        reload()
                    } else {
                        mainViewModel.searchUser(query)
                    }
                } else {
                    Toast.makeText(this@MainActivity, resources.getString(R.string.txt_toast_network), Toast.LENGTH_SHORT).show()
                }
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuSetting){
            startActivity(Intent(this, SettingActivity::class.java))
            finish()
        } else if (item.itemId == R.id.menuFav){
            startActivity(Intent(this, FavoriteActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun reload() {
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) loadingDialog.showDialog() else loadingDialog.hideDialog()
    }
}