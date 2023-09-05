package Vendedor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

        cargar_producto();
    }

    @SuppressLint("SetTextI18n")
    public void cargar_producto(){

        textNombreProducto2.setText(productoNombre);
        textPrecioProducto2.setText("MX $" + productoPrecio);
        Glide.with(imgProducto2.getContext())
                .load(productoImg)
                .into(imgProducto2);


    }

}