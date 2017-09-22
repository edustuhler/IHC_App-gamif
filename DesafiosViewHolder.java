package ihc.ihc_app.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ihc.ihc_app.R;
import ihc.ihc_app.models.Desafio;

public class DesafiosViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "DesafioViewHolder";
    public TextView titleView;
    public TextView descricaoView;
    public TextView pontosView;
    public ImageView emblemView;
    public RelativeLayout emblemLayout;


    public DesafiosViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.desafio_title);
        descricaoView = (TextView) itemView.findViewById(R.id.desafio_etapas);
       // pontosView = (TextView) itemView.findViewById(R.id.desafio_pontos);
        emblemView = (ImageView) itemView.findViewById(R.id.emblem);
        emblemLayout = (RelativeLayout) itemView.findViewById(R.id.emblem_layout);
    }

    public void updateUI(Desafio s) {
        titleView.setText(s.getNome());
        descricaoView.setText(s.getDescricao());
       // pontosView.setText("");


    }

}
