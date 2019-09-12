package com.example.e_doctor

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.e_doctor.core.Preferencias
import com.example.e_doctor.helper.UtilidadesHelper
import com.example.e_doctor.model.ConfigPreferencias
import com.example.e_doctor.model.Usuario
import com.example.e_doctor.retrofit.error.ErrorUtils
import com.example.e_doctor.retrofit.modelos.API_LOGIN
import com.example.e_doctor.retrofit.service.RETROFIT_API_BENEFITS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class Login : AppCompatActivity() {
    lateinit var tvRegistro:TextView
    lateinit var tvOlvidoContrasena:TextView
    lateinit var etContrasena:TextView
    lateinit var etCorreo:TextView
//    lateinit var relativeLayoutProgress:RelativeLayout
//    private lateinit var mPreferencias:Preferencias
//    private val MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 201
//    private var usuario = ""
//    private var clave = ""
//    private  lateinit var mUsuario: Usuario
//    private lateinit var configPreferencias: ConfigPreferencias
//    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124
//
//    //    private var configPlataforma: ConfigPlataform? = null
//    private val longitudInputContrasenia = 19
////    @BindView(R.id_usuario_proyecto.relative_layout_progress)
////    internal var relativeLayoutProgress: RelativeLayout? = null
//
//
//    //esta parte sirve para restringir los caracteres especiales de clave
//    private val blockCharacterSet = "´¿¡€/¬£¨<>Ý¥®©¦Ç±÷°"
//    private val filter = InputFilter { source, start, end, dest, dstart, dend ->
//        if (dest.length > longitudInputContrasenia) {
//            return@InputFilter ""
//        } else {
//            if (source != null && blockCharacterSet.contains("" + source)) {
//                return@InputFilter ""
//            }
//        }
//        null
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tvRegistro= findViewById(R.id.tvRegistro)
        tvOlvidoContrasena= findViewById(R.id.tvOlvidoContrasena)
//        etCorreo= findViewById(R.id.etCorreo)
//        etContrasena=findViewById(R.id.etContrasena)
//        relativeLayoutProgress=findViewById(R.id.relative_layout_progress)
//        askPermissions()
//        ButterKnife.bind(this)
//        mPreferencias = Preferencias(this@Login)
////        configStatusBarBlanco() //cuando la barra de estado aparece sin color
//        if (intent.getBooleanExtra("EXIT", false)) {
//            finish()
//        }

//        val i = intent
//        if (i.hasExtra("usuario")) {
//            usuario = intent.getStringExtra("usuario")
//
//        }


        tvRegistro.setOnClickListener {
            val intent = Intent(applicationContext, Registro::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.enter, R.anim.exit)
        }
        tvOlvidoContrasena.setOnClickListener {
            val intent = Intent(applicationContext, LoginCorreo::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.enter, R.anim.exit)
        }
    }
