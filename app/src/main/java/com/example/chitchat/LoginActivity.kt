package com.example.chitchat

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    lateinit var etLoginEmail: EditText
    lateinit var etLoginPassword: EditText
    lateinit var btnLogin: Button
    lateinit var goToSignUp: TextView

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etLoginEmail = findViewById(R.id.etLoginEmail)
        etLoginPassword = findViewById(R.id.etLoginPassword)
        btnLogin = findViewById(R.id.btnLogin)
        goToSignUp = findViewById(R.id.goToSignUp)

        //Hiding Action Bar
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance() // Creating Firebase Instance

        //Login in App
        btnLogin.setOnClickListener {
            login()
        }

        //Go to SignUp Page
        goToSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun login() {
        when {
            // If no input given by user then display Toast
            TextUtils.isEmpty(etLoginEmail.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this,
                    "Please enter email.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            TextUtils.isEmpty(etLoginPassword.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this,
                    "Please enter password.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                //If proper input given by user then proceed :-
                val email = etLoginEmail.text.toString().trim { it <= ' ' }
                val password = etLoginPassword.text.toString().trim { it <= ' ' }

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        val firebaseUser: FirebaseUser = it.result!!.user!!
                        Toast.makeText(this, "Login Successful !", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("user-id", firebaseUser.uid)
                        intent.putExtra("email-id", email)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, " Login Failed !", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}