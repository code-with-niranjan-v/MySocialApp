package com.example.mysocialapp2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialapp2.R
import com.example.mysocialapp2.databinding.FragmentHomeBinding
import com.example.mysocialapp2.firebase.MyFirebase
import com.example.mysocialapp2.fragments.fragmentsUtilities.HomeFeedAdapter
import com.example.mysocialapp2.fragments.fragmentsUtilities.HomeOnItemListener
import com.example.mysocialapp2.repository.Repository
import com.example.mysocialapp2.viewmodel.UserViewModel
import com.example.mysocialapp2.viewmodel.UserViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Home : Fragment(),HomeOnItemListener {

    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var viewModel: UserViewModel
    @Inject
    lateinit var viewModelFactory:UserViewModelFactory
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater,container,false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeBinding.shimmerLayout.startShimmer()

        viewModel = ViewModelProvider(this,viewModelFactory).get(UserViewModel::class.java)

        viewModel.loadAllPost()
        viewModel.getCurrentUser()
        viewModel.listOfPost.observe(viewLifecycleOwner){
            Log.e("Testing-1",it.toString())
            if (context!=null){
                val adapter = HomeFeedAdapter(it,requireContext(),this)
                homeBinding.homeFeedRv.adapter = adapter
                homeBinding.homeFeedRv.layoutManager = LinearLayoutManager(context)
                homeBinding.shimmerLayout.stopShimmer()
                homeBinding.homeFeedRv.visibility = View.VISIBLE
                homeBinding.shimmerLayout.visibility = View.GONE
            }
        }

    }


    override fun replaceFragment(postData: com.example.mysocialapp2.data.Post) {
        val bundle = Bundle()
        bundle.putString("name",postData.userName)
        bundle.putString("desc",postData.description)
        bundle.putString("imgUrl",postData.url)
        bundle.putString("uid",postData.uid)
        bundle.putString("userUid",postData.userUid)
        bundle.putString("likes", postData.likes.toString())
        bundle.putString("profile",postData.userProfilePic)
        bundle.putString("time",postData.timeStamp.toString())
        bundle.putSerializable("PostData",postData)

        PostDetails().arguments = bundle
        parentFragmentManager.setFragmentResult("dataPost",bundle)
        parentFragmentManager.beginTransaction()
            .replace(R.id.container,PostDetails())
            .commit()
        Log.e("Testing-1","Fragment - post details = ${postData.toString()} ")
    }


}