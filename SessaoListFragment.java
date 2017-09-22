package ihc.ihc_app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import ihc.ihc_app.FirebaseHelper;
import ihc.ihc_app.R;
import ihc.ihc_app.models.Sessao;

public class SessaoListFragment extends Fragment {

    private static final String TAG = "SessaoListFragment" ;

    private FirebaseAuth mAuth;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private Long timestamp;
    private Boolean onlyFavorites;
    private Boolean onlyRecommended;

        public SessaoListFragment(){}

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);

            timestamp = getArguments().getLong("timestamp");
            onlyFavorites = getArguments().getBoolean("onlyFavorites");
            onlyRecommended = getArguments().getBoolean("onlyRecommended");

            View rootView;
            if(onlyRecommended){
                Log.d(TAG, "ONLY RECOMMENDED INFLATING");
                rootView = inflater.inflate(R.layout.fragment_session_list_recommended, container, false);
            }else{
                rootView = inflater.inflate(R.layout.fragment_session_list, container, false);
            }
            mAuth = FirebaseAuth.getInstance();
            mRecycler = (RecyclerView) rootView.findViewById(R.id.session_list);
            mRecycler.setHasFixedSize(true);

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Set up Layout Manager, reverse layout
            mManager = new LinearLayoutManager(getActivity());
            mManager.setReverseLayout(true);
            mManager.setStackFromEnd(true);
            mRecycler.setLayoutManager(mManager);

            // Set up FirebaseRecyclerAdapter with the Query
            //Query sessionsQuery = FirebaseHelper.getAllSessions();
            Query sessionsQueryByDay = FirebaseHelper.getAllSessionsByDay(timestamp);
            mRecycler.setAdapter(new SessaoAdapter(sessionsQueryByDay, onlyFavorites, onlyRecommended));

            //TODO: Se não tiver nada pra mostrar, colocar um bg diferente (nao deixar tudo branco)
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

    }