//    fun inicioSesionApi() {
//        usuario = etCorreo.text.toString().trim { it <= ' ' }
//        clave = etContrasena.text.toString().trim { it <= ' ' }
////        if (!camposVacios()) {
//            accesoApp(usuario, clave)
////        }
//    }
//
//    @OnClick(R.id.btnInicioSesion)
//    //    @TargetApi(Build.VERSION_CODES.M)
//    fun iniciarSesion(view: View) {
//            inicioSesionApi()
//    }
//
//    private fun askPermissions() {
//        val permissionsNeeded = ArrayList<String>()
//        val permissionsList = ArrayList<String>()
////        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
////            permissionsNeeded.add("\n- GPS: Para poder agregar tu ubicación al mapa requerimos permisos para acceder al gps.")
//        if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
//            permissionsNeeded.add("\n\n- Llamada: Permiso para realizar llamada telefónica.")
//        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
//            permissionsNeeded.add("\n- Estado del teléfono: Para poder recibir notificaciones requerimos permisos para acceder a los datos del teléfono.")
//        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
//            permissionsNeeded.add("\n- Cámara: Permisos para acceder a la cámara.")
////        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
////            permissionsNeeded.add("\n- Memoria: Permisos para acceder  a la memoria del teléfono.")
//
//        if (permissionsNeeded.size > 0) {
//            // Need Rationale
//            var message = "Necesita darle acceso a: " + permissionsNeeded[0]
//            for (i in 1 until permissionsNeeded.size)
//                message = message + " " + permissionsNeeded[i]
//
//            ActivityCompat.requestPermissions(
//                this@Login, permissionsList.toTypedArray<String>(),
//                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
//            )
//
//        }
//    }
//
//    private fun addPermission(permissionsList: MutableList<String>, permission: String): Boolean {
//        if (ActivityCompat.checkSelfPermission(this, permission) !== PackageManager.PERMISSION_GRANTED) {
//            permissionsList.add(permission)
//            return false
//        }
//        return true
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        when (requestCode) {
//            MY_PERMISSIONS_REQUEST_READ_PHONE_STATE -> {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    inicioSesionApi()
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    val msg = getString(R.string.permisos_necesarios)
//                    showMessageOKCancel(msg,
//                        DialogInterface.OnClickListener { dialog, which ->
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                ActivityCompat.requestPermissions(
//                                    this@Login,
//                                    arrayOf(Manifest.permission.READ_PHONE_STATE),
//                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE
//                                )
//                            }
//                        })
//                }
//                return
//            }
//        }// other 'case' lines to check for other
//        // permissions this app might request
//    }
//    private fun limpiarPreferenciasLogin() {
//        mPreferencias!!.accessToken = ""
//        mPreferencias!!.usuario = ""
//        mPreferencias!!.uidUsuario = ""
//        mPreferencias!!.refreshToken = ""
//    }
//    fun recuperarContrasena(view: View) {
//        val intent = Intent(this, LoginCorreo::class.java)
//        intent.putExtra("usuario", usuario)
//        startActivity(intent)
//        overridePendingTransition(R.anim.enter, R.anim.exit)
//    }
//    private fun cerrarTeclado() {
//        val view = this.currentFocus
//        if (view != null) {
//            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(view.windowToken, 0)
//        }
//    }
//    private fun accesoApp(usuario: String, contrasenia: String) {
//        cerrarTeclado()
//        this.relativeLayoutProgress.visibility = View.VISIBLE
//        try {
//            if (UtilidadesHelper.hasInternet(this@Login)) {
//                val service = RETROFIT_API_BENEFITS.settingService()
//                val login = service.Login(
//                    RETROFIT_API_BENEFITS.getHeaders(),
//                    usuario,
//                    contrasenia,
//                    BuildConfig.CLIENT_ID,
//                    BuildConfig.CLIENT_SECRET
//                )
//                login.enqueue(object : Callback<API_LOGIN> {
//                    override fun onResponse(call: Call<API_LOGIN>, response: Response<API_LOGIN>) {
//                        if (response.isSuccessful) {//200 o 201
//                            mPreferencias!!.accessToken = response.body()!!.data.oauth.access_token //guardo token en mPreferencias
//                            mPreferencias!!.refreshToken = response.body()!!.data.oauth.refresh_token //guardo refreshtoken en mPreferencias
//                            mPreferencias!!.usuario = response.body()!!.data.usuario.email
//                            mPreferencias!!.uidUsuario = UtilidadesHelper.isValidNull(response.body()!!.data.usuario.id_usuario_proyecto)
//                            mPreferencias!!.prefTipoToken = response.body()!!.data.oauth.token_type
//
//                            //valido si la imagen viene en null, si es así guardo vacío
//                            var imageUri = ""
//                            if (response.body()!!.data.usuario.profile.id_persona != null) {
//                                imageUri = response.body()!!.data.usuario.profile.id_persona
//                            }
//
//                            var apiSeguimiento = ""
//                            //valido si el api_seguimiento  viene en null, si es así guardo vacío
//                            if (response.body()!!.data.usuario.id_seguimiento != null) {
//                                apiSeguimiento = response.body()!!.data.usuario.id_seguimiento
//                            }
//
//                            var nombre = ""
//                            if (response.body()!!.data.usuario.profile.telefono!= null) {
//                                nombre = response.body()!!.data.usuario.profile.telefono
//                            }
//
//                            var apellido = ""
//                            if (response.body()!!.data.usuario.profile.fecha_nacimiento != null) {
//                                apellido = response.body()!!.data.usuario.profile.fecha_nacimiento
//                            }
//
//                            var fecha_nacimiento = ""
//                            if (response.body()!!.data.usuario.profile.correo != null) {
//                                fecha_nacimiento = response.body()!!.data.usuario.profile.correo
//                            }
//
//
//                            mUsuario = Usuario(
//                                nombre,
//                                apellido,
//                                response.body()!!.data.usuario.profile.genero,
//                                response.body()!!.data.usuario.profile.foto,
//                                response.body()!!.data.usuario.email,
//                                fecha_nacimiento,
//                                imageUri,
//                                response.body()!!.data.usuario.profile.country.code,
//                                apiSeguimiento,
//                                "", //este campo lo obtengo al entrar al main
//                                response.body()!!.data.usuario.id_usuario_proyecto
//                            )
////                            ControladorUsuario.updatePerfil(mUsuario)
//                            //mPreferencias push - correo (mPreferencias)
//                            if (response.body()!!.data.usuario.preferences != null) {
//                                configPreferencias = ConfigPreferencias(
//                                    response.body()!!.data.usuario.preferences.receive_email,
//                                    response.body()!!.data.usuario.preferences.receive_push
//                                )
////                                ControladorConfigPreferencias.updateConfigPreferencias(configPreferencias)
//                            }
//
////                            var api_map = ""
////                            if (response.body()!!.data.configuraciones.api_map != null) {
////                                api_map = response.body()!!.data.configuraciones.api_map
////                            } else {
////                                api_map = BuildConfig.KEY_MAPAS
////                            }
//
//                            //configuraciones de plataforma
////                            if (response.body()!!.getData().getConfiguraciones() != null) {
////                                configPlataforma = ConfigPlataform(
////                                    response.body()!!.getData().getConfiguraciones().getRequiere_verificacion(),
////                                    response.body()!!.getData().getConfiguraciones().getSpeeding(),
////                                    response.body()!!.getData().getConfiguraciones().getRange_arrival(),
////                                    api_map,
////                                    response.body()!!.getData().getConfiguraciones().getPaymentez_url_server().trim(),
////                                    response.body()!!.getData().getConfiguraciones().getPaymentez_client_app_code().trim(),
////                                    response.body()!!.getData().getConfiguraciones().getPaymentez_client_secret_key().trim(),
////                                    response.body()!!.getData().getConfiguraciones().getPaymentez_server_app_code().trim(),
////                                    response.body()!!.getData().getConfiguraciones().getPaymentez_server_secret_key().trim(),
////                                    response.body()!!.getData().getConfiguraciones().getCoupon_expiration_time(),
////                                    response.body()!!.getData().getConfiguraciones().getCoupon_expiration_message()
////
////                                )
////                                ControladorConfigPlataform.updateConfigPlataform(configPlataforma)
////                            }
////                            finish()
////                            val intent = Intent(applicationContext, MainActivity::class.java)
////                            intent.flags = intent.FLAG_ACTIVITY_NEW_TASK or intent.FLAG_ACTIVITY_CLEAR_TASK
////                            overridePendingTransition(R.anim.enter, R.anim.exit)
////                            startActivity(intent)
//
//                        } else { //Si el API retorna un código HTTP 400 o 500
//                            relativeLayoutProgress.visibility = View.GONE
//                            if (response.code() < 500) {
//                                val error = ErrorUtils.parseError(response)
//                                UtilidadesHelper.mensajeAlerta(
//                                    this@Login,
//                                    error.noticias.titulo,
//                                    error.noticias.mensaje
//                                ).show()
//                                limpiarPreferenciasLogin()
//                            } else {
//                                UtilidadesHelper.mensajeAlerta(
//                                    this@Login, getString(R.string.perfil_advertencia),
//                                    getString(R.string.perfil_error_server), R.drawable.ic_alert_error
//                                ).show()
//                            }
//
//                        }
//                    }
//
//                    override fun onFailure(call: Call<API_LOGIN>, t: Throwable) {
//                        relativeLayoutProgress.visibility = View.GONE
//                        UtilidadesHelper.mensajeAlerta(
//                            this@Login,
//                            getString(R.string.login_title_advertencia),
//                            getString(R.string.login_message_error_server)
//                        ).show()
//                        limpiarPreferenciasLogin()
//                    }
//                })
//            } else {
//                this.relativeLayoutProgress.visibility = View.GONE
//                UtilidadesHelper.mensajeAlerta(
//                    this@Login,
//                    getString(R.string.login_title_advertencia),
//                    getString(R.string.error_internet)
//                ).show()
//            }
//        } catch (e: Exception) {
//            e.message
//            this.relativeLayoutProgress.visibility = View.GONE
//            UtilidadesHelper.mensajeAlerta(
//                this@Login,
//                getString(R.string.login_title_advertencia),
//                e.message,
//                R.drawable.ic_alert_error
//            ).show()
//        }
//
//    }
//
//    override fun onBackPressed() {
//        super.onBackPressed()
//        overridePendingTransition(R.anim.back_enter, R.anim.back_exit)
//    }
//    fun retrocederLoginCorreo(view: View) {
//        val intent = Intent(applicationContext, Login::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//        startActivity(intent)
//        overridePendingTransition(R.anim.back_enter, R.anim.back_exit)
//        finish()
//
//    }
//    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
//        AlertDialog.Builder(this@Login)
//            .setMessage(message)
//            .setPositiveButton(getString(R.string.perfil_Aceptar), okListener)
//            .setNegativeButton(getString(R.string.perfil_cancelar), null)
//            .create()
//            .show()
//    }

}
