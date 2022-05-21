package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.icu.text.CaseMap
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory //Maybe remove this
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MapsFragment : Fragment() {

//    List of each service type *Probably should be it's own class*
    val serviceDone = arrayOf<String>(
        "Did something uplifting that is not on this list",
        "Wrote a kind text to someone",
        "Posted on social media about something kind done to me",
        "Donated blood",
        "Had lunch with someone new",
        "Said a prayer of gratitude",
        "Donated to a charity", "Cleaned a family member's room",
        "Shared a motivational scripture with someone in need",
        "Attended an event to support someone I know",
        "Called my parents",
        "Made art that reminds me of Christ",
        "Wrote a thank you letter to someone",
        "Had a one-on-one activity with a loved one",
        "Invited someone to church",
        "Shared a story about an ancestor with a friend",
        "Smiled at someone",
        "Prayed for a troubled friend",
        "Made thank you notes for my teachers",
        "Babysat someone's children so they could accomplish a task",
        "Prayed for an opportunity to serve",
        "Brought goodies or a gift to a neighbor",
        "Thanked a parent for the things they have given me/gratitude list",
        "Shared the things Christ does for me on social me"
    )

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
//        Get database info
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("lights")


        myRef.addValueEventListener(object: ValueEventListener {
//        Wait for database data to change
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.children

//        Get data from database and store it for display
                for (data in value) {
                    var latitude: Double = 0.0
                    var longitude: Double = 0.0
                    var serviceType: Long = 0
                    for (info in data.children) {
                        println(info.key + ": " + info.value)
                        if (info.key == "latitude") {
                            latitude = info.value as Double
                        }
                        if (info.key == "longitude") {
                            longitude = info.value as Double
                        }
                        if (info.key == "messageType"){
                            serviceType = (info.value as Long)
                        }
                    }
                    

//                  Display the beacon of light on the map
                    val beacon = LatLng(longitude, latitude)
                    val bitmapDrawable = ContextCompat.getDrawable(context!!, R.drawable.alight) as BitmapDrawable
                    val bitmap = bitmapDrawable.bitmap
                    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 10, 10, false)
                    googleMap.addMarker(MarkerOptions().position(beacon)
                            .title(serviceDone[serviceType.toInt()])
                            .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)))

                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(beacon))
                }



                //Log.d(TAG, "Value is: " + value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }

        })

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_maps, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}