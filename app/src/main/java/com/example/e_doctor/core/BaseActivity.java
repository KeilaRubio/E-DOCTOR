package com.example.e_doctor.core;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.e_doctor.R;
import com.google.android.material.navigation.NavigationView;


/**
 * Created by app on 16/5/2018.
 */

public class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tvTituloToolbar;
    private DrawerLayout drawer;
    private NavigationView navview;
    private ActionBarDrawerToggle drawerToggle;
    private Preferencias preferencias;
    private Context contexto;

    protected void onCreate(Bundle savedInstanceState, int layout, Context con) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        contexto = con;
        preferencias = new Preferencias(this);
    }

    //agregado para cambiar el theme barra de estado
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void configStatusBar(){
        if(android.os.Build.VERSION.SDK_INT >= 21){
           getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark)); //barra de estado advance
        }
    }

    protected void configStatusBarBlanco(){
        if(android.os.Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(getResources().getColor(R.color.blanco)); //barra de estado advance
        }
    }

    protected void configToolbar(int toolbarId, String toolbarTittle) {
        toolbar = (Toolbar) findViewById(toolbarId);
        tvTituloToolbar = (TextView) findViewById(R.id.text_titulo_toolbar);
        tvTituloToolbar.setText(toolbarTittle);
        toolbar.setNavigationIcon(R.drawable.ic_saeta_izquierda_blanca);
        //toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //color toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        preferencias = new Preferencias(getApplicationContext());
    }



    protected void configToolbar(int toolbarId, String toolbarTittle, boolean displayShow) {
        toolbar = (Toolbar) findViewById(toolbarId);
        toolbar.setNavigationIcon(R.drawable.ic_saeta_izquierda_blanca);
        tvTituloToolbar = (TextView) findViewById(R.id.text_titulo_toolbar);
        tvTituloToolbar.setText(toolbarTittle);
        //toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //color toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(displayShow);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        preferencias = new Preferencias(getApplicationContext());
    }

    protected void configToolbarImage(int toolbarId) {
        toolbar = (Toolbar) findViewById(toolbarId);
        toolbar.setNavigationIcon(R.drawable.ic_saeta_izquierda_blanca);
        //toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //color toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        preferencias = new Preferencias(getApplicationContext());
    }

    protected void setTittleToolbar(String toolbarTittle) {
        tvTituloToolbar.setText(toolbarTittle);
    }

}