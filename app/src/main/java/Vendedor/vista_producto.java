package Vendedor;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agenda.R;

import org.w3c.dom.Text;

public class vista_producto extends AppCompatActivity {

    ImageView imgProducto = findViewById(R.id.imgProducto);
    TextView textNombreProducto = findViewById(R.id.textNombreProducto);
    TextView textDescripcionProducto = findViewById(R.id.textDescripcionProducto);
    TextView textPrecioProducto = findViewById(R.id.textPrecioProducto);
    TextView textExtraProducto = findViewById(R.id.textExtraProducto);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_producto);

        String productoNombre= getIntent().getStringExtra("productoNombre");
        String productoDescripcion= getIntent().getStringExtra("productoDescripcion");
        String productoPrecio= getIntent().getStringExtra("productoPrecio");
        String productoExtra= getIntent().getStringExtra("productoExtra");
        String productoImg= getIntent().getStringExtra("productoImg");

    }
}