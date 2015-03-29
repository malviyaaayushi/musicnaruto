package com.uzumaki.naruto;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by aarushi on 27/3/15.
 */
public class MusicLoader extends AsyncTaskLoader<ArrayList<Song>> {

    private ArrayList<Song> main_songs_list;
    private Cursor musicCursor;

    public MusicLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Song> loadInBackground() {
        ArrayList<Song> songs_list = new ArrayList<Song>();

        //query external audio
        ContentResolver musicResolver = getContext().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        musicCursor = musicResolver.query(musicUri, null, null, null, null);

        //iterate over results if valid
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songs_list.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }

        return songs_list;
    }

    @Override
    public void deliverResult(ArrayList<Song> data) {
        if(isReset())
        {
            releaseResources(main_songs_list);
        }
        ArrayList<Song> oldData = main_songs_list;
        main_songs_list = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (main_songs_list != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(main_songs_list);
        }

        /*
        // Begin monitoring the underlying data source.
        if (mObserver == null) {
            mObserver = new SampleObserver();
            // TODO: register the observer
        }
        */
        if (takeContentChanged() || main_songs_list == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (main_songs_list != null) {
            releaseResources(main_songs_list);
            main_songs_list = null;
        }
    }

    @Override
    public void onCanceled(ArrayList<Song> data) {
        super.onCanceled(data);
        releaseResources(data);
    }

    private void releaseResources(ArrayList<Song> data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we
        // would close it in this method. All resources associated with the Loader
        // should be released here.
        musicCursor.close();
    }


}
