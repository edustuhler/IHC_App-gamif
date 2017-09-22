package ihc.ihc_app.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ihc.ihc_app.R;
import ihc.ihc_app.models.Ranking;

public class RankingViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "RankingViewHolder";
    public TextView titleView;
    public TextView descricaoView;
    public TextView pontosView;
    public ImageView emblemView;
    public RelativeLayout emblemLayout;


    public RankingViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.ranking_title);
        descricaoView = (TextView) itemView.findViewById(R.id.ranking_pontos);
       // pontosView = (TextView) itemView.findViewById(R.id.desafio_pontos);
        emblemView = (ImageView) itemView.findViewById(R.id.emblem);
        emblemLayout = (RelativeLayout) itemView.findViewById(R.id.emblem_layout);
    }

    public void updateUI(Ranking s) {
        titleView.setText(s.getNome()+" "+s.getSobrenome());
        descricaoView.setText(String.valueOf("Pontuação: "+s.getPontos()+" pontos"));
       // pontosView.setText("");


    }

}
