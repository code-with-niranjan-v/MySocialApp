package com.example.mysocialapp2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysocialapp2.R
import com.example.mysocialapp2.data.User
import com.example.mysocialapp2.databinding.FragmentProfileBinding
import com.example.mysocialapp2.firebase.MyFirebase
import com.example.mysocialapp2.fragments.fragmentsUtilities.PostImgAdapter
import com.example.mysocialapp2.repository.Repository
import com.example.mysocialapp2.viewmodel.UserViewModel
import com.example.mysocialapp2.viewmodel.UserViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import org.checkerframework.checker.index.qual.LengthOf
import javax.inject.Inject

@AndroidEntryPoint
class Profile : Fragment() {

    private lateinit var profileBinding: FragmentProfileBinding
    private lateinit var viewModel: UserViewModel
    @Inject
    lateinit var viewModelFactory: UserViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        profileBinding = FragmentProfileBinding.inflate(inflater,container,false)

        viewModel = ViewModelProvider(this,viewModelFactory).get(UserViewModel::class.java)
        viewModel.getCurrentUser()

        return profileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.currentUserLive.observe(viewLifecycleOwner){ user ->
            setProfile(user)
            viewModel.loadPostImg(user.post)
            viewModel.listOfPostImg.observe(viewLifecycleOwner){
                loadImg(it)
                if(context!=null){
                    val adapter = PostImgAdapter(it,context)
                    profileBinding.rvPosts.adapter = adapter
                    profileBinding.rvPosts.layoutManager = GridLayoutManager(context,3,RecyclerView.VERTICAL,false)
                    profileBinding.dataLayer.visibility = View.VISIBLE

                }

            }
        }

        val imgPostPicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            if (it != null){
                profileBinding.imgProfile.setImageURI(it)
                val user = viewModel.currentUser
                if (user!=null){
                    viewModel.storeProfile(it,user)
                }else{
                    Log.e("Testing-1","Null error")
                }

            }
        }

        profileBinding.imgProfile.setOnClickListener {
            imgPostPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }




    }

    fun setProfile(user: User){
        profileBinding.tvAbout.text = user.about
        profileBinding.tvUsername.text = user.userName
        profileBinding.tvZeroFollowers.text = user.followers.size.toString()
        profileBinding.tvZeroFollowings.text = user.following.size.toString()
        profileBinding.tvZeroPost.text = user.post.size.toString()
       if (user.profile.isNotEmpty()){
           Glide.with(requireContext())
               .load(user.profile)
               .circleCrop()
               .into(profileBinding.imgProfile)
       }
    }

    fun loadImg(list: List<String>){
        Log.e("Testing-1",list.toString())
    }

}