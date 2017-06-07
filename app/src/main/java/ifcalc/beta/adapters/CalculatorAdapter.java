package ifcalc.beta.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ifcalc.beta.R;
import ifcalc.beta.activities.fragments.CalculatorAnualFragment;
import ifcalc.beta.activities.fragments.CalculatorSemestralFragment;

public class CalculatorAdapter extends FragmentPagerAdapter {

    private Context context;

    public CalculatorAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return CalculatorAnualFragment.newInstance();
            case 1:
                return CalculatorSemestralFragment.newInstance();
            default:
                return CalculatorAnualFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.disc_anual);
            case 1:
                return context.getString(R.string.disc_semestral);
        }
        return null;
    }
}