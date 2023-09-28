package com.example.mysocialapp2.fragments.fragmentsUtilities

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mysocialapp2.R
import com.example.mysocialapp2.data.Post
import com.example.mysocialapp2.databinding.FragmentPostBinding
import com.example.mysocialapp2.databinding.ProfilePostImgBinding

class PostImgViewHolder(private val itemBinding: ProfilePostImgBinding):ViewHolder(itemBinding.root){

    fun bind(uri: String,context: Context?){
        Glide.with(context!!)
            .load(uri)
            .thumbnail(0.03f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(itemBinding.postImg)


    }

}

class PostImgAdapter(private val listOfPost:List<String>,private val context: Context?):Adapter<PostImgViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostImgViewHolder {
        val binding = ProfilePostImgBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostImgViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostImgViewHolder, position: Int) {
        holder.bind(listOfPost[position],context)
    }

    override fun getItemCount(): Int {
        return listOfPost.size
    }


}