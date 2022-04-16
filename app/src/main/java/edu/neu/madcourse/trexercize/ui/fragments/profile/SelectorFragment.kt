package edu.neu.madcourse.trexercize.ui.fragments.profile

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageButton
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

class SelectorFragment : Fragment(R.layout.fragment_selector) {
    private lateinit var galleryButton: Button
    private lateinit var cameraButton: Button
    private var path: Uri? = null
    private lateinit var storageRef: StorageReference
    private var db = Firebase.database.reference
    private lateinit var storage: FirebaseStorage
    private lateinit var doneButton: Button
    private lateinit var cameraPicture: ImageView
    private lateinit var backButton: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        galleryButton = view.findViewById(R.id.gallery)
        cameraButton = view.findViewById(R.id.camera)
        doneButton = view.findViewById(R.id.send_image_btn)
        backButton = view.findViewById(R.id.back_btn_edit)
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        cameraPicture = view.findViewById(R.id.camera_picture)
        val imageUploader = ImageUploaderFunctions()
        galleryButton.setOnClickListener {
            val action: NavDirections =
                SelectorFragmentDirections.actionSelectorFragmentToGalleryFragment()
            view.findNavController().navigate(action)
        }

        backButton.setOnClickListener {
            val action: NavDirections =
                SelectorFragmentDirections.actionSelectorFragmentToEditFragment()
            view.findNavController().navigate(action)
        }

        cameraButton.setOnClickListener {
            path = imageUploader.getImagePathFromCamera(this.context, request, this.requireActivity())
        }

        doneButton.setOnClickListener {
            path?.let { it1 -> imageUploader.uploadImageFromCameraToDb(it1, storageRef,  db, this.context) }
            val action: NavDirections =
                SelectorFragmentDirections.actionSelectorFragmentToEditFragment()
            view.findNavController().navigate(action)
        }
    }

    private val request =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                cameraPicture.setImageURI(path)
            }
        }
}