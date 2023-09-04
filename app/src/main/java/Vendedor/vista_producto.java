package Vendedor;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.agenda.R;

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

    public void comprar_producto(View view){

        Intent intent = new Intent(this, comprar_producto.class);
        intent.putExtra("productoNombre", productoNombre);
        intent.putExtra("productoPrecio", productoPrecio);
        intent.putExtra("productoImg", productoImg);
        startActivity(intent);

    }

}