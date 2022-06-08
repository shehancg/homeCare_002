package com.homecare.homecare

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeCarelg : AppCompatActivity() {

    //defining instance for firebase auth
    private lateinit var auth: FirebaseAuth

    //defining widgets
    private lateinit var email: TextInputEditText
    private lateinit var pwlg: TextInputEditText
    private lateinit var reg: Button
    private lateinit var login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_carelg)

        // Initialize Firebase Auth
        auth = Firebase.auth

        //assigning widgets
        reg = findViewById(R.id.btn_reg)
        login = findViewById(R.id.btn_lglogin)
        pwlg = findViewById(R.id.EditTextlgpassword)
        email = findViewById(R.id.EditTextlgemail)


        //when Login button click
        login.setOnClickListener{
            login()
        }

        //when Register button click
        reg.setOnClickListener{
            val intent = Intent(this,HomeCarerg::class.java)
            startActivity(intent)
        }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this,HomeCaredashboardct::class.java)
            startActivity(intent)
        }
    }
    private fun login(){
        var userEmail = email.text.toString()
        var userPassword = pwlg.text.toString()

        if(userEmail.isEmpty()){
            email.error = "Please enter your email address"
        }else if(userPassword.isEmpty()){
            pwlg.error = "Please enter your password"
        }else{
            auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        val intent = Intent(this,HomeCaredashboardct::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }


}