package com.example.trysample

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi

object  Helper {

    fun convertImageUriToBase64(context: Context, uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes() ?: return ""
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun getUserId(email : String) : String {
        return email.substringBefore("@")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateTime(): String {
        val current = java.time.LocalDateTime.now()
        val formatter = java.time.format.DateTimeFormatter.ofPattern("hh:mm a  dd MMMM yyyy")
        return current.format(formatter)
    }

    fun uriToBase64(context: Context, uri: Uri): String {
        Log.d("ListOfPost","Image Uri is $uri")
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()

        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }


}