package edu.neu.madcourse.trexercize.ui.fragments.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import edu.neu.madcourse.trexercize.R
import java.util.*

class GalleryFragment : Fragment(R.layout.fragment_gallery) {
    private lateinit var path: Uri
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var galleryButton: Button
    private lateinit var cancelButton: Button
    private lateinit var preview: ImageView
    private lateinit var done: Button
    private lateinit var userProfileUri: String
    private var db = Firebase.database.reference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        galleryButton = view.findViewById(R.id.select_pic_btn)
        cancelButton = view.findViewById(R.id.close_gallery_btn)
        preview = view.findViewById(R.id.gallery_picture)
        done = view.findViewById(R.id.send_image)

        galleryButton.setOnClickListener {
            val intent = Intent()
            // this defines what the intent is sending
            intent.type = "image/*"
            // this allows user to get a particular kind of data and return it
            intent.action = Intent.ACTION_GET_CONTENT
            // create chooser lets user pick how they want to send data
            request.launch(Intent.createChooser(intent, "Image"))
        }
        cancelButton.setOnClickListener {
            val action: NavDirections =
                GalleryFragmentDirections.actionGalleryFragmentToSelectorFragment()
            view.findNavController().navigate(action)
        }

        done.setOnClickListener {

            val fileName = "${path.lastPathSegment}.png"
            val userId = Firebase.auth.currentUser?.uid

            if (userId != null) {
                storageRef.child("images").child(userId).child(fileName).putFile(path)
            }

            Log.i("Gallery", "images/${userId}/${fileName}")
            storageRef.child("images/${userId}/${fileName}").downloadUrl.addOnSuccessListener { picture ->
                userProfileUri = picture.toString()
                Log.i("SUCCESS", userProfileUri)
                Firebase.auth.currentUser?.uid?.let { it1 ->
                    db.child("users").child(it1).child("profilePicture").setValue(userProfileUri)
                }
            }.addOnFailureListener {
                Toast.makeText(this.context, "Sorry, the picture could not be uploaded", Toast.LENGTH_LONG).show()
                userProfileUri =
                    "https://firebasestorage.googleapis.com/v0/b/t-rexercize.appspot.com/o/frustratedino.png?alt=media&token=59cbbe30-0715-46e8-910c-9958b14c2a30"
            }


            val action: NavDirections =
                GalleryFragmentDirections.actionGalleryFragmentToSelectorFragment()
            view.findNavController().navigate(action)
        }
    }

    private val request =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val uri = it.data?.data
                Log.i("URI", uri.toString())
                preview.setImageURI(uri)
                if (uri != null) {
                    path = uri
                }
            }
        }
}