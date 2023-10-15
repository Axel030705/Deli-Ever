package Vendedor;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.agenda.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class vista_producto extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;
    private String userId;
    private DatabaseReference userRef;
    public TextView textNombreProducto, textDescripcionProducto, textPrecioProducto, textExtraProducto, textCantidadProducto;
    public ImageView imgProducto;
    public String productoImg, productoNombre, productoDescripcion, productoPrecio, productoExtra, idTienda, productoCantidad, productoId;
    public Button Btn_comprarProducto, Btn_EditarProducto, Btn_EliminarProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_producto);

        FirebaseApp.initializeApp(this);
        imgProducto = findViewById(R.id.imgProducto);
        textNombreProducto = findViewById(R.id.textNombreProducto);
        textDescripcionProducto = findViewById(R.id.textDescripcionProducto);
        textPrecioProducto = findViewById(R.id.textPrecioProducto);
        textExtraProducto = findViewById(R.id.textExtraProducto);
        textCantidadProducto = findViewById(R.id.textCantidadProducto);
        Btn_comprarProducto = findViewById(R.id.Btn_comprarProducto);
        Btn_EditarProducto = findViewById(R.id.Btn_EditarProducto);
        Btn_EliminarProducto = findViewById(R.id.Btn_EliminarProducto);
        productoId = getIntent().getStringExtra("productoId");
        productoImg= getIntent().getStringExtra("productoImg");
        productoNombre= getIntent().getStringExtra("productoNombre");
        productoDescripcion= getIntent().getStringExtra("productoDescripcion");
        productoPrecio= getIntent().getStringExtra("productoPrecio");
        productoExtra= getIntent().getStringExtra("productoExtra");
        idTienda= getIntent().getStringExtra("tiendaId");
        productoCantidad= getIntent().getStringExtra("productoCantidad");
        CargarProducto();
        Logicas();
        obtenerToken();
        ///////////////////////////////////////////////////////////////////////////////
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

                    if(tipo.equals("Vendedor")){ // Valida si es Administrador
                        Btn_EditarProducto.setVisibility(View.VISIBLE);
                        Btn_EliminarProducto.setVisibility(View.VISIBLE);
                    }else if (tipo.equals("Cliente")){
                        Btn_EditarProducto.setVisibility(View.GONE);
                        Btn_EliminarProducto.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Maneja cualquier error en la lectura de datos
            }
        });

    }

    private void obtenerToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Token generado exitosamente
                        String token = task.getResult();
                        Log.d(TAG, "Token: " + token);

                        // Puedes almacenar este token o utilizarlo directamente para enviar notificaciones.
                    } else {
                        // Ocurrió un error al obtener el token
                        Log.w(TAG, "No se pudo obtener el token", task.getException());
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    public void CargarProducto(){

        textNombreProducto.setText(productoNombre);
        textDescripcionProducto.setText(productoDescripcion);
        textPrecioProducto.setText("MX $" + productoPrecio);
        textExtraProducto.setText(productoExtra);
        textCantidadProducto.setText("Cantidad: "+productoCantidad);
        Glide.with(imgProducto.getContext())
                .load(productoImg)
                .into(imgProducto);
    }

    public void EditarProductoActivity(View view){
        Intent i = new Intent(this, editar_producto_form.class);
        i.putExtra("productoId", productoId);
        i.putExtra("tiendaId", idTienda);
        startActivity(i);
    }

    public void EliminarProducto(View view) {
        // Obtén una referencia a la base de datos de Firebase
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Ubica la entrada del producto en la base de datos utilizando su tiendaId y productoId
        DatabaseReference productoRef = databaseRef.child("Tienda").child(idTienda).child("productos").child(productoId);

        // Elimina el producto
        productoRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Producto eliminado con éxito de la base de datos
                mostrarMensaje("El producto se ha eliminado con éxito.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Manejar errores si no se pudo eliminar el producto de la base de datos
                mostrarMensaje("Error al eliminar el producto. Inténtalo de nuevo.");
            }
        });
    }

    // Método para mostrar el BottomSheetDialog
    @SuppressLint("SetTextI18n")
    public void mostrarDetallesDelProducto(View view) {
        // Crea una instancia del BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        // Infla el diseño de tu BottomSheet personalizado
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);

        // Configura los elementos del BottomSheet según sus IDs
        ImageView imgProducto2 = bottomSheetView.findViewById(R.id.imgProducto2);
        TextView textNombreProducto2 = bottomSheetView.findViewById(R.id.textNombreProducto2);
        TextView textPrecioProducto2 = bottomSheetView.findViewById(R.id.textPrecioProducto2);
        AutoCompleteTextView cantidad = bottomSheetView.findViewById(R.id.cantidad2);
        Button Btn_finalizarProducto2 = bottomSheetView.findViewById(R.id.Btn_finalizarProducto2);
        cantidad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Obtiene la cantidad seleccionada del AutoCompleteTextView
                String cantidadSeleccionada = cantidad.getText().toString();

                // Verifica si la cantidad seleccionada es un número válido
                int cantidad = 0;
                try {
                    cantidad = Integer.parseInt(cantidadSeleccionada);
                } catch (NumberFormatException ignored) {

                }

                // Calcula el precio total multiplicando la cantidad por el precio unitario
                double precioUnitario = Double.parseDouble(productoPrecio);
                double precioTotal = cantidad * precioUnitario;

                // Establece el texto del botón con el precio total
                Btn_finalizarProducto2.setText("Comprar MX $" + precioTotal);
            }
        });



        //Aquí puedes cargar la imagen del producto si está disponible:
        Glide.with(imgProducto2.getContext()).load(productoImg).into(imgProducto2);

        // Asigna valores a los elementos según los datos del producto
        textNombreProducto2.setText(productoNombre);
        textPrecioProducto2.setText("MX $" + productoPrecio);

        // Aquí debes configurar las opciones de cantidad y un ArrayAdapter
        /*String[] opcionesCantidad = {productoCantidad};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, opcionesCantidad);
        cantidad.setAdapter(adapter);*/

        //Spinner con la cantidad de productos disponibles
        int cantidadInt = Integer.parseInt(productoCantidad);
        List<String> opcionesCantidad = new ArrayList<>();
        for (int i = 1; i <= cantidadInt; i++) {
            opcionesCantidad.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, opcionesCantidad);
        cantidad.setAdapter(adapter);

        bottomSheetDialog.setContentView(bottomSheetView);
        // Muestra el BottomSheet
        bottomSheetDialog.show();

    }

    @SuppressLint("SetTextI18n")
    public void Logicas(){

        // Verificar si el textView de extra esta vacio
        if(textExtraProducto.getText().toString().isEmpty()){
            textExtraProducto.setVisibility(View.GONE);
        }
        // Verificar si el textView de cantidad esta vacio
        if(textCantidadProducto.getText().toString().equals("Cantidad: ")){
            textCantidadProducto.setText("Sin stock");
        }
    }

    private void mostrarMensaje(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mensaje")
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    startActivity(new Intent(vista_producto.this, Tiendas_Activity.class));
                })
                .show();
    }

}