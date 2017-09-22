package ihc.ihc_app.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ihc.ihc_app.R;

public class EscalaFragment extends Fragment {

    public final String TAG = "EscalaFragment";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_escala, container, false);

        mPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {
                    new EscalaListFragment(),
            };
            private final String[] mFragmentNames = new String[] {
                    "Escala de Prêmios",
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };


        mViewPager = (ViewPager) rootView.findViewById(R.id.escala_viewpager);
        mViewPager.setAdapter(mPagerAdapter);

        final TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.app_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }



}