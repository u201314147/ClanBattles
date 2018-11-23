package com.zetagh.clanbattles.networking

import com.zetagh.clanbattles.models.Publication

class PublicationResponse(
        val status: String? = null,
        val code: String? = null,
        val totalResult: String? = null,
        val publications: ArrayList<Publication>? = ArrayList()
)
