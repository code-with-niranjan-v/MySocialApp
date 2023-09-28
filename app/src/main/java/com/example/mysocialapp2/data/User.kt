package com.example.mysocialapp2.data

import java.io.Serializable

data class User(
    val uid: String = "",
    val userName:String = "",
    val email:String = "",
    val about:String = "",
    val profile:String = "",
    val followers:MutableList<String> = mutableListOf(),
    val following:MutableList<String> = mutableListOf(),
    val post:MutableList<String> = mutableListOf()
):Serializable