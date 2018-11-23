package com.zetagh.clanbattles.networking

class ClanBattlesApi {
    companion object {
        private val baseUrl = "http://clanbattles.somee.com/clanbattles"
        private val baseUrlV1 = "http://clanbattlesv1.somee.com/clanbattles"


        val authenticationUrl = "$baseUrlV1/v1/authentications"
        val getGamesUrl = "$baseUrlV1/v1/games"
        val getPublicationsUrl = "$baseUrlV1/v1/publications"
        val getCustomersUrl = "$baseUrlV1/v1/customers"  //query parameters : q -> rolName

        fun getCustomerUrl(customerId:Int):String{return "$baseUrlV1/v1/customers/$customerId"}


        val getGameUrl = "$baseUrl/v1/games"
        val getLanCentersUrl = "$baseUrl/v1/lancenters"
        val getGamersUrl = "$baseUrl/v1/gamers"
        fun getPublicationByGamer(gamerId:Int):String{return "$getGameUrl/$gamerId/publications"}
        fun urlPostPublication(gamerId:Int):String{return "$getGamersUrl/$gamerId/publications"}
        fun getClanUrl(gameId: Int): String {return "$getGameUrl/$gameId/clans"}
        val tag = "ClanBattles"
    }
}