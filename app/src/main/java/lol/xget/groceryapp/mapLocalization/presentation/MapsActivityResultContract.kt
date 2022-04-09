package lol.xget.groceryapp.mapLocalization.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.maps.model.LatLng
import lol.xget.groceryapp.common.Constants

class MapsActivityResultContract : ActivityResultContract<LatLng, LatLng>() {



    override fun createIntent(context: Context, lgnLat: LatLng): Intent {
        return Intent(context, MapsActivity::class.java).apply {
            putExtra(Constants.LATITUDE, lgnLat.latitude)
            putExtra(Constants.LONGITUDE, lgnLat.longitude)
        }
    }

    override fun parseResult(
        resultCode: Int, intent: Intent?
    ): LatLng {
        return if (resultCode == Activity.RESULT_OK) {
            return LatLng(
                intent?.getDoubleExtra(Constants.LATITUDE, 0.0)!!,
                intent.getDoubleExtra(Constants.LONGITUDE, 0.0)
            )
                ?: LatLng(0.1, 0.1)
        } else {
            LatLng(0.2, 0.2)
        }
    }


}