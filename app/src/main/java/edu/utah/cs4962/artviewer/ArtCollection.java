package edu.utah.cs4962.artviewer;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
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
                    contentString = contentScanner.hasNext() ? contentScanner.next() : null;
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
            protected void onPostExecute(URI[] uriStrings)
            {
                super.onPostExecute(uriStrings);
            }
        };

        imageScrapeTask.execute(uriString);
    }
}
