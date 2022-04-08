package edu.neu.madcourse.trexercize.ui.fragments.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import edu.neu.madcourse.trexercize.BuildConfig
import edu.neu.madcourse.trexercize.R
import java.io.File
import kotlin.random.Random


class SelectorFragment : Fragment(R.layout.fragment_selector) {
    private lateinit var file: File
    private lateinit var galleryButton: Button
    private lateinit var cameraButton: Button
    private lateinit var path: Uri
    private lateinit var storageRef: StorageReference
    private var userProfileUri: String = ""
    private var db = Firebase.database.reference
    private lateinit var storage: FirebaseStorage
    private lateinit var doneButton : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        galleryButton = view.findViewById(R.id.gallery)
        cameraButton = view.findViewById(R.id.camera)
        doneButton = view.findViewById(R.id.send_image_btn)
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        galleryButton.setOnClickListener {
            val action: NavDirections =
                SelectorFragmentDirections.actionSelectorFragmentToGalleryFragment()
            view.findNavController().navigate(action)
        }

        cameraButton.setOnClickListener {
            // this will give us a File for where the image taken is stored
            file = File.createTempFile(
                "image:${Timestamp.now().toDate().date}"
                , ".png", this.context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES))

            path = FileProvider.getUriForFile(this.requireActivity(), BuildConfig.APPLICATION_ID + ".provider", file)
            val intent = Intent()
            intent.action = MediaStore.ACTION_IMAGE_CAPTURE
            request.launch(path)
        }

        doneButton.setOnClickListener {
            val fileName = "${path.lastPathSegment}"
            val userId = Firebase.auth.currentUser?.uid
            Log.i("SUCCESS", "images/${userId}/${fileName}")

            if (userId != null) {
                storageRef.child("images").child(userId).child(fileName).putFile(path)
            }

            storageRef.child("images/${userId}/${fileName}").downloadUrl.addOnSuccessListener { picture ->
                userProfileUri = picture.toString()

                Firebase.auth.currentUser?.uid?.let { it1 ->
                    db.child("users").child(it1).child("profilePicture").setValue(userProfileUri)
                }
            }.addOnFailureListener {
                userProfileUri =
                    "https://firebasestorage.googleapis.com/v0/b/t-rexercize.appspot.com/o/frustratedino.png?alt=media&token=59cbbe30-0715-46e8-910c-9958b14c2a30"
            }
            val action: NavDirections =
                SelectorFragmentDirections.actionSelectorFragmentToEditFragment()
            view.findNavController().navigate(action)
        }
    }

    private val request =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            success ->

            if(success) {
                Log.i("Picture", "$path")
            }
            else {
                Log.i("Oh no", "$path")
            }
        }
}