package com.zetagh.clanbattles.viewcontrollers.fragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener

import com.zetagh.clanbattles.R
import com.zetagh.clanbattles.models.Publication
import com.zetagh.clanbattles.networking.ClanBattlesApi
import com.zetagh.clanbattles.networking.PublicationResponse
import com.zetagh.clanbattles.viewcontrollers.adapters.PublicationAdapter
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var bundle:Bundle
    private lateinit var publication: Publication
    private lateinit var sharePref:SharedPreferences
    private var preferences = ""
    private var publications = ArrayList<Publication>()
    private lateinit var publicationRecyclerView: RecyclerView
    private lateinit var publicationAdapter: PublicationAdapter
    private lateinit var publicationLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharePref = context!!.getSharedPreferences("com.zetagh.clanbattles.userData",Context.MODE_PRIVATE)
        preferences = sharePref.getString("preferences","noPreferences")
        Log.d("HomeFragment","preferenceHomeFragment: $preferences")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        val getPublicationUrl = ClanBattlesApi.getPublicationByGamer(1)
        publicationAdapter = PublicationAdapter(publications,view.context)
        publicationLayoutManager = LinearLayoutManager(view.context)
        publicationRecyclerView = view.HomeRecyclerView
        publicationRecyclerView.adapter = publicationAdapter
        publicationRecyclerView.layoutManager = publicationLayoutManager

        AndroidNetworking.get(ClanBattlesApi.getPublicationsUrl)
                .addQueryParameter("q",preferences)
                .setPriority(Priority.LOW)
                .setTag("homeFragment")
                .build()
                .getAsObject(PublicationResponse::class.java,object : ParsedRequestListener<PublicationResponse> {
                    override fun onResponse(response: PublicationResponse) {
                        if(response.status!="ok"){
                            Log.d(ClanBattlesApi.tag,"Error on response!")
                        }
                        publications = response.publications!!
                        publicationAdapter.publications = publications
                        publicationAdapter.notifyDataSetChanged()
                        Log.d("HomeFragment","publications: ${publications.toString()}")
                    }

                    override fun onError(anError: ANError?) {
                        Log.d("HomeFragment", anError!!.message)
                    }

                })

        return view
    }


}
