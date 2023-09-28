package com.example.mysocialapp2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialapp2.R
import com.example.mysocialapp2.data.User
import com.example.mysocialapp2.databinding.FragmentSearchBinding
import com.example.mysocialapp2.fragments.fragmentsUtilities.OnSearchItemClick
import com.example.mysocialapp2.fragments.fragmentsUtilities.UserAdapter
import com.example.mysocialapp2.viewmodel.UserViewModel
import com.example.mysocialapp2.viewmodel.UserViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Search : Fragment(),OnSearchItemClick {

    private lateinit var searchBinding: FragmentSearchBinding
    @Inject lateinit var viewModelFactory: UserViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        searchBinding = FragmentSearchBinding.inflate(inflater,container,false)
        return searchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this,viewModelFactory).get(UserViewModel::class.java)
        viewModel.loadAllUser()
        viewModel.listOfUser.observe(viewLifecycleOwner){
            val adapter = UserAdapter(it,this)
            searchBinding.recyclerViewUser.adapter = adapter
            searchBinding.recyclerViewUser.layoutManager = LinearLayoutManager(context)

            searchBinding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText!!.isNotEmpty()){
                        val filterList = mutableListOf<User>()
                        for(user in it){
                            if (user.userName.lowercase().contains(newText.lowercase())){
                                filterList.add(user)
                            }
                        }

                        if(filterList.isEmpty()){
                            Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_SHORT).show()
                            adapter.setFilterList(it)
                        }
                        else{
                            adapter.setFilterList(filterList)
                        }
                    }

                    return true
                }



            })
        }



    }

    override fun onClickListener(user:User) {
        val bundle = Bundle()
        bundle.putSerializable("userData",user)
        parentFragmentManager.setFragmentResult("userData",bundle)
        parentFragmentManager.beginTransaction().replace(R.id.container,UserProfile())
            .commit()
    }






}