package Vendedor;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import org.w3c.dom.Text;

public class vista_producto extends AppCompatActivity {

    public TextView textNombreProducto, textDescripcionProducto, textPrecioProducto, textExtraProducto;
    public ImageView imgProducto;
    public String productoImg, productoNombre, productoDescripcion, productoPrecio, productoExtra;
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
        CargarProducto();

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
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Obtiene la cantidad seleccionada del AutoCompleteTextView
                String cantidadSeleccionada = cantidad.getText().toString();

                // Verifica si la cantidad seleccionada es un número válido
                int cantidad = 0;
                try {
                    cantidad = Integer.parseInt(cantidadSeleccionada);
                } catch (NumberFormatException e) {

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
    }

}