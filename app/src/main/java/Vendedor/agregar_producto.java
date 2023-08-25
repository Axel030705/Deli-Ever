package Vendedor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class agregar_producto extends AppCompatActivity {

    private DatabaseReference tiendaRef;
    private DatabaseReference productosRef;
    private EditText txtNombreProducto, txtDescripcionProducto, txtPrecioProducto, txtExtraProducto;
    private Button btnGuardarProducto, btnSalir;
    private CircleImageView imgProducto;
    private String usuarioId;
    private static final int REQUEST_IMAGE_PICK = 1;
    private Uri imagenSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        // Obtener referencia a la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tiendaRef = database.getReference("Tienda");

        // Obtener el usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            usuarioId = currentUser.getUid();
            cargarTienda();
        } else {
            // Manejar el caso en el que el usuario no esté autenticado
        }

        // Inicializar vistas
        txtNombreProducto = findViewById(R.id.txt_NombreProducto);
        txtDescripcionProducto = findViewById(R.id.txt_DescripcionProducto);
        txtPrecioProducto = findViewById(R.id.txt_PrecioProducto);
        txtExtraProducto = findViewById(R.id.txt_ExtraProducto);
        btnGuardarProducto = findViewById(R.id.Btn_GuardarProducto);
        btnSalir = findViewById(R.id.Btn_Salir);
        imgProducto = findViewById(R.id.ImagenProducto);
        imgProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(agregar_producto.this, Vendedor_Main.class);
                startActivity(intent);
                finish();
            }
        });

        configurarGuardarProducto();
    }

    private void cargarTienda() {
        tiendaRef.orderByChild("usuarioAsociado").equalTo(usuarioId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot tiendaSnapshot = dataSnapshot.getChildren().iterator().next();
                    String tiendaId = tiendaSnapshot.getKey();
                    if (tiendaId != null) {
                        DatabaseReference tiendaUsuarioRef = tiendaRef.child(tiendaId);
                        productosRef = tiendaUsuarioRef.child("productos");
                    } else {
                        // Manejar el caso en el que no se encuentre la tienda del usuario
                    }
                } else {
                    // Manejar el caso en el que no se encuentre la tienda del usuario
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar el error de la consulta
            }
        });
    }

    private void configurarGuardarProducto() {
        btnGuardarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarProducto();
            }
        });
    }

    private void guardarProducto() {
        // Obtener los valores ingresados por el vendedor
        String nombreProducto = txtNombreProducto.getText().toString();
        String descripcionProducto = txtDescripcionProducto.getText().toString();
        String precioProducto = txtPrecioProducto.getText().toString();
        String extraProducto = txtExtraProducto.getText().toString();

        // Validar que se hayan ingresado valores para los campos obligatorios
        if (TextUtils.isEmpty(nombreProducto) || TextUtils.isEmpty(descripcionProducto) || TextUtils.isEmpty(precioProducto)) {
            Toast.makeText(agregar_producto.this, "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si se ha seleccionado una imagen
        if (imagenSeleccionada == null) {
            Toast.makeText(agregar_producto.this, "Por favor, selecciona una imagen del producto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un objeto Producto con los valores ingresados
        Producto producto = new Producto(nombreProducto, descripcionProducto, precioProducto, extraProducto);

        // Generar un nuevo identificador único para el producto
        String nuevoProductoId = productosRef.push().getKey();

        // Subir la imagen a Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("productos").child(nuevoProductoId);
        storageRef.putFile(imagenSeleccionada)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Obtener la URL de la imagen subida
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imagenUrl = uri.toString();
                                // Agregar la URL de la imagen al objeto Producto
                                producto.setImagenUrl(imagenUrl);
                                // Guardar el producto en la base de datos bajo el identificador generado
                                productosRef.child(nuevoProductoId).setValue(producto)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(agregar_producto.this, "Producto agregado exitosamente", Toast.LENGTH_SHORT).show();
                                                limpiarCampos();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(agregar_producto.this, "Error al agregar el producto", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(agregar_producto.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void limpiarCampos() {
        int colorAzulCielo = R.color.azulCielo;
        txtNombreProducto.setText("");
        txtDescripcionProducto.setText("");
        txtPrecioProducto.setText("");
        txtExtraProducto.setText("");
        imgProducto.setImageResource(colorAzulCielo);
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            imagenSeleccionada = data.getData();
            imgProducto.setImageURI(imagenSeleccionada);
        }
    }
}