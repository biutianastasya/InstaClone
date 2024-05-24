import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.Model.User
import com.example.instaclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class UserAdapter(
    private var mContext: Context,
    private var mUser: List<User>,
    private var isFragment: Boolean = false
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val user = mUser[position]

        holder.userNameTextView.text = user.username
        holder.userFullnameTextView.text = user.fullname
        Picasso.get().load(user.image).placeholder(R.drawable.profile).into(holder.userProfileImageView)

        checkFollowingStatus(user.uid, holder.followButton)

        holder.followButton.setOnClickListener {
            if (holder.followButton.text.toString() == "Follow") {
                FirebaseDatabase.getInstance().reference
                    .child("Follow").child(firebaseUser?.uid.toString())
                    .child("Following").child(user.uid).setValue(true)
                FirebaseDatabase.getInstance().reference
                    .child("Follow").child(user.uid)
                    .child("Followers").child(firebaseUser?.uid.toString()).setValue(true)
            } else {
                FirebaseDatabase.getInstance().reference
                    .child("Follow").child(firebaseUser?.uid.toString())
                    .child("Following").child(user.uid).removeValue()
                FirebaseDatabase.getInstance().reference
                    .child("Follow").child(user.uid)
                    .child("Followers").child(firebaseUser?.uid.toString()).removeValue()
            }
        }
    }

    private fun checkFollowingStatus(uid: String, followButton: Button) {
        val followingRef = firebaseUser?.uid.let {
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it.toString())
                .child("Following")
        }

        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(uid).exists()) {
                    followButton.text = "Following"
                } else {
                    followButton.text = "Follow"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Not needed for now
            }
        })
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.findViewById(R.id.user_name_search)
        val userFullnameTextView: TextView = itemView.findViewById(R.id.user_full_name_search)
        val userProfileImageView: com.google.android.material.imageview.ShapeableImageView = itemView.findViewById(R.id.user_profile_image_search)
        val followButton: Button = itemView.findViewById(R.id.follow_btn_search)



        init {
            // Atur listener klik atau inisialisasi lainnya jika diperlukan
        }
    }
}


