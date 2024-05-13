package com.example.talk_in

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException




class LogIn : AppCompatActivity() {
  private lateinit var mAuth: FirebaseAuth
  private lateinit var edtEmail : com.google.android.material.textfield.TextInputEditText
  private lateinit var edtPassword : com.google.android.material.textfield.TextInputEditText
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_log_in)
    mAuth = FirebaseAuth.getInstance()
    supportActionBar?.hide()

    edtEmail = findViewById(R.id.edt_email)
    edtPassword = findViewById(R.id.edt_password)
    val btnLogIn = findViewById<Button>(R.id.btnLogin)
    val backbtn = findViewById<ImageView>(R.id.btnBack)

    btnLogIn.setOnClickListener {
      val email = edtEmail.text.toString()
      val password = edtPassword.text.toString()
      if (email.isBlank() || password.isBlank()) {
        Toast.makeText(this, "Please enter details.", Toast.LENGTH_SHORT).show()
      } else{
        login(email, password)
      }
    }
    backbtn.setOnClickListener{
      val intent = Intent(this@LogIn,EntryActivity::class.java)
      startActivity(intent)
      finish()
    }


  }

    private fun login(email: String, pwd: String) {
      // CHecking for empty texts
      if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)) {
        mAuth.signInWithEmailAndPassword(email, pwd)
          .addOnCompleteListener { task ->
            if (task.isSuccessful) {
              if (isEmailVerified()) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LogIn, MainActivity::class.java)
                startActivity(intent)
                finish()
              }
            }
          }.addOnFailureListener { e ->
            if (e is FirebaseAuthInvalidCredentialsException) {
              edtPassword.error = "Invalid Password"
              edtPassword.requestFocus()
            }
            if (e is FirebaseAuthInvalidUserException) {
              edtEmail.error = "Email Not Registered"
              edtEmail.requestFocus()
            } else {
              Toast.makeText(
                this,
                "Something went Wrong",
                Toast.LENGTH_SHORT
              ).show()
            }
          }
      } else {
        Toast.makeText(this, "Please Enter Email & Password", Toast.LENGTH_SHORT).show()
      }
    }
  private fun isEmailVerified(): Boolean {
    if (mAuth.currentUser != null) {
      val isEmailVerified: Boolean = mAuth.currentUser!!.isEmailVerified
      if (isEmailVerified) {
        return true
      } else {
        Toast.makeText(this, "please verify your email address first", Toast.LENGTH_SHORT).show()
      }
    }
    return false
  }

}