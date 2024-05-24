package com.example.instaclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.instaclone.R

data class Comment(
    val username: String,
    val text: String
)

class CommentAdapter(context: Context, resource: Int, objects: List<Comment>) :
    ArrayAdapter<Comment>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.custom_comment, parent, false)
        }

        // Get the comment object at the current position
        val comment = getItem(position)

        // Connect the data to the view in the item
        val usernameTextView = view?.findViewById<TextView>(R.id.comment_username1)
        val commentTextView = view?.findViewById<TextView>(R.id.comment_text1)

        usernameTextView?.text = comment?.username
        commentTextView?.text = comment?.text

        return view!!
    }
}
