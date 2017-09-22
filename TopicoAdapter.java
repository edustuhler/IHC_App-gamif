package ihc.ihc_app.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ihc.ihc_app.R;
import ihc.ihc_app.models.Topico;

public class TopicoAdapter extends BaseAdapter {

    private Context context;
    private List<Topico> listaTopicos;
    private final List<Integer> idSelecionados = new ArrayList<>(); // Para armazenar os itens selecionados


    public TopicoAdapter(Context context, List<Topico> listaTopicos){
        this.context = context;
        this.listaTopicos = listaTopicos;
    }

    @Override
    public int getCount() {
        return this.listaTopicos.size();
    }

    @Override
    public Object getItem(int posicao) {
        return this.listaTopicos.get(posicao);
    }

    @Override
    public long getItemId(int posicao) {
        return posicao;
    }

    public List<String> getAllSelectedNames(){
        String[] arr = new String[42];
        List<String> ret;
        for (Integer id : idSelecionados){
            arr[id] = listaTopicos.get(id).getNome();
        }
        ret = Arrays.asList(arr);
        return ret;
    }

    @Override
    public View getView(int posicao, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            /*
             * Recupera o serviço LayoutInflater que é o servidor que irá
             * transformar o nosso layout item_pessoa em uma View
             */
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            /*
             * Converte nosso layout em uma view
             */
            view = inflater.inflate(R.layout.topic_item, null);
        }

        /**
         * Recupero os compoentes que estão dentro do lista_item_pessoa
         */

        TextView nome = (TextView) view.findViewById(R.id.topic_name);
        CheckBox checked = (CheckBox) view.findViewById(R.id.topic_selected);

        /**
         * Recupera denteo da lista de pessoas, uma pessoa(nome) especifico de acordo com a
         * posição passada no parämetro do getView
         */
        Topico topic = listaTopicos.get(posicao);

        /**
         * Seta os valores nos TextView
         */

        nome.setText(topic.getNome().toString());
        checked.setChecked(topic.getChecked());
        //Define uma tag para o checBox, uma recuperarmos os dados quando for clicado no checkBox
        checked.setTag(topic);



        /** Definindo uma ação ao clicar no checkbox.
         * Será chamado quando for clicado no checkBox.
         * Aqui poderiamos armazenar um valor chave,
         * que identifique o objeto selecionado para que o mesmo possa ser, por exemplo, excluído
         * mais tarde.
         */
        checked.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                CheckBox check = (CheckBox) v;

                Topico t = (Topico) v.getTag();
                t.setChecked(((CheckBox) v).isChecked());


                if (check.isChecked()) {
                    //Toast.makeText(context, "Marcado Nº " + t.getId(), Toast.LENGTH_SHORT).show();
                    //Faz uma checagem se existe o mesmo valor na lista de inteiros
                    if(!idSelecionados.contains(t.getId())){
                        //Adiciona em uma lista para poder manipular os dados depois
                        idSelecionados.add(t.getId());
                    }

                } else {
                    //Toast.makeText(context, "Desmarcado Nº " + t.getId(), Toast.LENGTH_SHORT).show();
                    //Faz uma checagem se existe o mesmo valor na lista de inteiros
                    if(idSelecionados.contains(t.getId())){
                        //Remove da lista se existir na lista
                        idSelecionados.add(t.getId());
                    }

                }

            }
        });




        /*
         * Retorna a View para o ListView, para que o mesmo pinte esse item
         * na lista.
         */
        return view;
    }

}