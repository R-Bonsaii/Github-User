package com.snobos.githubuser.ui.favorite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.snobos.githubuser.databinding.ActivityFavoriteBinding
import com.snobos.githubuser.adapter.UserAdapter
import com.snobos.githubuser.data.remote.response.ItemsItem
import com.snobos.githubuser.viewmodel.MainViewModel
import com.snobos.githubuser.viewmodel.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showRecyclerList()

        val mainViewModel by viewModels<MainViewModel> {
            ViewModelFactory.getInstance(application)
        }

        mainViewModel.getAllFavoriteUser().observe(this) { result ->
            val users = arrayListOf<ItemsItem>()
            result.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl.toString())
                users.add(item)
            }
            setData(users)
        }
    }

    private fun setData(users: List<ItemsItem>) {
        val adapter = UserAdapter(users)
        binding.cardView.adapter = adapter
    }

    private fun showRecyclerList() {
        val layoutManager = LinearLayoutManager(this)
        binding.cardView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.cardView.addItemDecoration(itemDecoration)
    }

}