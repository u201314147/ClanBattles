package com.zetagh.clanbattles.networking

import com.zetagh.clanbattles.models.Customer

class CustomersResponse(val status : String?,
                        val code:String?,
                        val totalResults:String?,
                        val customers:ArrayList<Customer>?,
                        val message:String?)