package com.example.mysocialapp2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mysocialapp2.R
import com.example.mysocialapp2.databinding.ActivityUserBinding
import com.example.mysocialapp2.di.ApplicationClass
import com.example.mysocialapp2.fragments.Home
import com.example.mysocialapp2.fragments.Post
import com.example.mysocialapp2.fragments.Profile
import com.example.mysocialapp2.fragments.Search
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {
    private lateinit var userBinding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(userBinding.root)
        onReplaceFragment(Home())
        userBinding.bottomNavigationBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {onReplaceFragment(Home())}
                R.id.search -> {onReplaceFragment(Search())}
                R.id.post -> {onReplaceFragment(Post())}
                R.id.profile -> {onReplaceFragment(Profile())}
            }
            true
        }
    }

    private fun onReplaceFragment(fragment: Fragment){

        supportFragmentManager.beginTransaction()
            .replace(userBinding.container.id,fragment)
            .commit()

    }
}