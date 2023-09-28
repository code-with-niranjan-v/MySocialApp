package com.example.mysocialapp2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mysocialapp2.repository.Repository
import javax.inject.Inject

class UserViewModelFactory @Inject constructor(
    private val repo : Repository
):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(repo) as T
    }

}