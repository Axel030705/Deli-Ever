package Cliente.Pedidos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.R;

public class detalles_pedido extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedido);

        // Obtén el pedido de los extras
        PedidoClase pedido = (PedidoClase) getIntent().getSerializableExtra("pedido");
    }
}