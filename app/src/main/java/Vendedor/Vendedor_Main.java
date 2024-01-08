package Vendedor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Perfil.Perfil_Activity;
import Vendedor.Productos.agregar_producto;
import Vendedor.Tiendas.EditarTiendaForm;
import Vendedor.Tiendas.TiendaAdapter;
import Vendedor.Tiendas.TiendaClase;
import de.hdodenhof.circleimageview.CircleImageView;

public class Vendedor_Main extends AppCompatActivity {

    private TextView TXTNombreUsuarioVendedor;
    private CircleImageView ImagenUsuarioVendedor;
    private RecyclerView recyclerView;
    private TiendaAdapter adapter;
    private List<TiendaClase> tiendas = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference tiendaRef = database.getReference("Tienda");
    public FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    private String tiendaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor_main);

        ImagenUsuarioVendedor = findViewById(R.id.ImagenUsuarioVendedor);
        TXTNombreUsuarioVendedor = findViewById(R.id.TXTNombreUsuarioVendedor);
        String userId = currentUser.getUid();
        recyclerView = findViewById(R.id.recyclerViewVendedor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TiendaAdapter(tiendas);
        recyclerView.setAdapter(adapter);
        Button Btn_EditarTienda = findViewById(R.id.Btn_EditarTienda);
        Button Btn_AgregarProducto = findViewById(R.id.Btn_AgregarProducto);
        Button Btn_EliminarTienda = findViewById(R.id.Btn_EliminarTienda);
        tiendaId = getIntent().getStringExtra("tiendaId");

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
                intent.putExtra("tiendaId", tiendaId);
                startActivity(intent);
            }
        });

        tiendaRef.orderByChild("usuarioAsociado").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nombreUsuario = dataSnapshot.getValue(String.class);
                    TXTNombreUsuarioVendedor.setText("Bienvenido(a): " + " " + nombreUsuario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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

        Btn_EliminarTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId = currentUser.getUid();

                // Obtén una referencia a la base de datos de Firebase
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

                AlertDialog.Builder builder = new AlertDialog.Builder(Vendedor_Main.this);
                builder.setTitle("Advertencia!")
                        .setMessage("Deseas eliminar la tienda y todo lo relacionado con esta?")
                        .setPositiveButton("Aceptar", (dialog, which) -> {

                            // Busca la tienda asociada al usuario vendedor
                            databaseRef.child("Tienda")
                                    .orderByChild("usuarioAsociado")
                                    .equalTo(userId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot tiendaSnapshot : dataSnapshot.getChildren()) {
                                                String tiendaId = tiendaSnapshot.getKey(); // Obtiene el ID de la tienda
                                                String imageUrl = tiendaSnapshot.child("imageUrl").getValue(String.class); // Obtiene la URL de la imagen de la tienda
                                                imageUrl = imageUrl.trim();

                                                // Ahora que tienes el ID de la tienda y la URL de la imagen, puedes eliminar la tienda
                                                EliminarTienda(tiendaId, imageUrl);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Manejar errores si la búsqueda se cancela
                                        }
                                    });
                        })
                        .show();

            }
        });

    }

    private void EliminarTienda(String tiendaId, String imageUrl) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tiendaRef = databaseRef.child("Tienda").child(tiendaId);

        tiendaRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Tienda eliminada con éxito de la base de datos

                // Eliminar imágenes relacionadas con la tienda desde Firebase Storage
                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);

                // Elimina la imagen directamente
                storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Imagen eliminada con éxito
                        mostrarMensaje("La tienda se ha eliminado con éxito.");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Manejar errores si no se pudo eliminar la tienda de la base de datos
                mostrarMensaje("Error al eliminar la tienda. Inténtalo de nuevo.");
            }
        });
    }




    private void mostrarMensaje(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mensaje")
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    // Redirigir al usuario a una actividad de inicio de sesión o página principal
                    startActivity(new Intent(Vendedor_Main.this, Activity_Vendedor.class));
                    finish(); // Cerrar la actividad
                })
                .show();
    }

}

