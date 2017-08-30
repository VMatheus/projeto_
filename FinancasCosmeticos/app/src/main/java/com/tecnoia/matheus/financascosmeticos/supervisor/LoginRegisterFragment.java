package com.tecnoia.matheus.financascosmeticos.supervisor;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tecnoia.matheus.financascosmeticos.R;

public class LoginRegisterFragment extends Fragment {

    private FragmentStatePagerAdapter mSectionsPagerAdaper;
    private FragmentActivity mContext;
    private ViewPager viewPager;



    public static LoginRegisterFragment newInstace() {
        LoginRegisterFragment loginRegisterFragmentFragment = new LoginRegisterFragment();
        return loginRegisterFragmentFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_login_register, container, false);

        mSectionsPagerAdaper = new SectionsPagerAdaper(mContext.getSupportFragmentManager());

        viewPager = rootView.findViewById(R.id.container);
        viewPager.setAdapter(mSectionsPagerAdaper);

        TabLayout tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return rootView;

    }

    @Override
    public void onAttach(Activity context) {
        mContext = (FragmentActivity) context;
        super.onAttach(context);
    }

    private class SectionsPagerAdaper extends FragmentStatePagerAdapter {
        public SectionsPagerAdaper(FragmentManager fragmentManager) {
            super(fragmentManager);
        }


        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.acessar);
                case 1:
                    return getString(R.string.registrar);
            }
            return null;

        }

        @Override
        public Fragment getItem(int position) {
            LoginSupervisor loginSupervisor = new LoginSupervisor();
            CadastroSupervisor cadastroSupervisor = new CadastroSupervisor();
            switch (position) {
                case 0:
                    return loginSupervisor;
                case 1:
                    return cadastroSupervisor;

                default:
                    return loginSupervisor;
            }


        }
    }
}
