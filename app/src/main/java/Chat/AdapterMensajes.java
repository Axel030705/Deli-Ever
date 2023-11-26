package Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agenda.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensaje> {

    private final List<MensajeRecibir> listMensaje = new ArrayList<>();
    private final Context c;

    public AdapterMensajes(Context c) {
        this.c = c;
    }
    public void addMensaje(MensajeRecibir m){
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes, parent, false);
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(HolderMensaje holder, int position) {
        holder.getNombre().setText(listMensaje.get(position).getNombre());
        holder.getMensaje().setText(listMensaje.get(position).getMensaje());

        if(listMensaje.get(position).getType_mensaje().equals("2")){
            holder.getMandarFoto().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
            holder.getFotoMensajePerfil().setVisibility(View.GONE);
            Glide.with(c).load(listMensaje.get(position).getUrlFoto()).into(holder.getMandarFoto());
        }else if(listMensaje.get(position).getType_mensaje().equals("1")){
            holder.getMandarFoto().setVisibility(View.GONE);
            holder.getMensaje().setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }

    public void clear() {
        listMensaje.clear();
    }

}
