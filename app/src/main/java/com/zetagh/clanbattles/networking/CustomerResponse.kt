package com.zetagh.clanbattles.networking

import com.zetagh.clanbattles.models.Customer

class CustomerResponse(val status: String?,
                       val code: String?,
                       val customer: Customer?,
                       val token: String?,
                       val message: String?)