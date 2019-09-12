package com.example.e_doctor.helper;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import com.example.e_doctor.Login;
import com.example.e_doctor.MainActivity;
import com.example.e_doctor.R;
import com.example.e_doctor.core.AppController;
import com.example.e_doctor.core.Preferencias;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by app on 6/4/2018.
 */

public class UtilidadesHelper {
    private static AlertDialog.Builder mensaje;
    public static final String TAG = AppController.class.getSimpleName();
    private static String sistemaOperativo = "Android";
    private static Preferencias mPreferencias;

    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

    public static boolean isTooLarge(TextView text, String newText) {

        float textWidth = text.getPaint().measureText(newText);
        return (textWidth >= text.getMeasuredWidth());
    }

    public static int getScreenDensity(Context context) {
        int response = 0;
        int density = context.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                response = 36;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                response = 48;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                response = 72;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                response = 96;
                break;
            default:
                response = 96;
        }
        return response;
    }

    public static boolean hasInternet(Context context) {

        String service = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(service);

        if ((connectivity.getActiveNetworkInfo() != null)
                && connectivity.getActiveNetworkInfo().isAvailable()
                && connectivity.getActiveNetworkInfo().isConnected()) {
//            Runtime runtime = Runtime.getRuntime();
//            try {
//                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//                int exitValue = ipProcess.waitFor();
//                return (exitValue == 0);
//            }catch (IOException e){
//                e.printStackTrace();
//            }catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return true;
        } else {
            Log.d(TAG, "No network available!");
        }
        return false;

    }

    public static boolean isMyServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service
                    .getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean locationPovidersAvailable(Context context) {
        boolean gpsEnabled = false, netEnabled = false;
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsEnabled = true;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            netEnabled = true;
        }
        if (gpsEnabled || netEnabled) {
            return true;
        }
        return false;

    }

    public static String md5(String input) throws NoSuchAlgorithmException {
        String result = input;
        if (input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while (result.length() < 32) { //40 for SHA-1
                result = "0" + result;
            }
        }
        return result;
    }



    // generates a SHA-1 hash for any string
    public static String getHash(String stringToHash) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] result = null;
        try {
            result = digest.digest(stringToHash.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            sb.append(String.format("%02X", b));
        }
        String messageDigest = sb.toString();
        return messageDigest;
    }



    public static void llamarPantallaPrincipal() {
        /* Aqui se valida si se encuentra logueado y el tipo de perfil*/
        Preferencias preferencias = new Preferencias(AppController.getInstance().getApplicationContext());
        if(!preferencias.getUsuario().equals("")){
            Intent startActivity = new Intent(AppController.getInstance().getApplicationContext(), MainActivity.class);
            startActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            AppController.getInstance().getApplicationContext().startActivity(startActivity);

//            if(preferencias.getTipoPlan().equals("")){
//                Intent startActivity = new Intent(AppController.getInstance().getApplicationContext(), TipoPlanActivity.class);
//                startActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                AppController.getInstance().getApplicationContext().startActivity(startActivity);
//            }
//            else{
//                Intent startActivity = new Intent(AppController.getInstance().getApplicationContext(), MainActivity.class);
//                startActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                AppController.getInstance().getApplicationContext().startActivity(startActivity);
//            }
        }else{
            Intent startActivity = new Intent(AppController.getInstance().getApplicationContext(), Login.class);
            startActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            AppController.getInstance().getApplicationContext().startActivity(startActivity);
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public final static String isValidNull(String target) {
        if (target == null) {
            return "";
        }
        if (target.equals("null")) {
            return "";
        }
        return target;
    }




    public static AlertDialog.Builder mensajeAlerta(Context contexto, String titulo, String alerta) {
        inicializaAlertDialog(contexto);
        mensaje.setTitle(titulo);
        mensaje.setIcon(R.drawable.ic_alert_error);
        mensaje.setMessage(alerta);
        mensaje.setPositiveButton(R.string.helper_btn_aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
            }
        });
        return mensaje;
    }



    public static AlertDialog.Builder mensajeAlerta(Context contexto, String titulo, String msj, int icono) {
        inicializaAlertDialog(contexto);
        mensaje.setTitle(titulo);
        mensaje.setIcon(icono);
        mensaje.setMessage(msj);
        mensaje.setPositiveButton(R.string.helper_btn_aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
            }
        });
        return mensaje;
    }

    public static AlertDialog.Builder mensajeAlertaNoCancelable(Context contexto, String titulo, String msj, int icono) {
        inicializaAlertDialog(contexto);
        mensaje.setTitle(titulo);
        mensaje.setIcon(icono);
        mensaje.setCancelable(false);
        mensaje.setMessage(msj);
        mensaje.setPositiveButton(R.string.helper_btn_aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
            }
        });
        return mensaje;
    }

    public static AlertDialog.Builder mensajeConfirmacion(Context contexto, String alerta) {
        inicializaAlertDialog(contexto);
        mensaje.setTitle(R.string.app_name);
        mensaje.setMessage(alerta);
        mensaje.setPositiveButton(R.string.helper_btn_aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
            }
        });
        return mensaje;
    }

    public static AlertDialog.Builder mensajeConfirmacionNoCancelable(Context contexto, String alerta) {
        inicializaAlertDialog(contexto);
        mensaje.setTitle(R.string.app_name);
        mensaje.setCancelable(false);
        mensaje.setMessage(alerta);
        mensaje.setPositiveButton(R.string.helper_btn_aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
            }
        });
        return mensaje;
    }

    public static AlertDialog.Builder mensajeIndicador(Context contexto, String titulo, String msj) {
        inicializaAlertDialog(contexto);
        mensaje.setTitle(titulo);
        mensaje.setCancelable(false);
        mensaje.setMessage(msj);
        mensaje.setPositiveButton(R.string.helper_btn_aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
            }
        });
        return mensaje;
    }

    public static AlertDialog.Builder mensajeIndicadorNegativo(Context contexto, String titulo, String msj) {
        inicializaAlertDialog(contexto);
        mensaje.setTitle(titulo);
        mensaje.setCancelable(false);
        mensaje.setMessage(msj);
        mensaje.setPositiveButton(R.string.helper_btn_aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
            }
        });
        mensaje.setPositiveButton(R.string.helper_btn_cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        return mensaje;
    }




    public static AlertDialog.Builder mensajeAlerta(Context contexto, String alerta) {
        mensaje.setTitle(AppController.getInstance().getApplicationContext().getString(R.string.app_name));
        mensaje.setIcon(R.drawable.ic_alert_error);
        mensaje.setMessage(alerta);
        mensaje.setPositiveButton(R.string.helper_btn_aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
            }
        });
        return mensaje;
    }

    public static AlertDialog.Builder mensajeInformativo(Context contexto, String titulo, String texto) {
        inicializaAlertDialog(contexto);
        mensaje.setTitle(titulo);
        mensaje.setMessage(texto);
        mensaje.setPositiveButton(R.string.helper_btn_aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
            }
        });

        return mensaje;
    }

    public static AlertDialog.Builder mensajeConfirmacionCancelacion(Context contexto, String titulo, String texto) {
        inicializaAlertDialog(contexto);
        mensaje.setTitle(titulo);
        mensaje.setMessage(texto);
        mensaje.setPositiveButton(R.string.helper_btn_aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
            }
        });
        mensaje.setNegativeButton(R.string.helper_btn_cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        return mensaje;
    }



    public static void inicializaAlertDialog(Context contexto) {
        //Control de Versiones en Estilos del Dialogo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mensaje = new AlertDialog.Builder(contexto, R.style.AlertDialogCustomTheme);
        } else {
            mensaje = new AlertDialog.Builder(contexto);
        }
    }

    public static String getFecha(){
        Date date = new Date();
        String fecha = String.valueOf((date.getYear()+1900))+"-"+ String.valueOf(date.getMonth()+1)+"-"+ String.valueOf(date.getDate());
        String hora = String.valueOf(date.getHours())+":"+ String.valueOf(date.getMinutes())+":"+ String.valueOf(date.getSeconds());
        String retorna = fecha+" "+hora;
        return retorna;
    }

    public static String letraCapital(String string){
        if(string!=null && string.length()>0)
            string = String.valueOf(string.charAt(0)).toUpperCase() + string.substring(1).toLowerCase();
        return string;
    }

    public static String textoFecha(String fecha){
        //ano-mes-dia --> asi me viene
        String fechaDividida[] = fecha.split("-");

        return fechaDividida[2] + " de " + getNombreMes(Integer.parseInt(fechaDividida[1])) + " del " + fechaDividida[0] ;
    }


    public static String getNombreMes(int mes){
        String nombreMes = "";
        switch (mes){
            case 1:
                nombreMes = "Enero";
                break;
            case 2:
                nombreMes = "Febrero";
                break;
            case 3:
                nombreMes = "Marzo";
                break;
            case 4:
                nombreMes = "Abril";
                break;
            case 5:
                nombreMes = "Mayo";
                break;
            case 6:
                nombreMes = "Junio";
                break;
            case 7:
                nombreMes = "Julio";
                break;
            case 8:
                nombreMes = "Agosto";
                break;
            case 9:
                nombreMes = "Septiembre";
                break;
            case 10:
                nombreMes = "Octubre";
                break;
            case 11:
                nombreMes = "Noviembre";
                break;
            default:
                nombreMes = "Diciembre";
                break;
        }
        return nombreMes;
    }








    //se eliminan todas las tablas al cerrar sesión

}