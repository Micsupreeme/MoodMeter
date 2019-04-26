package pebbleinc.moodmeter;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class diaryActivity extends AppCompatActivity {
    RecyclerView recyclerDiary;
    String currentDate;
    public static final String EXTRA_MESSAGE = "pebbleinc.MoodMeter.MESSAGE"; //defines the "extra" prefix for sending messages, ensuring that it's unique

    private ArrayList<String> allTimes = new ArrayList<>();
    private ArrayList<String> allBodies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        //Receiving the previous response
        Intent gotoDiary = getIntent();
        currentDate = gotoDiary.getStringExtra(EXTRA_MESSAGE);
        setDate(currentDate);
        System.out.println("Received date from track: " + currentDate);

        recyclerDiary = findViewById(R.id.recyclerDiary);
        refreshDiaryEntries();
        displayDiaryEntries();
    }

    /**
     * Sets up and populates the diary entries RecyclerView with diary data by sending the ArrayLists.
     * above
     */
    private void displayDiaryEntries() {
        diaryEntriesAdapter adapter = new diaryEntriesAdapter(allTimes, allBodies);
        recyclerDiary.setAdapter(adapter);
        recyclerDiary.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Gets up-to-date diary entries from the DIARY_ENTRY table of the local database.
     */
    private int refreshDiaryEntries() {
        databaseHelper dbHelp;
        dbHelp = new databaseHelper(this);

        Cursor diaryEntries = dbHelp.getDiaryEntryRecordByDate(currentDate);

        if (diaryEntries != null) {
            //Splits the Cursor (results data set) into columns and stores the values in the ArrayLists above
            allTimes = dbHelp.getDiaryEntryColumnAsList(diaryEntries, 2);
            allBodies = dbHelp.getDiaryEntryColumnAsList(diaryEntries, 3);

            //Close the database helper and Cursor (results set) to preserve memory
            diaryEntries.close();
            dbHelp.close();
            return 0;
        } else {
            return 1; //The DIARY_ENTRY table was empty
        }
    }

    //Sets the date at the top of the screen to the current date
    private void setDate(String date) {
        TextView lblDate = findViewById(R.id.lblDate);
        try{
            if(date.equals("")) {
                lblDate.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
            } else {
                lblDate.setText(date);
            }
        } catch(NullPointerException npe) {
            System.out.println("Nothing to display");
        }
    }
}
