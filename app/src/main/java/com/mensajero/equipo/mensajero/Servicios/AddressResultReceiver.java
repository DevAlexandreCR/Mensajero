package com.mensajero.equipo.mensajero.Servicios;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.mensajero.equipo.mensajero.Constantes.Constantes;

import static android.content.ContentValues.TAG;

/**
 * Created by equipo on 14/08/2017.
 */

public class AddressResultReceiver extends ResultReceiver {
    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */


    public static String mAddressOutput;
    public static String mensajeError;


    public AddressResultReceiver(Handler handler) {
        super(handler);
    }


    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {


        mAddressOutput = resultData.getString(Constantes.RESULT_DATA_KEY);
        mensajeError=resultData.getString(Constantes.MENSAJE_SERVICIO_DIRECCION);

        Log.i("address", mAddressOutput);
        // Show a toast message if an address was found.
        if (resultCode == Constantes.SUCCESS_RESULT) {
            Log.i(TAG,"direccion encontrada");
        }

    }

}