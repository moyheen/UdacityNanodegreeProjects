package com.moyinoluwa.gdgngevents;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeGDGFragment extends Fragment {
    private View v;
    public static final List<GDGOrganizer> organizerList = new ArrayList<GDGOrganizer>();
    private TextView noGdgAvailableText;
    private TextView gdgIntroText;
    private Spinner gdgSpinner;
    private Button gdgSubmit;
    private NetworkConnection networkConnection;
    private Firebase mFirebaseRef;
    private String gdg;
    private SharedPreferences sharedPreferences;


    public ChangeGDGFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_change_gdg, container, false);

        noGdgAvailableText = (TextView) v.findViewById(R.id.no_gdgs_available);
        gdgIntroText = (TextView) v.findViewById(R.id.gdg_intro_text);
        gdgSpinner = (Spinner) v.findViewById(R.id.gdg_spinner);
        gdgSubmit = (Button) v.findViewById(R.id.submit);

        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));
        networkConnection = new NetworkConnection(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("gdg_events", Context.MODE_PRIVATE);

        displayGDGList();

        gdgSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Updates the value of the saved GDG
                SharedPreferences.Editor editor = sharedPreferences.edit();
                gdg = gdgSpinner.getSelectedItem().toString();

                editor.putString("gdg", gdg);
                editor.commit();

                Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                if (toolbar != null) {
                    String gdgName = sharedPreferences.getString("gdg", "GDG");
                    toolbar.setTitle(gdgName + " Events");
                }

                Toast.makeText(getActivity(), "GDG option saved successfully.", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    /**
     * Loads the list of GDGs from the server
     **/
    private void displayGDGList() {
        if (networkConnection.isInternetOn()) {

            mFirebaseRef.child("gdg").addValueEventListener(new ValueEventListener() {
                // Retrieve new GDGs as they are added to the database
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getChildrenCount() > 0) {
                        organizerList.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            GDGOrganizer gdgOrganizer = data.getValue(GDGOrganizer.class);
                            organizerList.add(gdgOrganizer);
                        }

                        if (organizerList.size() > 0) {
                            noGdgAvailableText.setVisibility(View.GONE);
                            gdgIntroText.setVisibility(View.VISIBLE);
                            gdgSpinner.setVisibility(View.VISIBLE);
                            gdgSubmit.setVisibility(View.VISIBLE);

                            String[] gdgNames = new String[organizerList.size()];

                            // Loads the list of GDGs into an array to populate the spinner
                            for (int i = 0; i < organizerList.size(); i++) {
                                gdgNames[i] = organizerList.get(i).getGdg();
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, gdgNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            gdgSpinner.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e("UpcomingEventsFragment", "The read failed: " + firebaseError.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_network_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
