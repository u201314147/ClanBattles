package com.zetagh.clanbattles.models

import android.os.Bundle

data class Publication(
        var id:Int? = null,
        var preferencesId: Int? = null,
        var title:String? = null,
        var description:String? = null,
        var imageUrl:String? = null ,
        var preference : Preference,
        var createdAt:String? = null,
        var updatedAt:String? = null,
        var status:String? = null
){
    companion object {
        fun from(bundle: Bundle):Publication {
            return Publication(
                    bundle.getInt("id"),
                    bundle.getInt("preferencesId"),
                    bundle.getString("title"),
                    bundle.getString("description"),
                    bundle.getString("imageUrl"),
                    Preference.from(bundle.getBundle("preference")),
                    bundle.getString("createdAt"),
                    bundle.getString("updatedAt"),
                    bundle.getString("status")
            )
        }
    }

    fun toBundle():Bundle{
        val bundle = Bundle()
        with(bundle){
            putInt("id",id!!)
            putInt("preferencesId",preferencesId!!)
            putString("title",title)
            putString("description",description)
            putString("imageUrl",imageUrl)
            putBundle("preference",preference.toBundle())
            putString("createdAt",createdAt)
            putString("updatedAt",updatedAt)
            putString("status",status)
        }
        return bundle
    }
}