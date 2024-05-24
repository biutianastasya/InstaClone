package com.example.instaclone

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.instaclone.fragment.HomeFragment
import com.example.instaclone.fragment.NotificationFragment
import com.example.instaclone.fragment.ProfileFragment
import com.example.instaclone.fragment.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)


        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    moveToFragment(HomeFragment())
                    true
                }
                R.id.nav_search -> {
                    moveToFragment(SearchFragment())
                    true
                }
                R.id.nav_add_post -> {
                    item.isChecked = false
                    startActivity(Intent(this@MainActivity, AddPostActivity::class.java))
                    true
                }
                R.id.nav_notifications -> {
                    moveToFragment(NotificationFragment())
                    true
                }
                R.id.nav_profile -> {
                    moveToFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        // Pindahkan ke fragmen Beranda saat aplikasi pertama kali dijalankan
        if (savedInstanceState == null) {
            moveToFragment(HomeFragment())
        }
    }

    private fun moveToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
