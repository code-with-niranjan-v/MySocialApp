package com.example.mysocialapp2.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.mysocialapp2.data.Post
import com.example.mysocialapp2.data.User
import com.example.mysocialapp2.firebase.MyFirebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class Repository @Inject constructor(
    private val myFirebase: MyFirebase
) {

    var currentUser:User? = User()
    var listOfPostImg:List<String> = listOf()
    var listOfPost = listOf<Post>()
    var currentPost = Post()
    val userPostDetails:MutableList<Post> = mutableListOf()
    var listOfUser = listOf<User>()
    suspend fun signIn(email:String, password:String, switchActivity: () -> Unit) = myFirebase.signIn(email, password,switchActivity)

    suspend fun signUp(password: String,user: User,switchActivity: () -> Unit) = myFirebase.signUp(password, user,switchActivity)

    suspend fun saveUser(user: User,switchActivity: () -> Unit) = myFirebase.saveUser(user)

    suspend fun storePost(uri: Uri, post: Post, user: User) = myFirebase.storePost(uri, post, user)

    suspend fun getCurrentUser() {
        currentUser = myFirebase.getCurrentUser()
    }

    suspend fun loadPostImg(postUIDs:List<String>){
        listOfPostImg = myFirebase.loadPostImg(postUIDs)
    }

    suspend fun loadAllPost(){
        listOfPost = myFirebase.loadAllPost()
    }

    suspend fun storeProfile(uri: Uri,user: User) =  myFirebase.storeProfile(uri,user)

    suspend fun onLike(post: Post,user: User) = myFirebase.onLike(post, user)

    suspend fun loadCurrentPost(uid:String){
        currentPost = myFirebase.loadCurrentPost(uid)
    }

    suspend fun loadAllUser():List<User>{
        listOfUser = myFirebase.loadAllUser()
        return listOfUser
    }

    suspend fun addToFollowers(user: User,followingUser: User) = myFirebase.addToFollowers(user, followingUser)


}