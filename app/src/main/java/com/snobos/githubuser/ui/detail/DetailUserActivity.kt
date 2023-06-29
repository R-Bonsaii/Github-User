package com.snobos.githubuser.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.snobos.githubuser.R
import com.snobos.githubuser.adapter.SectionsPagerAdapter
import com.snobos.githubuser.data.Result
import com.snobos.githubuser.data.local.entity.FavoriteUser
import com.snobos.githubuser.data.remote.response.DetailResponse
import com.snobos.githubuser.databinding.ActivityDetailBinding
import com.snobos.githubuser.viewmodel.DetailViewModel
import com.snobos.githubuser.viewmodel.ViewModelFactory

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var fab: FloatingActionButton
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(USER)
        val avatarUrl = intent.getStringExtra(AVATARURL)

        detailViewModel = viewModels<DetailViewModel> {
            ViewModelFactory.getInstance(application)
        }.value

        detailViewModel.getDetailDataUser(name!!)

        detailViewModel.githubDetailUser.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val data = result.data
                        setDataDetailUser(data)
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = name
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_POSITION[position])
        }.attach()
        supportActionBar?.elevation = 0f

        fab = binding.fab

        val dataFavorite = FavoriteUser(
            name, avatarUrl
        )
        detailViewModel.getFavoriteUser(dataFavorite) { isFavorite ->
            runOnUiThread {
                setFavoriteButtonImage(isFavorite)
            }
        }

        fab.setOnClickListener {
            detailViewModel.getFavoriteUser(dataFavorite) { isFavorite ->
                runOnUiThread {
                    setFavoriteButtonImage(!isFavorite)
                    if (isFavorite) {
                        detailViewModel.deleteFavoriteUser(dataFavorite)
                        Toast.makeText(this, "Remove $name from favorite", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        detailViewModel.saveFavoriteUser(dataFavorite)
                        Toast.makeText(this, "Add $name to favorite", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setFavoriteButtonImage(isFavorite: Boolean) {
        val drawable =
            if (isFavorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
        fab.setImageDrawable(ContextCompat.getDrawable(fab.context, drawable))
    }

    private fun setDataDetailUser(item: DetailResponse) {
        Glide.with(this).load(item.avatarUrl).into(binding.ItemAvatar)
        binding.name.text = item.name
        binding.username.text = item.login
        binding.followersUser.text = "${item.followers} Followers"
        binding.followingUser.text = "${item.following} Following"
    }

    override fun onResume() {
        super.onResume()
        val name = intent.getStringExtra(USER)
        val avatarUrl = intent.getStringExtra(AVATARURL)
        val dataFavorite = FavoriteUser(
            name!!, avatarUrl
        )
        detailViewModel.getFavoriteUser(dataFavorite) { isFavorite ->
            runOnUiThread {
                setFavoriteButtonImage(isFavorite)
            }
        }
    }

    companion object {
        const val USER = "USER"
        const val AVATARURL = "AVATARURL"

        @StringRes
        private val TAB_POSITION = intArrayOf(
            R.string.Followers, R.string.Following
        )
    }
}
