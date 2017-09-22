package ihc.ihc_app.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ihc.ihc_app.R;
import ihc.ihc_app.models.Trabalho;

public class TrabalhoAdapter extends BaseAdapter {

    private static final String TAG = "TrabalhoAdapter" ;
    List<Trabalho> trabalhos;
    Context context;
    LayoutInflater layoutInflater;


    public TrabalhoAdapter(Context context, Map<String, Object> map) {
        super();
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        trabalhos = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()){
            HashMap<String, Object> m = (HashMap<String, Object>) entry.getValue();
            Trabalho t = new Trabalho();
            List<String> autores = new ArrayList<>();
            for (Map.Entry<String, Object> e : m.entrySet()) {
                if (Objects.equals(e.getKey(),"titulo")){
                    t.titulo = (String)e.getValue();
                }else{
                    t.autores = ((ArrayList)e.getValue());
                }
            }
           trabalhos.add(t);
        }
    }

    @Override
    public int getCount() {
        return trabalhos.size();
    }

    @Override
    public Trabalho getItem(int position) {
        return trabalhos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Trabalho t = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.trabalho_item, parent, false);
        }

        TextView titulo = (TextView) convertView.findViewById(R.id.titulo);
        TextView autores = (TextView) convertView.findViewById(R.id.autores);

        titulo.setText(t.titulo);
        StringBuilder s = new StringBuilder();
        for (int i=0; i<t.autores.size();i++){
            s.append(t.autores.get(i));
            if(i != t.autores.size()-1){
                s.append(", ");
            }
        }

        Log.d(TAG, "Nome:"+t.titulo+", Autores:"+s.toString());
        autores.setText(s.toString());
        return convertView;
    }
}