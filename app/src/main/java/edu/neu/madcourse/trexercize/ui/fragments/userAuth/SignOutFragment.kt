package edu.neu.madcourse.trexercize.ui.fragments.userAuth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R

class SignOutFragment : Fragment(R.layout.fragment_sign_out) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(
            this.context, "Sign out was successfully.",
            Toast.LENGTH_SHORT
        ).show()
        Firebase.auth.signOut()
        val action : NavDirections =
            SignOutFragmentDirections.actionSignOutFragmentToSignInFragment()
        view.findNavController().navigate(action)
    }
}