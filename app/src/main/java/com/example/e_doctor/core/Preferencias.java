package com.example.e_doctor.core;

import android.content.Context;
import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
import androidx.preference.PreferenceManager;

/**
 * Created by PC-JANINA on 8/4/2018.
 */

public class Preferencias{
    public static SharedPreferences mPreferencias;
    public static SharedPreferences.Editor mEditor;
    public static SharedPreferences pSharedPref;
    public static SharedPreferences.Editor editor;

    //etiquetas para la clave: se usa el nombre del paquete
    private static final String PREF_USUARIO = AppController.getInstance().getPackageName()+".usuario";
    private static final String PREF_TIPO_USUARIO = AppController.getInstance().getPackageName()+".usertype";
    private static final String PREF_REGISTRADO_SERVIDOR = AppController.getInstance().getPackageName()+".registered";
    private static final String PREF_REGID = AppController.getInstance().getPackageName() + ".regid";
    private static final String PREF_AYUDA = AppController.getInstance().getPackageName()+ ".ayuda";
    private static final String ACCESS_TOKEN = AppController.getInstance().getPackageName()+ ".accessToken";
    private static final String REFRESH_TOKEN = AppController.getInstance().getPackageName()+ ".refreshToken";
    private static final String PREF_REGISTRADO_SERVIDOR_MOBILE = AppController.getInstance().getPackageName()+ ".mobile";
    private static final String UID_USUARIO = AppController.getInstance().getPackageName()+ ".uidUsuario";
    private static final String PREF_TIPO_PLAN = AppController.getInstance().getPackageName()+ ".tipoPlan";
    private static final String PREF_TIPO_PLAN_X_BOTON= AppController.getInstance().getPackageName()+ ".tipoPlanBoton";
    private static final String PREF_POLITICAS = AppController.getInstance().getPackageName()+ ".politicas";
    private static final String PREF_TIPO_TOKEN = AppController.getInstance().getPackageName()+ ".tipoToken";
    private static final String PREF_IS_AFILIADO = AppController.getInstance().getPackageName()+ ".afiliado";
    private static final String PREF_NOTICIAS_TITULO = AppController.getInstance().getPackageName()+ ".noticiasTitulo";
    private static final String PREF_NOTICIAS_MENSAJE = AppController.getInstance().getPackageName()+ ".noticiasMensaje";
    private static final String PREF_INICIAR_NOTICIAS = AppController.getInstance().getPackageName()+ ".iniciarNoticias";

    //preferencias nuevas
    private static final String PREF_CORREO_PRE_REGISTRADO = AppController.getInstance().getPackageName()+".correoPreRegistrado";

    public Preferencias(Context context){
        mPreferencias = PreferenceManager.getDefaultSharedPreferences(context);
//        pSharedPref = AppController.getInstance().getSharedPreferences(PREF_ASIST_LAT_LNG, Context.MODE_PRIVATE);
//        editor  = pSharedPref.edit();
    }

    public String getUsuario() {
        return mPreferencias.getString(PREF_USUARIO, "");
    }

    public void setUsuario(String usuario) {
        mEditor = mPreferencias.edit();
        mEditor.putString(PREF_USUARIO, usuario);
        mEditor.commit();
    }

    public String getTipoUsuario() {
        return mPreferencias.getString(PREF_TIPO_USUARIO, "");
    }

    public void setTipoUsuario(String tipoUsuario) {
        mEditor = mPreferencias.edit();
        mEditor.putString(PREF_TIPO_USUARIO, tipoUsuario);
        mEditor.commit();
    }

    public boolean getRegistradoServidor() {
        return mPreferencias.getBoolean(PREF_REGISTRADO_SERVIDOR, false);
    }

    public void setRegistradoServidor(boolean registrado) {
        mEditor = mPreferencias.edit();
        mEditor.putBoolean(PREF_REGISTRADO_SERVIDOR, registrado);
        mEditor.commit();
    }

    public String getRegId() {
        return mPreferencias.getString(PREF_REGID, "");
    }

    public void setRegId(String regid) {
        mEditor = mPreferencias.edit();
        mEditor.putString(PREF_REGID, regid);
        mEditor.commit();
    }


    public boolean getAyuda() {
        return mPreferencias.getBoolean(PREF_AYUDA, true);
    }

    public void setAyuda(boolean ayuda) {
        mEditor = mPreferencias.edit();
        mEditor.putBoolean(PREF_AYUDA, ayuda);
        mEditor.commit();
    }

