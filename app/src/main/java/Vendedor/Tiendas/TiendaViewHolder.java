package Vendedor.Tiendas;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agenda.R;

public class TiendaViewHolder extends RecyclerView.ViewHolder {

    private final TextView txtNombreTienda;
    private final TextView txtDescripcionTienda;
    private final TextView txtDireccionTienda;
    private final TextView txtExtraTienda;
    private final ImageView imagenTienda;

    @SuppressLint("NewApi")
    public TiendaViewHolder(View itemView) {
        super(itemView);

        txtNombreTienda = itemView.findViewById(R.id.TXTView_NombreTienda);
        txtDescripcionTienda = itemView.findViewById(R.id.TXTView_DescripcionTienda);
        txtDireccionTienda = itemView.findViewById(R.id.TXTView_DireccionTienda);
        txtExtraTienda = itemView.findViewById(R.id.TXTView_ExtraTienda);
        imagenTienda = itemView.findViewById(R.id.ImagenTienda);

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Obtener la tienda correspondiente al ViewHolder
                TiendaClase tienda = (TiendaClase) v.getTag();

                // Por ejemplo, mostrar un diálogo con detalles de la tienda
                mostrarDetallesTienda(v.getContext(), tienda);
                return true;
            }
        });

    }

    public void bind(TiendaClase tienda) {
        itemView.setTag(tienda);
        txtNombreTienda.setText(tienda.getNombre());
        txtDescripcionTienda.setText(tienda.getDescripcion());
        txtDireccionTienda.setText(tienda.getDireccion());
        txtExtraTienda.setText(tienda.getExtra());

        Glide.with(imagenTienda.getContext())
                .load(tienda.getImageUrl())
                .into(imagenTienda);
    }

    @SuppressLint("SetTextI18n")
    public void mostrarDetallesTienda(Context context, TiendaClase tienda) {

        // Inflar el diseño del diálogo
        View dialogView = LayoutInflater.from(context).inflate(R.layout.detalles_tienda_dialog, null);

        // Inicializar vistas del diálogo
        ImageView imgTienda = dialogView.findViewById(R.id.imgTienda);
        TextView nombreTienda = dialogView.findViewById(R.id.nombreTienda);
        TextView descripcionTienda = dialogView.findViewById(R.id.descripcionTienda);
        TextView ubicacionTienda = dialogView.findViewById(R.id.ubicacionTienda);
        TextView extraTienda = dialogView.findViewById(R.id.extraTienda);

        // Establecer valores de las vistas con los detalles de la tienda
        nombreTienda.setText(tienda.getNombre());
        descripcionTienda.setText("Descripcion: " +tienda.getDescripcion());
        ubicacionTienda.setText("Ubicacion: " +tienda.getDireccion());
        extraTienda.setText(tienda.getExtra());

        // Cargar imagen de la tienda utilizando Glide o cualquier otra biblioteca
        Glide.with(context)
                .load(tienda.getImageUrl())
                .into(imgTienda);

        // Crear el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

}

