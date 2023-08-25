package Vendedor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.agenda.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Cliente.Perfil_Activity;
import de.hdodenhof.circleimageview.CircleImageView;

public class Vendedor_Main extends AppCompatActivity {

    private TextView TXTNombreUsuarioVendedor;
    private CircleImageView ImagenUsuarioVendedor;
    private RecyclerView recyclerView;
    private TiendaAdapter adapter;
    private List<TiendaClase> tiendas = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference tiendaRef = database.getReference("Tienda");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor_main);

        ImagenUsuarioVendedor = findViewById(R.id.ImagenUsuarioVendedor);
        TXTNombreUsuarioVendedor = findViewById(R.id.TXTNombreUsuarioVendedor);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String userId = currentUser.getUid();
        recyclerView = findViewById(R.id.recyclerViewVendedor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TiendaAdapter(tiendas);
        recyclerView.setAdapter(adapter);
        Button Btn_EditarTienda = findViewById(R.id.Btn_EditarTienda);
        Button Btn_AgregarProducto = findViewById(R.id.Btn_AgregarProducto);

        Btn_EditarTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Vendedor_Main.this, EditarTiendaForm.class);
                startActivity(intent);
            }
        });

        Btn_AgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Vendedor_Main.this, agregar_producto.class);
                startActivity(intent);
            }
        });

        tiendaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tiendas.clear();
                for (DataSnapshot tiendaSnapshot : dataSnapshot.getChildren()) {
                    String nombre = tiendaSnapshot.child("nombre").getValue(String.class);
                    String descripcion = tiendaSnapshot.child("descripcion").getValue(String.class);
                    String direccion = tiendaSnapshot.child("direccion").getValue(String.class);
                    String extra = tiendaSnapshot.child("extra").getValue(String.class);
                    String usuarioAsociado = tiendaSnapshot.child("usuarioAsociado").getValue(String.class);
                    String imageUrl = tiendaSnapshot.child("imageUrl").getValue(String.class);
                    String tiendaId = tiendaSnapshot.child("id").getValue(String.class);
                    TiendaClase tienda = new TiendaClase(tiendaId, nombre, descripcion, direccion, extra, usuarioAsociado, imageUrl);
                    tiendas.add(tienda);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(userId);
        usuariosRef.child("nombre").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nombreUsuario = dataSnapshot.getValue(String.class);
                    TXTNombreUsuarioVendedor.setText("Bienvenido(a): " + " " + nombreUsuario);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        ImagenUsuarioVendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Vendedor_Main.this, Perfil_Activity.class);
                startActivity(intent);
            }
        });

        usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imageUrl = dataSnapshot.child("imagenPerfil").child("url").getValue(String.class);
                if (imageUrl != null) {
                    Picasso.get().load(imageUrl).into(ImagenUsuarioVendedor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}