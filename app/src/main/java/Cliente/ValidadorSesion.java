package Cliente;

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
import Vendedor.MainActivityEspera;
import Vendedor.SolicitudesVendedores;
import Vendedor.Tiendas.Tiendas_Activity;
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

    /*private void ValidarDatos() {
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
    }*/

    private void ValidarDatos() {

        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(user.getUid());
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String passwordUsuario = dataSnapshot.child("password").getValue(String.class);
                    String tipoUsuario = dataSnapshot.child("Tipo de usuario").getValue(String.class);
                    String estado = dataSnapshot.child("estado").getValue(String.class);

                    assert tipoUsuario != null;
                    if ("Administrador".equals(passwordUsuario)) {
                        iniciarActividad(SolicitudesVendedores.class);
                    } else {
                        if ("Cliente".equals(tipoUsuario)) {
                            iniciarActividad(Tiendas_Activity.class);
                        } else if ("Vendedor".equals(tipoUsuario)) {
                            if ("aprobado".equals(estado)) {
                                // El usuario "Vendedor" está aprobado, ahora verifica si existe una tienda
                                DatabaseReference tiendasRef = FirebaseDatabase.getInstance().getReference("Tienda");
                                tiendasRef.orderByChild("usuarioAsociado").equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            // Hay una tienda registrada, redirigir a la actividad de vendedor
                                            iniciarActividad(Vendedor_Main.class);
                                        } else {
                                            // No hay una tienda registrada, redirigir a la actividad de registro de tienda
                                            iniciarActividad(Activity_Vendedor.class);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Manejar el error en caso de cancelación de la operación
                                    }
                                });
                            } else {
                                // El usuario "Vendedor" no está aprobado, redirigir a la actividad de espera
                                iniciarActividad(MainActivityEspera.class);
                            }
                        } else {
                            // Otro tipo de usuario no válido, manejar de acuerdo a tus necesidades
                            iniciarActividad(MainActivity.class); // Otra opción podría ser redirigir a la actividad principal
                        }
                    }
                } else {
                    // No se encontró información del usuario, manejar de acuerdo a tus necesidades
                    iniciarActividad(MainActivity.class); // Otra opción podría ser redirigir a la actividad principal
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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
