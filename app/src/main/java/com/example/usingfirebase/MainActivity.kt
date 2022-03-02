package com.example.usingfirebase

import android.app.Instrumentation
import android.app.backup.BackupManager
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FacebookAuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainActivity : AppCompatActivity() {

   lateinit var callBackManager : CallbackManager
    private var EMAIL = "email"

    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JavaHashKey.printHashKey(this)

        forgotPassword.setOnClickListener {
            startActivity(Intent(this,ResetPassword::class.java))
        }

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
        login_button.setOnClickListener {
            login_button.setReadPermissions(listOf(EMAIL))
            callBackManager = CallbackManager.Factory.create()
            login_button.setReadPermissions(listOf("email"))

            login_button.registerCallback(callBackManager, object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.d(TAG, "onCancel: Cancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d(TAG, "onError: Error")
                }

                override fun onSuccess(result: LoginResult) {
                    val request = GraphRequest.newMeRequest(
                        result?.accessToken
                    ) { obj, response ->
                        try {
                            if (obj!!.has("id"))
                                Log.d(TAG, "onSuccess: fbObject $obj")
                        } catch (e: Exception) {
                        }
                    }
                }

            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callBackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}

