package com.example.usingfirebase

import android.app.AppComponentFactory
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class SignupActivity : AppCompatActivity() {
    val myData = FirebaseFirestore.getInstance().collection("User")
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signLogin.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        btnSignUp.setOnClickListener {
            val username = signUsername.text.toString()
            val email = signEmail.text.toString()
            val password = signPassword.text.toString()
            if (validation()) {
                val users = User(
                    username = username,
                    email = email,
                    password = password
                )
                signUp(users)
            }
        }
    }

    private fun signUp(users: User) = CoroutineScope(Dispatchers.IO).launch {
        try {
            myData.add(users).await()
            auth.createUserWithEmailAndPassword(
                signEmail.text.toString(),
                signPassword.text.toString()
            )
            withContext(Dispatchers.Main) {
                Toast.makeText(this@SignupActivity, "Registration Successful", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@SignupActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun validation(): Boolean {
        val username = signUsername.text.toString()
        val emailId = signEmail.text.toString()
        val password = signPassword.text.toString()

        return if (username.isEmpty()) {
            signUsername.error = "Please fill this field"
            false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
            signEmail.error = "Please fill this field"
            false
        } else if (password.length < 8) {
            signPassword.error = "Password must have more than 8 digits"
            false
        } else {
            true
        }
    }
}