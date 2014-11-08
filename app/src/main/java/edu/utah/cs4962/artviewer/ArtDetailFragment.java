package edu.utah.cs4962.artviewer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.UUID;

/**
 * Created by zbynek on 10/22/2014.
 */
public class ArtDetailFragment extends Fragment
{
    ImageView _artDetailView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        _artDetailView = new ImageView(getActivity());
        return _artDetailView;
    }

//    public ImageView getArtDetailView()
//    {
//        return _artDetailView;
//    }

    public void setArtDetailIdentifier(UUID identifier)
    {
        if(_artDetailView == null)
            return;

        Art art = ArtCollection.getInstance().getArt(identifier);
        _artDetailView.setImageBitmap(art.image);
    }
}
