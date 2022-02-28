package com.example.usingfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSign.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
        }

        btnLogin.setOnClickListener {

            if (validation()) login()
        }


    }

    private fun login() = CoroutineScope(Dispatchers.IO).launch {
        try {
            auth.signInWithEmailAndPassword(loginEmail.text.toString(), loginPassword.text.toString())
            withContext(Dispatchers.Main) {
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                Toast.makeText(this@MainActivity, "Login Successful", Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validation(): Boolean {

        val email = loginEmail.text.toString()
        val password = loginPassword.text.toString()

        return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            loginEmail.error="Please fill out this field"
            false
        }
        else if (password.length < 8)
        {
            loginPassword.error="Password must be minimum 8 digits"
            false
        }
        else{
            true
        }

    }
}