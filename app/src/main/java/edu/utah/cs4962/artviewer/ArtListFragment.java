package edu.utah.cs4962.artviewer;

import android.app.Fragment;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;
import java.util.UUID;

/**
 * Created by zbynek on 10/22/2014.
 */
public class ArtListFragment extends Fragment implements ListAdapter
{
    UUID[] _artIdentifiersByName = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ListView artListView = new ListView(getActivity());
        artListView.setAdapter(this);
        return artListView;
    }

    @Override
    public boolean isEmpty()
    {
        return getCount() <= 0;
    }

    @Override
    public int getCount()
    {
        return ArtCollection.getInstance().getIdentifiers().size();
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public Object getItem(int i)
    {
        if(_artIdentifiersByName == null)
        {
            Set<UUID> artIdentifiers = ArtCollection.getInstance().getIdentifiers();
            // TODO: Order list
            _artIdentifiersByName = artIdentifiers.toArray(new UUID[artIdentifiers.size()]);
        }
        return _artIdentifiersByName[(int)getItemId(i)];
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        UUID artIdentifier = (UUID)getItem(i); //(UUID)_artIdentifiersByName[(int)getItemId(i)];
        Art art = ArtCollection.getInstance().getArt(artIdentifier);

        TextView artTitleView = new TextView(getActivity());
        artTitleView.setText(art.name);

        return artTitleView;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public int getItemViewType(int i)
    {
        return 0;
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int i)
    {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver)
    {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver)
    {

    }


}
