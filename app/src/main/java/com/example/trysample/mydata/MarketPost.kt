package com.example.trysample.mydata

data class MarketPost(
    var postId: String = "",
    val cropName: String  = "",
    val quantity: String  = "",
    var seedImage : String  = "",
    val price: String  = "",
    val sellerName: String  = "",
    val location: String  = "",
    val datePosted: String  = ""
)
