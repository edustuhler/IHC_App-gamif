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
import ihc.ihc_app.models.Ranking;

public class RankingListFragment extends Fragment {

    private static final String TAG = "RankingListFragment" ;

    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Ranking, RankingViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    public  boolean first = true;

        public RankingListFragment(){

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View rootView = inflater.inflate(R.layout.fragment_ranking_list, container, false);

            mDatabase = FirebaseDatabase.getInstance().getReference();
            mRecycler = (RecyclerView) rootView.findViewById(R.id.ranking_list);
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
            Query rankingQuery = FirebaseHelper.getUsers().orderByChild("pontos");
            mAdapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(Ranking.class, R.layout.ranking_item, RankingViewHolder.class, rankingQuery) {
                @Override
                protected void populateViewHolder(final RankingViewHolder viewHolder, final Ranking model, final int position) {

                                viewHolder.emblemView.setImageResource(R.drawable.ic_gray_emblem);





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
