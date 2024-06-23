package com.example.instaclone

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

//        progressBar = findViewById(R.id.progressBar)
//        progressText = findViewById(R.id.progressText)

        val signinLinkBtn = findViewById<Button>(R.id.signin_link_btn)
        val signupBtn = findViewById<Button>(R.id.signup_btn)

        signupBtn.setOnClickListener {
            createAccount()
        }

        signinLinkBtn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    private fun createAccount() {
        val fullName = findViewById<EditText>(R.id.fullname_signup).text.toString()
        val userName = findViewById<EditText>(R.id.username_signup).text.toString()
        val email = findViewById<EditText>(R.id.email_signup).text.toString()
        val password = findViewById<EditText>(R.id.password_signup).text.toString()

        when {
            TextUtils.isEmpty(fullName) -> Toast.makeText(this, "Full Name is Required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(userName) -> Toast.makeText(this, "Username is Required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(email) -> Toast.makeText(this, "Email is Required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(password) -> Toast.makeText(this, "Password is Required", Toast.LENGTH_SHORT).show()
            else -> {
//                progressBar.visibility = ProgressBar.VISIBLE
//                progressText.text = getString(R.string.please_wait_message)

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            saveUserInfo(fullName, userName, email)
                        } else {
                            val message = task.exception?.toString()
                            Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                            auth.signOut()
//                            progressBar.visibility = ProgressBar.GONE
                        }
                    }
            }
        }
    }

    private fun saveUserInfo(fullName: String, userName: String, email: String) {
        val currentUserID = auth.currentUser?.uid
        val usersRef = FirebaseDatabase.getInstance().reference.child("Users").child(currentUserID ?: "")

        val userMap = hashMapOf<String, Any>(
            "uid" to (currentUserID ?: ""),
            "fullname" to fullName,
            "username" to userName.toLowerCase(),
            "email" to email,
            "bio" to "Hey, I'm using InstaClone App by Bannerd",
            "image" to "https://firebasestorage.googleapis.com/v0/b/instaclone-app-f7f32.appspot.com/o/Default%20Images%2Fprofile.png?alt=media&token=279fc566-8074-4d2b-a271-96d54b3bb5fa"
        )

        usersRef.updateChildren(userMap).addOnCompleteListener { task ->
//            progressBar.visibility = ProgressBar.GONE
            if (task.isSuccessful) {
                Toast.makeText(this, "Account has been created successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                val message = task.exception?.message
                Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                auth.signOut()
            }
        }
    }
}
