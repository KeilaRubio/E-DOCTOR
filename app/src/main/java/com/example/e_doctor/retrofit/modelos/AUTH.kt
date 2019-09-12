package com.example.e_doctor.retrofit.modelos

data class AUTH (
     var token_type: String,
     var expires_in: String,
     var access_token: String,
     var refresh_token: String

)