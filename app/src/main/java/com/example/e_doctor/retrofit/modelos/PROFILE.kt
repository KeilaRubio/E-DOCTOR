package com.example.e_doctor.retrofit.modelos

data class PROFILE (
     var name: String,
     var last_name: String,
     var dni: String,
     var phone: String,
     var birthdate: String,
    var profile_picture: String,
     var country: COUNTRY
)