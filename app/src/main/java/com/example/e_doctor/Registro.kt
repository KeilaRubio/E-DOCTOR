package com.example.e_doctor

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputType
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.example.e_doctor.core.BaseActivity
import com.example.e_doctor.core.Config
import com.example.e_doctor.core.Preferencias
import com.example.e_doctor.helper.UtilidadesHelper
import com.example.e_doctor.model.ConfigPreferencias
import com.example.e_doctor.model.Usuario
import com.example.e_doctor.retrofit.error.ErrorUtils
import com.example.e_doctor.retrofit.modelos.API_REGISTRO
import com.example.e_doctor.retrofit.modelos.API_REGISTRO_PERFIL
import com.example.e_doctor.retrofit.modelos.EXTRAS
import com.example.e_doctor.retrofit.service.RETROFIT_API_BENEFITS
import com.github.clans.fab.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class Registro :BaseActivity(){
    @BindView(R.id.relative_clave_1)
    internal lateinit var mRelativeClave1: RelativeLayout
    @BindView(R.id.relative_clave_2)
    internal lateinit var mRelativeClave2: RelativeLayout
    @BindView(R.id.text_input_nombres)
     lateinit var mTilNombre: TextInputLayout
     lateinit var mTilTelefono: TextInputLayout
     lateinit var mTilClave: TextInputLayout
     lateinit var mTilClaveConfirmar: TextInputLayout
     lateinit var mEditNombre: EditText
     lateinit var mEditTelefono: EditText
    lateinit var mEditClave: EditText //ya
    lateinit var mEditClaveConfirmar: EditText
     lateinit var mEditCorreo: EditText //ya
     lateinit var relativeLayoutProgress: RelativeLayout
    @BindView(R.id.circle_image_foto_usuario)
    internal lateinit var mFotoUsuario: CircleImageView
    @BindView(R.id.image_ic_camara)
    internal lateinit var mFotoDefault: ImageView
//    private lateinit var data: API_REGISTRO
    private lateinit var data: API_REGISTRO
    private lateinit var mDateFormatter: SimpleDateFormat//datepick para fecha de nacimiento
    private lateinit var outputFileUri: Uri
    private lateinit var bitmap: Bitmap
    private val widthMax = 600.0
    private lateinit var baos: ByteArrayOutputStream
    private var nombre =""
    private var telefono = ""
    private var clave = ""
    private var claveConfirmar = ""
    private var correo = ""
    private lateinit var mPreferencias: Preferencias
    private lateinit var mUsuario: Usuario
    private lateinit var mLinearToolbarContent: LinearLayout
    private var existeFotoSeleccionada = false
    lateinit var floatingRegistrar: FloatingActionButton
    lateinit var imageAtras:ImageView

    //enviar respuesta
    private val extras=EXTRAS(fecha_nacimiento = "2003-11-11",telefono = "0992426763",genero = "M")

    //foto usuario
    private val PICK_IMAGE_REQUEST = 100
    private val CAMERA_TAKE_REQUEST = 200
    private val ALL_PERMISSIONS_RESULT = 101
    internal lateinit var file: File
    internal lateinit var uri: Uri
    private var context: Context? = null
    private var activity: Activity? = null


    internal lateinit var permissionsToRequest: ArrayList<String>
    internal var permissionsRejected = ArrayList<String>()
    private var pictureImagePath = ""
    private var pictureDirPath:String? = null
    private var imgFile: File? = null
    private lateinit var dirFile:File
    private val longitudInputContrasenia = 19
    private val mCorreo = ""
    private var fotoUsuario: String =""
    private var imagenCodificada64 = ""

    //esta parte sirve para restringir los caracteres especiales de clave
    private val blockCharacterSet = "´¿¡€/¬£¨<>Ý¥®©¦Ç±÷°"
    private val filter = InputFilter { source, start, end, dest, dstart, dend ->
        if (dest.length > longitudInputContrasenia) {
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
        setContentView(R.layout.activity_registro)
        ButterKnife.bind(this)
        configStatusBar()
        mPreferencias = Preferencias(this)
        context = this
        activity = this@Registro
        mLinearToolbarContent = findViewById<LinearLayout>(R.id.linear_toolbar)
        super.configToolbar(R.id.toolbar, getString(R.string.perfil_titulo), false)
        mLinearToolbarContent.gravity = Gravity.CENTER
        mEditCorreo=findViewById(R.id.edit_correo_usuario)
        mEditClave=findViewById(R.id.edit_clave_usuario)
        mEditClaveConfirmar=findViewById(R.id.edit_clave_confirmar_usuario)
        floatingRegistrar=findViewById(R.id.floating_button_registrar)
        //restringe caract especiales
        caracteresBloqueados()
        mEditNombre= findViewById(R.id.edit_nombre_usuario)
        mEditTelefono=findViewById(R.id.edit_telefono_usuario)
        mTilTelefono=findViewById(R.id.text_input_telefono)
        mTilClave=findViewById(R.id.text_input_clave)
        mTilClaveConfirmar=findViewById(R.id.text_input_clave_confirmar)
        relativeLayoutProgress=findViewById(R.id.relative_layout_progress)
        imageAtras=findViewById(R.id.image_atras)
        floatingRegistrar.setOnClickListener {
            registrarse()
        }
        imageAtras.setOnClickListener {
            retrocederPantallaBienvenida()
        }
    }
    private fun caracteresBloqueados() {
        //restringe caract especiales
        mEditClave.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        mEditClave.filters = arrayOf(filter)

        mEditClaveConfirmar.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        mEditClaveConfirmar.filters = arrayOf(filter)

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick(R.id.circle_image_foto_usuario)
    fun mostrarFoto() {
        mostrarOpcionesFotoPerfil()
    }

    @OnClick(R.id.relative_add_imagen)
    fun cambiarFotoPerfil() {
        mostrarOpcionesFotoPerfil()
    }

    private fun mostrarOpcionesFotoPerfil() {
    val pictureDialog = AlertDialog.Builder(this)
    pictureDialog.setTitle(getString(R.string.perfil_establecer_foto_perfil_seleccionar_foto))
    val pictureDialogItems:Array<String>
    if (existeFotoSeleccionada)
    {
    pictureDialogItems = arrayOf(getString(R.string.perfil_establecer_foto_perfil_dialogo_galeria), getString(R.string.perfil_establecer_foto_perfil_dialogo_camara), getString(R.string.perfil_establecer_foto_perfil_dialogo_eliminar_foto))
    }
    else
    {
    pictureDialogItems = arrayOf(getString(R.string.perfil_establecer_foto_perfil_dialogo_galeria), getString(R.string.perfil_establecer_foto_perfil_dialogo_camara))
    }

    pictureDialog.setItems(pictureDialogItems
    ) { dialog, which ->
        when (which) {
            0 -> abrirGaleria()
            1 -> abrirCamara()
            2 -> eliminarFoto()
        }
    }
        pictureDialog.show()
    }
    fun retrocederPantallaBienvenida(){
        val intent = Intent(applicationContext, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        overridePendingTransition(R.anim.back_enter, R.anim.back_exit)
        finish()
    }

    //si se cae al eliminar la foto (siendo eliminar lo primero que hago en registro)
    //puede ser porque en actualizaFoto estoy llamando a   mUsuario = ControladorUsuario.getUsuario();
    //y aun no tengo un usuario registrado
    fun eliminarFoto() {
        //actualizarFotoBase("");
        imagenCodificada64 = ""
        mFotoUsuario.visibility = View.GONE
        mFotoDefault.visibility = View.VISIBLE
    }
    private fun armarRespuesta() {
        data= API_REGISTRO(
            email = correo,
            password = clave,
            password_confirmation = claveConfirmar,
            foto = fotoUsuario,
            nombres =nombre,
            client_id =  BuildConfig.CLIENT_ID,
            client_secret = BuildConfig.CLIENT_SECRET,
            extras = extras)
        registrarPerfil()
//        data.email(correo)
//        data.password(clave)
//        data.password_confirmation(claveConfirmar)
//        data.nombres(nombre)
//        data.foto(fotoUsuario)
//        data.extras(extras)
    }

//    fun actualizarFotoBase(imgString64: String) {
//        //ControladorUsuario.updateFotoPerfil(imgString64); //guardo en bdb la img String 64
//        //mUsuario = ControladorUsuario.getUsuario();
//
//    }

    fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun abrirCamara() {
        val permissions = ArrayList<String>()
        permissions.add(Manifest.permission.CAMERA)
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        permissionsToRequest = findUnAskedPermissions(permissions)

        if (checkCameraExists()) {
            if (permissionsToRequest.size > 0) {
                Log.e("permi", "entro aqui 1")
                requestPermissions(
                    permissionsToRequest.toTypedArray<String>(),
                    ALL_PERMISSIONS_RESULT
                )
            } else {
                Log.e("permi", "entro aqui 2")

                Toast.makeText(context, getString(R.string.registro_perfil_camara_disponible), Toast.LENGTH_LONG).show()
               // launchCamera()
            }
        } else {
            Toast.makeText(activity, getString(R.string.registro_perfil_camara_no_disponible), Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun checkCameraExists(): Boolean {
        return activity!!.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

//    private fun launchCamera() {
//        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
//        file = File(Environment.getExternalStorageDirectory(), System.currentTimeMillis().toString() + ".png")
//        uri = FileProvider.getUriForFile(this, activity!!.applicationContext.packageName + ".provider", file)
//        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri)
//        startActivityForResult(intent, CAMERA_TAKE_REQUEST)
//    }
//    private fun escalarImagen(b: Bitmap): Bitmap {
//        var b = b
//        val width = java.lang.Double.valueOf(b.width.toDouble())
//        val height = java.lang.Double.valueOf(b.height.toDouble())
//        val newWidth = widthMax
//        val newHeight: Double
//        if (width > newWidth) {
//            newHeight = height / width * newWidth
//            b = Bitmap.createScaledBitmap(b, newWidth.toInt(), newHeight.toInt(), true)
//        }
//        return b
//    }
    private fun comprimirImagen(b: Bitmap): Bitmap {
        baos = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG, 60, baos)
        return b
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        var selectedImage: Uri
//        if (resultCode == RESULT_OK) {
//            when (requestCode) {
//                CAMERA_TAKE_REQUEST -> {
//                    bitmap = BitmapFactory.decodeFile(file.absolutePath)
//                    try {
//                        bitmap = rotarImagen(file.absolutePath,bitmap)
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//
//                    bitmap = escalarImagen(bitmap)
//                    bitmap = comprimirImagen(bitmap)
//                    //remplazo la img de la camara por una menos pesada
//                    reemplazarImgAComprimida(file.absolutePath)
//                    //convierto a base64
//                    encodeTobase64(bitmap)
//
//                    //setea la foto en circleImage
//                    mFotoUsuario!!.setImageBitmap(decodeBase64(imagenCodificada64)) //seteo la foto bitmap
//                    mFotoUsuario!!.setVisibility(View.VISIBLE)
//                    mFotoDefault!!.setVisibility(View.GONE)
//                    existeFotoSeleccionada = true
//                }
//                PICK_IMAGE_REQUEST -> {
//                    if (data!!.data != null) {
//                        selectedImage = data!!.data!!
//                        try {
//                            //Creando el directorio para guardar la foto tomada de la cámara
//                            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//                            val imageFileName = "$timeStamp.png"
//                            val storageDir_1 =
//                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                            pictureImagePath =
//                                storageDir_1.absolutePath + File.separator + Constantes.DIRECTORY_FOLDER + File.separator + imageFileName
//                            imgFile = File(pictureImagePath)
//                            //Crear el directorio Picture en caso de no existir
//                            pictureDirPath = storageDir_1.absolutePath + File.separator + Constantes.DIRECTORY_FOLDER
//                            dirFile = File(pictureDirPath)
//                            if (!dirFile.exists()) {
//                                dirFile.mkdirs()
//                            }
//                            val inputStream = contentResolver.openInputStream(selectedImage)
//                            val outputStream = FileOutputStream(pictureImagePath)
//                            // Copy the bits from instream to outstream
//                            val buf = ByteArray(1024)
//                            var len: Int
//
//                            while ((len == inputStream!!.read(buf)) > 0) {
//                                outputStream.write(buf, 0, len)
//                            }
//
//                            inputStream!!.close()
//                            outputStream.close()
//                        } catch (e: FileNotFoundException) {
//                            e.printStackTrace()
//                        } catch (e: IOException) {
//                            e.printStackTrace()
//                        }
//
//                        try {
//                            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
//                            //                            tratamientoFotoGalery(bitmap);
//                            try {
//                                bitmap = rotarImagen(pictureImagePath, bitmap)
//                            } catch (e: IOException) {
//                                e.printStackTrace()
//                            }
//
//                            bitmap = escalarImagen(bitmap)
//                            bitmap = comprimirImagen(bitmap)
//                            //convierto a base64
//                            encodeTobase64(bitmap)
//
//                            //setea la foto en circleImage
//                            mFotoUsuario!!.setImageBitmap(decodeBase64(imagenCodificada64)) //seteo la foto bitmap
//                            mFotoUsuario!!.setVisibility(View.VISIBLE)
//                            mFotoDefault!!.setVisibility(View.GONE)
//                            existeFotoSeleccionada = true
//                        } catch (e: IOException) {
//                            e.printStackTrace()
//                        }
//
//                    }
//                    Toast.makeText(
//                        this@Registro,
//                        getString(R.string.registro_perfil_accion_cancelada),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    mFotoUsuario!!.visibility = View.GONE
//                    mFotoDefault!!.visibility = View.VISIBLE
//                }
//                0 -> {
//                    Toast.makeText(
//                        this@Registro,
//                        getString(R.string.registro_perfil_accion_cancelada),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    mFotoUsuario!!.visibility = View.GONE
//                    mFotoDefault!!.visibility = View.VISIBLE
//                }
//            }
//        }
//    }
    //................................................................................//
    //convierte la imagen de bitmap a string base 64
    private fun encodeTobase64(image: Bitmap): String {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 90, baos)
        val b = baos.toByteArray()
        imagenCodificada64 = Base64.encodeToString(b, Base64.DEFAULT)
        //        verFotos.setImageBitmap(decodeBase64(imageEncoded));
        return imagenCodificada64
    }

    //convierte la imagen de String base 64 a bitmap
    private fun decodeBase64(input: String): Bitmap {
        val decodedByte = Base64.decode(input, 0)
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
    }


    fun registrarse() {
        this.nombre = mEditNombre.text.toString().trim { it <= ' ' }
        this.telefono = mEditTelefono.text.toString().trim { it <= ' ' }
        this.telefono = mEditTelefono.text.toString().trim { it <= ' ' }
        this.clave = mEditClave.text.toString().trim { it <= ' ' }
        this.claveConfirmar = mEditClaveConfirmar.text.toString().trim { it <= ' ' }
        this.correo = mEditCorreo.text.toString().trim { it <= ' ' }

        if (imagenCodificada64 == "" || imagenCodificada64.length == 0) {
            fotoUsuario = null.toString()
        } else {
            fotoUsuario = imagenCodificada64
        }

        if (!camposObligatorios()) {
           // registrarPerfil()
            armarRespuesta()
        }
    }

    fun camposObligatorios(): Boolean {
        var retorna = false

        if (telefono.isEmpty()) {
            mTilTelefono.setError(getText(R.string.registro_perfil_error_campo_obligatorio))
            retorna = true
        } else
            mTilTelefono.setError("")
        if (clave.isEmpty()) {
            mTilClave.setError(getText(R.string.registro_perfil_error_campo_obligatorio))
            retorna = true
        } else {
            if (validarContrasesenia(clave)) {
                mTilClave.setError(getString(R.string.registro_especificacion_clave_error))
                return true
            } else {
                mTilClave.setError("")
            }
        }
        if (claveConfirmar.isEmpty()) {
            mTilClaveConfirmar.setError(getText(R.string.registro_perfil_error_campo_obligatorio))
            retorna = true
        } else {
            if (validarContrasesenia(claveConfirmar)) {
                mTilClaveConfirmar.setError(getString(R.string.registro_especificacion_clave_error))
                return true
            } else {
                mTilClaveConfirmar.setError("")
            }
        }
        if (clave != claveConfirmar) {
            mTilClaveConfirmar.setError(getString(R.string.registro_coincidencia_contrasena_titulo))
            return true
        }
        return retorna
    }
    fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        //        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=\\S+$).{4,}$"; //esto es para solicitar mayus, minusc, numero
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Za-z])(?=\\S+$).{4,20}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()

    }
    fun validarContrasesenia(clave: String): Boolean {
        if (!isValidPassword(clave) || clave.length < Config.LONGITUD_MINIMO_CLAVE) {
            condicionesClave()
            return true
        } else {
            return false
        }
    }

    //    Guarda la foto en el directorio del dispositivo reemplazando la original tomada con la cámara
    //    por la foto ya escalada y comprimida
//    private fun reemplazarImgAComprimida(pictureImagePath: String) {
//        imgFile = File(pictureImagePath)
//        outputFileUri = Uri.fromFile(imgFile)
//        var fo: FileOutputStream? = null
//        try {
//            imgFile!!.createNewFile()
//            fo = FileOutputStream(imgFile)
//            fo.write(baos!!.toByteArray())
//            fo.close()
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//    }

    private fun findUnAskedPermissions(wanted: ArrayList<String>): ArrayList<String> {

        var result = ArrayList<String>()

        for (perm in wanted) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }

        return result
    }

    private fun hasPermission(permission: String): Boolean {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return checkSelfPermission(permission) === PackageManager.PERMISSION_GRANTED
            }
        }
        return true
    }
    private fun canAskPermission(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }


    @TargetApi(Build.VERSION_CODES.M)
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        Log.e("permi", "1.0")
//
//        when (requestCode) {
//
//            ALL_PERMISSIONS_RESULT -> {
//                for (perms in permissionsToRequest) {
//                    if (!hasPermission(perms)) {
//                        permissionsRejected.add(perms)
//                    }
//                }
//                if (permissionsRejected.size > 0) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (shouldShowRequestPermissionRationale(permissionsRejected[0])) {
//                            val msg = getString(R.string.registro_perfil_camara_permisos_necesarios)
//                            showMessageOKCancel(msg,
//                                DialogInterface.OnClickListener { dialog, which ->
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                                        requestPermissions(
//                                            permissionsRejected.toTypedArray<String>(),
//                                            ALL_PERMISSIONS_RESULT
//                                        )
//                                    }
//                                })
//                            return
//                        }
//                    }
//                } else {
//                    Toast.makeText(
//                        context,
//                        getString(R.string.registro_perfil_camara_permisos_concedidos),
//                        Toast.LENGTH_LONG
//                    ).show()
//                    launchCamera()
//                }
//            }
//        }
//    }
    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(activity)
            .setMessage(message)
            .setPositiveButton(getString(R.string.registro_perfil_ok), okListener)
            .setNegativeButton(getString(R.string.registro_perfil_cancel), null)
            .create()
            .show()
    }
    fun condicionesClave() {
        val factory = LayoutInflater.from(this@Registro)
        val dialogoView = factory.inflate(R.layout.dialogo_condiciones_clave, null)
        val dialogo = AlertDialog.Builder(this@Registro).create()
//        val textLongitudMinima = dialogoView.findViewById(R.id.longitud_caracteres)
        val textLongitudMinima = dialogoView.findViewById<TextView>(R.id.longitud_caracteres)
        textLongitudMinima.setText("Mínimo " + Config.LONGITUD_MINIMO_CLAVE + " caracteres")
        dialogo.setView(dialogoView)
        dialogoView.findViewById<LinearLayout>(R.id.text_view_ok).setOnClickListener(View.OnClickListener { dialogo.dismiss() })
//        dialogoView.findViewById(R.id.text_view_ok).setOnClickListener(View.OnClickListener { dialogo.dismiss() })
        dialogo.show()
    }
    private fun registrarPerfil() {
        cerrarTeclado()
        this.relativeLayoutProgress.visibility = View.VISIBLE
        try {
            if (UtilidadesHelper.hasInternet(this@Registro)) {
                val service = RETROFIT_API_BENEFITS.settingService()
                val regsitrar_perfil = service.RegistrarPerfil(
                    RETROFIT_API_BENEFITS.getHeaders(),data
                )
                regsitrar_perfil.enqueue(object : Callback<API_REGISTRO_PERFIL> {
                    override fun onResponse(call: Call<API_REGISTRO_PERFIL>, response: Response<API_REGISTRO_PERFIL>) {
                        if (response.isSuccessful) {
                            var titulo:String = response.body()!!.noticias.titulo
                            var mensaje:String = response.body()!!.noticias.mensaje

                            UtilidadesHelper.mensajeAlerta(
                                this@Registro,
                                titulo,
                                mensaje)
                                .setPositiveButton(R.string.solicitar_asistencia_btn_aceptar,
                                    DialogInterface.OnClickListener { dialog, which -> })
                                .show()
                            finish()
                            val intent = Intent(applicationContext, MenuPrincipal::class.java)
//                            intent.flags = intent.FLAG_ACTIVITY_NEW_TASK or intent.FLAG_ACTIVITY_CLEAR_TASK
                            overridePendingTransition(R.anim.enter, R.anim.exit)
                            startActivity(intent)

                        } else { //Si el API retorna un código HTTP 400 o 500
                            relativeLayoutProgress!!.visibility = View.GONE
                            if (response.code() < 500) {
                                val error = ErrorUtils.parseError(response)
                                UtilidadesHelper.mensajeAlerta(
                                    this@Registro,
                                    error.noticias.titulo,
                                    error.noticias.mensaje
                                )
                                    .setPositiveButton(R.string.solicitar_asistencia_btn_aceptar,
                                        DialogInterface.OnClickListener { dialog, which -> })
                                    .show()
                            } else {
                                UtilidadesHelper.mensajeAlerta(
                                    this@Registro,
                                    getString(R.string.login_title_advertencia),
                                    getString(R.string.login_message_error_server)
                                ).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<API_REGISTRO_PERFIL>, t: Throwable) {
                        relativeLayoutProgress.visibility = View.GONE
                        UtilidadesHelper.mensajeAlerta(
                            this@Registro,
                            getString(R.string.login_title_advertencia),
                            getString(R.string.login_message_error_server)
                        )
                            .setPositiveButton(R.string.solicitar_asistencia_btn_aceptar,
                                DialogInterface.OnClickListener { dialog, which -> limpiarPreferenciasLogin() })
                            .setCancelable(false)
                            .show()

                    }
                })
            } else {
                this.relativeLayoutProgress!!.visibility = View.GONE
                UtilidadesHelper.mensajeAlerta(
                    this@Registro,
                    getString(R.string.login_title_advertencia),
                    getString(R.string.error_internet)
                ).show()
            }
        } catch (e: Exception) {
            e.message
            this.relativeLayoutProgress.visibility = View.GONE
            UtilidadesHelper.mensajeAlerta(
                this@Registro,
                getString(R.string.cambiar_contrasenia_advertencia),
                e.message,
                R.drawable.ic_alert_error
            ).show()
        }

    }
//    private fun autoLogin() {
//        val intent = Intent(applicationContext, MainActivity::class.java)
//        intent.flags = intent.FLAG_ACTIVITY_NEW_TASK or intent.FLAG_ACTIVITY_CLEAR_TASK
//        intent.putExtra("vieneDeRegistro", true)
//        startActivity(intent)
//    }
    fun limpiarPreferenciasLogin() {
        mPreferencias.accessToken = ""
        mPreferencias.usuario = ""
        mPreferencias.uidUsuario = ""
        mPreferencias.refreshToken = ""
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            cancelarRegistroPerfil()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    fun cancelarRegistroPerfil() {
        UtilidadesHelper.mensajeAlerta(
            this@Registro,
            getString(R.string.registro_aviso),
            getString(R.string.registro_mensaje)
        )
            .setPositiveButton(getString(R.string.registro_ok_entiendo),
                DialogInterface.OnClickListener { dialog, id ->
                    finish()
                    mPreferencias!!.correoPreregistrado = ""
                    intentActivity(Login::class.java)
                })
            .show()
    }
    //cierra las de arriba y muestra una que ya estaba abeierta
    fun intentActivity(clase: Class<*>) {
        val intent = Intent(applicationContext, clase)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        overridePendingTransition(R.anim.back_enter, R.anim.back_exit)
        finish()
    }
    private fun cerrarTeclado() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    @Throws(IOException::class)
//    private fun rotarImagen(photoPath: String, bitmapGaleria: Bitmap): Bitmap {
//        val ei = ExifInterface(photoPath)
//        val orientation = ei.getAttributeInt(
//            ExifInterface.TAG_ORIENTATION,
//            ExifInterface.ORIENTATION_UNDEFINED
//        )
//        Log.e("rojo orientacion", orientation.toString())
//        var rotatedBitmap: Bitmap? = null
//        when (orientation) {
//            ExifInterface.ORIENTATION_ROTATE_90 -> {
//                rotatedBitmap = rotateImage(bitmapGaleria, 90f)
//                Log.e("rojo", "" + 90)
//            }
//            ExifInterface.ORIENTATION_ROTATE_180 -> {
//                rotatedBitmap = rotateImage(bitmapGaleria, 180f)
//                Log.e("rojo", "" + 180)
//            }
//            ExifInterface.ORIENTATION_ROTATE_270 -> {
//                rotatedBitmap = rotateImage(bitmapGaleria, 270f)
//                Log.e("rojo", "" + 270)
//            }
//            ExifInterface.ORIENTATION_NORMAL -> {
//                rotatedBitmap = rotateImage(bitmapGaleria, 360f)
//                Log.e("rojo", "" + 180)
//            }
//            else -> rotatedBitmap = bitmapGaleria
//        }
//        return rotatedBitmap
//    }
    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }


}
