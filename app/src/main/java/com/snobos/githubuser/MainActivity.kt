package com.snobos.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.snobos.githubuser.adapter.UserAdapter
import com.snobos.githubuser.data.Result
import com.snobos.githubuser.data.remote.response.ItemsItem
import com.snobos.githubuser.data.remote.response.ResponseGithub
import com.snobos.githubuser.databinding.ActivityMainBinding
import com.snobos.githubuser.viewmodel.MainViewModel
import com.snobos.githubuser.viewmodel.SettingViewModel
import com.snobos.githubuser.viewmodel.ViewModelFactory
import com.snobos.githubuser.viewmodel.ViewModelFactorySetting
import com.snobos.githubuser.ui.favorite.FavoriteUserActivity
import com.snobos.githubuser.ui.setting.SettingActivity
import com.snobos.githubuser.ui.setting.SettingPreferences
import com.snobos.githubuser.ui.setting.dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showRecyclerList()

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel =
            ViewModelProvider(this, ViewModelFactorySetting(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel.githubListUser.observe(this) { result ->
            observeResult(result)
        }
    }

    private fun setData(items: List<ItemsItem>) {
        val adapter = UserAdapter(items)
        binding.cardView.adapter = adapter
    }

    private fun setData(response: ResponseGithub) {
        val adapter = UserAdapter(response.items)
        binding.cardView.adapter = adapter
    }

    private fun showRecyclerList() {
        val layoutManager = LinearLayoutManager(this)
        binding.cardView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.cardView.addItemDecoration(itemDecoration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        fun searchUser(username: String) {
            mainViewModel.getSearchDataUser(username)
            mainViewModel.githubUser.observe(this@MainActivity) { result ->
                observeResult(result)
            }
        }

        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                searchUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchUser(newText)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val i = Intent(this, FavoriteUserActivity::class.java)
                startActivity(i)
                true
            }
            R.id.setting -> {
                val i = Intent(this, SettingActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }

    private fun observeResult(result: Result<*>) {
        when (result) {
            is Result.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is Result.Success -> {
                binding.progressBar.visibility = View.GONE
                val data = result.data
                if (data is ResponseGithub) {
                    setData(data)
                } else if (data is List<*>) {
                    setData(data as List<ItemsItem>)
                }
            }
            is Result.Error -> {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}