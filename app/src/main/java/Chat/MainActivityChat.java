package Chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import Perfil.ImageInfo;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivityChat extends AppCompatActivity {

    private CircleImageView fotoPerfil;
    private TextView nombreUsr;
    private RecyclerView rvMensajes;
    private EditText txt_Mensaje;
    private AdapterMensajes adapter;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ImageButton BtnEnviarFoto;
    private static final int PHOTO_SEND = 1;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    //Perfil del Usuario
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;
    private String userId;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        fotoPerfil = findViewById(R.id.fotoPerfil);
        nombreUsr = findViewById(R.id.nombreUsr);
        rvMensajes = findViewById(R.id.rvMensajes);
        txt_Mensaje = findViewById(R.id.txt_Mensaje);
        Button btnEnviar = findViewById(R.id.BtnEnviar);
        Button Btn_menu_chat = findViewById(R.id.Btn_menu_chat);
        BtnEnviarFoto = findViewById(R.id.BtnEnviarFoto);
        adapter = new AdapterMensajes(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat"); //Sala de chat
        storage = FirebaseStorage.getInstance();

        //Perfil del Usuario
        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Usuarios");
        userId = firebaseAuth.getCurrentUser().getUid();
        userRef = usersRef.child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nombre = dataSnapshot.child("nombre").getValue(String.class);
                    String password = dataSnapshot.child("password").getValue(String.class);
                    String correo = dataSnapshot.child("correo").getValue(String.class);
                    String tipo = dataSnapshot.child("Tipo de usuario").getValue(String.class);

                    /*if(tipo.equals("Vendedor")){ // Valida si es Vendedor

                    }else{

                    }*/

                    nombreUsr.setText(nombre);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Maneja cualquier error en la lectura de datos
            }
        });

        // Recupera la URL de la imagen almacenada en Firebase Database
        userRef.child("imagenPerfil").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ImageInfo imageInfo = dataSnapshot.getValue(ImageInfo.class);
                    String imageUrl = imageInfo.getUrl();
                    // Carga la imagen almacenada en el CircleImageView
                    Picasso.get().load(imageUrl).into(fotoPerfil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Maneja cualquier error en la recuperación de la URL de la imagen
            }
        });

        Btn_menu_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creamos el objeto PupupMenu
                PopupMenu popupMenu = new PopupMenu(MainActivityChat.this, view);
                //Infla el menu desde el archivo XML
                popupMenu.getMenuInflater().inflate(R.menu.menu_opt_chat, popupMenu.getMenu());
                //Configura el listener para manejar las opciones del menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        int itemId = menuItem.getItemId();

                        if (itemId == R.id.opcion1) {
                            // Acción para la opción 1
                            // Borra todos los elementos del adaptador
                            adapter.clear();
                            // Notifica al RecyclerView que los datos han cambiado
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivityChat.this, "Borraste la conversación", Toast.LENGTH_SHORT).show();
                            return true;
                        }/* else if (itemId == R.id.opcion2) {
                            // Acción para la opción 2
                            Toast.makeText(MainActivityChat.this, "Seleccionaste Opción 2", Toast.LENGTH_SHORT).show();
                            return true;
                        }*/ else {
                            // Otros casos si es necesario
                            return false;
                        }
                    }
                });
                //Muestra el PupupMenu
                popupMenu.show();
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validar si el campo txt_mensaje esta vacio
                if(txt_Mensaje.getText().toString().isEmpty()){

                }else{
                    databaseReference.push().setValue(new MensajeEnviar(txt_Mensaje.getText().toString(), nombreUsr.getText().toString(),"","1"));
                    txt_Mensaje.setText("");
                }
            }
        });

        BtnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(i,"Selecciona una foto"),PHOTO_SEND);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScroll();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MensajeRecibir m = snapshot.getValue(MensajeRecibir.class);
                adapter.addMensaje(m);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void setScroll(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PHOTO_SEND && resultCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("imagenes chat");
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            fotoReferencia.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> downloadUrlTask = taskSnapshot.getStorage().getDownloadUrl();
                    downloadUrlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUrl) {
                            String imageUrl = downloadUrl.toString();
                            MensajeEnviar m = new MensajeEnviar(nombreUsr.getText().toString() + " te ha enviado una foto",nombreUsr.getText().toString(),"","2",imageUrl);
                            databaseReference.push().setValue(m);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivityChat.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }else {
            Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
        }
    }
}