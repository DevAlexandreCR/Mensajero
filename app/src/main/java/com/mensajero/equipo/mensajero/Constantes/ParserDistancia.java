package com.mensajero.equipo.mensajero.Constantes;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParserDistancia extends AsyncTask<String, Integer, JSONObject> {

    private OnCompleteTaskListener taskListener;

    public String distanciatexto = "";
    public String tiempotexto = "";
    public int distancia = 0;
    public int tiempo = 0;

    public ParserDistancia(OnCompleteTaskListener taskListener) {
        this.taskListener = taskListener;
    }

    public ParserDistancia() {
        super();
    }

    public String getDistanciatexto() {
        return distanciatexto;
    }

    public String getTiempotexto() {
        return tiempotexto;
    }

    public int getDistancia() {
        return distancia;
    }

    public int getTiempo() {
        return tiempo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(JSONObject result) {

        try {
            JSONArray rowsArray = result.getJSONArray("rows");
            Log.i("Jsonelement", rowsArray.toString());
            JSONObject rowsOBJ = rowsArray.getJSONObject(0);
            JSONArray elementArray = rowsOBJ.getJSONArray("elements");
            //para la distancia
            JSONObject elementOBJ = elementArray.getJSONObject(0);
            JSONObject distance = elementOBJ.getJSONObject("distance");
            //para el tiempo
            JSONObject tiempoOBJ = elementOBJ.getJSONObject("duration");

            Log.i("Jsondistance", distance.toString());
            distanciatexto = distance.getString("text");
            distancia = distance.getInt("value");
            tiempotexto = tiempoOBJ.getString("text");
            tiempo = tiempoOBJ.getInt("value");
            taskListener.onCompleteTask(distanciatexto,distancia,tiempotexto,tiempo);
        }catch (Exception e){

        }
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        JSONObject jObject = null;
        try {
            jObject = new JSONObject(strings[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jObject;
    }
}
