package ihc.ihc_app.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ihc.ihc_app.R;

public class FavoritesFragment extends Fragment {

    public final String TAG = "SessaoFragment";

    private FragmentsAdapter mAdapter;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_session, container, false);

        mAdapter = new FragmentsAdapter(getChildFragmentManager(), true, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.session_viewpager);
        mViewPager.setAdapter(mAdapter);

        final TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.app_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }
}