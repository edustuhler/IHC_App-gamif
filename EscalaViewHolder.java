package ihc.ihc_app.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ihc.ihc_app.R;
import ihc.ihc_app.models.Escala;

public class EscalaViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "EscalaViewHolder";
    public TextView titleView;
    public TextView descricaoView;
    public ImageView escalaView;
    public RelativeLayout escalaLayout;


    public EscalaViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.escala_title);
        descricaoView = (TextView) itemView.findViewById(R.id.escala_pontuacao);
        escalaView = (ImageView) itemView.findViewById(R.id.escala);
        escalaLayout = (RelativeLayout) itemView.findViewById(R.id.escala_layout);
    }

    public void updateUI(Escala d) {
        titleView.setText(d.getNome());
        descricaoView.setText(d.getDescricao() + " - Pontos necessários:" + String.valueOf(d.getPontosNeces()));
    }

}
