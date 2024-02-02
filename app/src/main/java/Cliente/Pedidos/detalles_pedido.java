package Cliente.Pedidos;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.agenda.FragmentDetalles;
import com.example.agenda.FragmentUbicacion;
import com.example.agenda.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class detalles_pedido extends AppCompatActivity {

    FragmentTransaction transactionDP;
    Fragment fragmentDetalles, fragmentUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedido);

        //Fragments
        fragmentDetalles = new FragmentDetalles();
        fragmentUbicacion = new FragmentUbicacion();

        //Fragment Inicio
        getSupportFragmentManager().beginTransaction().add(R.id.contentFragments_detalles_pedido, fragmentDetalles).commit();

        //Botones de navegación
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation_detalles_pedido);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                transactionDP = getSupportFragmentManager().beginTransaction();

                // Obtener el id de cada botón
                int itemId = item.getItemId();


                // Realizar las acciones correspondientes al elemento seleccionado
                if (itemId == R.id.menu_detalles) {
                    transactionDP.replace(R.id.contentFragments_detalles_pedido, fragmentDetalles).commit();
                    // Cambia el ícono del elemento del menú seleccionado
                    item.setIcon(R.drawable.info_2);

                    return true;
                } else if (itemId == R.id.menu_ubicacion) {
                    transactionDP.replace(R.id.contentFragments_detalles_pedido, fragmentUbicacion).commit();
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

}