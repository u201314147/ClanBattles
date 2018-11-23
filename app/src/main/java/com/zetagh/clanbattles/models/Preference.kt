package com.zetagh.clanbattles.models

import android.os.Bundle

data class Preference(val id:Int?,
                      val customerId:Int?,
                      val gameId:Int?,
                      val playerId:String?,
                      val playerName:String?,
                      val clanId:String?,
                      val createdAt:String?,
                      val updatedAt:String?){
    companion object {
        fun from(bundle: Bundle):Preference{
            return Preference(
                    bundle.getInt("id"),
                    bundle.getInt("customerId"),
                    bundle.getInt("gameId"),
                    bundle.getString("playerId"),
                    bundle.getString("playerName"),
                    bundle.getString("clanId"),
                    bundle.getString("createdAt"),
                    bundle.getString("updatedAt")
                    )
        }
    }

    fun toBundle(): Bundle{
        var bundle = Bundle()
        with(bundle){
            putInt("id",id!!)
            putInt("customerId",customerId!!)
            putInt("gameId",gameId!!)
            putString("playerId",playerId)
            putString("playerName",playerName)
            putString("clanId",clanId)
            putString("createdAt",createdAt)
            putString("updatedAt",updatedAt)
        }
        return bundle
    }
}