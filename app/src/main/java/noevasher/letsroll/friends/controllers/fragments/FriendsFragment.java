package noevasher.letsroll.friends.controllers.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import noevasher.letsroll.R;
import noevasher.letsroll.commons.parents.fragments.BaseFragment;
import noevasher.letsroll.moto.controllers.activities.RegisterMotoActivity;

public class FriendsFragment extends BaseFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false); //setContentView(R.layout.fragment_login);
        ButterKnife.bind(this, view);
        
        return view;
    }


}
