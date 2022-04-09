package lol.xget.groceryapp.mapLocalization.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import lol.xget.groceryapp.R
import lol.xget.groceryapp.common.Constants
import lol.xget.groceryapp.databinding.ActivityMaps2Binding
import lol.xget.groceryapp.login.domain.LocationLiveData

@OptIn(ExperimentalMaterialApi::class, ExperimentalCoroutinesApi::class)
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMaps2Binding


    var currentMarker: Marker? = null
    var saveCoordinatesUser = mutableStateOf(LatLng(0.0, 0.0))
    var userLongitude = mutableStateOf(0.0)
    var userLatitude = mutableStateOf(0.0)


    private fun sendActivityResult(longitude: Double, latitude: Double) {
        saveCoordinatesUser.value =
            LatLng(
                intent.getDoubleExtra(Constants.LATITUDE, 0.0),
                intent.getDoubleExtra(Constants.LONGITUDE, 0.0)
            )

        setResult(RESULT_OK, Intent().apply {
            putExtra(Constants.LONGITUDE, longitude)
            putExtra(Constants.LATITUDE, latitude)
        })
        finish()
    }

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaps2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@MapsActivity)




        // Grab our reference to the ComposeView from our layout.
        val composeView = findViewById<ComposeView>(R.id.compose_view)
        // Just like before, use the setContent to display Composable UI.
        composeView.setContent {
            // We then render a simple Text component from Compose.
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {

                    IconButton(

                        onClick = {

                        },
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp),
                            contentDescription = "My Location"
                        )
                    }

                    OutlinedButton(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 20.dp)
                            .width(200.dp)
                            .height(50.dp),
                        border = BorderStroke(1.dp, Color.White),
                        shape = RoundedCornerShape(50),
                        onClick = {
                            sendActivityResult(
                                saveCoordinatesUser.value.longitude,
                                saveCoordinatesUser.value.latitude
                            )
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White,
                            backgroundColor = Color.Black
                        )

                    ) {
                        Text(text = "Save this location")
                    }
                }
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val locationData = LocationLiveData(application)
        fun getLocationData() = locationData
        getLocationData().observe(this) {
            userLatitude.value = it.latitude
            userLongitude.value = it.longitude

            val userCoordinates = LatLng(userLatitude.value, userLongitude.value)
            saveCoordinatesUser.value = LatLng(userLatitude.value, userLongitude.value)
            drawMarker(userCoordinates)
        }




        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(p0: Marker) {
                if (currentMarker != null)
                    currentMarker?.remove()

                val newPosition = LatLng(p0.position.latitude, p0.position.longitude)
                drawMarker(newPosition)
                saveCoordinatesUser.value = newPosition

            }

            override fun onMarkerDragStart(p0: Marker) {

            }

        })

    }

    private fun drawMarker(latLng: LatLng) {
        val markerOption =
            MarkerOptions().position(latLng).title("Move to you locaiton").draggable(true)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f), 3000, null)
        currentMarker = mMap.addMarker(markerOption)
        currentMarker?.showInfoWindow()
    }

}