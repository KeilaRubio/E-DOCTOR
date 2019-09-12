package com.example.e_doctor.retrofit.modelos

data class API_REGISTRO(
    var email:String,
    var password: String,
    var password_confirmation: String,
    var nombres: String,
    var foto: String,
    var client_id: String,
    var client_secret: String,
    var extras: EXTRAS
)