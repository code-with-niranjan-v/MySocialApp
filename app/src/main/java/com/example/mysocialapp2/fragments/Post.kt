package com.example.mysocialapp2.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.mysocialapp2.R
import com.example.mysocialapp2.data.Post
import com.example.mysocialapp2.databinding.FragmentPostBinding
import com.example.mysocialapp2.firebase.MyFirebase
import com.example.mysocialapp2.repository.Repository
import com.example.mysocialapp2.viewmodel.UserViewModel
import com.example.mysocialapp2.viewmodel.UserViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class Post : Fragment() {

    private lateinit var postBinding: FragmentPostBinding
    private lateinit var selectedUri:Uri
    private lateinit var viewModel:UserViewModel
    @Inject
    lateinit var viewModelFactory:UserViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        postBinding = FragmentPostBinding.inflate(inflater,container,false)

        return postBinding.root

       // (activity?.application as ApplicationClass).applicationComponent.inject(activity)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity?.application as ApplicationClass).applicationComponent.inject(this)

        viewModel = ViewModelProvider(this,viewModelFactory).get(UserViewModel::class.java)


        val imgPostPicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            if (it != null){
                selectedUri = it
                postBinding.imgPost.setImageURI(it)
            }
        }

        postBinding.imgPost.setOnClickListener {
            imgPostPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))}



        postBinding.btnPost.setOnClickListener {
            val desc = postBinding.etPost.text.toString()
            val post = Post(UUID.randomUUID().toString(), description = desc)
            if(viewModel.currentUser != null){
                viewModel.storePost(selectedUri,post, user = viewModel.currentUser!!)
            }

            postBinding.imgPost.setImageResource(R.drawable.baseline_add_circle_outline_24)
            postBinding.etPost.text.clear()
        }


    }


}