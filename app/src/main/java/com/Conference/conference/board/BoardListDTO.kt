package com.Conference.conference.board

data class BoardListDTO(
    var nickname : String = "",
    var uid : String = "",
    var title : String = "",
    var contents : String = "",
    var favoriteCount : Int = 0,
    var timestamp : Long? = null
)
