package com.example.conference.board

data class BoardListDTO(
    var nickname : String = "",
    var uid : String = "",
    var contents : String = "",
    var timestamp : Long? = null
)
