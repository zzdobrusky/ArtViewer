package edu.utah.cs4962.artviewer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by zbynek on 10/22/2014.
 */
public class ArtCollection
{
    static ArtCollection _instance = null;
    Map<UUID, Art> _art = new HashMap<UUID, Art>();

    static ArtCollection getInstance()
    {
        if (_instance == null)
            _instance = new ArtCollection();
        return _instance;
    }

    private ArtCollection()
    {
    }

    public Set<UUID> getIdentifiers()
    {
        return _art.keySet();
    }

    public Art getArt(UUID identifier)
    {
      return _art.get(identifier);
    }

    public void addArt(Art art)
    {
        UUID identifier = UUID.randomUUID();
        art.identifier = identifier;
        _art.put(identifier, art);
    }

    public void removeArt(UUID identifier)
    {
        _art.remove(identifier);
    }
}
