package lol.xget.groceryapp.domain.util

import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class LoginUtil {

    companion object UserType {
        fun getUserType(currentUser: FirebaseUser?, navController: NavController) {
            val db = FirebaseDatabase.getInstance()
            if (currentUser?.uid != null) {
                db.getReference("users").child(currentUser.uid).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document.exists()) {
                                Log.e("RESULTSCREEN", "Exists in user")
                                navController.navigate(Destinations.UserHomeDestinations.route)
                            } else {
                                Log.e("RESULTSCREEN", "Dont in user , its a seller")
                                navController.navigate(Destinations.SellerHomeDestinations.route)
                            }
                        }
                    }
            } else {
                navController.navigate(Destinations.LoginDestinations.route)
            }
        }
    }
}