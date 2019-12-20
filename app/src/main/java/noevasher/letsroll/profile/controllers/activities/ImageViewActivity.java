package noevasher.letsroll.profile.controllers.activities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import noevasher.letsroll.R;
import noevasher.letsroll.main.controllers.activities.ParentActivity;

public class ImageViewActivity extends ParentActivity {

    @BindView(R.id.imageView_profile)
    public ImageView profile;

    @BindView(R.id.imageView_close)
    public ImageView close;

    @OnClick(R.id.imageView_close)
    public void close() {
        finish();
    }

    public Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ButterKnife.bind(this);
        String image64 = sessionManager.getKeyImage64();
        //userId = sessionManager.getUserId();

        if (image64 == null) {

        } else {
            //putImage(image64, profile);
        }

        //*/

        //putImage(image64, profile);
        close.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);

        //profile.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add_circle_24px));
    }
}
