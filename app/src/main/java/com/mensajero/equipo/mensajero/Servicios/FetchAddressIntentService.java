package com.mensajero.equipo.mensajero.Servicios;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.mensajero.equipo.mensajero.Constantes.Constantes;
import com.mensajero.equipo.mensajero.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {


    private static final String TAG = "ServiciodeDireccion";
    public ResultReceiver mReceiver;
    private Location location;
    private int requestCode;
    String errorMessage;


    public FetchAddressIntentService() {
        super(TAG);
    }

    // ...
    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constantes.RESULT_DATA_KEY, message);
        bundle.putString(Constantes.MENSAJE_SERVICIO_DIRECCION,errorMessage);
        mReceiver.send(resultCode, bundle);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        mReceiver = intent.getParcelableExtra(Constantes.RECEIVER);
        requestCode = intent.getIntExtra(Constantes.REQUESTCODE, 0);
        Intent bcIntent = new Intent();
        bcIntent.setAction(Constantes.ACTION_FIN);
        bcIntent.putExtra(Constantes.REQUESTCODE,requestCode);

        Intent intentError= new Intent();
        intentError.setAction(Constantes.ACTION_FIN_ERROR);


        // Check if receiver was properly registered.
        if (mReceiver == null) {
            Log.wtf(TAG, "Ningún receptor recibido. No hay ningún lugar para enviar los resultados.");
            return;
        }
        location= intent.getParcelableExtra(
                Constantes.LOCATION_DATA_EXTRA);

        List<Address> addresses = null;

        try {

                addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        // In this sample, get just a single address.
                        1);

        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
            //enviar el mensaje de error a la activity solicitante
            sendBroadcast(intentError);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            //enviar el mensaje de error a la activity solicitante
            sendBroadcast(intentError);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);

        }catch (NullPointerException e){
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            //enviar el mensaje de error a la activity solicitante
            sendBroadcast(intentError);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage== null||errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
                sendBroadcast(intentError);
            }
            deliverResultToReceiver(Constantes.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
                Intent IntentProgss = new Intent();
                IntentProgss.setAction(Constantes.ACTION_PROGRESO);
                IntentProgss.putExtra("progreso", i*10);
                sendBroadcast(IntentProgss);
            }
            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(Constantes.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
            bcIntent.putExtra(Constantes.BD_LAT_DIR_INICIAL,location.getLatitude());
            bcIntent.putExtra(Constantes.BD_LONG_DIR_INICIAL,location.getLongitude());
            sendBroadcast(bcIntent);
        }
}
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent,flags,startId);
    }
}
