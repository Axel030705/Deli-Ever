package Cliente.Pedidos;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agenda.R;

import java.util.ArrayList;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private final ArrayList<PedidoClase> listaPedidos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public PedidoClase getPedidoAt(int position) {
        return listaPedidos.get(position);
    }
    public PedidoAdapter(ArrayList<PedidoClase> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido_cliente, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        PedidoClase pedido = listaPedidos.get(position);
        holder.bind(pedido);

        // Configurar el click listener
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreProductoTextView;
        private final TextView cantidadTextView;
        private final TextView precioTextView;
        private final TextView estadoTextView;
        private final ImageView ImgProductoPedido;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProductoTextView = itemView.findViewById(R.id.NombreProductoPedido);
            cantidadTextView = itemView.findViewById(R.id.CantidadProductoPedido);
            precioTextView = itemView.findViewById(R.id.PrecioProductoPedido);
            estadoTextView = itemView.findViewById(R.id.EstadoPedido);
            ImgProductoPedido = itemView.findViewById(R.id.ImgProductoPedido);
        }

        @SuppressLint("SetTextI18n")
        public void bind(PedidoClase pedido) {
            nombreProductoTextView.setText(pedido.getProducto());
            cantidadTextView.setText("Cantidad comprada: " + pedido.getCantidad());
            precioTextView.setText("Monto: $" + pedido.getMonto());
            estadoTextView.setText("Estado: " + pedido.getEstado());
            Glide.with(ImgProductoPedido.getContext())
                    .load(pedido.getImgProducto())
                    .into(ImgProductoPedido);
        }
    }
}
