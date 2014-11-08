package edu.utah.cs4962.artviewer;

import android.app.Fragment;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    int _currentPosition = -1;
    public interface OnArtSelectedListener
    {
        public void onArtSelected(ArtListFragment artListFragment, UUID identifier);
    }
    OnArtSelectedListener _onArtSelectedListener = null;

    public OnArtSelectedListener getOnArtSelectedListener()
    {
        return _onArtSelectedListener;
    }

    public void setOnArtSelectedListener(OnArtSelectedListener onArtSelectedListener)
    {
        _onArtSelectedListener = onArtSelectedListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final ListView artListView = new ListView(getActivity());
        artListView.setAdapter(this);

        artListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                _currentPosition = i;
                artListView.invalidateViews();
                if(_onArtSelectedListener != null)
                    _onArtSelectedListener.onArtSelected(ArtListFragment.this, (UUID) getItem(i));
            }
        });

        ArtCollection.getInstance().setOnArtChangedListener(new ArtCollection.OnArtChangedListener()
        {
            @Override
            public void onArtChanged()
            {
                Set<UUID> artIdentifiers = ArtCollection.getInstance().getIdentifiers();
                // TODO: order list
                _artIdentifiersByName = artIdentifiers.toArray(new UUID[artIdentifiers.size()]);

                artListView.invalidateViews();
            }
        });

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
        UUID artIdentifier = (UUID)getItem(i);
        Art art = ArtCollection.getInstance().getArt(artIdentifier);

        TextView artTitleView = new TextView(getActivity());
        artTitleView.setText(art.name);
        if(i == _currentPosition)
            artTitleView.setBackgroundColor(Color.LTGRAY);

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
