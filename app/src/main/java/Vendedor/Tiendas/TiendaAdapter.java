package Vendedor.Tiendas;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;

import java.util.List;

import Vendedor.Productos.productos_tienda;

public class TiendaAdapter extends RecyclerView.Adapter<TiendaViewHolder> {
    private final List<TiendaClase> tiendas;

    public TiendaAdapter(List<TiendaClase> tiendas) {
        this.tiendas = tiendas;
    }

    @NonNull
    @Override
    public TiendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tienda, parent, false);
        return new TiendaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TiendaViewHolder holder, int position) {
        TiendaClase tienda = tiendas.get(position);
        holder.bind(tienda);

        // Establecer el OnClickListener en el CardView
        holder.itemView.setOnClickListener(v -> {
            // Acción a realizar al hacer clic en el CardView
            // Redirigir a otra actividad
            Intent intent = new Intent(v.getContext(), productos_tienda.class);
            // Puedes pasar datos adicionales a la otra actividad utilizando putExtra()
            intent.putExtra("tiendaId", tienda.getId()); // Ejemplo de pasar el id de la tienda
            //Toast.makeText(v.getContext(), tienda.getNombre(), Toast.LENGTH_SHORT).show();
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tiendas.size();
    }

}
