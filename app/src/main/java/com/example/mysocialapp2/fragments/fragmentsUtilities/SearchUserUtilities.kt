package com.example.mysocialapp2.fragments.fragmentsUtilities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mysocialapp2.data.User
import com.example.mysocialapp2.databinding.ListUserBinding

class UserViewHolder(private val itemBinding:ListUserBinding):ViewHolder(itemBinding.root) {

    fun bindData(user:User,listener: OnSearchItemClick){
        itemBinding.tvUser.text = user.userName
        itemBinding.tvUser.setOnClickListener {
            listener.onClickListener(user)
        }
    }

}


class UserAdapter(private var listOfUser:List<User>,private val listener:OnSearchItemClick):Adapter<UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ListUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindData(listOfUser[position],listener)
    }

    override fun getItemCount(): Int {
        return listOfUser.size
    }

    fun setFilterList(list: List<User>){
        this.listOfUser = list
        notifyDataSetChanged()
    }

}

interface OnSearchItemClick{
    fun onClickListener(user: User)
}