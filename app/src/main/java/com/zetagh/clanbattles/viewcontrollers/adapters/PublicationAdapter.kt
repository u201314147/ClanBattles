package com.zetagh.clanbattles.viewcontrollers.adapters

import android.content.Context
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
import com.zetagh.clanbattles.models.Clan
import com.zetagh.clanbattles.models.Customer
import com.zetagh.clanbattles.models.Game
import com.zetagh.clanbattles.models.Publication
import com.zetagh.clanbattles.networking.ClanBattlesApi
import kotlinx.android.synthetic.main.item_publication.view.*

class PublicationAdapter(var publications:ArrayList<Publication>,val context: Context): RecyclerView.Adapter<PublicationAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_publication,parent,false))
    }

    override fun getItemCount(): Int {
        return publications.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val publication = publications[position]
        holder.updateFrom(publication)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        var customer = Customer()
        lateinit var game: Game
        val context = view.context
        val avatarImage = view.avatarImagePublication
        val titleTextView  = view.titleTextViewPublication
        val usernameTextView = view.usernameTextViewPublication
        val descriptionImageView = view.descriptionImageViewPublication
        val descriptionTextView = view.descriptionTextViewPublication

        fun updateFrom(publication: Publication) {

            titleTextView.text = publication.title
            usernameTextView.text = publication.title
            descriptionTextView.text = publication.description

            getCustomer(publication,context)
            avatarImage.setDefaultImageResId(R.mipmap.ic_launcher)
            avatarImage.setErrorImageResId(R.mipmap.ic_launcher)
            avatarImage.setImageUrl(customer.avatar)
//            avatarImage.clipToOutline = true

            descriptionImageView.setDefaultImageResId(R.mipmap.ic_launcher)
            descriptionImageView.setErrorImageResId(R.mipmap.ic_launcher)
            descriptionImageView.setImageUrl(publication.imageUrl)
            descriptionImageView.clipToOutline = true
        }

        fun getCustomer(publication:Publication,context:Context){
            var token:String=""
            var sharePref = context.getSharedPreferences("com.zetagh.clanbattles.userData",Context.MODE_PRIVATE)
            token = sharePref.getString("token","noToken")
            AndroidNetworking.get(ClanBattlesApi.getCustomerUrl(publication.preference.customerId!!))
                    .addHeaders("Authorization",token)
                    .setPriority(Priority.LOW)
                    .setTag(ClanBattlesApi.tag)
                    .build()
                    .getAsObject(Customer::class.java, object:ParsedRequestListener<Customer>{
                        override fun onResponse(response: Customer?) {
                            customer = response!!
                            Log.d(ClanBattlesApi.tag,"Customer:${customer.toString()}")
                        }

                        override fun onError(anError: ANError?) {
                            Log.d(ClanBattlesApi.tag,"Error: ${anError.toString()}")
                        }

                    })
        }

    }
}