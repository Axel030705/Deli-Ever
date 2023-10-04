package Vendedor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.agenda.R;

import java.util.List;

public class VendedorAdapter extends BaseAdapter {
    private Context context;
    private List<ClaseVendedor> vendedorList;

    public VendedorAdapter(Context context, List<ClaseVendedor> vendedorList) {
        this.context = context;
        this.vendedorList = vendedorList;
    }

    @Override
    public int getCount() {
        return vendedorList.size();
    }

    @Override
    public Object getItem(int position) {
        return vendedorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_vendedor, parent, false);
        }

        ClaseVendedor vendedor = vendedorList.get(position);

        // Personaliza la vista del elemento aquí según tus necesidades
        TextView nombreTextView = convertView.findViewById(R.id.nombreTextView);
        TextView correoTextView = convertView.findViewById(R.id.correoTextView);
        TextView estadoTextView = convertView.findViewById(R.id.estadoTextView);

        nombreTextView.setText(vendedor.getNombre());
        correoTextView.setText(vendedor.getCorreo());
        estadoTextView.setText(vendedor.getEstado());

        return convertView;
    }
}

