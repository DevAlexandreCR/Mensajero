package com.mensajero.equipo.mensajero.Constantes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mensajero.equipo.mensajero.R;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.nikartm.support.ImageBadgeView;

public class AdapterOferta extends RecyclerView.Adapter<AdapterOferta.OfertasViewHolder> {

    public List<Oferta> ofertas;
    Context context;
    ImageBadgeView canasta;
    public List<Oferta> ofertas_agregadas;

    public AdapterOferta(List<Oferta> ofertas, Context context, ImageBadgeView canasta) {
        this.ofertas = ofertas;
        this.context = context;
        this.canasta = canasta;
    }

    @NonNull
    @Override
    public OfertasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_oferta, parent, false);
        OfertasViewHolder holder = new OfertasViewHolder(v);
        ofertas_agregadas =  new ArrayList<>();

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OfertasViewHolder holder, int position) {

        final Oferta oferta = ofertas.get(position);

        View.OnClickListener cantidadlistener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = 1;
                Double nuevo_precio = 0.0;
                switch (view.getId()) {
                    case R.id.mas:
                        cantidad = oferta.getCantidad() + 1;
                        nuevo_precio = (oferta.getPrecio()/ (cantidad-1)) * cantidad;
                        oferta.setPrecio(nuevo_precio);
                        holder.text_cantidad.setText(String.valueOf(cantidad));
                        holder.precio.setText("$ " + nuevo_precio);
                        oferta.setCantidad(cantidad);
                        break;
                    case R.id.menos:
                        if (oferta.getCantidad() > 1) {
                           cantidad = oferta.getCantidad() - 1;
                           nuevo_precio = oferta.getPrecio()- (oferta.getPrecio() / (cantidad + 1 ));
                           oferta.setPrecio(nuevo_precio);
                            holder.precio.setText("$ " + nuevo_precio);
                            holder.text_cantidad.setText(String.valueOf(cantidad));
                            oferta.setCantidad(cantidad);
                        }
                        break;
                }
            }
        };
        holder.texto_titulo.setText(oferta.getTitulo());
        holder.texto_descripcion.setText(oferta.getDescripcion());
        holder.precio.setText("$" + oferta.getPrecio());
        holder.text_cantidad.setText(String.valueOf(oferta.getCantidad()));
        holder.mas.setOnClickListener(cantidadlistener);
        holder.menos.setOnClickListener(cantidadlistener);
        GlideApp.with(context)
                .load(oferta.getUrl_foto_oferta())
                .into(holder.img_oferta);
        holder.check_agregar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Animation animation = AnimationUtils.loadAnimation(context,R.anim.animar_canasta);
                canasta.startAnimation(animation);
                if(b){
                    ofertas_agregadas.add(oferta);
                    Log.i("oferta agregada", "ok "+ oferta.getCantidad());
                    canasta.setBadgeValue(ofertas_agregadas.size());
                } else {
                    ofertas_agregadas.remove(oferta);
                    oferta.setCantidad(1);
                    holder.text_cantidad.setText(String.valueOf(1));
                    canasta.setBadgeValue(ofertas_agregadas.size());
                    if(ofertas_agregadas.size() == 0)canasta.clearBadge();
                    Log.i("oferta removida", "ok "  + oferta.getCantidad());
                }
            }
        });
        holder.img_oferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.check_agregar.setChecked(!holder.check_agregar.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return ofertas.size();
    }

    public static class OfertasViewHolder extends RecyclerView.ViewHolder{

        private TextView texto_titulo, texto_descripcion, precio, text_cantidad;
        private CircleImageView img_oferta;
        private CheckBox check_agregar;
        private RadioButton mas, menos;
        private static final String TAG = "ofertasviewHolder";

        private OfertasViewHolder(@NonNull View itemView) {
            super(itemView);

            texto_titulo = itemView.findViewById(R.id.ver_nombre_oferta);
            texto_descripcion = itemView.findViewById(R.id.ver_descripcion_oferta);
            precio = itemView.findViewById(R.id.ver_precio_oferta);
            img_oferta = itemView.findViewById(R.id.ver_foto_oferta);
            text_cantidad = itemView.findViewById(R.id.text_cantidad);
            check_agregar = itemView.findViewById(R.id.check_agregar);
            mas = itemView.findViewById(R.id.mas);
            menos = itemView.findViewById(R.id.menos);

        }

    }
}
