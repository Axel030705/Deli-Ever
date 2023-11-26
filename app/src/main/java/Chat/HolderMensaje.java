package Chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HolderMensaje extends RecyclerView.ViewHolder {

    private TextView nombre, mensaje;
    public CircleImageView fotoMensajePerfil;
    ImageView MandarFoto;

    public HolderMensaje(View itemView){
        super(itemView);

        nombre = itemView.findViewById(R.id.nombreMensaje);
        mensaje = itemView.findViewById(R.id.mensajeMensaje);
        fotoMensajePerfil = itemView.findViewById(R.id.fotoPerfilMensaje);
        MandarFoto = itemView.findViewById(R.id.msj_foto);
    }

    public TextView getNombre() {
        return nombre;
    }

    public TextView getMensaje() {
        return mensaje;
    }

    public ImageView getMandarFoto() {
        return MandarFoto;
    }

    public CircleImageView getFotoMensajePerfil() {
        return fotoMensajePerfil;
    }

}
