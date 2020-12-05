package com.mensajero.equipo.mensajero.Fragmentos;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mensajero.equipo.mensajero.MainActivity;
import com.mensajero.equipo.mensajero.R;

public class Ayuda extends Fragment {

    public static String TAG="Ayuda";
    public View viewroot;
    private TextView texto;


    public Ayuda() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewroot=  inflater.inflate(R.layout.fragment_ayuda, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(TAG);

        View v = viewroot.findViewById(R.id.frame_ayuda);
        v.setPadding(0,MainActivity.tamanoAppBar,0,0);
        texto = viewroot.findViewById(R.id.texto_ayuda);


        return viewroot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
