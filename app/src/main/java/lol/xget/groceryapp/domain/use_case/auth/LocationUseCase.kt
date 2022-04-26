package lol.xget.groceryapp.domain.use_case.auth

import android.app.Activity
import android.app.Application
import androidx.compose.material.ExperimentalMaterialApi
import com.google.firebase.FirebaseException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lol.xget.groceryapp.MainActivity
import lol.xget.groceryapp.common.Resource
import lol.xget.groceryapp.auth.mapLocalization.presentation.SelectLocationState
import java.io.IOException

class LocationUseCase {


    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterialApi::class)
    operator fun invoke(activity: Activity, application: Application): Flow<Resource<SelectLocationState>> = callbackFlow {
        val locationData = lol.xget.groceryapp.auth.login.domain.LocationLiveData(application)
        fun getLocationData() = locationData
        try {
            try {
                trySend(Resource.Loading())

                getLocationData().observe(activity as MainActivity){

                }

            } catch (e: FirebaseException) {
                cancel()
                trySend(Resource.Error(e.localizedMessage!!))
            }
            try {
                awaitClose { channel.close() }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        } catch (e: FirebaseException) {
            trySend(Resource.Error(e.localizedMessage ?: "An unexpected error occured")).isFailure
            awaitClose { cancel() }
        } catch (e: IOException) {
            awaitClose { cancel() }
            trySend(Resource.Error("Couldn't reach server. Check your internet connection.")).isFailure
        }
    }
}