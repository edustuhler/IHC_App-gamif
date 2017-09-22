package ihc.ihc_app.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ihc.ihc_app.models.Topico;
import ihc.ihc_app.R;

public class TopicoViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "TopicoViewHolder";
    public CheckBox topicSelected;
    public TextView topicName;
    public RelativeLayout checkboxLayout;
    public RelativeLayout topicLayout;


    public TopicoViewHolder(View itemView) {
        super(itemView);
        topicSelected = (CheckBox) itemView.findViewById(R.id.topic_selected);
        topicName = (TextView) itemView.findViewById(R.id.topic_name);
        checkboxLayout = (RelativeLayout) itemView.findViewById(R.id.checkbox_layout);
        topicLayout = (RelativeLayout) itemView.findViewById(R.id.topic_layout);
    }

    public void updateUI(String s) {
        topicName.setText(s);
    }

}
