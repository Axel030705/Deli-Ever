package Vendedor.Tiendas;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agenda.R;

public class TiendaViewHolder extends RecyclerView.ViewHolder {
    private TextView txtNombreTienda;
    private TextView txtDescripcionTienda;
    private TextView txtDireccionTienda;
    private TextView txtExtraTienda;
    private ImageView imagenTienda;

    public TiendaViewHolder(View itemView) {
        super(itemView);

        txtNombreTienda = itemView.findViewById(R.id.TXTView_NombreTienda);
        txtDescripcionTienda = itemView.findViewById(R.id.TXTView_DescripcionTienda);
        txtDireccionTienda = itemView.findViewById(R.id.TXTView_DireccionTienda);
        txtExtraTienda = itemView.findViewById(R.id.TXTView_ExtraTienda);
        imagenTienda = itemView.findViewById(R.id.ImagenTienda);
    }

    public void bind(TiendaClase tienda) {
        txtNombreTienda.setText(tienda.getNombre().toString());
        txtDescripcionTienda.setText(tienda.getDescripcion().toString());
        txtDireccionTienda.setText(tienda.getDireccion().toString());
        txtExtraTienda.setText(tienda.getExtra().toString());

        Glide.with(imagenTienda.getContext())
                .load(tienda.getImageUrl())
                .into(imagenTienda);
    }
}
