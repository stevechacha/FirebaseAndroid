package com.example.firebaseandroid

import android.app.Person
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class UpdateProfileActivity : AppCompatActivity() {
    lateinit var mFirstName: EditText
    lateinit var mLastName: EditText
    lateinit var mGender:EditText
    lateinit var mPhoneNo:EditText
    lateinit var mCountry:EditText
    lateinit var mUpdateProfile:Button

    private val personCollectionR=Firebase.firestore.collection("persons")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        mFirstName=findViewById(R.id.update_FirstName)
        mLastName=findViewById(R.id.update_lastName)
        mGender=findViewById(R.id.update_gender)
        mPhoneNo=findViewById(R.id.updatePhoneNo)
        mCountry=findViewById(R.id.country_name)
        mUpdateProfile=findViewById(R.id.updatebtn)


        mUpdateProfile.setOnClickListener {
            val firstName=mFirstName.text.toString()
            val lastName=mLastName.text.toString()
            val gender=mGender.text.toString()
            val phoneNo=mPhoneNo.text.toString().toInt()
            val country=mCountry.text.toString()
            val person=Person(firstName,lastName,gender,country,phoneNo)
//            savePerson(person)

        }


    }

    private fun retrivePersonData()= CoroutineScope(Dispatchers.IO).launch {
        try {

            val querrySnapshot=personCollectionR.get().await()

            withContext(Dispatchers.Main){
                Toast.makeText(this@UpdateProfileActivity,"succesfully", Toast.LENGTH_LONG).show()
            }

        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@UpdateProfileActivity,e.message, Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun savePerson(person: Person)= CoroutineScope(Dispatchers.IO).launch {
        try {
            personCollectionR.add(person).await()
            withContext(Dispatchers.Main){
                Toast.makeText(this@UpdateProfileActivity,"succesfully", Toast.LENGTH_LONG).show()
            }

        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@UpdateProfileActivity,e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}