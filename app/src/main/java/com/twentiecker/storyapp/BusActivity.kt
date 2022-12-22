package com.twentiecker.storyapp

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.twentiecker.storyapp.databinding.ActivityBusBinding
import java.util.concurrent.TimeUnit

class BusActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityBusBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var isTracking = false
    private lateinit var locationCallback: LocationCallback
    private var allLatLng = ArrayList<LatLng>()
    private val centerLat = -6.890336202165823  // dinaikkan jadi turun ring nya (y)
    private val centerLng = 107.6104263660377 // dinaikkan jadi kanan ring nya (x)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor(getString(R.string.blue)))
        actionBar?.setBackgroundDrawable(colorDrawable)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val itb = LatLng(centerLat, centerLng)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itb, 15f))

        val south = LatLng(-6.891030382971091, 107.61045566716005)
        mMap.addMarker(MarkerOptions().position(south).title("Shelter Selatan"))
        val north = LatLng(-6.88869904024249, 107.61024100252867)
        mMap.addMarker(MarkerOptions().position(north).title("Shelter Utara"))

        mMap.setOnPoiClickListener { pointOfInterest ->
            val poiMarker = mMap.addMarker(
                MarkerOptions()
                    .position(pointOfInterest.latLng)
                    .title(pointOfInterest.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            )
            poiMarker?.showInfoWindow()
        }

        getMyLocation()
        setMapStyle(R.raw.map_itb)

//        getMyLastLocation()
//        createLocationRequest()
//        createLocationCallback()

//        binding.btnStart.setOnClickListener {
//            if (!isTracking) {
//                updateTrackingStatus(true)
//                startLocationUpdates()
//            } else {
//                updateTrackingStatus(false)
//                stopLocationUpdates()
//            }
//        }
    }

    private val requestBackgroundLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    @TargetApi(Build.VERSION_CODES.Q)
    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                if (runningQOrLater) {
                    requestBackgroundLocationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                } else {
                    getMyLocation()
                }
            }
        }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.Q)
    private fun checkForegroundAndBackgroundLocationPermission(): Boolean {
        val foregroundLocationApproved = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (checkForegroundAndBackgroundLocationPermission()) {
            mMap.isMyLocationEnabled = true
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_maps, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setMapStyle(x: Int) {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, x))
            if (!success) {
                Toast.makeText(this@BusActivity, "Style parsing failed.", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (exception: Resources.NotFoundException) {
            Toast.makeText(
                this@BusActivity,
                "Can't find style. Error: $exception",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

//    private fun startLocationUpdates() {
//        try {
//            fusedLocationClient.requestLocationUpdates(
//                locationRequest,
//                locationCallback,
//                Looper.getMainLooper()
//            )
//        } catch (exception: SecurityException) {
//            Log.e(TAG, "Error : " + exception.message)
//        }
//    }
//
//    private fun stopLocationUpdates() {
//        fusedLocationClient.removeLocationUpdates(locationCallback)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (isTracking) {
//            startLocationUpdates()
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        stopLocationUpdates()
//    }
//
////    private fun updateTrackingStatus(newStatus: Boolean) {
////        isTracking = newStatus
////        if (isTracking) {
////            binding.btnStart.text = getString(R.string.stop_running)
////        } else {
////            binding.btnStart.text = getString(R.string.start_running)
////        }
////    }
//
//    private fun createLocationCallback() {
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                for (location in locationResult.locations) {
//                    Log.d(TAG, "onLocationResult: " + location.latitude + ", " + location.longitude)
//                    val lastLatLng = LatLng(location.latitude, location.longitude)
//
//                    //draw polyline
//                    allLatLng.add(lastLatLng)
//                    mMap.addPolyline(
//                        PolylineOptions()
//                            .color(Color.CYAN)
//                            .width(10f)
//                            .addAll(allLatLng)
//                    )
//                }
//            }
//        }
//    }
//
//    private val requestPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            when {
//                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
//                    // Precise location access granted.
//                    getMyLastLocation()
//                }
//                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
//                    // Only approximate location access granted.
//                    getMyLastLocation()
//                }
//                else -> {
//                    // No location access granted.
//                }
//            }
//        }
//
//    private fun checkPermission(permission: String): Boolean {
//        return ContextCompat.checkSelfPermission(
//            this,
//            permission
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun getMyLastLocation() {
//        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
//            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
//        ) {
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                if (location != null) {
//                    showStartMarker(location)
//                } else {
//                    Toast.makeText(
//                        this@BusActivity,
//                        "Location is not found. Try Again",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        } else {
//            requestPermissionLauncher.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            )
//        }
//    }
//
//    private fun showStartMarker(location: Location) {
//        val startLocation = LatLng(location.latitude, location.longitude)
//        mMap.addMarker(
//            MarkerOptions()
//                .position(startLocation)
//                .title(getString(R.string.start_point))
//        )
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 17f))
//    }
//
//    private val resolutionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.StartIntentSenderForResult()
//        ) { result ->
//            when (result.resultCode) {
//                RESULT_OK ->
//                    Log.i(TAG, "onActivityResult: All location settings are satisfied.")
//                RESULT_CANCELED ->
//                    Toast.makeText(
//                        this@BusActivity,
//                        "Anda harus mengaktifkan GPS untuk menggunakan aplikasi ini!",
//                        Toast.LENGTH_SHORT
//                    ).show()
//            }
//        }
//
//    private fun createLocationRequest() {
//        locationRequest = LocationRequest.create().apply {
//            interval = TimeUnit.SECONDS.toMillis(1)
//            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
//        val builder = LocationSettingsRequest.Builder()
//            .addLocationRequest(locationRequest)
//        val client = LocationServices.getSettingsClient(this)
//        client.checkLocationSettings(builder.build())
//            .addOnSuccessListener {
//                getMyLastLocation()
//            }
//            .addOnFailureListener { exception ->
//                if (exception is ResolvableApiException) {
//                    try {
//                        resolutionLauncher.launch(
//                            IntentSenderRequest.Builder(exception.resolution).build()
//                        )
//                    } catch (sendEx: IntentSender.SendIntentException) {
//                        Toast.makeText(this@BusActivity, sendEx.message, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//    }
//
//    companion object {
//        private const val TAG = "BusActivity"
//    }
}