package com.moyinoluwa.gdgngevents.Favorites;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moyinoluwa.gdgngevents.Data.EventsContract;
import com.moyinoluwa.gdgngevents.Data.EventsProvider;
import com.moyinoluwa.gdgngevents.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteEventsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    protected View v;
    private boolean mTwoPane;
    private static final int URL_LOADER = 0;
    private RecyclerView recyclerView;
    private TextView eventType;
    private TextView noEventsAvailable;
    private FavoritesRecyclerViewAdapter mRecyclerViewAdapter;

    public FavoriteEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_favorite_events, container, false);

        eventType = (TextView) v.findViewById(R.id.event_type);
        noEventsAvailable = (TextView) v.findViewById(R.id.no_events_available);
        recyclerView = (RecyclerView) v.findViewById(R.id.item_list);

        new EventLoaderAsyncTask().execute();

        return v;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, View v, Cursor cursor) {
        if (v.findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mRecyclerViewAdapter =
                new FavoritesRecyclerViewAdapter(cursor, mTwoPane, getActivity());
        recyclerView.setAdapter(mRecyclerViewAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EventsProvider.CONTENT_URI, EventsContract.Events.PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRecyclerViewAdapter.swapCursor(data);
        mRecyclerViewAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerViewAdapter.swapCursor(null);
    }

    public class EventLoaderAsyncTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {

            Cursor cursor = getContext().getContentResolver().query(
                    EventsProvider.CONTENT_URI, EventsContract.Events.PROJECTION,
                    null, null, null);

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            // Confirm that the cursor is not empty before changing the views
            if (cursor.getCount() > 0) {

                assert recyclerView != null;
                setupRecyclerView(recyclerView, v, cursor);

                getLoaderManager().initLoader(URL_LOADER, null, FavoriteEventsFragment.this);

                noEventsAvailable.setVisibility(View.GONE);
                eventType.setText(getResources().getString(R.string.favorite_events));
                eventType.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
}