    public String getAccessToken() {
        return mPreferencias.getString(ACCESS_TOKEN, "");
    }

    public void setAccessToken(String accessToken) {

        mEditor = mPreferencias.edit();
        mEditor.putString(ACCESS_TOKEN, accessToken);
        mEditor.commit();
    }



    public String getRefreshToken() {
        return mPreferencias.getString(REFRESH_TOKEN, "");
    }

    public void setRefreshToken(String refreshToken) {
        mEditor = mPreferencias.edit();
        mEditor.putString(REFRESH_TOKEN, refreshToken);
        mEditor.commit();
    }

    public String getUidUsuario() {
        return mPreferencias.getString(UID_USUARIO,"");
    }

    public void setUidUsuario(String uidUsuario) {
        mEditor = mPreferencias.edit();
        mEditor.putString(UID_USUARIO, uidUsuario);
        mEditor.commit();
    }

    public boolean getRegistradoServidorMobile() {
        return mPreferencias.getBoolean(PREF_REGISTRADO_SERVIDOR_MOBILE, false);
    }

    public void setRegistradoServidorMobile(boolean registrado) {
        mEditor = mPreferencias.edit();
        mEditor.putBoolean(PREF_REGISTRADO_SERVIDOR_MOBILE, registrado);
        mEditor.commit();
    }


    public String getTipoPlan() {
        return mPreferencias.getString(PREF_TIPO_PLAN, "");
    }
    public void setTipoPlan(String tipoServicio) {
        mEditor = mPreferencias.edit();
        mEditor.putString(PREF_TIPO_PLAN, tipoServicio);
        mEditor.commit();
    }

    public String getTipoPlanBoton() {
        return mPreferencias.getString(PREF_TIPO_PLAN_X_BOTON, "");
    }
    public void setTipoPlanBoton(String tipoServicio) {
        mEditor = mPreferencias.edit();
        mEditor.putString(PREF_TIPO_PLAN_X_BOTON, tipoServicio);
        mEditor.commit();
    }



    public String getCorreoPreregistrado() {
        return mPreferencias.getString(PREF_CORREO_PRE_REGISTRADO, "");
    }

    public void setCorreoPreregistrado(String correo) {
        mEditor = mPreferencias.edit();
        mEditor.putString(PREF_CORREO_PRE_REGISTRADO, correo);
        mEditor.commit();
    }



    public String getPoliticas() {
        return mPreferencias.getString(PREF_POLITICAS, "");
    }

    public void setPoliticas(String politicas) {
        mEditor = mPreferencias.edit();
        mEditor.putString(PREF_POLITICAS, politicas);
        mEditor.commit();
    }

    public String getPrefTipoToken() {
        return mPreferencias.getString(PREF_TIPO_TOKEN, "");
    }

    public void setPrefTipoToken(String tipoToken) {
        mEditor = mPreferencias.edit();
        mEditor.putString(PREF_TIPO_TOKEN, tipoToken);
        mEditor.commit();
    }

    public String getIsAfiliado() {
        return mPreferencias.getString(PREF_IS_AFILIADO, "");
    }

    public void setIsAfiliado(String usuario) {
        mEditor = mPreferencias.edit();
        mEditor.putString(PREF_IS_AFILIADO, usuario);
        mEditor.commit();
    }

    public String getPrefNoticiasTitulo() {
        return mPreferencias.getString(PREF_NOTICIAS_TITULO, "");
    }

    public void setPrefNoticiasTitulo(String titulo) {
        mEditor = mPreferencias.edit();
        mEditor.putString(PREF_NOTICIAS_TITULO, titulo);
        mEditor.commit();
    }

    public String getPrefNoticiasMensaje() {
        return mPreferencias.getString(PREF_NOTICIAS_MENSAJE, "");
    }

    public void setPrefNoticiasMensaje(String mensaje) {
        mEditor = mPreferencias.edit();
        mEditor.putString(PREF_NOTICIAS_MENSAJE, mensaje);
        mEditor.commit();
    }

    public String getPrefNoticias() {
        return mPreferencias.getString(PREF_INICIAR_NOTICIAS, "");
    }

    public void setPrefNoticias(String mensaje) {
        mEditor = mPreferencias.edit();
        mEditor.putString(PREF_INICIAR_NOTICIAS, mensaje);
        mEditor.commit();
    }

}
