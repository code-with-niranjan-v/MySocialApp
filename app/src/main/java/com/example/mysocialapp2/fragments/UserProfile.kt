package com.example.mysocialapp2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysocialapp2.R
import com.example.mysocialapp2.data.User
import com.example.mysocialapp2.databinding.FragmentProfile2Binding
import com.example.mysocialapp2.databinding.FragmentProfileBinding
import com.example.mysocialapp2.fragments.fragmentsUtilities.PostImgAdapter
import com.example.mysocialapp2.viewmodel.UserViewModel
import com.example.mysocialapp2.viewmodel.UserViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserProfile : Fragment() {

    private lateinit var userProfile: FragmentProfile2Binding
    private lateinit var viewModel: UserViewModel
    @Inject
    lateinit var viewModelFactory: UserViewModelFactory
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userProfile = FragmentProfile2Binding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this,viewModelFactory).get(UserViewModel::class.java)
        viewModel.getCurrentUser()
        return userProfile.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragmentManager.setFragmentResultListener("userData",this){requestKey,bundle ->
            user = bundle.getSerializable("userData") as User
            loadDetails(user)
            viewModel.loadPostImg(user.post)
        }
        viewModel.listOfPostImg.observe(viewLifecycleOwner){
            if(context!=null){
                val adapter = PostImgAdapter(it,context)
                userProfile.rvPosts.adapter = adapter
                userProfile.rvPosts.layoutManager = GridLayoutManager(context,3,
                    RecyclerView.VERTICAL,false)
                userProfile.dataLayer.visibility = View.VISIBLE
            }
        }






        userProfile.btnFollow.setOnClickListener {
            if(!user.followers.contains(viewModel.currentUser?.uid)){
                val followingUser = viewModel.currentUser
                viewModel.addToFollowers(user,followingUser!!)
                userProfile.btnFollow.text = "Following"
            }

        }

    }

    private fun loadDetails(user: User){
        userProfile.tvAbout.text = user.about
        userProfile.tvUsername.text = user.userName
        if(user.uid == viewModel.currentUser?.uid){
            userProfile.btnFollow.visibility = View.INVISIBLE
        }
        if(user.followers.contains(viewModel.currentUser?.uid)){
            userProfile.btnFollow.text = "Following"
        }
        userProfile.tvZeroFollowers.text = user.followers.size.toString()
        userProfile.tvZeroFollowings.text = user.following.size.toString()
        userProfile.tvZeroPost.text = user.post.size.toString()
        if (user.profile.isNotEmpty()){
            Glide.with(requireContext())
                .load(user.profile)
                .circleCrop()
                .into(userProfile.imgProfile)
        }
    }



}