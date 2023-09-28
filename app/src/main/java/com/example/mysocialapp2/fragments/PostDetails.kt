package com.example.mysocialapp2.fragments

import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mysocialapp2.R
import com.example.mysocialapp2.data.Post
import com.example.mysocialapp2.databinding.FragmentPostDetailsBinding
import com.example.mysocialapp2.databinding.PostRvItemsBinding
import com.example.mysocialapp2.firebase.MyFirebase
import com.example.mysocialapp2.repository.Repository
import com.example.mysocialapp2.viewmodel.UserViewModel
import com.example.mysocialapp2.viewmodel.UserViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostDetails : Fragment() {

    private lateinit var postDetailsBinding: FragmentPostDetailsBinding
    private lateinit var currentPost: Post
    private lateinit var viewModel: UserViewModel
    @Inject
    lateinit var viewModelFactory: UserViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postDetailsBinding = FragmentPostDetailsBinding.inflate(inflater, container, false)

        return postDetailsBinding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        viewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)
        viewModel.getCurrentUser()

        parentFragmentManager.setFragmentResultListener("dataPost", this) { requestKey, bundle ->
            currentPost = getPostData(bundle)
            Log.e("testing1",currentPost.likes?.size.toString())
            loadDetails(
                currentPost.userName.toString(),
                currentPost.url.toString(),
                currentPost.description.toString(),
                currentPost.likes?.size ?:0
            )
            if (!currentPost.uid.isNullOrBlank()) {
                postDetailsBinding.imgLike.setOnClickListener {

                    var num = postDetailsBinding.tvNumberLike.text.toString()
                    if (num.isNullOrBlank()){
                        num = 0.toString()
                    }

                        if (!currentPost.likes.toString().contains(viewModel.currentUser!!.uid)) {
                            postDetailsBinding.tvNumberLike.text = (num.toInt() + 1).toString()
                            viewModel.onLike(post = currentPost, viewModel.currentUser!!)
                        }


                }


            }



        }


    }

    fun loadDetails(name: String, imgUrl: String, descp: String,likes:Int) {

        postDetailsBinding.tvName.text = name
        postDetailsBinding.tvDescription.text = descp

        Glide.with(requireContext())
            .load(imgUrl)
            .into(postDetailsBinding.imgPostsRv)

        postDetailsBinding.tvNumberLike.text = (likes).toString()
    }

    fun getPostData(bundle: Bundle): Post {
        val name = bundle.getString("name")
        val imgUrl = bundle.getString("imgUrl")
        val descp = bundle.getString("desc")
        val uid = bundle.getString("uid")
        var likes = bundle.getString("likes")
        val profile = bundle.getString("profile")
        val time = bundle.getString("time")
        val userUid = bundle.getString("userUid")
        val postData = bundle.getSerializable("PostData") as Post

        Log.e("testing1",postData.toString())

        var listLikes = likes?.split(",")?.toMutableList() ?: mutableListOf()
        if(listLikes!!.contains("[]")){
            val index = listLikes.indexOf("[]")
            listLikes.removeAt(index)

        }


        return Post(
            uid!!,
            imgUrl!!,
            descp!!,
            userUid!!,
            name!!,
            profile!!,
            likes = listLikes  ,
            time!!.toLong()
        )
    }

}

