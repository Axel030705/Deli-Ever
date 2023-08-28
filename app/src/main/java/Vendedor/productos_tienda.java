package Vendedor;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
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

public class productos_tienda extends AppCompatActivity {

    private ProductosAdapter adapter;
    private final List<Producto> productosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_tienda);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProductosAdapter(productosList);
        recyclerView.setAdapter(adapter);

        // Obtén el ID de la tienda desde los extras del Intent
        String tiendaId = getIntent().getStringExtra("tiendaId");

        if (tiendaId != null) {
            DatabaseReference tiendaRef = FirebaseDatabase.getInstance().getReference("Tienda").child(tiendaId);

            tiendaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        DataSnapshot productosSnapshot = dataSnapshot.child("productos");

                        productosList.clear();

                        for (DataSnapshot productoSnapshot : productosSnapshot.getChildren()) {
                            String nombre = productoSnapshot.child("nombre").getValue(String.class);
                            String descripcion = productoSnapshot.child("descripcion").getValue(String.class);
                            String precio = productoSnapshot.child("precio").getValue(String.class);
                            String extra = productoSnapshot.child("extra").getValue(String.class);
                            Producto producto = new Producto(nombre, descripcion, precio, extra);
                            productosList.add(producto);
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), "No se encontraron productos en esta tienda", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No se proporcionó el ID de la tienda", Toast.LENGTH_SHORT).show();
        }
    }
}