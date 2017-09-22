package ihc.ihc_app.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ihc.ihc_app.FirebaseHelper;
import ihc.ihc_app.R;
import ihc.ihc_app.models.Sessao;

public class SessaoAdapter extends RecyclerView.Adapter<SessaoViewHolder> {

    private static final String TAG = "SessaoAdapter";
    private ArrayList<Sessao> items = new ArrayList<>();
    private DatabaseReference sessionsRef;
    private DatabaseReference favoritesRef;
    private DatabaseReference recommendedRef;
    private List<String> favorites;
    private List<String> recommended;
    private Boolean onlyFavorites;
    private Boolean onlyRecommended;
    private FirebaseAuth mAuth;


    public SessaoAdapter(Query sRef, final Boolean showOnlyFavorites, final Boolean showOnlyRecommended) {
        sessionsRef = sRef.getRef();
        onlyFavorites = showOnlyFavorites;
        onlyRecommended = showOnlyRecommended;
        mAuth = FirebaseAuth.getInstance();
        favorites = new ArrayList<>();
        recommended = new ArrayList<>();

        if (mAuth.getCurrentUser() != null){
            //Pega os favoritos
            favoritesRef = FirebaseHelper.getRef().child("usuarios").child(mAuth.getCurrentUser().getUid()).child("favoritos");
            favoritesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    favorites.clear();
                    for (DataSnapshot fav: dataSnapshot.getChildren()){
                        favorites.add(fav.getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "ERROR");
                }
            });
            favoritesRef.keepSynced(true);

            //Pega os recomendados
            recommendedRef = FirebaseHelper.getRef().child("usuarios").child(mAuth.getCurrentUser().getUid()).child("recomendados");
            recommendedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    recommended.clear();
                    for (DataSnapshot days: dataSnapshot.getChildren()){
                        for (DataSnapshot session: days.getChildren()) {
                            recommended.add(session.getKey());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "ERROR");
                }
            });
            recommendedRef.keepSynced(true);

            //Pega as sessoes
            sessionsRef.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot sessoes) {
                    items.clear();
                    for (DataSnapshot sessao : sessoes.getChildren()) {
                        Sessao tmp = new Sessao();
                        tmp.UID = sessao.getKey();
                        for( DataSnapshot userData : sessao.getChildren()){
                            tmp.addInfo(userData.getKey(), userData.getValue());
                        }
                        if(!onlyFavorites){
                            if (!showOnlyRecommended){
                                items.add(tmp);
                            }else{
                                if (recommended.contains(tmp.UID)){
                                    items.add(tmp);
                                }
                            }
                        }else if(onlyFavorites && favorites.contains(tmp.UID)){
                            if (!showOnlyRecommended){
                                items.add(tmp);
                            }else{
                                if (recommended.contains(tmp.UID)){
                                    items.add(tmp);
                                }
                            }
                        }
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}

            });
            sessionsRef.keepSynced(true);
        }
    }

    public SessaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(onlyRecommended){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item_recommended, parent, false);
        }else {
            //TODO: if items are empty, show something else
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item, parent, false);
        }
        return new SessaoViewHolder(view);
    }

    public void onBindViewHolder(final SessaoViewHolder viewHolder, final int position) {
        //Log.d(TAG, "Binding ViewHolder ...");
        final Sessao s = items.get(position);
        if(favorites != null && favorites.contains(s.UID)){
            s.starred = true;
        }

        View.OnClickListener starListener = new View.OnClickListener(){
            @Override
            public void onClick(View starLayout){
                ImageView starView = (ImageView)starLayout.findViewById(R.id.star);
                if(favorites != null ) {
                    if (favorites.contains(s.UID)) {
                        //Log.d(TAG,"Unstar::"+s.UID+"::"+s.getNome());
                        starView.setImageResource(R.drawable.ic_star_border);
                        if (onlyFavorites) {
                            Integer p = items.indexOf(s);
                            items.remove(s);
                            notifyItemRemoved(p);
                        }
                        favorites.remove(s.UID);
                    } else {
                        //Log.d(TAG,"Star::"+s.UID+"::"+s.getNome());
                        starView.setImageResource(R.drawable.ic_star);
                        favorites.add(s.UID);
                    }
                    favoritesRef.setValue(favorites);
                }
            }
        };

        View.OnClickListener sessionListener = new View.OnClickListener() {
            @Override
            public void onClick(View sessionView) {
                Intent intent = new Intent(sessionView.getContext(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.EXTRA_SESSION_KEY, s.UID);
                sessionView.getContext().startActivity(intent);
            }
        };

        viewHolder.updateUI(s, starListener, sessionListener);
    }

    public int getItemCount() {
        return items.size();
    }
}