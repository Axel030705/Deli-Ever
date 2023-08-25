package Vendedor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agenda.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarTiendaForm extends AppCompatActivity {

    private EditText txtNombreTiendaEditar;
    private EditText txtDescripcionTiendaEditar;
    private EditText txtDireccionTiendaEditar;
    private EditText txtExtraTiendaEditar;
    private CircleImageView imageTienda;
    private DatabaseReference tiendaRef;
    private TiendaClase tienda;
    private String imageUrl;
    private String tiendaId;

    private Button Btn_GuardarCambios;

    private boolean nuevaFotoSeleccionada = false;
    private Uri nuevaFotoUri;
    private static final int PICK_IMAGE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tienda_form);

        tiendaRef = FirebaseDatabase.getInstance().getReference("Tienda");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String userId = currentUser.getUid();

        txtNombreTiendaEditar = findViewById(R.id.txt_NombreTiendaEdit);
        txtDescripcionTiendaEditar = findViewById(R.id.txt_DescripcionTiendaEdit);
        txtDireccionTiendaEditar = findViewById(R.id.txt_DireccionTiendaEdit);
        txtExtraTiendaEditar = findViewById(R.id.txt_ExtraTiendaEdit);
        imageTienda = findViewById(R.id.ImagenTiendaEdit);
        Btn_GuardarCambios = findViewById(R.id.Btn_GuardarCambios);

        // Obtén la tienda asociada al usuario actual
        tiendaRef.orderByChild("usuarioAsociado").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Se encontró la tienda del usuario
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        tiendaId = snapshot.getKey();
                        tienda = snapshot.getValue(TiendaClase.class);
                        if (tienda != null) {
                            // Asigna los valores de la tienda a los campos de edición correspondientes
                            txtNombreTiendaEditar.setText(tienda.getNombre());
                            txtDescripcionTiendaEditar.setText(tienda.getDescripcion());
                            txtDireccionTiendaEditar.setText(tienda.getDireccion());
                            txtExtraTiendaEditar.setText(tienda.getExtra());
                            imageUrl = tienda.getImageUrl();
                            if (imageUrl != null) {
                                Picasso.get().load(imageUrl).into(imageTienda);
                            }
                            break; // Solo necesitas editar una tienda, por lo que puedes salir del bucle después de encontrar la tienda adecuada.
                        }
                    }
                } else {
                    // No se encontró ninguna tienda para el usuario actual
                    Toast.makeText(EditarTiendaForm.this, "No se encontró la tienda del usuario", Toast.LENGTH_SHORT).show();
                    finish(); // Cierra la actividad de edición de tienda
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejo de errores
            }
        });

        // Agrega el Listener para seleccionar una imagen de la galería al tocar la CircleImageView
        imageTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre el selector de imágenes
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
            }
        });

        Btn_GuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarCambios();
            }
        });
    }

    // Método para guardar los cambios en la tienda
    public void guardarCambios() {
        // Verifica que la referencia de la tienda y los datos no sean nulos
        if (tiendaRef != null && tienda != null) {
            // Verifica si se seleccionó una nueva foto y si había una imagen anterior en Firebase Storage
            if (nuevaFotoSeleccionada && imageUrl != null && nuevaFotoUri != null) {
                // Elimina la imagen anterior de Firebase Storage
                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // La imagen anterior se eliminó exitosamente, ahora se puede cargar la nueva imagen
                        cargarNuevaImagen();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Manejo de error si no se puede eliminar la imagen anterior
                        Toast.makeText(EditarTiendaForm.this, "Error al eliminar la imagen anterior", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (!nuevaFotoSeleccionada) {
                // No se seleccionó una nueva foto
                // Actualiza los demás datos de la tienda sin cambiar la imagen
                actualizarDatosTienda();
            } else {
                // Manejo de error si no se seleccionó una nueva foto pero hay una URL de imagen anterior
                Toast.makeText(EditarTiendaForm.this, "Error en la selección de foto", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Manejo de error si la referencia de la tienda o los datos son nulos
            Toast.makeText(EditarTiendaForm.this, "Error al obtener la referencia de la tienda o los datos son nulos", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para cargar la nueva imagen y actualizar la URL de la imagen en la tienda
    private void cargarNuevaImagen() {
        // Continúa con el proceso de carga de la nueva imagen y actualización de la URL de la imagen en la tienda
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String nuevaImagenPath = "images/" + UUID.randomUUID().toString();
        StorageReference nuevaImagenRef = storageRef.child(nuevaImagenPath);

        // Carga la nueva imagen en Firebase Storage
        nuevaImagenRef.putFile(nuevaFotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Obtén la URL de descarga de la nueva imagen
                nuevaImagenRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        // Actualiza la URL de la imagen en la tienda
                        tienda.setImageUrl(downloadUri.toString());

                        // Actualiza los demás datos de la tienda
                        actualizarDatosTienda();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Manejo de error si no se puede obtener la URL de la nueva imagen
                        Toast.makeText(EditarTiendaForm.this, "Error al obtener la URL de la nueva imagen", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Manejo de error si no se puede cargar la nueva imagen
                Toast.makeText(EditarTiendaForm.this, "Error al cargar la nueva imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para actualizar los demás datos de la tienda sin cambiar la imagen
    private void actualizarDatosTienda() {
        // Actualiza los demás datos de la tienda
        tienda.setNombre(txtNombreTiendaEditar.getText().toString());
        tienda.setDescripcion(txtDescripcionTiendaEditar.getText().toString());
        tienda.setDireccion(txtDireccionTiendaEditar.getText().toString());
        tienda.setExtra(txtExtraTiendaEditar.getText().toString());

        // Guarda los cambios en la base de datos
        tiendaRef.child(tiendaId).setValue(tienda).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Manejo de éxito al guardar los cambios
                Toast.makeText(EditarTiendaForm.this, "Cambios guardados con éxito", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la actividad de edición de tienda
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Manejo de error al guardar los cambios
                Toast.makeText(EditarTiendaForm.this, "Error al guardar los cambios", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


