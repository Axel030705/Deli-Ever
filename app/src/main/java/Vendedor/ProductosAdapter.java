package Vendedor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }
}
