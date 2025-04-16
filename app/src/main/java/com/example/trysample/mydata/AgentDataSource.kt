package com.example.trysample.mydata


import com.example.trysample.R

data class AgentPost(
    var posId: String = "",
    var name: String = "",
    val datePosted: String = "",
    val totalLabor: String = "",
    val chargePerLabor: String = "",
    val locationServiceOffered  : String = ""
)
