package com.example.e_doctor

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import butterknife.ButterKnife
import com.example.e_doctor.core.BaseActivity
import com.example.e_doctor.core.Preferencias
import com.example.e_doctor.helper.UtilidadesHelper
import com.example.e_doctor.retrofit.error.ErrorUtils
import com.example.e_doctor.retrofit.modelos.API_RECUPERAR_CONTRASENA_LOGIN
import com.example.e_doctor.retrofit.modelos.API_REGISTRO_PERFIL
import com.example.e_doctor.retrofit.service.RETROFIT_API_BENEFITS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecuperarContrasenaCorreo : BaseActivity() {
    lateinit var relativeLayoutProgress: RelativeLayout
    lateinit var mTextErrorCorreo: TextView
    lateinit var mEditTextCorreo: EditText

    private var efectoPress = AlphaAnimation(3f, 0.8f)
    private lateinit var mPreferencias:Preferencias
    lateinit var correo: String
    private var api:String = "API_MOBILE"
    private var usuario:String = ""
    private val longitudInputCorreo:Int = 120
    private val MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:Int = 201
    private lateinit var data: API_RECUPERAR_CONTRASENA_LOGIN


    //esta parte sirve para restringir los caracteres especiales de clave
    private val blockCharacterSet = "~#^|$%&*!,;:{}()´'¿?¡€/¬+£=¨[]<>Ý¥®©¦Ç±÷°ABCDEFGHIJKLMNOPQRSTUVWXYZÑ"
    private val filter = InputFilter { source, start, end, dest, dstart, dend ->
        if (dest.length > longitudInputCorreo) {
            return@InputFilter ""
        } else {
            if (source != null && blockCharacterSet.contains("" + source)) {
                return@InputFilter ""
            }
        }

        null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_correo)
        relativeLayoutProgress=findViewById(R.id.relative_layout_progress)
        mTextErrorCorreo=findViewById(R.id.text_view_error_correo_vacio)
        mEditTextCorreo=findViewById(R.id.edit_text_codigo_confirmacion)

        ButterKnife.bind(this)
        configStatusBarBlanco() //cuando la barra de estado aparece sin color
        mPreferencias = Preferencias(this@RecuperarContrasenaCorreo)

        val i = intent
        if (i.hasExtra("usuario")) {
            usuario = intent.getStringExtra("usuario")
            mEditTextCorreo.setText(usuario)
        }

        mEditTextCorreo.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        mEditTextCorreo.filters = arrayOf(filter)

    }
    private fun cerrarTeclado() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun retrocederPantallaBienvenida(view: View) {
        val intent = Intent(applicationContext, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        overridePendingTransition(R.anim.back_enter, R.anim.back_exit)
        finish()
    }

    fun enviarPinActivacion(view: View){
        val intent = Intent(applicationContext, OlvidastesConfirmarPin::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        overridePendingTransition(R.anim.enter, R.anim.exit)
    }
    private fun armarRespuesta() {
        data= API_RECUPERAR_CONTRASENA_LOGIN(
            email = correo,
            client_id =  BuildConfig.CLIENT_ID,
            client_secret = BuildConfig.CLIENT_SECRET
            )


        solicitarCodigoRecuperacion(correo)
       // comprobarCorreo()

//        data.email(correo)
//        data.password(clave)
//        data.password_confirmation(claveConfirmar)
//        data.nombres(nombre)
//        data.foto(fotoUsuario)
//        data.extras(extras)
    }


    private fun solicitarCodigoRecuperacion(correo: String) {
        cerrarTeclado()

        this.relativeLayoutProgress.visibility = View.VISIBLE
        try {
            if (UtilidadesHelper.hasInternet(this@RecuperarContrasenaCorreo)) {
                val service = RETROFIT_API_BENEFITS.settingService()
                val recuperar_correo = service.RecuperarContraseniaCorreo(
                    RETROFIT_API_BENEFITS.getHeaders(),data
                )
                recuperar_correo.enqueue(object : Callback<API_REGISTRO_PERFIL> {
                    override fun onResponse(call: Call<API_REGISTRO_PERFIL>,response: Response<API_REGISTRO_PERFIL>
                    ) {
                        if (response.isSuccessful) {
                            relativeLayoutProgress.visibility = View.GONE
                            llamarPantallaNueva(OlvidastesConfirmarPin::class.java)
                        } else { //Si el API retorna un código HTTP 400 o 500
                            relativeLayoutProgress.visibility = View.GONE
                            if (response.code() < 500) {
                                val error = ErrorUtils.parseError(response)
                                when (error.estado) {
                                    "404" -> {
                                        //no existe el usuario
                                        Log.e(api, "No existe el usuario")
                                        UtilidadesHelper.mensajeAlerta(
                                            this@RecuperarContrasenaCorreo,
                                            error.noticias.titulo, error.noticias.mensaje
                                        )
                                            .setPositiveButton(
                                                getString(R.string.registro_correo_ok)
                                            ) { dialog, id -> llamarPantallAnterior(Login::class.java) }
                                            .show()
                                    }
                                    "402" -> {
                                        //El usuario ha registrado su correo y ha confirmado su pin de validación, pero no ha creado su perfil
                                        Log.e(api, "No existe el usuario")
                                        mPreferencias.correoPreregistrado = correo
                                        finish()
                                        llamarPantallaNueva(Registro::class.java)
                                    }
                                    "409" -> {
                                        //Hubo un error al guardar en base de datos
                                        Log.e(api, "No existe el usuario")
                                        UtilidadesHelper.mensajeAlerta(
                                            this@RecuperarContrasenaCorreo,
                                            error.noticias.titulo, error.noticias.mensaje
                                        )
                                            .setPositiveButton(getString(R.string.registro_correo_ok)) { dialog, id ->
                                                finish()
                                                llamarPantallAnterior(Login::class.java)
                                            }.show()
                                    }

                                    "403" -> {
                                        //Hubo un error al enviar el correo
                                        Log.e(api, "Hubo un error al enviar el correo")
                                        UtilidadesHelper.mensajeAlerta(
                                            this@RecuperarContrasenaCorreo,
                                            error.noticias.titulo, error.noticias.mensaje
                                        )
                                            .setPositiveButton(getString(R.string.registro_correo_ok)) { dialog, id ->
                                                finish()
                                                llamarPantallAnterior(Login::class.java)
                                            }.show()
                                    }
                                    else -> UtilidadesHelper.mensajeAlerta(
                                        this@RecuperarContrasenaCorreo,
                                        error.noticias.titulo, error.noticias.mensaje
                                    )
                                        .setPositiveButton(getString(R.string.registro_correo_ok)) { dialog, id ->
                                            finish()
                                            llamarPantallAnterior(Login::class.java)
                                        }.show()
                                }
                            } else {
                                UtilidadesHelper.mensajeAlerta(
                                    this@RecuperarContrasenaCorreo,
                                    getString(R.string.perfil_advertencia),
                                    getString(R.string.perfil_error_server), R.drawable.ic_alert_error
                                ).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<API_REGISTRO_PERFIL>, t: Throwable) {
                        relativeLayoutProgress.visibility = View.GONE
                        mPreferencias.correoPreregistrado = ""
                        UtilidadesHelper.mensajeAlerta(
                            this@RecuperarContrasenaCorreo,
                            getString(R.string.login_title_advertencia),
                            getString(R.string.login_message_error_server)
                        ).show()
                    }
                })
            } else {
                this.relativeLayoutProgress.visibility = View.GONE
                UtilidadesHelper.mensajeAlerta(
                    this@RecuperarContrasenaCorreo,
                    getString(R.string.login_title_advertencia),
                    getString(R.string.error_internet)
                ).show()
            }
        } catch (e: Exception) {
            e.message
            this.relativeLayoutProgress.visibility = View.GONE
            UtilidadesHelper.mensajeAlerta(
                this@RecuperarContrasenaCorreo,
                getString(R.string.cambiar_contrasenia_advertencia),
                e.message,
                R.drawable.ic_alert_error
            ).show()
        }

    }


    private fun llamarPantallAnterior(clase: Class<*>) {
        val intent = Intent(applicationContext, clase)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }



    //llama pantalla nueva sin cerrar las anteriores
    private fun llamarPantallaNueva(clase: Class<*>) {
        val intent = Intent(this@RecuperarContrasenaCorreo, clase)

        startActivity(intent)
    }

    fun comprobarCorreo(view: View) {
        if (ContextCompat.checkSelfPermission(
                this@RecuperarContrasenaCorreo, Manifest.permission.READ_PHONE_STATE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(
                this@RecuperarContrasenaCorreo,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                MY_PERMISSIONS_REQUEST_READ_PHONE_STATE
            )
        } else {
            Log.e("permiso", "ON!")
//            armarRespuesta()
            comprobarCorreo()
        }
    }

    fun comprobarCorreo() {
        if (!validarDireccionCorreo()) {
            //solicitarCodigoRecuperacion(correo)
            armarRespuesta()
            mTextErrorCorreo.text = ""
        } else {
            mTextErrorCorreo.startAnimation(efectoPress)
        }
    }

    private fun validarDireccionCorreo(): Boolean {
        var retorna = false
        this.correo = mEditTextCorreo.text.toString().trim { it <= ' ' }
        if (correo.isEmpty()) {
            mTextErrorCorreo.visibility = View.VISIBLE
            mTextErrorCorreo.text = getText(R.string.registro_correo_vacio)
            retorna = true
        } else {
            if (!UtilidadesHelper.isValidEmail(correo)) {
                mTextErrorCorreo.visibility = View.VISIBLE
                mTextErrorCorreo.text = getText(R.string.registro_correo_no_valido)
                retorna = true
            } else {
                mTextErrorCorreo.text = ""
            }
        }
        return retorna
    }
    fun retroceder(view: View) {
        onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_PHONE_STATE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    comprobarCorreo()
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    val msg = getString(R.string.permisos_necesarios)
                    showMessageOKCancel(msg,
                        DialogInterface.OnClickListener { dialog, which ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ActivityCompat.requestPermissions(
                                    this@RecuperarContrasenaCorreo,
                                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE
                                )
                            }
                        })
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@RecuperarContrasenaCorreo)
            .setMessage(message)
            .setPositiveButton(getString(R.string.perfil_Aceptar), okListener)
            .setNegativeButton(getString(R.string.perfil_cancelar), null)
            .create()
            .show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.back_enter, R.anim.back_exit)
    }




}
