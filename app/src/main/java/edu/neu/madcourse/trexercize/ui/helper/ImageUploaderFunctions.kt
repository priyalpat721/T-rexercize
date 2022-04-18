package edu.neu.madcourse.trexercize.ui.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import edu.neu.madcourse.trexercize.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImageUploaderFunctions {
    var profile: Boolean = false

    constructor()
    constructor(profile: Boolean) {
        this.profile = profile
    }

    // gets the path of the image taken from the camera
    fun getImagePathFromCamera(
        context: Context?, request: ActivityResultLauncher<Uri>, requireActivity: FragmentActivity
    ): Uri        // this will give us a File for where the image taken is stored
    {
        val file = File.createTempFile(
            "image:${Timestamp.now().toDate()}",
            ".png",
            context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        val path = FileProvider.getUriForFile(
            requireActivity,
            BuildConfig.APPLICATION_ID + ".provider",
            file
        )
        val intent = Intent()
        intent.action = MediaStore.ACTION_IMAGE_CAPTURE
        request.launch(path)

        return path
    }

    // uploads the image to the database
    fun uploadImageFromCameraToDb(
        path: Uri,
        storageRef: StorageReference,
        db: DatabaseReference,
        context: Context?
    ) {
        lateinit var userCalendar: String
        val fileName = path.lastPathSegment.toString()
        val userId = Firebase.auth.currentUser?.uid
        val date = SimpleDateFormat("M-d-yyyy", Locale.getDefault()).format(
            Date()
        )

        Log.i("FileName", fileName)
        if (userId != null) {
            // task snapshot returns a lot of information, including the path
            storageRef.child("images").child(userId).child(fileName).putFile(path)
                .addOnSuccessListener {
                    // once we have the path, we just download the url from cloud
                    storageRef.child("${it.metadata?.path}").downloadUrl.addOnSuccessListener { picture ->
                        val pictureUri = picture.toString()
                        // updates the user's profile picture
                        if (profile) {
                            Firebase.auth.currentUser?.uid?.let { it1 ->
                                db.child("users").child(it1).child("profilePicture")
                                    .setValue(pictureUri)
                            }
                        } else {
                            Firebase.auth.currentUser?.uid?.let { it1 ->
                                db.child("users").child(
                                    it1
                                ).child("calendar").get().addOnSuccessListener {
                                    userCalendar = it.value.toString()
                                    println("Calendar " + userCalendar)
                                    // populate the page with data from database
                                    db.child("calendars").child(userCalendar).child(date)
                                        .child("dailySnap").setValue(pictureUri)
                                }
                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Sorry, the picture could not be uploaded",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    fun upLoadImageFromGalleryToDb(
        path: Uri,
        storageRef: StorageReference,
        db: DatabaseReference,
        context: Context?
    ) {
        val fileName = "${path.lastPathSegment}.png"
        val userId = Firebase.auth.currentUser?.uid

        if (userId != null) {
            storageRef.child("images").child(userId).child(fileName).putFile(path)
        }

        Log.i("Gallery", "images/${userId}/${fileName}")
        storageRef.child("images/${userId}/${fileName}").downloadUrl.addOnSuccessListener { picture ->
            val userProfileUri = picture.toString()
            Log.i("SUCCESS", userProfileUri)
            Firebase.auth.currentUser?.uid?.let { it1 ->
                db.child("users").child(it1).child("profilePicture").setValue(userProfileUri)
            }
        }.addOnFailureListener {
            Toast.makeText(
                context,
                "Sorry, the picture could not be uploaded",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}