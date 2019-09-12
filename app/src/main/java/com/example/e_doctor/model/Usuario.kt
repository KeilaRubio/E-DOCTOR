package com.example.e_doctor.model

data class Usuario (
     var nombres: String,
     var apellidos: String,
     var cedula: String,
     var telefono: String,
     var correo: String,
     var fecha_nacimiento: String,
     var foto_perfil: String,
     var ciudad_id: String,
     var api_seguimiento_id: String,
     var api_notificaciones_id: String,
     var uid_usuario: String
)