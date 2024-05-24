package com.example.instaclone.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.instaclone.MessageActivity
import com.example.instaclone.R
import com.example.instaclone.ViewCommentActivity
import com.example.instaclone.adapter.CommentAdapter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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

        val imgPost: ImageView = view.findViewById(R.id.imgPost)
        val heartLike: ImageView = view.findViewById(R.id.heart_like)
        val comment: ImageView = view.findViewById(R.id.comment)
        val share: ImageView = view.findViewById(R.id.share)
        val bookmark: ImageView = view.findViewById(R.id.bookmark)

        // Set the image resource from drawable
        imgPost.setImageResource(R.drawable.cat2)
        heartLike.setImageResource(R.drawable.heart)
        comment.setImageResource(R.drawable.comment)
        share.setImageResource(R.drawable.send_icon)
        bookmark.setImageResource(R.drawable.action_bookmark_border)

        comment.setOnClickListener {
            // Create an Intent to start ViewCommentActivity
            val intent = Intent(activity, ViewCommentActivity::class.java)
            startActivity(intent)
        }

        return view
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
