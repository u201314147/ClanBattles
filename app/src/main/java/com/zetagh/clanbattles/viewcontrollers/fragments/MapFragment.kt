package com.zetagh.clanbattles.viewcontrollers.fragments


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import com.zetagh.clanbattles.R
import com.zetagh.clanbattles.models.Customer
import com.zetagh.clanbattles.models.Game
import com.zetagh.clanbattles.networking.ClanBattlesApi
import com.zetagh.clanbattles.networking.CustomersResponse
import com.zetagh.clanbattles.networking.GameResponse
import com.zetagh.clanbattles.viewcontrollers.activities.LanCenterActivity
import okhttp3.Credentials

class MapFragment : Fragment(), OnMapReadyCallback {


    private lateinit var  mapFragment: SupportMapFragment
    private lateinit var  mMap: GoogleMap
    private var lanCenters = ArrayList<Customer>()
    private lateinit var sharePref : SharedPreferences
    private  var  token =""

    private val LOCATION_REQUEST_CODE = 101
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        sharePref = view.context.getSharedPreferences("com.zetagh.clanbattles.userData", Context.MODE_PRIVATE)
        token = sharePref.getString("token","noToken")
        mapFragment = childFragmentManager.findFragmentById(R.id.mapViewFragment) as SupportMapFragment


        mapFragment.getMapAsync(this)
        return view
    }


    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!
        getLanCenters()
        createMarkers(mMap)
        mMap.setOnMarkerClickListener(object:GoogleMap.OnMarkerClickListener{
            override fun onMarkerClick(p0: Marker?): Boolean {
                val intent = Intent(context,LanCenterActivity::class.java)
                        .putExtra("idLanCenter", p0!!.zIndex)
                startActivity(intent)
                return false
            }

        })

    }


   private fun getLanCenters(){
        AndroidNetworking.get(ClanBattlesApi.getCustomersUrl)
                .addQueryParameter("q","LanCenter")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(CustomersResponse::class.java, object:ParsedRequestListener<CustomersResponse>{
                    override fun onResponse(response: CustomersResponse?) {
                        if(response!!.status!="ok") {
                            Log.d(ClanBattlesApi.tag, "Error on response!")
                        }
                            lanCenters = response.customers!!
                            createMarkers(mMap)
                            Log.d("MapFragment","response: ${lanCenters.size}")

                    }

                    override fun onError(anError: ANError?) {
                        Log.d("MapFragment", "Error: ${anError!!.message.toString()}")
                   }

                })
    }

   private fun createMarkers(mMap:GoogleMap){
       Log.d("MapFragment", "not found")
        var location:LatLng
        for(item in lanCenters){
            location = LatLng(item.latitud!!.toDouble(), item.longitud!!.toDouble())
            mMap.addMarker(MarkerOptions().position(location).title(item.firstName)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.lancenter_icon)).zIndex(item.id!!.toFloat()))

        }
    }





}


