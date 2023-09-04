package Vendedor;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;

import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosViewHolder> {

    private final List<Producto> productos;
    public ProductosAdapter(List<Producto> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ProductosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ProductosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductosViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.bind(producto);

        holder.itemView.setOnClickListener(v -> {
            // Acción a realizar al hacer clic en el CardView
            // Redirigir a otra actividad
            Intent intent = new Intent(v.getContext(), vista_producto.class);
            // Pasar datos adicionales a la otra actividad utilizando putExtra()
            intent.putExtra("productoNombre", producto.getNombre());
            intent.putExtra("productoDescripcion", producto.getDescripcion());
            intent.putExtra("productoPrecio", producto.getPrecio());
            intent.putExtra("productoExtra", producto.getExtra());
            intent.putExtra("productoImg", producto.getImagenUrl());

            //Toast.makeText(v.getContext(), producto.getNombre(), Toast.LENGTH_SHORT).show();
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return productos.size();
    }
}
