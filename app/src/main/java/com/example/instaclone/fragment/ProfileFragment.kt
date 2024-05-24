package com.example.instaclone.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.instaclone.AccountSettingsActivity
import com.example.instaclone.Model.User
import com.example.instaclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private var firebaseUser: FirebaseUser? = null
    private lateinit var followersCount: TextView
    private lateinit var followingCount: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        followersCount = view.findViewById(R.id.total_followers)
        followingCount = view.findViewById(R.id.total_following)

        val editAccountSettingsBtn = view.findViewById<Button>(R.id.edit_account_settings_btn)

        getFollowers()
        getFollowing()

        editAccountSettingsBtn.setOnClickListener {
            startActivity(Intent(context, AccountSettingsActivity::class.java))

        }
        val usernameTextView = view.findViewById<TextView>(R.id.profile_fragment_username)
        val fullNameTextView = view.findViewById<TextView>(R.id.full_name_profile_frag)
        // Asumsikan Anda memiliki TextView untuk bio dengan id bio_profile_frag
        val bioTextView = view.findViewById<TextView>(R.id.bio_profile_frag)

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser?.uid.toString())

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    usernameTextView.text = user?.username
                    fullNameTextView.text = user?.fullname
                    bioTextView.text = user?.bio
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

        return view
    }

    private fun getFollowers() {
        val followersRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(firebaseUser?.uid.toString())
            .child("Followers")

        followersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    followersCount.text = dataSnapshot.childrenCount.toString()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Not needed for now
            }
        })
    }

    private fun getFollowing() {
        val followingRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(firebaseUser?.uid.toString())
            .child("Following")

        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    followingCount.text = dataSnapshot.childrenCount.toString()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Not needed for now
            }
        })
    }
}