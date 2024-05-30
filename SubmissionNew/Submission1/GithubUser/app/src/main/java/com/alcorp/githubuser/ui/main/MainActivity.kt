package com.alcorp.githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alcorp.githubuser.R
import com.alcorp.githubuser.databinding.ActivityMainBinding
import com.alcorp.githubuser.utils.LoadingDialog
import com.alcorp.githubuser.utils.checkNetwork
import com.alcorp.githubuser.viewmodel.MainViewModel
import com.alcorp.githubuser.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var userAdapter: UserAdapter
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.refreshMain.setOnRefreshListener(this)

        loadingDialog = LoadingDialog(this)
        loadingDialog.showDialog()

        if (checkNetwork(this)){
            mainViewModel.getUser()
            lifecycleScope.launch {
                delay(1500)

                withContext(Dispatchers.Main) {
                    mainViewModel.userList.observe(this@MainActivity) {
                        userAdapter = UserAdapter(this@MainActivity, it)
                        binding.rvMain.setHasFixedSize(true)
                        binding.rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
                        binding.rvMain.adapter = userAdapter
                    }

                    mainViewModel.errorMessage.observe(this@MainActivity) {
                        if (it.isNotEmpty()) {
                            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                loadingDialog.hideDialog()
            }
        } else {
            loadingDialog.hideDialog()
            Toast.makeText(this@MainActivity, resources.getString(R.string.txt_toast_network), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRefresh() {
        init()
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
                loadingDialog.showDialog()
                lifecycleScope.launch {
                    delay(500)

                    withContext(Dispatchers.Main) {
                        if (checkNetwork(this@MainActivity)){
                            if (query.isEmpty()) {
                                mainViewModel.getUser()
                            } else {
                                mainViewModel.searchUser(query)
                            }
                        } else {
                            Toast.makeText(this@MainActivity, resources.getString(R.string.txt_toast_network), Toast.LENGTH_SHORT).show()
                        }
                    }
                    loadingDialog.hideDialog()
                }
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })
        return true
    }
}