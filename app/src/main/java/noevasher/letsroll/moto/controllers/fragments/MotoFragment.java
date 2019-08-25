package noevasher.letsroll.moto.controllers.fragments;

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

public class MotoFragment extends BaseFragment {
    //private final ExtLogger mlog = ExtLogger.getLogger(MotoFragment.class);
    private static final String ERROR_ADD_IMAGE = "login_ads_image_error";

    @BindView(R.id.view_flipper_moto)
    public ViewFlipper mFlipperMoto;

    @BindView(R.id.button_add_moto)
    public Button addBtn;

    /*
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAvailableActivity(activity -> {
            ((MainActivity)activity).setSearchFromLocation(false);
        });
    }

//*/
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moto, container, false); //setContentView(R.layout.fragment_login);
        ButterKnife.bind(this, view);

        listenerAdd();
        getAvailableActivity(activity -> {
        });


        return view;
    }

    private void listenerAdd() {
        addBtn.setOnClickListener(l -> {
            Intent intent = new Intent(getActivity(), RegisterMotoActivity.class);
            startActivity(intent);
        });
    }
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //case Consts.RESULTS_REQUEST_CODE_ID:
            case Consts.LOGIN_REQUEST_CODE_ID:
                if (resultCode == Activity.RESULT_OK) {
                    //((MainActivity) getActivity()).switchFragment(Consts.LOGIN_FRAGMENT, data.getExtras());
                    replaceFragment(requestCode, data.getExtras());
                }
                break;
        }
    }

    private void replaceFragment(int requestcode, Bundle params){
        //this.currentSearchFragment = Consts.RESULTS_FRAGMENT;
        getAvailableActivity(activity -> {
            ResultsFragment fragment = new ResultsFragment();
            if (params !=null) fragment.setArguments(params);

            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            //transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);

            if (requestcode == Consts.LOGIN_REQUEST_CODE_ID) {
                transaction.replace(R.id.ConstraintLayout_login, fragment).addToBackStack(null).commitAllowingStateLoss();
                //transaction.addToBackStack(null);

                //((MainActivity)getActivity()).setCurrentSearchFragment(Consts.RESULTS_FRAGMENT);
            }else if (requestcode == Consts.RESULTS_REQUEST_CODE_ID){
                transaction.replace(R.id.ConstraintLayout_login, fragment).commitAllowingStateLoss();
            }
            ((MainActivity)activity).replaceLoginFragment(fragment);
        });
    }
    //*/

}
