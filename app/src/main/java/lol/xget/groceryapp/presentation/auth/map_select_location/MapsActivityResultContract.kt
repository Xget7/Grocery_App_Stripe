package lol.xget.groceryapp.presentation.auth.map_select_location

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.maps.model.LatLng
import lol.xget.groceryapp.common.Constants.LATITUDE
import lol.xget.groceryapp.common.Constants.LONGITUDE

class MapsActivityResultContract : ActivityResultContract<LatLng, LatLng>() {



    override fun createIntent(context: Context, lgnLat: LatLng): Intent {
        return Intent(context, MapsActivity::class.java).apply {
            putExtra(LATITUDE, lgnLat.latitude)
            putExtra(LONGITUDE, lgnLat.longitude)
        }
    }

    override fun parseResult(
        resultCode: Int, intent: Intent?
    ): LatLng {
        return if (resultCode == Activity.RESULT_OK) {
            return LatLng(intent?.getDoubleExtra(LATITUDE, 0.0)!!, intent.getDoubleExtra(LONGITUDE, 0.0))
                ?: LatLng(0.1,0.1)
        } else {
            LatLng(0.2,0.2)
        }
    }


}