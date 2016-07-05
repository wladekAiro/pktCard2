package com.example.wladek.pocketcard.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wladek.pocketcard.R;

/**
 * Created by wladek on 7/5/16.
 */
public class ContentFragment extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.register_card_view,container,false);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnRegBack:
                /** Do things you need to..
                 fragmentTwo = new FragmentTwo();

                 fragmentTransaction.replace(R.id.frameLayoutFragmentContainer, fragmentTwo);
                 fragmentTransaction.addToBackStack(null);

                 fragmentTransaction.commit();
                 */

                break;
        }
    }
}
