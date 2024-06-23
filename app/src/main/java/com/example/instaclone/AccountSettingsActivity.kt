package com.example.instaclone

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.widget.ImageView
import com.example.instaclone.Model.User
import com.example.instaclone.R.id.main
import com.example.instaclone.fragment.ProfileFragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AccountSettingsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null
    private lateinit var usernameEditText: EditText
    private lateinit var fullnameEditText: EditText
    private lateinit var userBio: EditText
    private var checker = ""
    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storageProfilePicRef: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser
        storageProfilePicRef = FirebaseStorage.getInstance().reference.child("Profile Pictures")

        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser
        val userRef =
            FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)

        fullnameEditText = findViewById(R.id.full_name_profile_frag)
        usernameEditText = findViewById(R.id.username_profile_frag)
        userBio = findViewById(R.id.bio_profile_frag)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    usernameEditText.setText(user?.username)
                    fullnameEditText.setText(user?.fullname)
                    userBio.setText(user?.bio)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })


        val saveBtn = findViewById<ImageView>(R.id.save_info_profile_btn)
        saveBtn.setOnClickListener {
            val username = usernameEditText.text.toString()
            val firebaseUser = auth.currentUser

            val userRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser?.uid.toString())

            val userMap = HashMap<String, Any>()
            userMap["username"] = username
            userMap["fullname"] = fullnameEditText.text.toString()
            userMap["bio"] = userBio.text.toString()

            userRef.updateChildren(userMap).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Information has been updated successfully!", Toast.LENGTH_LONG).show()
                    // Kembali ke ProfileFragment setelah data berhasil disimpan
                    finish()
                } else {
                    Toast.makeText(this, "Failed to update information. Please try again.", Toast.LENGTH_LONG).show()
                }
            }
        }

        val closeBtn = findViewById<ImageView>(R.id.close_profile_btn)
        closeBtn.setOnClickListener {
            // Kode untuk kembali ke ProfileFragment
            // Anda mungkin perlu mengganti FragmentManager dengan instance yang benar
            supportFragmentManager.beginTransaction().replace(R.id.container, ProfileFragment()).commit()
        }

        val logoutButton = findViewById<Button>(R.id.logout_btn)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@AccountSettingsActivity, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

    }
}