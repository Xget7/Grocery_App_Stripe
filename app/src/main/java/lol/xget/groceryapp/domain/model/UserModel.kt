package lol.xget.groceryapp.domain.model

import android.net.Uri
import android.os.Parcelable
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserModel(
    var uid : String? = null,
    var profilePhoto: String? = null,
    val country: String? = null,
    val fullName: String? = null,
    val phone: String? = null,
    var gmail : String? = null,
    val latitude : Float? = null,
    val longitude :Float? = null,
    val state: String? = null,
    val city: String? = null,
    val address: String? = null,
    val accountType: String? = null,
    val email: String? = null,
    val online :Boolean? = null,
    val shopOpen : Boolean? = null,
    val deliveryFee : String? = null,
    val shopName: String? = null,
)


