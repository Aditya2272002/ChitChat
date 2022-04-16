package com.example.chitchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class SignUpActivity : AppCompatActivity() {

    lateinit var auth:FirebaseAuth
    lateinit var etRegisterName:EditText
    lateinit var etRegisterEmail:EditText
    lateinit var etRegisterPassword:EditText
    lateinit var btnRegister:Button
    lateinit var goToLogin:TextView
    private lateinit var databaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etRegisterName = findViewById(R.id.etRegisterName)
        etRegisterEmail = findViewById(R.id.etRegisterEmail)
        etRegisterPassword = findViewById(R.id.etRegisterPassword)
        btnRegister = findViewById(R.id.btnRegister)
        goToLogin = findViewById(R.id.goToLogin)

        //Hiding Action Bar
        supportActionBar?.hide()

        //Initializing Firebase Auth object
        auth = FirebaseAuth.getInstance()

        // Create user i.e SignUp
        btnRegister.setOnClickListener {
            signIn()
        }

        //Go to Login Screen
        goToLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signIn() {
        when {
            // If no input given by user then display Toast
            TextUtils.isEmpty(etRegisterName.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this,
                    "Invalid Name!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            TextUtils.isEmpty(etRegisterEmail.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this,
                    "Invalid Email!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            TextUtils.isEmpty(etRegisterPassword.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this,
                    "Invalid Password!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else->{
                val email: String = etRegisterEmail.text.toString().trim { it <= ' ' }
                val password: String = etRegisterPassword.text.toString().trim { it <= ' ' }
                val name:String = etRegisterName.text.toString().trim{ it <= ' '}

                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this) {
                        if(it.isSuccessful){
                            val firebaseUser: FirebaseUser = it.result!!.user!!
                            Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show()

                            //adding user to Database while creating
                            addUserToDatabase(name,email,auth.currentUser?.uid!!)

                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user-id", firebaseUser.uid)
                            intent.putExtra("email-id", email)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this, "Failed !", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        databaseRef = FirebaseDatabase.getInstance().getReference()
        databaseRef.child("user").child(uid).setValue(User(name,email,uid))
    }
}