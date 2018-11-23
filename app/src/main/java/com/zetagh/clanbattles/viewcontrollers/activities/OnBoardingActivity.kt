package com.zetagh.clanbattles.viewcontrollers.activities

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.zetagh.clanbattles.R
import com.zetagh.clanbattles.models.Game
import com.zetagh.clanbattles.models.SettingsRepository
import com.zetagh.clanbattles.networking.ClanBattlesApi
import com.zetagh.clanbattles.networking.GameResponse
import com.zetagh.clanbattles.viewcontrollers.adapters.GameAdapter
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoardingActivity : AppCompatActivity() {

    private var games = ArrayList<Game>()
    private lateinit var gamesAdapter : GameAdapter
    private lateinit var gamesLayoutManager: RecyclerView.LayoutManager
    private lateinit var gamesRecyclerView: RecyclerView
    private lateinit var sharePref : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        setSupportActionBar(toolbar)

      /*  //should show on boarding
        continueBottom.setOnClickListener { view ->
            val settings = SettingsRepository(view.context)
            settings.didShowOnboarding = true
            finish()
        }*/

        toolbar.title = "What are your preferences?"

        gamesAdapter = GameAdapter(games, this)
        gamesLayoutManager = LinearLayoutManager(this)
        gamesRecyclerView = this.gamesRecyclerViewLayout
        gamesRecyclerView.adapter = gamesAdapter
        gamesRecyclerView.layoutManager  = gamesLayoutManager


        continueBottom.setOnClickListener{view ->
            sharePref = getSharedPreferences("com.zetagh.clanbattles.userData",Context.MODE_PRIVATE)
            gamesAdapter.saveGamesPreferences(view.context)
            val setting = SettingsRepository(view.context)
            setting.didShowOnboarding = true
            Log.d("ClanBattles", "Las preferencias del usuario: ${sharePref.getString("preferences","NoPreferences")}")
            finish()
        }


        AndroidNetworking.get(ClanBattlesApi.getGamesUrl)
                .setPriority(Priority.MEDIUM)
                .setTag(ClanBattlesApi.tag)
                .build()
                .getAsObject(GameResponse::class.java, object : ParsedRequestListener<GameResponse> {
                    override fun onResponse(response: GameResponse) {
                        if(response.status != "ok"){
                            Log.d(ClanBattlesApi.tag, "Error on response!");
                        }
                        games = response.games!!
                        Log.d("onBoardingActivity", "Parsed: Found ${games.size} games")
                        gamesAdapter.games = games
                        gamesAdapter.notifyDataSetChanged()
                    }

                    override fun onError(anError: ANError?) {
                        Log.d("onBoardingActivity", anError!!.message)
                    }

                })
    }




}