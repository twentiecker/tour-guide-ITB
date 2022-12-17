package com.twentiecker.storyapp.maps.direction

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.twentiecker.storyapp.R
import com.twentiecker.storyapp.databinding.ActivityDirectionBinding

class DirectionActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDirectionBinding

    private val centerLat = -6.890336202165823  // dinaikkan jadi turun ring nya (y)
    private val centerLng = 107.6104263660377 // dinaikkan jadi kanan ring nya (x)
    private val geofenceRadius = 255.0

    private lateinit var geofencingClient: GeofencingClient

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        intent.action = GeofenceBroadcastReceiver.ACTION_GEOFENCE_EVENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDirectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#0064fe"))
        actionBar?.setBackgroundDrawable(colorDrawable)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        val stanford = LatLng(centerLat, centerLng)
        mMap.addMarker(MarkerOptions().position(stanford).title(getString(R.string.campus)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stanford, 15f))
        mMap.addCircle(
            CircleOptions()
                .center(stanford)
                .radius(geofenceRadius)
                .fillColor(0x22FF0000)
                .strokeColor(Color.RED)
                .strokeWidth(3f)
        )

        getMyLocation()
        addGeofence()
        setMapStyle(R.raw.map_itb)
    }

    @SuppressLint("MissingPermission")
    private fun addGeofence() {
        geofencingClient = LocationServices.getGeofencingClient(this)
        val geofence = Geofence.Builder()
            .setRequestId("kampus")
            .setCircularRegion(
                centerLat,
                centerLng,
                geofenceRadius.toFloat()
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_ENTER)
            .setLoiteringDelay(1000)
            .build()
        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()
        geofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnCompleteListener {
                geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
                    addOnSuccessListener {
                        showToast("Geofencing added")
                    }
                    addOnFailureListener {
                        showToast("Geofencing not added : ${it.message}")
                    }
                }
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this@DirectionActivity, text, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@DirectionActivity, "Style parsing failed.", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (exception: Resources.NotFoundException) {
            Toast.makeText(
                this@DirectionActivity,
                "Can't find style. Error: $exception",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}