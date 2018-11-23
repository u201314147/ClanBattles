package com.zetagh.clanbattles.models

import android.os.Bundle

data  class Customer (val id: Int? = null,
                      var roleId: Int? = null,
                      val firstName: String? =null,
                      val lastName: String? =null,
                      val email: String? =null,
                      val birthDate: String?=null,
                      val address: String?=null,
                      val ruc : String?=null,
                      val longitud: String?=null,
                      val latitud: String?=null,
                      val username: String?=null,
                      val password: String?=null,
                      val rating : String?=null,
                      val avatar: String?=null
){

    companion object {
        fun from(bundle: Bundle) : Customer {
            return Customer(
                    bundle.getInt("id"),
                    bundle.getInt("roleId"),
                    bundle.getString("firstName"),
                    bundle.getString("lastName"),
                    bundle.getString("email"),
                    bundle.getString("birthDate"),
                    bundle.getString("address"),
                    bundle.getString("ruc"),
                    bundle.getString("longitud"),
                    bundle.getString("latitud"),
                    bundle.getString("username"),
                    bundle.getString("password"),
                    bundle.getString("rating"),
                    bundle.getString("avatar")
                    )
        }
    }


    fun toBundle(): Bundle{
        val bundle = Bundle()
        with(bundle){
            putInt("id",id!!)
            putInt("roleId",roleId!!)
            putString("firstName",firstName)
            putString("lastName",lastName)
            putString("email",email)
            putString("birthDate",birthDate)
            putString("address",address)
            putString("ruc",ruc)
            putString("longitud",longitud)
            putString("latitud",latitud)
            putString("username",username)
            putString("password",password)
            putString("rating",rating)
            putString("avatar",avatar)
        }
        return bundle
    }

}