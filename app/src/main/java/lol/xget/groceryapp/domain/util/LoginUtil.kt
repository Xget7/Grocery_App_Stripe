package lol.xget.groceryapp.domain.util

import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow

class LoginUtil {
    companion object UserType {
        fun getUserType(currentUser: FirebaseUser?, navController: NavController)  {
                if (currentUser?.uid != null) {
                    val db = FirebaseDatabase.getInstance()
                    db.getReference("users").child(currentUser.uid).get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val document = task.result
                                if (document.exists()) {
                                    navController.popBackStack()
                                    navController.navigate(Destinations.UserMainDestination.route)
                                } else {
                                    navController.popBackStack()
                                    navController.navigate( Destinations.SellerMainDestinations.route)
                                }
                            }

                        }
                } else {
                    navController.popBackStack()
                    navController.navigate(Destinations.LoginDestinations.route)
                }


        }
    }
}