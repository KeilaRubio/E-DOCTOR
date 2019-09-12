package com.example.e_doctor.retrofit.service

import com.example.e_doctor.retrofit.modelos.API_LOGIN
import com.example.e_doctor.retrofit.modelos.API_REGISTRO
import com.example.e_doctor.retrofit.modelos.API_REGISTRO_PERFIL
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    /**
     * Servicio de Login
     */
    @FormUrlEncoded
    @POST("login")
    fun Login(
        @HeaderMap headers: Map<String, String>, //Header
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String
    ): Call<API_LOGIN>

    /**
     * Servicio para registrar el perfil, una vez confirmado el pin
     */
    @Headers("Content-Type: application/json")
    @POST("registro")
    fun RegistrarPerfil(
        @HeaderMap headers: Map<String, String>,
        @Body json: API_REGISTRO
    ): Call<API_REGISTRO_PERFIL>
//    abstract fun RegistrarPerfil(
//        @HeaderMap headers: Map<String, String>, //Header
//        @Field("email") email: String,
////        @Field("fecha_nacimiento") fecha_nacimiento: String,
////        @Field("genero") genero: String,
////        @Field("foto") foto: String,
//        @Field("password") password: String,
//        @Field("password_confirmation") password_confirmation: String,
//        @Field("nombres") name: String,
////        @Field("country_iso") country_iso: String,
////        @Field("id_persona") id_persona: String,
//        @Field("client_id") client_id: String,
//        @Field("client_secret") client_secret: String
//    ): Call<API_REGISTRO_PERFIL>

}