package com.example.hopekipuppy

import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import timber.log.Timber
import java.io.IOException
import java.util.*

class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        val seoul = LatLng(37.574, 126.97458) //광화문 광장
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 15f))

        if (!requireArguments().getString("addr").isNullOrEmpty()) {

            val geocoder = Geocoder(this.context, Locale.KOREA)
            val str = requireArguments().getString("addr")
            var addressList: List<Address>? = null
            try {
                addressList = geocoder!!.getFromLocationName(str, 10)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if(addressList!!.isNotEmpty()) {
                var latitude = addressList[0].latitude
                var longitude = addressList[0].longitude
                val point = LatLng(latitude, longitude) // 좌표(위도, 경도) 생성
                val mOptions2 = MarkerOptions() // 마커 생성
                mOptions2.title("search result")
                mOptions2.position(point) // 마커 추가
                mOptions2.draggable(true)
                googleMap!!.addMarker(mOptions2) // 해당 좌표로 화면 줌
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 16f))
                googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                    override fun onMarkerDragStart(arg0: Marker) {
                    }
                    override fun onMarkerDragEnd(arg0: Marker) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.position))
                        val position = arg0.position
                        latitude = position.latitude
                        longitude = position.longitude
                    }
                    override fun onMarkerDrag(arg0: Marker) {
                    }
                })
                Timber.d("lat $latitude , long $longitude")
            }
        }
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