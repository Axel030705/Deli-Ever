package Vendedor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.agenda.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Vendedor extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String usuarioId;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    boolean ImagenCargada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        assert user != null;
        usuarioId = user.getUid();

        EditText txtNombreTienda = findViewById(R.id.txt_NombreTienda);
        EditText txtDescripcionTienda = findViewById(R.id.txt_DescripcionTienda);
        EditText txtDireccionTienda = findViewById(R.id.txt_DireccionTienda);
        EditText txtExtraTienda = findViewById(R.id.txt_ExtraTienda);
        Button btnRegistrarTienda = findViewById(R.id.Btn_RegistrarTienda);
        CircleImageView imagenTienda = findViewById(R.id.ImagenTienda);

        imagenTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnRegistrarTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtNombreTienda.getText().toString().isEmpty() ||
                        txtDescripcionTienda.getText().toString().isEmpty() ||
                        !ImagenCargada) {

                    Toast.makeText(Activity_Vendedor.this, "Rellena todos los campos y carga la imagen", Toast.LENGTH_SHORT).show();

                } else {
                    ProgressDialog progressDialog = new ProgressDialog(Activity_Vendedor.this);
                    progressDialog.setTitle("Registrando tienda");
                    progressDialog.setMessage("Por favor, espera...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    String nombreTienda = txtNombreTienda.getText().toString();
                    String descripcionTienda = txtDescripcionTienda.getText().toString();
                    String direccionTienda = txtDireccionTienda.getText().toString();
                    String extraTienda = txtExtraTienda.getText().toString();

                    // Obtener la referencia al Storage de Firebase
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

                    // Crea una referencia al archivo en el Storage con un nombre único
                    StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());

                    // Carga la imagen al Storage
                    uploadImageToFirebaseStorage(imageRef, progressDialog, nombreTienda, descripcionTienda, direccionTienda, extraTienda);
                }
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Asigna la imagenUri a la variable de instancia imageUri
            ImagenCargada = true;
            CircleImageView imagenTienda = findViewById(R.id.ImagenTienda);
            imagenTienda.setImageURI(imageUri);
        }
    }

    private void uploadImageToFirebaseStorage(StorageReference imageRef, ProgressDialog progressDialog, String nombreTienda, String descripcionTienda, String direccionTienda, String extraTienda) {
        // Crea un ProgressDialog
        progressDialog.setTitle("Cargando imagen");
        progressDialog.setMessage("Por favor, espera...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

        // Carga la imagen al Storage
        imageRef.putFile(imageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                int progress = (int) (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setProgress(progress);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                // Aquí puedes obtener la URL de descarga de la imagen
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();
                        // Aquí puedes hacer uso de la URL de descarga de la imagen
                        // por ejemplo, guardarla en la base de datos asociada a tu tienda

                        // Crea una instancia de TiendaClase y establece los valores
                        TiendaClase tienda = new TiendaClase(null, nombreTienda, descripcionTienda, direccionTienda, extraTienda, usuarioId, imageUrl);

                        // Después de cargar la imagen con éxito, puedes mostrarla en el CircleImageView
                        CircleImageView imagenTienda = findViewById(R.id.ImagenTienda);
                        Glide.with(Activity_Vendedor.this)
                                .load(imageUrl)
                                .apply(RequestOptions.circleCropTransform())
                                .into(imagenTienda);

                        // Establece ImagenCargada como true
                        ImagenCargada = true;

                        DatabaseReference tiendaRef = FirebaseDatabase.getInstance().getReference("Tienda");
                        String tiendaId = tiendaRef.push().getKey();
                        tienda.setId(tiendaId);
                        tiendaRef.child(tiendaId).setValue(tienda).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(Activity_Vendedor.this, Vendedor_Main.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
        progressDialog.show();
    }
}