package com.example.mysocialapp2.fragments.fragmentsUtilities

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.mysocialapp2.data.Post
import com.example.mysocialapp2.databinding.HomeFeedListBinding

class HomeFeedViewHolder(private val itemBinding: HomeFeedListBinding):ViewHolder(itemBinding.root) {

    fun bind(post: Post,context: Context,listener: HomeOnItemListener){
        itemBinding.tvName.text = post.userName

        itemBinding.tvDescription.text = post.description

        Glide.with(context)
            .load(post.url)
            .into(itemBinding.imgPostsRv)

        itemView.setOnClickListener {
            listener.replaceFragment(post)
        }
    }

}

class HomeFeedAdapter(private val listOfPost:List<Post>,private val context: Context,private val listener: HomeOnItemListener):Adapter<HomeFeedViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFeedViewHolder {
        val binding = HomeFeedListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomeFeedViewHolder(itemBinding = binding)
    }

    override fun onBindViewHolder(holder: HomeFeedViewHolder, position: Int) {
        holder.bind(listOfPost[position], context = context,listener)
    }

    override fun getItemCount(): Int {
        return listOfPost.size
    }
}

interface HomeOnItemListener{
    fun replaceFragment(postData: Post)
}