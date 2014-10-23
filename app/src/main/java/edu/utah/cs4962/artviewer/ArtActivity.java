package edu.utah.cs4962.artviewer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


public class ArtActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Add test info
        Art mona = new Art();
        mona.name = "Mona Lisa";
        mona.image = R.drawable.mona;
        ArtCollection.getInstance().addArt(mona);

        Art girl1 = new Art();
        girl1.name = "girl1";
        girl1.image = R.drawable.girl1;
        ArtCollection.getInstance().addArt(girl1);

        Art girl2 = new Art();
        girl2.name = "girl2";
        girl2.image = R.drawable.girl2;
        ArtCollection.getInstance().addArt(girl2);

        Art girl3 = new Art();
        girl3.name = "girl3";
        girl3.image = R.drawable.girl3;
        ArtCollection.getInstance().addArt(girl3);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        setContentView(rootLayout);

        FrameLayout artListLayout = new FrameLayout(this);
        artListLayout.setId(10);
        LinearLayout.LayoutParams artListLayoutLP = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                30
        );
        rootLayout.addView(artListLayout, artListLayoutLP);

        FrameLayout artDetailLayout = new FrameLayout(this);
        artDetailLayout.setId(20);
        LinearLayout.LayoutParams artDetailLayoutLP = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                70
        );
        rootLayout.addView(artDetailLayout, artDetailLayoutLP);

        FragmentTransaction addTransaction = getFragmentManager().beginTransaction();
        addTransaction.add(10, new ArtListFragment());
        addTransaction.add(20, new ArtDetailFragment());
        addTransaction.commit();
    }
}
