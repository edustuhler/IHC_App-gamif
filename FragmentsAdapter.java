package ihc.ihc_app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class FragmentsAdapter extends FragmentPagerAdapter {

    public final Integer FRAG_COUNT = 5;
    Boolean onlyFavorites;
    Boolean onlyRecommended;
    Fragment[] mFragments;
    String[] mFragmentsName;

    public FragmentsAdapter(FragmentManager fm, Boolean onlyFavorites, Boolean onlyRecommended) {
        super(fm);
        this.onlyFavorites = onlyFavorites;
        this.onlyRecommended = onlyRecommended;
        mFragments = generateFragmentsArray("1508727600", 86400);
        mFragmentsName = generateFragmentsName();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return FRAG_COUNT;
    }

    @Override
    public String getPageTitle(int position){
        return mFragmentsName[position];
    }

    public Fragment[] generateFragmentsArray(String baseTimestamp, Integer step){
        Fragment[] ret = new Fragment[FRAG_COUNT];
        for(int i = 0; i < FRAG_COUNT; i++){
            Bundle b = new Bundle();
            Long timestamp = Long.parseLong(baseTimestamp) + i*step;
            b.putLong("timestamp", timestamp);
            b.putBoolean("onlyFavorites", onlyFavorites);
            b.putBoolean("onlyRecommended", onlyRecommended);
            Fragment f = new SessaoListFragment();
            f.setArguments(b);
            ret[i] = f;
        }
        return ret;
    }

    public String[] generateFragmentsName(){
        String[] ret = new String[FRAG_COUNT];
        for (int i = 0; i < FRAG_COUNT; i++){
            ret[i] = "Dia "+(23+i)+"";
        }
        return ret;
    }
}
