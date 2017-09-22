package ihc.ihc_app.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ihc.ihc_app.R;
import ihc.ihc_app.models.Sessao;

public class SessaoViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView timeStartView;
    public TextView timeAtView;
    public TextView timeEndView;
    public TextView roomView;
    public ImageView starView;
    public RelativeLayout starLayout;
    public RelativeLayout sessionlayout;


    public SessaoViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.session_title);
        timeStartView = (TextView) itemView.findViewById(R.id.session_time_start);
        timeAtView = (TextView) itemView.findViewById(R.id.session_time_at);
        timeEndView = (TextView) itemView.findViewById(R.id.session_time_end);
        roomView = (TextView) itemView.findViewById(R.id.session_room);
        starView = (ImageView) itemView.findViewById(R.id.star);
        starLayout = (RelativeLayout) itemView.findViewById(R.id.star_layout);
        sessionlayout = (RelativeLayout) itemView.findViewById(R.id.session_layout);
    }

    public void updateUI(Sessao s, View.OnClickListener starListener, View.OnClickListener sessionListener) {
        titleView.setText(s.getNome());
        timeStartView.setText(Sessao.getTime(s.getTimestamp_start()));
        timeAtView.setText("-");
        timeEndView.setText(Sessao.getTime(s.getTimestamp_end()));
        roomView.setText(s.getSala());

        if(s.starred){
            starView.setImageResource(R.drawable.ic_star);
        }

        starLayout.setOnClickListener(starListener);
        sessionlayout.setOnClickListener(sessionListener);
    }
}
