package Vendedor.Pedidos;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Cliente.Pedidos.PedidoClase;

public class detalles_pedido_vendedor extends AppCompatActivity {

    //XML
    EditText txt_descuento;
    TextView txt_productosV, txt_precio, txt_envio, txt_precioTotal, txt_direccion;
    LinearLayout layout_btn_descuento;
    Button btn_descuento;

    //Variables
    private int descuento;

    //Pedido//
    private PedidoClase pedidoV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedido_vendedor);

        //XML
        txt_descuento = findViewById(R.id.txt_descuentoV);
        layout_btn_descuento = findViewById(R.id.layout_btn_descuento);
        txt_productosV = findViewById(R.id.txt_productosV);
        txt_precio = findViewById(R.id.txt_precioV);
        txt_envio = findViewById(R.id.txt_envioV);
        txt_precioTotal = findViewById(R.id.txt_precioTotalV);
        txt_direccion = findViewById(R.id.txt_direccionV);
        btn_descuento = findViewById(R.id.btn_descuento);

        //Pedido
        pedidoV = (PedidoClase) getIntent().getSerializableExtra("pedido");

        //Escuchar el ingreso de numeros en txt_descuento
        txt_descuento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (txt_descuento.getText().length() > 0) {
                    layout_btn_descuento.setVisibility(View.VISIBLE);
                } else {
                    layout_btn_descuento.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_descuento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(detalles_pedido_vendedor.this);

                // Set the dialog title and message
                builder.setTitle("Mensaje")
                        .setMessage("Deseas aprobar el descuento? Este ya no podrá ser editado ni cancelado");

                // Add positive button
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Si button
                        dialog.dismiss(); // Close the dialog
                        aprobar_descuento();
                    }
                });

                // Add negative button
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Cancel button
                        dialog.dismiss(); // Close the dialog
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });


        //Funciones
        InformacionPedido();

    }

    @SuppressLint("SetTextI18n")
    private void InformacionPedido() {

        //Setear la cantidad de productos y el precio de ellos
        txt_productosV.setText("Productos (" + pedidoV.getCantidad() + ")");
        txt_precio.setText("$ " + pedidoV.getMonto());
        //Validar si tiene descuento
        if (pedidoV.getDescuento().equals("Ninguno")) {
            int colorRojo = getResources().getColor(R.color.red);
            txt_descuento.setTextColor(colorRojo);
            txt_descuento.setHint("Ninguno");
        } else {
            int colorVerde = getResources().getColor(R.color.green);
            txt_descuento.setTextColor(colorVerde);
            txt_descuento.setText("- $ " + pedidoV.getDescuento());
        }
        //Validar si tiene envio gratis
        /*if(pedido.getEnvio.equals("Gratis")){
            int colorVerde = getResources().getColor(R.color.green);
            txt_envio.setTextColor(colorVerde);
            txt_envio.setText(pedido.getEnvio);
        }else{

        }*/
        //Precio total - Descuento
        if (pedidoV.getDescuento().equals("Ninguno")) {
            txt_precioTotal.setText("$ " + pedidoV.getMonto());
        }
        //Setear la ubicación
        txt_direccion.setText(pedidoV.getDireccion());
    }

    public void aprobar_descuento() {
        double monto = Double.parseDouble(pedidoV.getMonto());
        String descuento = String.valueOf(txt_descuento.getText());
        double descuentoD = Double.parseDouble(descuento);
        double precioTotal = monto - descuentoD;
        String precioTotalString = String.format("$ %.2f", precioTotal);
        txt_precioTotal.setText(precioTotalString);

        txt_descuento.setText("- $" + txt_descuento.getText());
        int colorVerde = getResources().getColor(R.color.green);
        txt_descuento.setTextColor(colorVerde);
        txt_descuento.setEnabled(false);
        layout_btn_descuento.setVisibility(View.GONE);
        actualizar_pedido(descuento);
    }

    public void actualizar_pedido(String descuento){
        String idPedido = pedidoV.getIdPedido();
        String idTienda = pedidoV.getIdTienda();

        DatabaseReference pedidoRef = FirebaseDatabase.getInstance().getReference("Tienda")
                .child(idTienda)
                .child("Pedidos")
                .child(idPedido);
        pedidoRef.child("descuento").setValue(descuento);

        //Actualizar pedido cliente
        String idCliente = pedidoV.getIdCliente();
        String idPedidoC = pedidoV.getIdPedido();

        DatabaseReference clientePedidoRef = FirebaseDatabase.getInstance().getReference("Usuarios")
                .child(idCliente)
                .child("Pedidos")
                .child(idPedidoC);

        clientePedidoRef.child("descuento").setValue(descuento);
    }

}