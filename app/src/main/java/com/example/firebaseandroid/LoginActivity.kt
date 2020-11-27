package com.example.firebaseandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    lateinit var mAuth:FirebaseAuth

    lateinit var mLoginEmail:EditText
    lateinit var mLoginPassword:EditText
    lateinit var mLoginButton: Button
    lateinit var mForgotPassword:TextView
    lateinit var mRegister:TextView
    lateinit var mResendEmailVerification:TextView
    lateinit var mLoggedIn:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth=FirebaseAuth.getInstance()

        mLoginEmail=findViewById(R.id.login_email)
        mLoginPassword=findViewById(R.id.login_password)
        mLoginButton=findViewById(R.id.btn_login)
        mForgotPassword=findViewById(R.id.forgot_password)
        mRegister=findViewById(R.id.link_register)
        mResendEmailVerification=findViewById(R.id.resend_verification_email)
        mLoggedIn=findViewById(R.id.loggedIn)

        mAuth.signOut()


        mLoginButton.setOnClickListener {
            loginUser()
        }
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }

    private fun loginUser(){

        val email=mLoginEmail.text.toString().trim()
        val password=mLoginPassword.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()){
                //use courotines

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        mAuth.signInWithEmailAndPassword(email,password).isSuccessful
                        withContext(Dispatchers.Main){
                            checkLoggedInState()

                        }

                    }catch(e: Exception){
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@LoginActivity,e.message, Toast.LENGTH_LONG).show()
                        }

                    }
                }

            }
        }

    private fun checkLoggedInState() {

        if (mAuth.currentUser==null){
            mLoggedIn.text="You are not logged In"

        } else{
            mLoggedIn.text="You are logged in"
        }


    }
}
