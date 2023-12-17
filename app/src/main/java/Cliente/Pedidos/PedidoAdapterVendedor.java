package Cliente.Pedidos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agenda.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PedidoAdapterVendedor extends RecyclerView.Adapter<PedidoAdapterVendedor.PedidoViewHolderV> {

    private static ArrayList<PedidoClase> listaPedidosVendedor;
    private static PedidoAdapterVendedor.OnItemClickListener listenerV;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(PedidoAdapterVendedor.OnItemClickListener listenerV) {
        this.listenerV = listenerV;
    }

    public PedidoClase getPedidoAt(int position) {
        return listaPedidosVendedor.get(position);
    }

    public PedidoAdapterVendedor(ArrayList<PedidoClase> listaPedidosVendedor) {
        this.listaPedidosVendedor = listaPedidosVendedor;
    }

    @NonNull
    @Override
    public PedidoAdapterVendedor.PedidoViewHolderV onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido_vendedor, parent, false);
        return new PedidoAdapterVendedor.PedidoViewHolderV(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoAdapterVendedor.PedidoViewHolderV holder, int position) {
        PedidoClase pedido = listaPedidosVendedor.get(position);
        holder.bind(pedido);

        // Configurar el click listener
        holder.itemView.setOnClickListener(view -> {
            if (listenerV != null) {
                listenerV.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPedidosVendedor.size();
    }

    public static class PedidoViewHolderV extends RecyclerView.ViewHolder {
        private final TextView nombreProductoTextView;
        private final TextView cantidadTextView;
        private final TextView precioTextView;
        private final TextView estadoTextView;
        private final ImageView ImgProductoPedido;
        private final Button Btn_preparacion, Btn_camino, Btn_finalizarPedido;
        private final LinearLayout BotonesVendedor, LayoutFinalizar;

        public PedidoViewHolderV(@NonNull View itemView) {
            super(itemView);
            nombreProductoTextView = itemView.findViewById(R.id.NombreProductoPedido);
            cantidadTextView = itemView.findViewById(R.id.CantidadProductoPedido);
            precioTextView = itemView.findViewById(R.id.PrecioProductoPedido);
            estadoTextView = itemView.findViewById(R.id.EstadoPedido);
            ImgProductoPedido = itemView.findViewById(R.id.ImgProductoPedido);
            Btn_preparacion = itemView.findViewById(R.id.Btn_preparacion);
            Btn_camino = itemView.findViewById(R.id.Btn_camino);
            Btn_finalizarPedido = itemView.findViewById(R.id.Btn_finalizarPedido);
            BotonesVendedor = itemView.findViewById(R.id.BotonesVendedor);
            LayoutFinalizar = itemView.findViewById(R.id.LayoutFinalizar);


            Btn_preparacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Manejar el clic en el botón Btn_preparacion
                    if (listenerV != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Obtener el pedido en la posición actual
                            PedidoClase pedido = listaPedidosVendedor.get(position);

                            // Obtener el ID del pedido
                            String idPedido = pedido.getIdPedido();
                            //Obtener el ID de la tienda
                            String idTienda = pedido.getIdTienda();

                            // Actualizar el estado del pedido a "Preparación" en Firebase
                            DatabaseReference pedidoRef = FirebaseDatabase.getInstance().getReference("Tienda").child(idTienda).child("Pedidos").child(idPedido);
                            pedidoRef.child("estado").setValue("Preparando");
                            // Mostrar Toast
                            Context context = v.getContext();
                            Toast.makeText(context, "Pedido en preparación", Toast.LENGTH_SHORT).show();

                            //Actualizar pedido cliente
                            String idCliente = pedido.getIdCliente();
                            String idPedidoC = pedido.getIdPedido();

                            DatabaseReference clientePedidoRef = FirebaseDatabase.getInstance().getReference("Usuarios")
                                    .child(idCliente)
                                    .child("Pedidos")
                                    .child(idPedido);

                            clientePedidoRef.child("estado").setValue("Preparando");

                        }
                    }
                }
            });

            Btn_camino.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Manejar el clic en el botón Btn_camino
                    if (listenerV != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {

                            // Obtener el pedido en la posición actual
                            PedidoClase pedido = listaPedidosVendedor.get(position);

                            // Obtener el ID del pedido
                            String idPedido = pedido.getIdPedido();
                            //Obtener el ID de la tienda
                            String idTienda = pedido.getIdTienda();

                            // Actualizar el estado del pedido a "Preparación" en Firebase
                            DatabaseReference pedidoRef = FirebaseDatabase.getInstance().getReference("Tienda").child(idTienda).child("Pedidos").child(idPedido);
                            pedidoRef.child("estado").setValue("Camino");
                            // Mostrar Toast
                            Context context = v.getContext();
                            Toast.makeText(context, "Pedido en camino", Toast.LENGTH_SHORT).show();

                            //Actualizar pedido cliente
                            String idCliente = pedido.getIdCliente();
                            String idPedidoC = pedido.getIdPedido();

                            DatabaseReference clientePedidoRef = FirebaseDatabase.getInstance().getReference("Usuarios")
                                    .child(idCliente)
                                    .child("Pedidos")
                                    .child(idPedido);

                            clientePedidoRef.child("estado").setValue("Camino");


                        }
                    }
                }
            });


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
