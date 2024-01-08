package Vendedor.Productos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.agenda.R;


public class comprar_producto extends AppCompatActivity {

    public TextView textNombreProducto2, textPrecioProducto2;
    public ImageView imgProducto2;
    public String productoImg, productoNombre, productoPrecio;
    public Button Btn_finalizarProducto;
    public AutoCompleteTextView Cantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_producto);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        overridePendingTransition(0, R.anim.slide_up);

        productoImg= getIntent().getStringExtra("productoImg");
        productoNombre= getIntent().getStringExtra("productoNombre");
        productoPrecio= getIntent().getStringExtra("productoPrecio");
        imgProducto2 = findViewById(R.id.imgProducto2);
        textNombreProducto2 = findViewById(R.id.textNombreProducto2);
        textPrecioProducto2 = findViewById(R.id.textPrecioProducto2);
        Btn_finalizarProducto = findViewById(R.id.Btn_finalizarProducto);
        Cantidad = findViewById(R.id.cantidad);
        String[] OpcionesCantidad = {"1", "2", "3", "4", "5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, OpcionesCantidad);
        Cantidad.setAdapter(adapter);

        // Agrega el listener para el AutoCompleteTextView
        Cantidad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Llama a cargar_producto cuando el usuario selecciona una cantidad
                cargar_producto();
            }
        });

        // Llama a cargar_producto inicialmente (puedes omitir esta línea si no deseas cargarlo al inicio)
        cargar_producto();

    }

    @SuppressLint("SetTextI18n")
    public void cargar_producto() {
        // Obtiene la cantidad seleccionada del AutoCompleteTextView
        String cantidadSeleccionada = Cantidad.getText().toString();

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
        Btn_finalizarProducto.setText("Comprar MX $" + precioTotal);

        // Actualiza las vistas del nombre y el precio del producto
        textNombreProducto2.setText(productoNombre);
        textPrecioProducto2.setText("MX $" + productoPrecio);

        // Carga la imagen del producto
        Glide.with(imgProducto2.getContext())
                .load(productoImg)
                .into(imgProducto2);
    }
}