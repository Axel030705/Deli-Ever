package Cliente;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    FirebaseAuth firebaseAuth;


    @SuppressLint("AppCompatMethod")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ocultar la barra de título
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Establecer en pantalla completa
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Cambiar el color de la barra de estado
        this.getWindow().setStatusBarColor(getResources().getColor(R.color.azulCielo));
        setContentView(R.layout.activity_splas_screen);

        firebaseAuth = FirebaseAuth.getInstance();


        int tiempo = 3900;
         new Handler().postDelayed(this::VerificarUsuario,tiempo);

        GifImageView gifView = findViewById(R.id.LogoAnimation);
        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.raw.logo);
            gifView.setImageDrawable(gifDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*GifImageView gifbarra = findViewById(R.id.bp);
        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.raw.bp);
            gifbarra.setImageDrawable(gifDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        } */

    }

    private void VerificarUsuario(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios");

        if(firebaseUser == null){
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }
        else{
            ValidadorSesion validadorSesion = new ValidadorSesion(firebaseUser, usuariosRef, getApplicationContext());
            validadorSesion.validarInicioSesion();
        }
    }

}