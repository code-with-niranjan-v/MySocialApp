package com.example.mysocialapp2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.mysocialapp2.databinding.ActivityMainBinding
import com.example.mysocialapp2.firebase.MyFirebase
import com.example.mysocialapp2.repository.Repository
import com.example.mysocialapp2.viewmodel.UserViewModel
import com.example.mysocialapp2.viewmodel.UserViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: UserViewModel
    @Inject
    lateinit var viewModelFactory: UserViewModelFactory

    private lateinit var loginBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        val myFirebase = MyFirebase()
        val repo = Repository(myFirebase)
        //val viewModelFactory = UserViewModelFactory(repo)
        viewModel = ViewModelProvider(this,viewModelFactory).get(UserViewModel::class.java)


        viewModel = ViewModelProvider(this,viewModelFactory).get(UserViewModel::class.java)


        loginBinding.tvDontHave.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginBinding.btnLogin.setOnClickListener {
            val switchActivity = {
                val intent = Intent(this,UserActivity::class.java)
                startActivity(intent)
                finish()
            }
            val email = loginBinding.etEmail.text.toString()
            val password = loginBinding.etPassword.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                val result = viewModel.signIn(email,password,switchActivity).isCompleted
                if(result){
                    viewModel.getCurrentUser()
                }
            }else{
                if(email.isEmpty()){
                    loginBinding.txtLayout.error = "Enter your Email"
                }
                if (password.isEmpty()){
                    loginBinding.txtPassLayout.error = "Enter your password"
                }
            }


        }

    }


}