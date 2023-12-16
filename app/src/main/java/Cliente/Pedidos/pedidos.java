package Cliente.Pedidos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class pedidos extends AppCompatActivity {
    //XML
    private LinearLayout sinPedidos, conPedidos;
    //Firebase Usuario
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;
    private String userId;
    private DatabaseReference userRef;
    //Variables usuario
    public String nombreUsr;
    // Crear lista de pedidos
    private ArrayList<PedidoClase> listaPedidos = new ArrayList<>();
    private PedidoAdapter pedidoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        //Iniciar Firebase
        FirebaseApp.initializeApp(this);
        //XML
        sinPedidos = findViewById(R.id.sinPedidos);
        conPedidos = findViewById(R.id.conPedidos);

        //Firebase Usuario
        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Usuarios");
        userId = firebaseAuth.getCurrentUser().getUid();
        userRef = usersRef.child(userId);

        //OnclickEnPedido
        RecyclerView recyclerViewPedidos = findViewById(R.id.recyclerViewPedidos);
        pedidoAdapter = new PedidoAdapter(listaPedidos);
        recyclerViewPedidos.setAdapter(pedidoAdapter);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        pedidoAdapter.setOnItemClickListener(new PedidoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Accede al pedido seleccionado usando el adaptador
                PedidoClase pedidoSeleccionado = pedidoAdapter.getPedidoAt(position);

                // Ahora puedes hacer lo que necesitas con el pedido seleccionado
                // Por ejemplo, puedes abrir una nueva actividad y pasar el pedido como extra
                Intent intent = new Intent(getApplicationContext(), detalles_pedido.class);
                intent.putExtra("pedido", pedidoSeleccionado);
                startActivity(intent);
            }
        });

        ValidarPedidosCliente(); //Validar si el usuario tiene pedidos realizados
        ValidarPedidosTienda(); //Validar si la tienda tiene pedidos realizados
    }

    private void ValidarPedidosCliente() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    nombreUsr = dataSnapshot.child("nombre").getValue(String.class);
                    DataSnapshot pedidosSnapshot = dataSnapshot.child("Pedidos");

                    if (pedidosSnapshot.exists()) {
                        sinPedidos.setVisibility(View.GONE);
                        conPedidos.setVisibility(View.VISIBLE);

                        // Limpiar la lista antes de agregar nuevos pedidos
                        listaPedidos.clear();

                        // Obtener información de los pedidos
                        for (DataSnapshot pedidoDataSnapshot : pedidosSnapshot.getChildren()) {
                            String idPedido = pedidoDataSnapshot.child("idPedido").getValue(String.class);
                            String idCliente = pedidoDataSnapshot.child("idCliente").getValue(String.class);
                            String idTienda = pedidoDataSnapshot.child("idTienda").getValue(String.class);
                            String Producto = pedidoDataSnapshot.child("producto").getValue(String.class);
                            String cantidad = pedidoDataSnapshot.child("cantidad").getValue(String.class);
                            String direccion = pedidoDataSnapshot.child("direccion").getValue(String.class);
                            String monto = pedidoDataSnapshot.child("monto").getValue(String.class);
                            String estado = pedidoDataSnapshot.child("estado").getValue(String.class);
                            String fecha_hora = pedidoDataSnapshot.child("fecha_hora").getValue(String.class);
                            String imgProducto = pedidoDataSnapshot.child("imgProducto").getValue(String.class);
                            String nombre_Cliente = pedidoDataSnapshot.child("nombre_Cliente").getValue(String.class);
                            String descuento = pedidoDataSnapshot.child("descuento").getValue(String.class);
                            // Crear objeto Pedido y agregar a la lista
                            PedidoClase pedido = new PedidoClase();
                            pedido.setProducto(Producto);
                            pedido.setCantidad(cantidad);
                            pedido.setMonto(monto);
                            pedido.setEstado(estado);
                            pedido.setImgProducto(imgProducto);
                            pedido.setIdPedido(idPedido);
                            pedido.setIdCliente(idCliente);
                            pedido.setIdTienda(idTienda);
                            pedido.setDireccion(direccion);
                            pedido.setFecha_Hora(fecha_hora);
                            pedido.setNombre_Cliente(nombre_Cliente);
                            pedido.setDescuento(descuento);
                            listaPedidos.add(pedido);
                        }

                        // Notificar al adaptador que los datos han cambiado en el hilo principal de la interfaz de usuario
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pedidoAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void ValidarPedidosTienda() {

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String tipoU = snapshot.child("Tipo de usuario").getValue(String.class);
                    String idTienda = snapshot.child("tiendaId").getValue(String.class);

                    assert tipoU != null;
                    if (tipoU.equals("Vendedor")) {

                        // Crear una referencia a la tienda en la base de datos
                        assert idTienda != null;
                        DatabaseReference tiendaRef = FirebaseDatabase.getInstance().getReference("Tienda").child(idTienda).child("Pedidos");

                        tiendaRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists()) {
                                    sinPedidos.setVisibility(View.GONE);
                                    conPedidos.setVisibility(View.VISIBLE);
                                    // Limpiar la lista antes de agregar nuevos pedidos
                                    listaPedidos.clear();
                                    for (DataSnapshot pedidoSnapshot : snapshot.getChildren()) {
                                        String idPedido = pedidoSnapshot.child("idPedido").getValue(String.class);
                                        String idCliente = pedidoSnapshot.child("idCliente").getValue(String.class);
                                        String idTienda = pedidoSnapshot.child("idTienda").getValue(String.class);
                                        String Producto = pedidoSnapshot.child("producto").getValue(String.class);
                                        String cantidad = pedidoSnapshot.child("cantidad").getValue(String.class);
                                        String direccion = pedidoSnapshot.child("direccion").getValue(String.class);
                                        String monto = pedidoSnapshot.child("monto").getValue(String.class);
                                        String estado = pedidoSnapshot.child("estado").getValue(String.class);
                                        String fecha_hora = pedidoSnapshot.child("fecha_hora").getValue(String.class);
                                        String imgProducto = pedidoSnapshot.child("imgProducto").getValue(String.class);
                                        String nombre_Cliente = pedidoSnapshot.child("nombre_Cliente").getValue(String.class);
                                        String descuento = pedidoSnapshot.child("descuento").getValue(String.class);
                                        // Crear objeto Pedido y agregar a la lista
                                        PedidoClase pedido = new PedidoClase();
                                        pedido.setProducto(Producto);
                                        pedido.setCantidad(cantidad);
                                        pedido.setMonto(monto);
                                        pedido.setEstado(estado);
                                        pedido.setImgProducto(imgProducto);
                                        pedido.setIdPedido(idPedido);
                                        pedido.setIdCliente(idCliente);
                                        pedido.setIdTienda(idTienda);
                                        pedido.setDireccion(direccion);
                                        pedido.setFecha_Hora(fecha_hora);
                                        pedido.setNombre_Cliente(nombre_Cliente);
                                        pedido.setDescuento(descuento);
                                        listaPedidos.add(pedido);
                                    }
                                }
                                // Notificar al adaptador que los datos han cambiado en el hilo principal de la interfaz de usuario
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pedidoAdapter.notifyDataSetChanged();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}

