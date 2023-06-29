package com.snobos.githubuser.ui.follow

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.snobos.githubuser.adapter.UserAdapter
import com.snobos.githubuser.data.Result
import com.snobos.githubuser.data.remote.response.ItemsItem
import com.snobos.githubuser.databinding.FragmentFollowBinding
import com.snobos.githubuser.viewmodel.FollowViewModel
import com.snobos.githubuser.viewmodel.ViewModelFactory


class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private var position: Int = 0
    private var username: String? = null

    private val followViewModel: FollowViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerList()

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        if (position == 1) {
            username?.let { followViewModel.getDataFollowers(it) }
            followViewModel.followerUser.observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    observeResult(result)
                }
            }
        } else {
            username?.let { followViewModel.getDataFollowing(it) }
            followViewModel.followingUser.observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    observeResult(result)
                }
            }
        }
    }

    private fun setDataList(item: List<ItemsItem>) {
        val adapter = UserAdapter(item)
        binding.followCard.adapter = adapter
    }

    private fun observeResult(result: Result<List<ItemsItem>>) {
        when (result) {
            is Result.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is Result.Success -> {
                binding.progressBar.visibility = View.GONE
                val data = result.data
                setDataList(data)
            }
            is Result.Error -> {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showRecyclerList() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.followCard.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.followCard.addItemDecoration(itemDecoration)
    }

    companion object {
        const val ARG_POSITION = "POSITION"
        const val ARG_USERNAME = "USERNAME"
    }
}