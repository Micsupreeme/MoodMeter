package pebbleinc.moodmeter;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class submittedActivity extends AppCompatActivity {
    Intent gotoSubmitted;
    String domain = "";
    public static final String EXTRA_MESSAGE = "pebbleinc.MoodMeter.MESSAGE"; //defines the "extra" prefix for sending messages, ensuring that it's unique
    public static final String DEFAULT_NAME = "User";
    public static final String DEFAULT_TITLE = "Well Done";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitted);
        gotoSubmitted = getIntent();
        domain = gotoSubmitted.getStringExtra(EXTRA_MESSAGE);
        setTitle();
        setSubtitle();
    }

    private void setTitle() {
        String title = DEFAULT_TITLE + "!\n";
        TextView lblTitle = findViewById(R.id.lblTitle);

        String name = getName();
        if(!name.equals(DEFAULT_NAME)) {
            title = DEFAULT_TITLE + "\n" + name + "!";
        }
        lblTitle.setText(title);
    }

    private String getName() {
        String pName;
        databaseHelper dbHelp = new databaseHelper(this);
        Cursor profile = dbHelp.getProfileRecord(1);

        try {
            profile.moveToFirst();
        } catch(NullPointerException e) {
            System.out.println("No records to print from PROFILE.");
            return DEFAULT_NAME;
        }
        do {
            try{
                pName = profile.getString(profile.getColumnIndexOrThrow(databaseContract.tableProfile.COLUMN_NAME_P_NAME));
            } catch(CursorIndexOutOfBoundsException cioobe) {
                System.out.println("Warning: attempted to read an empty record.");
                return DEFAULT_NAME;
            }
        } while (profile.moveToNext());
        return pName;
    }

    private void setSubtitle() {
       TextView lblSubtitle = findViewById(R.id.lblSubtitle);
       String subtitle = "You have updated your " + domain;
       lblSubtitle.setText(subtitle);
    }

    //Navigates to the The Main Menu page
    public void navigateMainMenu(View view) {
        Intent gotoMainMenu = new Intent(this, mainMenuActivity.class);
        startActivity(gotoMainMenu);
    }

    //Navigates to the tracker page
    public void navigateTrack(View view) {
        Intent gotoTrack = new Intent(this, trackActivity.class);
        startActivity(gotoTrack);
    }
}
