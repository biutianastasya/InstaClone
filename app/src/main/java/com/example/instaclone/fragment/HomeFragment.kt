package com.example.instaclone.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclone.MessageActivity
import com.example.instaclone.Model.Post
import com.example.instaclone.Model.User
import com.example.instaclone.R
import com.example.instaclone.ViewCommentActivity
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.example.instaclone.viewHolders.PostsViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var adapter : FirebaseRecyclerAdapter<Post, PostsViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        val options = FirebaseRecyclerOptions.Builder<Post>()
            .setQuery(FirebaseDatabase.getInstance().getReference("Posts"), Post::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<Post, PostsViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.post_item, parent, false)
                return PostsViewHolder(view)
            }

            override fun onBindViewHolder(holder: PostsViewHolder, position: Int, model: Post) {
                val userRef = FirebaseDatabase.getInstance().getReference("Users").child(model.publisher!!)
                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user = dataSnapshot.getValue(User::class.java)
                        val fullname = user?.fullname
                        println(fullname)
                        // Now you can use fullname
                        // For example, you can set it to a TextView in your PostsViewHolder
                        holder.view.findViewById<TextView>(R.id.tvName).text = fullname

                        val imgUri = Uri.parse(model.postimage)
                        val imgPost: ImageView = holder.view.findViewById(R.id.imgPost)
                        Glide.with(holder.view.context)
                            .load(imgUri)
                            .into(imgPost)

                        val caption: TextView = holder.view.findViewById(R.id.tvCaption)
                        caption.text = model.description

                        val heartLike: ImageView = holder.view.findViewById(R.id.heart_like)
                        val comment: ImageView = holder.view.findViewById(R.id.comment)
                        val share: ImageView = holder.view.findViewById(R.id.share)
                        val bookmark: ImageView = holder.view.findViewById(R.id.bookmark)

                        heartLike.setImageResource(R.drawable.heart)
                        comment.setImageResource(R.drawable.comment)
                        share.setImageResource(R.drawable.send_icon)
                        bookmark.setImageResource(R.drawable.action_bookmark_border)

                        comment.setOnClickListener {
                            // Create an Intent to start ViewCommentActivity
                            val intent = Intent(activity, ViewCommentActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle possible errors.
                    }
                })
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val sendIcon: ImageView = view.findViewById(R.id.direct_message)
        sendIcon.setOnClickListener {
            val intent = Intent(activity, MessageActivity::class.java)
            startActivity(intent)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_firedb)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        return view
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}