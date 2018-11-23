package com.zetagh.clanbattles.viewcontrollers.adapters

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zetagh.clanbattles.R
import com.zetagh.clanbattles.models.Game
import kotlinx.android.synthetic.main.item_game.view.*

class GameAdapter(var games:ArrayList<Game>, val context: Context) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    var checkedGames = ArrayList<Game>()
    private lateinit var  sharePref : SharedPreferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_game, parent, false))
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games.get(position)
        holder.updateFrom(game,checkedGames)
    }

    fun saveGamesPreferences(context: Context){
        sharePref = context.getSharedPreferences("com.zetagh.clanbattles.userData",Context.MODE_PRIVATE)
        var preferences: String
        var preferencesIds = ArrayList<String>()
        for( i in checkedGames){
            preferencesIds.add(i.id.toString())
        }
        preferences = preferencesIds.joinToString(separator = ",")

        with(sharePref.edit()){
            putString("preferences",preferences)
            apply()
        }
        Log.d("save","savedPreferences: ${sharePref.getString("preferences","Not save preferences")}")
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var sharePref:SharedPreferences
        val pictureImageView = view.pictureImageView
        val gameLayout = view.gameLayout
        val preferenceCheckbox = view.preferenceCheckBox


        fun updateFrom(game: Game, checkedGames: ArrayList<Game>) {
            with(pictureImageView) {
                pictureImageView.setDefaultImageResId(R.mipmap.ic_launcher)
                pictureImageView.setErrorImageResId(R.mipmap.ic_launcher)
                pictureImageView.setImageUrl(game.imageUrl)
                pictureImageView.clipToOutline = true
            }
            preferenceCheckbox.isChecked = game.isSelected
            gameLayout.setOnClickListener{view ->
                if(preferenceCheckbox.isChecked){
                    game.isSelected = false
                    checkedGames.remove(game)
                    preferenceCheckbox.isChecked = false
                }else{
                    game.isSelected = true
                    checkedGames.add(game)
                    preferenceCheckbox.isChecked = true
                }
                Log.d("checkbox", checkedGames.toString())
            }
        }

    }
}