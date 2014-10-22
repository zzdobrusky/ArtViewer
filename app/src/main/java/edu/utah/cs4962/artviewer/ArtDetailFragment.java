package edu.utah.cs4962.artviewer;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zbynek on 10/22/2014.
 */
public class ArtDetailFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View testView = new View(getActivity());
        testView.setBackgroundColor(Color.GREEN);
        return testView;
    }
}
