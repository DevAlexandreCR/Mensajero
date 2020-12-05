package com.mensajero.equipo.mensajero.Constantes;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTaskDistancia extends AsyncTask<String, Void, String> {

    ProgressDialog progressDialog;
    private OnCompleteTaskListener taskListener;

    public DownloadTaskDistancia() {
    }

    public DownloadTaskDistancia(@Nullable ProgressDialog progressDialog, OnCompleteTaskListener taskListener) {
        this.progressDialog = progressDialog;
        this.taskListener = taskListener;
    }

    @Override
protected void onPreExecute() {
        super.onPreExecute();
        }

@Override
protected String doInBackground(String... url) {

        String data = "";

        try {
        data = downloadUrl(url[0]);
        } catch (Exception e) {
        Log.d("ERRORALOBTENERINFODELWS", e.toString());
        }
        return data;
        }

@Override
protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ParserDistancia parserTask = new ParserDistancia(taskListener);

        parserTask.execute(result);

        if(progressDialog != null)progressDialog.dismiss();

        }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creamos una conexion http
            urlConnection = (HttpURLConnection) url.openConnection();

            // Conectamos
            urlConnection.connect();

            // Leemos desde URL
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
            Log.i("error de Conexion", "sin internet");

        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
        }
