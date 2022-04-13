package edu.neu.madcourse.trexercize.ui.fragments.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.helper.ImageUploaderFunctions

class GalleryFragment : Fragment(R.layout.fragment_gallery) {
    private lateinit var path: Uri
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var galleryButton: Button
    private lateinit var cancelButton: Button
    private lateinit var preview: ImageView
    private lateinit var done: Button
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
            val imageUploader = ImageUploaderFunctions()
            imageUploader.upLoadImageFromGalleryToDb(path, storageRef, db, this.context)
            val action: NavDirections =
                GalleryFragmentDirections.actionGalleryFragmentToEditFragment()
            view.findNavController().navigate(action)
        }
    }

    private val request =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val uri = it.data?.data
                preview.setImageURI(uri)
                if (uri != null) {
                    path = uri
                }
            }
        }
}