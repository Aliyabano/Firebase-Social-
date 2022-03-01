package com.example.usingfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ResetPassword : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

            resetButton.setOnClickListener {
                resetEmail()
            }
    }

    private fun resetEmail() = CoroutineScope(Dispatchers.IO).launch{

        val email = resetEmailId.text.toString()
        try {
            if (email.isEmpty())
            {
                resetEmailId.error="Please enter your email id"
            }
            else {
                withContext(Dispatchers.Main) {
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(this@ResetPassword,"Check email reset your password", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                Toast.makeText(this@ResetPassword, "Fail to send reset password", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
        catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@ResetPassword, e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }
}