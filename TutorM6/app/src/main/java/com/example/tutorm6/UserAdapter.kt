package com.example.tutorm6

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorm6.databinding.UserItemBinding

class UserDiffUtil:DiffUtil.ItemCallback<UserEntity>(){
    override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
        return oldItem.username == newItem.username
    }

    override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
        return oldItem == newItem
    }
}

val userDiffUtil = UserDiffUtil()

class UserAdapter: ListAdapter<UserEntity, UserAdapter.ViewHolder>(userDiffUtil){
    var onEditClickListener:((UserEntity)->Unit)? = null
    var onDeleteClickListener:((UserEntity)->Unit)? = null

    class ViewHolder(val binding: UserItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.binding.user = user

        holder.binding.btnEdit.setOnClickListener {
            onEditClickListener?.invoke(user)
        }
        holder.binding.btnDelete.setOnClickListener {
            onDeleteClickListener?.invoke(user)
        }
    }
}