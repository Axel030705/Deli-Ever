package Perfil;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import Vendedor.MainActivityEspera;
import Vendedor.Tiendas_Activity;

public class Registro extends AppCompatActivity {

    EditText txt_Nombre, txt_Correo, txt_Password, txt_ConfirmarPassword;
    Button Btn_RegistrarUsuario;
    TextView TengoCuentaTXT;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    private AutoCompleteTextView spinner;


    ///////////////////////////////
    String nombre = "", correo = "", password = "", confirmarpassword = "", tipoUsuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        spinner = findViewById(R.id.spinner);
        String[] OpcionesUsuario = {"Cliente", "Vendedor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, OpcionesUsuario);
        spinner.setAdapter(adapter);

        txt_Nombre = findViewById(R.id.txt_Nombres);
        txt_Correo = findViewById(R.id.txt_Correo);
        txt_Password = findViewById(R.id.txt_Password);
        txt_ConfirmarPassword = findViewById(R.id.txt_ConfirmarPassword);
        Btn_RegistrarUsuario = findViewById(R.id.Btn_RegistrarUsuario);
        TengoCuentaTXT = findViewById(R.id.TengoCuentaTXT);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Registro.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        Btn_RegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidarDatos();
            }
        });

        TengoCuentaTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registro.this, Login.class));
            }
        });
    }

    private void ValidarDatos() {
        nombre = txt_Nombre.getText().toString();
        correo = txt_Correo.getText().toString();
        password = txt_Password.getText().toString();
        confirmarpassword = txt_ConfirmarPassword.getText().toString();
        tipoUsuario = spinner.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "Ingrese su nombre", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingrese un correo valido", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(confirmarpassword)) {
            Toast.makeText(this, "Confirme su contraseña", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmarpassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }else {
            CrearCuenta();
        }
    }

    private void CrearCuenta() {
        progressDialog.setMessage("Creando su cuenta...");
        progressDialog.show();

        // Crear usuario en Firebase
        firebaseAuth.createUserWithEmailAndPassword(correo, password)
                .addOnSuccessListener(authResult -> {
                    // Continuar con el flujo después de crear la cuenta
                    // Obtener el token FCM
                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    String token = task.getResult();

                                    // Aquí puedes guardar el token en la base de datos junto con otros datos del usuario
                                    GuardarInformacion(token);
                                } else {
                                    // Manejar el caso en que no se pudo obtener el token
                                    // Puedes mostrar un mensaje de error o realizar alguna otra acción
                                    Log.w(TAG, "No se pudo obtener el token de FCM.", task.getException());

                                    // Aún así, guarda la información del usuario sin el token si lo deseas
                                    GuardarInformacion(null);
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(Registro.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /*private void GuardarInformacion(String token) {
        progressDialog.setMessage("Guardando su información");

        // Obtener la identificación de usuario actual
        String uid = firebaseAuth.getUid();

        HashMap<String, Object> Datos = new HashMap<>();
        Datos.put("uid", uid);
        Datos.put("correo", correo);
        Datos.put("nombre", nombre);
        Datos.put("password", password);
        Datos.put("Tipo de usuario", tipoUsuario);

        if (token != null) {
            Datos.put("tokenFCM", token);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(uid).setValue(Datos)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(Registro.this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();

                    // Iniciar la actividad correspondiente según el tipo de usuario
                    if ("Cliente".equals(tipoUsuario)) {
                        startActivity(new Intent(Registro.this, Tiendas_Activity.class));
                    } else if ("Vendedor".equals(tipoUsuario)) {
                        startActivity(new Intent(Registro.this, Activity_Vendedor.class));
                    } else {
                        Toast.makeText(Registro.this, "Tipo de usuario no válido", Toast.LENGTH_SHORT).show();
                    }

                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(Registro.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }*/


    private void GuardarInformacion(String token) {
        progressDialog.setMessage("Guardando su información");

        // Obtener la identificación de usuario actual
        String uid = firebaseAuth.getUid();

        HashMap<String, Object> Datos = new HashMap<>();
        Datos.put("uid", uid);
        Datos.put("correo", correo);
        Datos.put("nombre", nombre);
        Datos.put("password", password);
        Datos.put("Tipo de usuario", tipoUsuario);

        if (token != null) {
            Datos.put("tokenFCM", token);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(uid).setValue(Datos)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(Registro.this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();

                    // Iniciar la actividad correspondiente según el tipo de usuario
                    if ("Cliente".equals(tipoUsuario)) {
                        startActivity(new Intent(Registro.this, Tiendas_Activity.class));
                    } else if ("Vendedor".equals(tipoUsuario)) {
                        // Establecer el estado como "pendiente" para los vendedores
                        DatabaseReference vendedorReference = FirebaseDatabase.getInstance().getReference("Usuarios").child(uid);
                        vendedorReference.child("estado").setValue("pendiente");
                        Intent intent = new Intent(this, MainActivityEspera.class);
                        intent.putExtra("uid", uid); // Aquí "uid" es el nombre de la variable y su valor
                        startActivity(intent);
                    } else {
                        Toast.makeText(Registro.this, "Tipo de usuario no válido", Toast.LENGTH_SHORT).show();
                    }

                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(Registro.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}





