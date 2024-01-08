package Cliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Vendedor.Activity_Vendedor;
import Vendedor.Tiendas.Tiendas_Activity;

public class MenuPrincipal extends AppCompatActivity {

    Button Btn_CerrarSesion;
    FirebaseAuth firebaseAuth;
    FirebaseUser User;

    TextView TextViewNombre, TextViewCorreo;
    ProgressBar ProgressBarDatos;
    DatabaseReference Usuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        Btn_CerrarSesion = findViewById(R.id.Btn_CerrarSesion);
        firebaseAuth = FirebaseAuth.getInstance();
        User = firebaseAuth.getCurrentUser();
        TextViewNombre = findViewById(R.id.TextViewNombre);
        TextViewCorreo = findViewById(R.id.TextViewCorreo);
        ProgressBarDatos = findViewById(R.id.ProgressBarDatos);
        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

        Btn_CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalirAplicacion();
            }
        });

    }

    @Override
    protected void onStart() {
        ComprobarInicioSesion();
        super.onStart();
    }

    private void ComprobarInicioSesion(){
        if(User!=null){
            CargaDeDatos();
        }else{
            startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
            finish();
        }
    }

    private void CargaDeDatos(){
        Usuarios.child(User.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String TipoUsuario = ""+snapshot.child("Tipo de usuario").getValue();

                if(snapshot.exists() && TipoUsuario.equals("Cliente")){
                    ProgressBarDatos.setVisibility(View.GONE);
                    TextViewNombre.setVisibility(View.VISIBLE);
                    TextViewCorreo.setVisibility(View.VISIBLE);

                    //Obtener datos
                    String nombre = ""+snapshot.child("nombre").getValue();
                    String correo = ""+snapshot.child("correo").getValue();

                    //Setear datos en text view
                    TextViewNombre.setText(nombre);
                    TextViewCorreo.setText(correo);
                    startActivity(new Intent(MenuPrincipal.this, Tiendas_Activity.class));
                    Toast.makeText(MenuPrincipal.this, "Cliente", Toast.LENGTH_SHORT).show();

                }
                else if(snapshot.exists() && TipoUsuario.equals("Vendedor")){
                    startActivity(new Intent(MenuPrincipal.this, Activity_Vendedor.class));
                    Toast.makeText(MenuPrincipal.this, "Vendedor", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MenuPrincipal.this, "Tipo de usuario invalido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SalirAplicacion() {

        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
        Toast.makeText(this, "Cerraste sesión exitosamente", Toast.LENGTH_SHORT).show();

    }
}