package com.zetagh.clanbattles.viewcontrollers.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.zetagh.clanbattles.R
import com.zetagh.clanbattles.models.Customer
import com.zetagh.clanbattles.networking.ClanBattlesApi
import com.zetagh.clanbattles.networking.CustomerResponse
import com.zetagh.clanbattles.networking.GameResponse
import kotlinx.android.synthetic.main.content_login.*
import kotlinx.android.synthetic.main.item_rank.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var customer: Customer
    private var token: String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var sharePref: SharedPreferences
    private val TAG = "loginModule"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        //signInButtonListener()
        logInButtonListener()
        //signUpButtonListener()

    }

   /* private fun isUserLoggedIn():Boolean{
        if(FirebaseAuth.getInstance().currentUser == null){
            Log.d(TAG,"User not logged in.")
            Toast.makeText(applicationContext,"Log in first please.",Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            return true
        }
    }*/


    private fun startOnBoardingActivity(view : View){
        val context = view.context
        context.startActivity(Intent(context,MainActivity::class.java))
    }


    private fun signUpButtonListener() {
        signUpButton.setOnClickListener {
            val context = it.context
            context.startActivity(Intent(context,SignUpActivity::class.java))
        }
    }

    private fun signInButtonListener(){
        signInButton.setOnClickListener {
            createSignInIntent()
        }
    }

    private fun logInButtonListener(){
        loginButton.setOnClickListener { view ->

            val userAccount = JSONObject()
            userAccount.put("username", usernameEditText.text)
            userAccount.put("password", passwordEditText.text)

            Log.d(ClanBattlesApi.tag, userAccount.toString())
            AndroidNetworking.post("http://clanbattlesv2.somee.com/clanbattles/v1/authentications")
                    .addJSONObjectBody(userAccount)
                    .setTag(ClanBattlesApi.tag)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(CustomerResponse::class.java, object : ParsedRequestListener<CustomerResponse>{
                        override fun onResponse(response: CustomerResponse?) {
                            if(response!!.status != "ok"){
                                Log.d(ClanBattlesApi.tag, "Error on response!")
                            }
                            customer = response.customer!!
                            token = response.token!!
                            Log.d(ClanBattlesApi.tag,"${customer.firstName}")
                            saveUserToSharePreferenceLogin(customer,token)
                            startOnBoardingActivity(view)
                        }

                        override fun onError(anError: ANError?) {
                            Log.d(ClanBattlesApi.tag, "Error on response!: ${anError.toString()}")
                            Toast.makeText(applicationContext,"Incorrect username or password!",Toast.LENGTH_SHORT)
                                    .show()
                        }
                    })


           /* if(isUserLoggedIn()){
                val context = it.context
                context.startActivity(Intent(context,MainActivity::class.java))
            }
            else{
                isUserLoggedIn()
            }*/

        }
    }

    private fun createSignInIntent(){

        val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                saveUserToSharePreference(user!!.displayName,user.photoUrl.toString())
                goMainScreen()
            } else {
                Toast.makeText(applicationContext,"Not possible to log in.",Toast.LENGTH_SHORT).show()
                goLogInScreen()
                Log.d(TAG,"Not possible to log in. -> ${response!!.error!!}")
            }
        }
    }*/

    private fun goLogInScreen() {
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }


    private fun saveUserToSharePreferenceLogin(userAccount:Customer,token:String){
        sharePref = getSharedPreferences("com.zetagh.clanbattles.userData",Context.MODE_PRIVATE)
        with(sharePref.edit()){
            putString("username", userAccount.username)
            putString("password", userAccount.password)
            putString("firstName", userAccount.firstName)
            putString("lastName", userAccount.lastName)
            putString("email", userAccount.email)
            putString("birthDate", userAccount.birthDate)
            putString("address", userAccount.address)
            putString("ruc", userAccount.ruc)
            putString("rating", userAccount.rating)
            putString("avatar", userAccount.avatar)
            putInt("roleId", userAccount.roleId!!)
            putInt("id", userAccount.id!!)
            putString("token",token)
            apply()
        }
    }


    private fun saveUserToSharePreference(username:String?,urlToUserImage:String) {
        sharePref = getSharedPreferences("com.zetagh.clanbattles.userData", Context.MODE_PRIVATE)
        with(sharePref.edit()){
            putString("username",username)
            putString("urlToUserImage",urlToUserImage)
            apply()
        }
        Log.d("test",sharePref.getString("username","NF"))
        Log.d("test",sharePref.getString("urlToUserImage","NF"))
    }


    private fun goMainScreen() {
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    companion object {
        private const val RC_SIGN_IN = 9001
    }
}

