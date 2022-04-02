package edu.neu.madcourse.trexercize.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.*

class HomeActivity : AppCompatActivity() {
    private var db = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.homeFragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
        bottomNavigation.background = null
        bottomNavigation?.setupWithNavController(navController)

        val newUser = hashMapOf(
            "core" to hashMapOf(
                "Reverse Crunch" to hashMapOf(
                    "muscle groups" to "Core",
                    "description" to "1. Keep your toes pointed up and heels pulled tight to your hamstrings. " +
                            "2. Exhale your ribs downward toward your pelvis and try to keep them in this position. " +
                            "3. Bring your knees to your shoulders by pulling your pelvis forward. " +
                            "4. Pull slowly and under control, and return to neutral starting point before each rep. Keep your head down. " +
                            "5. Don’t use momentum or jerk your head up.",
                    "video" to "https://vimeo.com/122683376",
                    "equipment" to arrayListOf(
                        "None"
                    )
                ),
                "Plank" to hashMapOf(
                    "muscle groups" to "core",
                    "description" to "1. Lock your ribs down with your abs. 2. Tuck your tailbone between your knees. 3. Do not let your lower back sag.",
                    "video" to "https://vimeo.com/122355869",
                    "equipment" to arrayListOf(
                        "None"
                    )
                ),
                "Stability Ball Rollout" to hashMapOf(
                    "muscle groups" to "core",
                    "description" to "1. Position your spine by locking your ribs down in an exhaled position with your abs. 2. Keep your tailbone tucked under and your ribs pulled down throughout the movement. 3. Roll your arms out in front of you as far as you can without losing tension in your abs. 4. Stop before your back arches and return.",
                    "video" to "https://vimeo.com/123731149",
                    "equipment" to arrayListOf(
                        "Stability ball"
                    )
                ),
                "Ab Wheel Rollout" to hashMapOf(
                    "muscle groups" to "core",
                    "description" to "1. Lock your ribs down with your abs. 2. Tuck your tailbone between your knees. 3. Do not let your lower back sag.",
                    "video" to "https://vimeo.com/121807110",
                    "equipment" to arrayListOf(
                        "Ab wheel"
                    )
                ),
                "Half-Kneeling Cable Lift" to hashMapOf(
                    "muscle groups" to "core",
                    "description" to "1. Start in a tall position with your chest high and a 90° angle at both knees. 2. Squeeze the glute of your down knee and tuck your tailbone under. Your pelvis should be squared and level. 3. Brace your abs to pull your lower ribs down. 4. Keep your knee out over your little toe as you chop the cable down with your arms.",
                    "video" to "https://vimeo.com/121853809",
                    "equipment" to arrayListOf(
                        "Cable/Pulley Machine",
                        "Band and door"
                    )
                )
            ),
            "arms" to hashMapOf(
                "Alternating Dumbbell Curl" to hashMapOf(
                    "muscle groups" to "biceps",
                    "description" to "1. Curl the weight without letting your shoulders pop forward. " +
                            "2. Don't arch your lower back.",
                    "video" to "https://vimeo.com/121808328",
                    "equipment" to arrayListOf(
                        "Dumbbells"
                    )
                ),
                "Hammer Curl" to hashMapOf(
                    "muscle groups" to "biceps",
                    "description" to "1. Curl the weight without letting your shoulders pop forward." +
                            "2. Don't arch your lower back.",
                    "video" to "https://vimeo.com/121851466",
                    "equipment" to arrayListOf(
                        "Dumbbells"
                    )
                ),
                "Barbell Curl" to hashMapOf(
                    "muscle groups" to "biceps",
                    "description" to "1. Curl the weight without letting your shoulders pop forward." +
                            " 2. Don't arch your lower back.",
                    "video" to "https://vimeo.com/121830740",
                    "equipment" to arrayListOf(
                        "Barbells"
                    )
                ),
                "Lying Dumbbell Triceps Extension" to hashMapOf(
                    "muscle groups" to "triceps",
                    "description" to "1. Brace your abs and keep your ribs down. " +
                            "2. Keep your upper arms still while you lower the weight towards your head. " +
                            "3. Roll the weights to a bench press position and then press back up while keeping your elbow close to your body.",
                    "video" to "https://vimeo.com/122318368",
                    "equipment" to arrayListOf(
                        "Dumbbells"
                    )
                ),
                "Triceps Press Down" to hashMapOf(
                    "muscle groups" to "triceps",
                    "description" to "1. Keep your shoulder blades squeezed backward and down. 2. Press down without letting your shoulders pop forward during the press. " +
                            "3. Keep your abs tight and your ribs pulled down so that your ribs don't flare out as you return upward.",
                    "video" to "https://vimeo.com/124064398",
                    "equipment" to arrayListOf(
                        "Cable/Pulley Machine",
                        "Band and door"
                    )
                )
            )
        )
        db.child("exercises").setValue(newUser)
    }
}