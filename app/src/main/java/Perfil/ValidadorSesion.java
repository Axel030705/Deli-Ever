package Perfil;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Vendedor.Activity_Vendedor;
import Vendedor.Tiendas_Activity;
import Vendedor.Vendedor_Main;

public class ValidadorSesion {

    private final FirebaseUser user;
    private final DatabaseReference usuarios;
    private final Context context;

    public ValidadorSesion(FirebaseUser user, DatabaseReference usuarios, Context context) {
        this.user = user;
        this.usuarios = usuarios;
        this.context = context;
    }

    public void validarInicioSesion() {
        if (user != null) {
            ValidarDatos();
        } else {
            iniciarActividad(MainActivity.class);
        }
    }

    private void ValidarDatos() {
        usuarios.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String tipoUsuario = snapshot.child("Tipo de usuario").getValue(String.class);

                    assert tipoUsuario != null;
                    if (tipoUsuario.equals("Cliente")) {
                        iniciarActividad(Tiendas_Activity.class);
                    } else if (tipoUsuario.equals("Vendedor")) {
                        DatabaseReference tiendasRef = FirebaseDatabase.getInstance().getReference("Tienda");
                        tiendasRef.orderByChild("usuarioAsociado").equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    iniciarActividad(Vendedor_Main.class); // Redirige a la actividad de vendedor si hay tiendas registradas
                                } else {
                                    iniciarActividad(Activity_Vendedor.class); // Redirige a la actividad de registro de tienda si no hay tiendas registradas
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Manejar el error en caso de cancelación de la operación
                            }
                        });
                    }
                } else {
                    iniciarActividad(MainActivity.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el error en caso de cancelación de la operación
            }
        });
    }

    private void iniciarActividad(Class<?> claseActividad) {
        Intent intent = new Intent(context, claseActividad);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
