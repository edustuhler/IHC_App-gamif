package ihc.ihc_app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import ihc.ihc_app.FirebaseHelper;
import ihc.ihc_app.R;
import ihc.ihc_app.models.Escala;

public class EscalaListFragment extends Fragment {

    private static final String TAG = "EscalaListFragment" ;

    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Escala, EscalaViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    public  boolean first = true;

        public EscalaListFragment(){

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View rootView = inflater.inflate(R.layout.fragment_escala_list, container, false);

            mDatabase = FirebaseDatabase.getInstance().getReference();
            mRecycler = (RecyclerView) rootView.findViewById(R.id.escala_list);
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
            Query escalaQuery = FirebaseHelper.getAllPremios();
            mAdapter = new FirebaseRecyclerAdapter<Escala, EscalaViewHolder>(Escala.class, R.layout.escala_item, EscalaViewHolder.class, escalaQuery) {
                @Override
                protected void populateViewHolder(final EscalaViewHolder viewHolder, final Escala model, final int position) {


                                viewHolder.escalaView.setImageResource(R.drawable.ic_escala);
                                viewHolder.escalaView.setTag("ho");



                    viewHolder.updateUI(model);
                }
            };
            mRecycler.setAdapter(mAdapter);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (mAdapter != null) {
                mAdapter.cleanup();
            }
        }


    }
