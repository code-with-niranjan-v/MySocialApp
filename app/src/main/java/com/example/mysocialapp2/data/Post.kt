package com.example.mysocialapp2.data

import java.io.Serializable

data class Post(
    val uid:String = "",
    val url:String = "",
    val description:String = "",
    val userUid:String = "",
    val userName:String = "",
    val userProfilePic:String = "",
    val likes: MutableList<String> = mutableListOf(""),
    val timeStamp:Long = 0,

    ):Serializable