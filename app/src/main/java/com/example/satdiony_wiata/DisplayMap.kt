package com.example.satdiony_wiata

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.satdiony_wiata.databinding.ActivityDisplayMapBinding
import com.example.satdiony_wiata.models.Stadiums
import com.google.android.gms.maps.model.*

class DisplayMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDisplayMapBinding
    private lateinit var stadiums: Stadiums
    private var temp : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDisplayMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stadiums = intent.getSerializableExtra(EXTRA_USER_MAP) as Stadiums

        supportActionBar?.title = stadiums.title

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId ){
            R.id.allButton -> placeMarkers(mMap, 0, false)
            R.id.fullButton -> placeMarkers(mMap, 2, false)
            R.id.freeButton -> placeMarkers(mMap, 1, false)
        }
        return super.onOptionsItemSelected(item)
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
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.setOnInfoWindowClickListener { latLng ->
            showAlertDialog(latLng)
        }

        placeMarker(mMap, 0)

    }

    private fun placeMarker(mMap: GoogleMap, shouldClr : Int ) {
        placeMarkers(mMap, 0 , true)

    }

    private fun placeMarkers (mMap: GoogleMap, temp: Int, first: Boolean){
        clear(mMap)
        val boundsBuilder = LatLngBounds.Builder()
        for (place in stadiums.places){
            val latLng = LatLng(place.latitude, place.longitude)
            boundsBuilder.include(latLng)
            if(place.description == "Wolne" && (temp == 0 || temp == 1)) {
                mMap.addMarker(
                    MarkerOptions().position(latLng).title(place.title).snippet(place.description)
                        .icon(
                            BitmapDescriptorFactory.fromResource(R.drawable.green_stadium)
                        )
                )
            }else if(place.description == "Zajęte" && (temp == 0 || temp == 2)) {
                mMap.addMarker(
                    MarkerOptions().position(latLng).title(place.title).snippet(place.description)
                        .icon(
                            BitmapDescriptorFactory.fromResource(R.drawable.red_stadium)
                        )
                )
            }
            if(first == true) mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 1000, 1000, 0 ))
        }
    }

    private fun showAlertDialog(latLng: Marker) {
        val placeFormView = LayoutInflater.from(this).inflate(R.layout.dialog_change, null)
        val dialog =
            AlertDialog.Builder(this,)
            .setView(placeFormView)
            .show()
        val stadionText = placeFormView.findViewById<TextView>(R.id.stadionName)
        val statusText = placeFormView.findViewById<TextView>(R.id.statusText)
        for(place in stadiums.places){
            var temp = 0
            if(latLng.position.latitude == place.latitude && latLng.position.longitude == place.longitude){
                stadionText.text = stadiums.places[temp].title

            }
            temp += 1
        }

        for(place in stadiums.places){
            var temp = 0
            if(latLng.position.latitude == place.latitude && latLng.position.longitude == place.longitude){
                statusText.text = "Status: " + stadiums.places[temp].description
                if(stadiums.places[temp].description == "Wolne") placeFormView.findViewById<Switch>(R.id.free).isChecked = true
                else placeFormView.findViewById<Switch>(R.id.free).isChecked = false
            }
            temp += 1
        }

        val button = placeFormView.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            var temp = 0
            val freeButton = placeFormView.findViewById<Switch>(R.id.free)
            if(freeButton.isChecked){
                for (place in stadiums.places ){
                    if(latLng.position.latitude == place.latitude && latLng.position.longitude == place.longitude){
                        stadiums.places[temp].description = "Wolne"

                    }
                    temp +=1
                }
            }else{
                for (place in stadiums.places ){
                    if(latLng.position.latitude == place.latitude && latLng.position.longitude == place.longitude){
                        stadiums.places[temp].description = "Zajęte"

                    }
                    temp +=1
                }
            }
            placeMarker(mMap, 1)
            dialog.dismiss()
            ModelPreferencesManager.put(stadiums, "KEY_STADIUMS")
        }

    }
    private fun clear(mMap : GoogleMap){
        mMap.clear()
    }




}