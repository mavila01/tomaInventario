package telcos.proyectos.logisticatelcos.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import telcos.proyectos.logisticatelcos.R;

public class EstadoAdapter extends ArrayAdapter<String> {
    public ArrayList<String> original;

    public EstadoAdapter(Context context,List<String> objects) {
        super(context,0,objects);
        this.original = new ArrayList<String>(objects);
    }
    public ArrayList<String> getOriginal() {
        return original;
    }

    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView,@NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.myspinner,
                    parent,
                    false);
        }

        return convertView;
    }



    @Nullable
    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

}
