package com.snobos.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snobos.githubuser.R
import com.snobos.githubuser.data.remote.response.ItemsItem
import com.snobos.githubuser.ui.detail.DetailUserActivity
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(private val listUser: List<ItemsItem>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageProfile: CircleImageView = itemView.findViewById(R.id.ItemAvatar)
        val username: TextView = itemView.findViewById(R.id.username)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
    )

    override fun onBindViewHolder(Holder: ViewHolder, position: Int) {
        val user = listUser[position]


        Glide.with(Holder.itemView.context)
            .load(user.avatarUrl)
            .into(Holder.imageProfile)
        Holder.username.text = user.login

        Holder.itemView.setOnClickListener {
            val intentDetail = Intent(Holder.itemView.context, DetailUserActivity::class.java)
            intentDetail.putExtra(DetailUserActivity.USER, user.login)
            intentDetail.putExtra(DetailUserActivity.AVATARURL, user.avatarUrl)
            Holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

}