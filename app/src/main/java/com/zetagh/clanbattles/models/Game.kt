package com.zetagh.clanbattles.models

import android.os.Bundle

data class Game(val id:Int,
                val name:String?,
                val description:String?,
                val imageUrl:String?,
                val status:String?,
                var isSelected: Boolean = false) {
    companion object {
        fun from(bundle: Bundle) : Game {
            return Game(
                    bundle.getInt("id"),
                    bundle.getString("name"),
                    bundle.getString("description"),
                    bundle.getString("imageUrl"),
                    bundle.getString("status"))
        }
    }


    fun toBundle(): Bundle {
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("name", name)
        bundle.putString("description", description)
        bundle.putString("imageUrl", imageUrl)
        bundle.putString("status", status)
        return bundle
    }

}