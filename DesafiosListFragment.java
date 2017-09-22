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
import ihc.ihc_app.models.Desafio;

public class DesafiosListFragment extends Fragment {

    private static final String TAG = "DesafiosListFragment" ;

    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Desafio, DesafiosViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    public  boolean first = true;

        public DesafiosListFragment(){

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View rootView = inflater.inflate(R.layout.fragment_desafio_list, container, false);

            mDatabase = FirebaseDatabase.getInstance().getReference();
            mRecycler = (RecyclerView) rootView.findViewById(R.id.desafio_list);
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
            Query desafiosQuery = FirebaseHelper.getAllDesafios();
            mAdapter = new FirebaseRecyclerAdapter<Desafio, DesafiosViewHolder>(Desafio.class, R.layout.desafio_item, DesafiosViewHolder.class, desafiosQuery) {
                @Override
                protected void populateViewHolder(final DesafiosViewHolder viewHolder, final Desafio model, final int position) {

                                viewHolder.emblemView.setImageResource(R.drawable.ic_desafio);





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
