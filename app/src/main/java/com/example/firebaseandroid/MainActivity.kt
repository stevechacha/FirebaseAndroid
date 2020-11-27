package com.example.firebaseandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception



const val REQUEST_CODE_SIGN_IN=0

class MainActivity : AppCompatActivity() {

    lateinit var mAuth:FirebaseAuth

    lateinit var mGoogleSignButton: Button

    lateinit var mRegiterEmail:EditText
    lateinit var mRegisterPassword:EditText
    lateinit var mConfirmRegisterPassword:EditText
    lateinit var mRegisterButton:Button
    lateinit var mLoggedIn:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth=FirebaseAuth.getInstance()

        mRegiterEmail=findViewById(R.id.register_email)
        mRegisterPassword=findViewById(R.id.register_password)
        mConfirmRegisterPassword=findViewById(R.id.register_confirm_password)
        mRegisterButton=findViewById(R.id.btn_register)
        mLoggedIn=findViewById(R.id.textEmailReg)

        mGoogleSignButton=findViewById(R.id.btnGoogleSignIn)

        mRegisterButton.setOnClickListener {
            registerUser()
        }

        mGoogleSignButton.setOnClickListener {
            val options= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.webclient_id))
                .requestEmail()
                .requestProfile()
                .build()
            val signInClient=GoogleSignIn.getClient(this,options)
            signInClient.signInIntent.also {
                startActivityForResult(it,REQUEST_CODE_SIGN_IN)
            }
        }

    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount){
        val credential=GoogleAuthProvider.getCredential(account.idToken,null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                mAuth.signInWithCredential(credential).isSuccessful
                withContext(Dispatchers.Main){
                    Toast.makeText(this@MainActivity,"succesful logged in",Toast.LENGTH_LONG).show()
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==REQUEST_CODE_SIGN_IN){
            val account=GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                googleAuthForFirebase(it)
            }
        }
    }


    private fun registerUser() {
        val email=mRegiterEmail.text.toString().trim()
        val password=mRegisterPassword.text.toString().trim()
        val confirm_pasword=mConfirmRegisterPassword.text.toString().trim()

        if (email.isNotEmpty() && password==confirm_pasword){

            //use courotines

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    mAuth.createUserWithEmailAndPassword(email,password).isSuccessful()
                    withContext(Dispatchers.Main){
                        checkLoggedInState()

                    }

                }catch(e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_LONG).show()
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