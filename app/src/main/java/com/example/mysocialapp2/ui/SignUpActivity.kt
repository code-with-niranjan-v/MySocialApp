package com.example.mysocialapp2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mysocialapp2.R
import com.example.mysocialapp2.data.User
import com.example.mysocialapp2.databinding.ActivityMainBinding
import com.example.mysocialapp2.databinding.ActivitySignUpBinding
import com.example.mysocialapp2.firebase.MyFirebase
import com.example.mysocialapp2.repository.Repository
import com.example.mysocialapp2.viewmodel.UserViewModel
import com.example.mysocialapp2.viewmodel.UserViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var signupBinding: ActivitySignUpBinding
    private lateinit var viewModel: UserViewModel
    @Inject
    lateinit var viewModelFactory:UserViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(signupBinding.root)
        viewModel = ViewModelProvider(this,viewModelFactory).get(UserViewModel::class.java)

        signupBinding.btnSignUp.setOnClickListener {
            val email = signupBinding.etEmail.text.toString()
            val password = signupBinding.etPassword.text.toString()
            val username = signupBinding.etUsername.text.toString()
            val about = signupBinding.etAbout.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty() && about.isNotEmpty()){

                try {
                    val user = User(email = email, about = about, userName = username)
                    val jobSignUp = viewModel.signUp(password, user,::switchActivity)

                }catch (e:Exception){
                    Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
                }





            }else
            {
                if(email.isEmpty()){
                    signupBinding.txtLayout.error = "Enter your Email"
                }
                if(password.isEmpty()){
                    signupBinding.txtPassLayout.error = "Enter your Password"
                }
                if(about.isEmpty()){
                    signupBinding.txtLayoutAbout.error = "Enter your About"
                }
                if(username.isEmpty()){
                    signupBinding.txtLayoutUsername.error = "Enter your Username"
                }
            }




        }
    }

    private fun switchActivity(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}