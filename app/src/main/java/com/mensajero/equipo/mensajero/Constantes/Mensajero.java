package com.mensajero.equipo.mensajero.Constantes;

/**
 * Created by equipo on 13/09/2017.
 */

        import android.app.Application;
        import android.content.Context;
        import androidx.multidex.MultiDex;

/**
 * Extended application that support multidex
 */
public class Mensajero extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
