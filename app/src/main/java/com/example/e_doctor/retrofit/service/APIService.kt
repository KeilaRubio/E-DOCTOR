package com.example.e_doctor.retrofit.service

import com.example.e_doctor.retrofit.modelos.API_LOGIN
import com.example.e_doctor.retrofit.modelos.API_RECUPERAR_CONTRASENA_LOGIN
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
        @Body json: API_REGISTRO //RECIBE
    ): Call<API_REGISTRO_PERFIL> //ENVIO

    /**
     * **********************************************************************
     * RECUPERACION DE CONTRASEÑA (desde login)
     * Servicio para verificar el correo electrónico y saber si es un usuario
     */
    @Headers("Content-Type: application/json")
    @POST("contrasena/recuperacion/correo")
    fun RecuperarContraseniaCorreo(
        @HeaderMap headers: Map<String, String>, //Header
        @Body json: API_RECUPERAR_CONTRASENA_LOGIN
    ): Call<API_REGISTRO_PERFIL>


}