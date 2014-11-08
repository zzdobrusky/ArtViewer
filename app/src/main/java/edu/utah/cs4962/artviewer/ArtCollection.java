package edu.utah.cs4962.artviewer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
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
        {
            synchronized (ArtCollection.class)
            {
                if(_instance == null)
                    _instance = new ArtCollection();
            }
        }

        return _instance;
    }

    //region listeners

    public interface OnArtChangedListener
    {
        public void onArtChanged();
    }

    OnArtChangedListener _onArtChangedListener = null;

    public OnArtChangedListener getOnArtChangedListener()
    {
        return _onArtChangedListener;
    }

    public void setOnArtChangedListener(OnArtChangedListener _onArtChangedListener)
    {
        this._onArtChangedListener = _onArtChangedListener;
    }

    //endregion

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

        if(_onArtChangedListener != null)
            _onArtChangedListener.onArtChanged();
    }

    public void removeArt(UUID identifier)
    {
        _art.remove(identifier);

        if(_onArtChangedListener != null)
            _onArtChangedListener.onArtChanged();
    }

    public void scrapeArt(String uriString)
    {
        AsyncTask<String, Integer, URI[]> imageScrapeTask = new AsyncTask<String, Integer, URI[]>()
        {
            @Override
            protected URI[] doInBackground(String... uriStrings)
            {
                if (uriStrings.length <= 0)
                    return new URI[0];

                String uriString = uriStrings[0];

                String contentString = null;
                try
                {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet(uriString);
                    HttpResponse response = client.execute(request);

                    InputStream contentStream = response.getEntity().getContent();
                    Scanner contentScanner = new Scanner(contentStream).useDelimiter("\\A");
                    //contentString = contentScanner.hasNext() ? contentScanner.next() : null;
                    if(contentScanner.hasNext())
                        contentString = contentScanner.next();
                    else
                        contentString = null;

                    Log.i("Network", contentString);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                if(contentString == null)
                    return new URI[0];

                ArrayList<URI> imageUris = new ArrayList<URI>();
                String imageTagStart = "<img src=\"";
                String imageTagEnd = "\"";
                while(true)
                {
                    int imageTagStartIndex = contentString.indexOf(imageTagStart);
                    if (imageTagStartIndex < 0)
                        break;

                    contentString = contentString.substring(imageTagStartIndex + imageTagStart.length());
                    int imageTagEndIndex = contentString.indexOf(imageTagEnd);
                    if (imageTagEndIndex < 0)
                        break;

                    String imageUriString = contentString.substring(0, imageTagEndIndex);
                    contentString = contentString.substring(imageTagEndIndex);

                    if(imageUriString.length() <= 0)
                        continue;

                    try
                    {
                        URI imageUri = new URI(imageUriString);
                        imageUris.add(imageUri);
                        publishProgress(imageUris.size());
                    }
                    catch (Exception e)
                    {
                    }


                }

                return imageUris.toArray(new URI[imageUris.size()]);
            }

            @Override
            protected void onProgressUpdate(Integer... values)
            {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(URI[] imageUris)
            {
                super.onPostExecute(imageUris);

                final AsyncTask<URI, Integer, Bitmap[]> bitmapDownloadTask = new AsyncTask<URI, Integer, Bitmap[]>()
                {
                    @Override
                    protected Bitmap[] doInBackground(URI... bitmapUris)
                    {
                        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
                        for (URI bitmapUri : bitmapUris)
                        {
                            try
                            {
                                HttpClient client = new DefaultHttpClient();
                                HttpGet request = new HttpGet(bitmapUri);
                                HttpResponse response = client.execute(request);

                                InputStream content = response.getEntity().getContent();
                                Bitmap bitmap = BitmapFactory.decodeStream(content);
                                if(bitmap != null)
                                {
                                    bitmaps.add(bitmap);
                                    publishProgress(bitmaps.size());
                                }
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }

                        return bitmaps.toArray(new Bitmap[bitmaps.size()]);
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values)
                    {
                        super.onProgressUpdate(values);
                    }

                    @Override
                    protected void onPostExecute(Bitmap[] bitmaps)
                    {
                        super.onPostExecute(bitmaps);

                        for (Bitmap bitmap : bitmaps)
                        {
                            Art art = new Art();
                            art.name = "nytimes.com bitmap";
                            art.image = bitmap;
                            addArt(art);
                        }
                    }
                };

                bitmapDownloadTask.execute(imageUris);
            }

        };

        imageScrapeTask.execute(uriString);
    }
}
