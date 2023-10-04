package Vendedor;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SolicitudesVendedores extends AppCompatActivity {

    private ListView listViewVendedores;
    private List<ClaseVendedor> vendedoresList;
    private VendedorAdapter vendedorAdapter; // Puedes seguir utilizando el adaptador ClienteAdapter para vendedores si deseas
    private DatabaseReference vendedoresReference; // Cambia el nombre de la referencia
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes_vendedores);

        listViewVendedores = findViewById(R.id.listViewVendedores); // Cambia el ID si es necesario
        vendedoresList = new ArrayList<>();
        vendedorAdapter = new VendedorAdapter(this, vendedoresList); // Cambia el nombre del adaptador si es necesario
        listViewVendedores.setAdapter(vendedorAdapter);

        vendedoresReference = FirebaseDatabase.getInstance().getReference("Usuarios"); // Cambia el nombre de la referencia a "Vendedores"

        vendedoresReference.orderByChild("estado").equalTo("pendiente")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        vendedoresList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ClaseVendedor vendedor = snapshot.getValue(ClaseVendedor.class);
                            if (vendedor != null) {
                                vendedor.setUid(snapshot.getKey());
                                vendedoresList.add(vendedor);
                            }
                        }
                        vendedorAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Manejar errores si es necesario
                    }
                });
    }


}

