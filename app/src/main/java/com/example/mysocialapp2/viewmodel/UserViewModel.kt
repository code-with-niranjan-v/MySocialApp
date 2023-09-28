package com.example.mysocialapp2.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.mysocialapp2.data.Post
import com.example.mysocialapp2.data.User
import com.example.mysocialapp2.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel(
    private val repo : Repository
):ViewModel() {
    var currentUser:User? = repo.currentUser
    var currentUserLive = MutableLiveData<User>()
    var listOfPostImg = MutableLiveData<List<String>>()
    var listOfPost = MutableLiveData<List<Post>>()
    var currentPost = MutableLiveData(Post())
    var listOfUser = MutableLiveData<List<User>>()
    init {

        getCurrentUser()
        currentUser = repo.currentUser
        currentUserLive.postValue(currentUser)
    }

    fun signIn(email:String,password:String,switchActivity: () -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        repo.signIn(email, password,switchActivity)

    }

    fun signUp(password: String,user: User,switchActivity: () -> Unit) = CoroutineScope(Dispatchers.IO).launch { repo.signUp(password, user,switchActivity) }

    fun storePost(uri: Uri, post: Post, user: User) = CoroutineScope(Dispatchers.IO).launch { repo.storePost(uri, post, user) }

    fun getCurrentUser() = CoroutineScope(Dispatchers.Main).launch{
        repo.getCurrentUser()
        currentUser = repo.currentUser
        currentUserLive.postValue(currentUser)
    }

    fun loadPostImg(postUIDs:List<String>){
        CoroutineScope(Dispatchers.Main).launch {
            repo.loadPostImg(postUIDs)
            listOfPostImg.postValue(repo.listOfPostImg)
        }
    }

    fun loadAllPost(){
        CoroutineScope(Dispatchers.IO).launch{
            repo.loadAllPost()
            listOfPost.postValue(repo.listOfPost)
        }
    }

    fun storeProfile(uri: Uri,user: User) = CoroutineScope(Dispatchers.IO).launch { repo.storeProfile(uri,user) }

    fun onLike(post: Post,user: User) = CoroutineScope(Dispatchers.IO).launch{
        repo.onLike(post,user)
    }

    fun loadCurrentPost(uid:String) = CoroutineScope(Dispatchers.IO).launch {
        repo.loadCurrentPost(uid)
        currentPost.postValue(repo.currentPost)
    }

    fun loadAllUser(){
        CoroutineScope(Dispatchers.IO).launch {
            listOfUser.postValue(repo.loadAllUser())
        }
    }

    fun addToFollowers(user: User,followingUser: User) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.addToFollowers(user,followingUser)
        }
    }


}