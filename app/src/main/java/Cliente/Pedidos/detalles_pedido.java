package Cliente.Pedidos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Chat.MainActivityChat;

public class detalles_pedido extends AppCompatActivity {

    //Pedido//
    private PedidoClase pedido;

    //XML//
    ImageView ImgEstado;
    TextView TextoEstado, txt_productos, txt_precio, txt_descuento, txt_envio, txt_precioTotal, txt_direccion;
    LinearLayout LayoutMsj;
    //Variables
    private String idUsr;

    //Firebase
    private DatabaseReference usuarioRef;
    private DatabaseReference pedidoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedido);

        //Obtén el pedido de los extras || Variables
        pedido = (PedidoClase) getIntent().getSerializableExtra("pedido");
        idUsr = pedido.getIdCliente();

        //XML
        ImgEstado = findViewById(R.id.ImgEstado);
        TextoEstado = findViewById(R.id.TextoEstado);
        txt_productos = findViewById(R.id.txt_productos);
        txt_precio = findViewById(R.id.txt_precio);
        txt_descuento = findViewById(R.id.txt_descuento);
        txt_envio = findViewById(R.id.txt_envio);
        txt_precioTotal= findViewById(R.id.txt_precioTotal);
        txt_direccion = findViewById(R.id.txt_direccion);
        LayoutMsj = findViewById(R.id.LayoutMsj);

        //Firebase
        // Referencia al nodo del usuario
        usuarioRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(idUsr);
        // Referencia al nodo de Pedidos dentro del nodo del usuario
        pedidoRef = usuarioRef.child("Pedidos").child(pedido.getIdPedido());

        //Escuchar cambios en el estado del pedido
        pedidoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Actualiza el objeto PedidoClase con los nuevos datos
                    PedidoClase nuevoPedido = dataSnapshot.getValue(PedidoClase.class);

                    // Realiza la actualización de la interfaz de usuario con el nuevo estado
                    if (nuevoPedido != null) {
                        pedido = nuevoPedido;
                        ValidarEstadoPedido();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Maneja errores si es necesario
            }
        });

        LayoutMsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el ID de la tienda asociada al usuario vendedor
                String idTienda = pedido.getIdTienda();
                // Obtener el ID del usuario actual
                String idUsuarioActual = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // Crear una referencia a la tienda en la base de datos
                DatabaseReference tiendaRef = FirebaseDatabase.getInstance().getReference("Tienda").child(idTienda);
                // Realizar la consulta para obtener los IDs de usuario asociados a la sala
                tiendaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Obtener IDs de usuario asociados a la sala
                            String idUsuario2 = dataSnapshot.child("usuarioAsociado").getValue(String.class);

                            DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chat");
                            String idSala = idTienda + "_" + idUsuarioActual;

                            // Crear la sala de chat
                            chatRef.child(idSala).child("usuario1").setValue(idUsuarioActual);
                            chatRef.child(idSala).child("usuario2").setValue(idUsuario2);

                            // Navegar a la actividad de chat
                            Intent intent = new Intent(detalles_pedido.this, MainActivityChat.class);
                            intent.putExtra("salaId", idSala);
                            intent.putExtra("idUsuario1", idUsuarioActual);
                            intent.putExtra("idUsuario2", idUsuario2);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Manejar errores de la consulta
                    }
                });
            }
        });

        InformacionPedido();
    }

    @SuppressLint("SetTextI18n")
    private void ValidarEstadoPedido() {
        if (pedido != null) {
            if (pedido.getEstado().equals("Pendiente")) {
                TextoEstado.setText("Tu pedido está esperando a ser aceptado por la tienda");
                ImgEstado.setImageResource(R.drawable.svg1);
            } else if (pedido.getEstado().equals("Preparando")) {
                TextoEstado.setText("Tu pedido está en preparación");
                ImgEstado.setImageResource(R.drawable.svg2);
            } else if (pedido.getEstado().equals("Camino")) {
                TextoEstado.setText("Tu pedido está en camino no te desesperes");
                ImgEstado.setImageResource(R.drawable.svg3);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void InformacionPedido(){

        //Setear la cantidad de productos y el precio de ellos
        txt_productos.setText("Productos ("+ pedido.getCantidad() +")");
        txt_precio.setText("$ "+pedido.getMonto());
        //Validar si tiene descuento
        if(pedido.getDescuento().equals("Ninguno")){
            int colorRojo = getResources().getColor(R.color.red);
            txt_descuento.setTextColor(colorRojo);
            txt_descuento.setText("Ninguno");
        }else{
            int colorVerde = getResources().getColor(R.color.green);
            txt_descuento.setTextColor(colorVerde);
            txt_descuento.setText("- $ "+pedido.getDescuento());
        }
        //Validar si tiene envio gratis
        /*if(pedido.getEnvio.equals("Gratis")){
            int colorVerde = getResources().getColor(R.color.green);
            txt_envio.setTextColor(colorVerde);
            txt_envio.setText(pedido.getEnvio);
        }else{

        }*/
        //Precio total - Descuento
        if(pedido.getDescuento().equals("Ninguno")){
            txt_precioTotal.setText("$ "+pedido.getMonto());
        }else{
            double monto = Double.parseDouble(pedido.getMonto());
            double descuento = Double.parseDouble(pedido.getDescuento());
            double precioTotal = monto - descuento;
            String precioTotalString = String.format("$ %.2f", precioTotal);
            txt_precioTotal.setText(precioTotalString);
        }
        //Setear la ubicación
        txt_direccion.setText(pedido.getDireccion());
    }

}