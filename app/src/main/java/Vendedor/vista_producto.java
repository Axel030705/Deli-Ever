package Vendedor;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.agenda.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Random;

public class vista_producto extends AppCompatActivity {

    public TextView textNombreProducto, textDescripcionProducto, textPrecioProducto, textExtraProducto;
    public ImageView imgProducto;
    public String productoImg, productoNombre, productoDescripcion, productoPrecio, productoExtra, idTienda;
    public Button Btn_comprarProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_producto);

        imgProducto = findViewById(R.id.imgProducto);
        textNombreProducto = findViewById(R.id.textNombreProducto);
        textDescripcionProducto = findViewById(R.id.textDescripcionProducto);
        textPrecioProducto = findViewById(R.id.textPrecioProducto);
        textExtraProducto = findViewById(R.id.textExtraProducto);
        Btn_comprarProducto = findViewById(R.id.Btn_comprarProducto);
        productoImg= getIntent().getStringExtra("productoImg");
        productoNombre= getIntent().getStringExtra("productoNombre");
        productoDescripcion= getIntent().getStringExtra("productoDescripcion");
        productoPrecio= getIntent().getStringExtra("productoPrecio");
        productoExtra= getIntent().getStringExtra("productoExtra");
        idTienda= getIntent().getStringExtra("tiendaId");
        CargarProducto();
        Logicas();
    }

    @SuppressLint("SetTextI18n")
    public void CargarProducto(){

        textNombreProducto.setText(productoNombre);
        textDescripcionProducto.setText(productoDescripcion);
        textPrecioProducto.setText("MX $" + productoPrecio);
        textExtraProducto.setText(productoExtra);
        Glide.with(imgProducto.getContext())
                .load(productoImg)
                .into(imgProducto);

    }

    /*public void comprar_producto(View view){

        Intent intent = new Intent(this, comprar_producto.class);
        intent.putExtra("productoNombre", productoNombre);
        intent.putExtra("productoPrecio", productoPrecio);
        intent.putExtra("productoImg", productoImg);
        startActivity(intent);
    } */


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
        String[] opcionesCantidad = {"1", "2", "3", "4", "5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, opcionesCantidad);
        cantidad.setAdapter(adapter);

        bottomSheetDialog.setContentView(bottomSheetView);
        // Muestra el BottomSheet
        bottomSheetDialog.show();

        Btn_finalizarProducto2.setOnClickListener(view1 -> {

            // Paso 1: Obtener el vendedorId de la tienda en la que se realiza la acción
            DatabaseReference tiendasRef = FirebaseDatabase.getInstance().getReference("Tienda");
            tiendasRef.child(idTienda).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String vendedorId = dataSnapshot.child("usuarioAsociado").getValue(String.class);

                        if (vendedorId != null) {
                            // Paso 2: Buscar el token del vendedor en la base de datos de usuarios
                            DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios");
                            usuariosRef.child(vendedorId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        String vendedorToken = dataSnapshot.child("tokenFCM").getValue(String.class);

                                        if (vendedorToken != null) {
                                            // Paso 3: Crear y enviar la notificación al vendedor
                                            RemoteMessage.Builder builder = new RemoteMessage.Builder(vendedorToken);
                                            builder.setMessageId(Integer.toString(new Random().nextInt(9999)));
                                            builder.addData("title", "Nuevo pedido");
                                            builder.addData("body", "Tienes un nuevo pedido de " + "Axel");
                                            builder.addData("pedido_id", "ID_del_pedido"); // Puedes incluir información adicional sobre el pedido aquí

                                            FirebaseMessaging.getInstance().send(builder.build());
                                            Toast.makeText(vista_producto.this, "Pedido realizado con exito!", Toast.LENGTH_LONG).show();
                                        } else {
                                            // Manejar el caso en que no se encontró el token del vendedor
                                            Toast.makeText(vista_producto.this, "Token del vendedor no encontrado", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(vista_producto.this, "Error base de datos de usuarios", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            // Manejar el caso en que no se encontró el vendedorId
                            Toast.makeText(vista_producto.this, "ID del vendedor no encontrado en la tienda", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(vista_producto.this, "Error base de datos de tiendas", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    public void Logicas(){
        // Verificar si el textView de extra esta vacio
        if(textExtraProducto.getText().toString().isEmpty()){
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) Btn_comprarProducto.getLayoutParams();
            params.topMargin = (int) getResources().getDimension(R.dimen.nuevo_margin_top);
            Btn_comprarProducto.setLayoutParams(params);
        }
    }

}