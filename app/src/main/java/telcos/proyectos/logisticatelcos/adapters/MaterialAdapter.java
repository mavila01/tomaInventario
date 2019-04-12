package telcos.proyectos.logisticatelcos.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import telcos.proyectos.logisticatelcos.models.Codigos;
import telcos.proyectos.logisticatelcos.R;

public class MaterialAdapter extends ArrayAdapter<Codigos> {
    private ArrayList<Codigos> original;
    private ArrayList<Codigos> fitems;
    private Filter filter;
    // private int idEditxt= 0;

    public MaterialAdapter(Context context,List<Codigos> objects) {
        super(context,0,objects);
        this.original = new ArrayList<Codigos>(objects);
        this.fitems = new ArrayList<Codigos>(objects);

    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(final int position,@Nullable View convertView,@NonNull ViewGroup parent) {

        final ViewHolder holder;

        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    R.layout.list_item_material,
                    parent,
                    false);

            holder = new ViewHolder();

            holder.codigotxView = (TextView) convertView.findViewById(R.id.tv_codigo);
            holder.descripciontxView = (TextView) convertView.findViewById(R.id.tv_descipcion);
            holder.cmd_agregarItemSerializadoView = (Button) convertView.findViewById(R.id.cmd_agregarItemSerializado);
            holder.serialEditView = (EditText) convertView.findViewById(R.id.serialEdit);
            holder.cantidadEditView = (EditText) convertView.findViewById(R.id.EditTextCantidad);
            //final TextView textSerial = (TextView) convertView.findViewById(R.id.textSerial);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Codigos cod = getItem(position);

        assert cod != null;

        holder.cmd_agregarItemSerializadoView.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //this.original.add(position+1,cod);

                        try {
                            MaterialAdapter ma = ((MaterialAdapter) ((ListView) view.getParent().getParent()).getAdapter());
                            Codigos duplicado = new Codigos(cod.getmCod(),cod.getmDesc(),"",null,cod.getmIdSerial());
                            ma.getOriginal().add(position + 1,duplicado);
                            ma.clear();
                            ma.addAll(ma.getOriginal());
                            ma.notifyDataSetChanged();
                        } finally {

                        }
                    }
                });


        holder.cantidadEditView.setText(cod.getmCant());
        holder.serialEditView.setText(cod.getmSerial());
        holder.codigotxView.setText(cod.getmCod());
        holder.descripciontxView.setText(cod.getmDesc());

        int idSerial = cod.getmIdSerial();
        if (idSerial == 0) {
            holder.serialEditView.setVisibility(View.GONE);
            holder.cmd_agregarItemSerializadoView.setVisibility(View.GONE);
        } else {
            holder.serialEditView.setVisibility(View.VISIBLE);
            //textSerial.setText("Serializado");
            holder.cmd_agregarItemSerializadoView.setVisibility(View.VISIBLE);
        }

        holder.cantidadEditView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view,boolean b) {
                if (view != null && !b) {
                    try {
                        cod.setmCant((holder.cantidadEditView.getText().toString()));
                    } catch (Exception ignored) {
                    }
                }
            }
        });

        holder.serialEditView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view,boolean b) {
                if (view != null && !b) {
                    try {
                        cod.setmSerial(holder.serialEditView.getText().toString());
                    } catch (Exception ignored) {

                    }

                }
            }
        });


        return convertView;
    }

    static class ViewHolder {

        private TextView codigotxView;
        private TextView descripciontxView;
        private Button cmd_agregarItemSerializadoView;
        private EditText serialEditView;
        private EditText cantidadEditView;
    }

    @Nullable
    @Override
    public Codigos getItem(int position) {
        return super.getItem(position);
    }


    public ArrayList<Codigos> getOriginal() {
        return original;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new MaterialesFilter();

        return filter;
    }

    private class MaterialesFilter extends Filter {

        MaterialesFilter() {
            super();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            String prefix = charSequence.toString().toLowerCase();

            if (prefix.length() == 0) {
                ArrayList<Codigos> list = new ArrayList<Codigos>(getOriginal());
                results.values = list;
                results.count = list.size();
            } else {
                final ArrayList<Codigos> list = new ArrayList<Codigos>(getOriginal());
                final ArrayList<Codigos> nlist = new ArrayList<Codigos>();
                int count = list.size();

                for (int i = 0; i < count; i++) {
                    // if (results.mName.toUpperCase().contains( constraint.toString().toUpperCase() )) {}

                    final Codigos material = list.get(i);
                    final String value = material.toString().toLowerCase();

                    if (value.toUpperCase().contains(prefix.toUpperCase())) {
                        nlist.add(material);
                    }
                }
                results.values = nlist;
                results.count = nlist.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence,FilterResults filterResults) {
            fitems = (ArrayList<Codigos>) filterResults.values;
            //notifyDataSetChanged();
            clear();
            int count = fitems.size();
            for (int i = 0; i < count; i++) {
                Codigos material = (Codigos) fitems.get(i);
                add(material);
            }
        }
    }


}
