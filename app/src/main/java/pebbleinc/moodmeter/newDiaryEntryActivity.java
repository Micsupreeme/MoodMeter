package pebbleinc.moodmeter;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Calendar;

public class newDiaryEntryActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "pebbleinc.MoodMeter.MESSAGE"; //defines the "extra" prefix for sending messages, ensuring that it's unique
    public static final String SUBMITTED_DOMAIN = "Diary.\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diary_entry);
    }

    //This method fires when the user taps the "Submit" button
    public void handleAddDiaryEntry(View view) {
        if(isValidDiaryEntry()) {
            //add
            databaseHelper dbHelp;

            dbHelp = new databaseHelper(this);
            EditText txtDiaryEntry = findViewById(R.id.txtDiaryEntry);
            dbHelp.insertDiaryEntryRecord(txtDiaryEntry.getText().toString());
            dbHelp.close();
            System.out.println("Diary entry inserted.");

            dbHelp = new databaseHelper(this);
            Cursor diaryEntries = dbHelp.getDiaryEntryRecordByDate(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
            dbHelp.printDiaryEntryRecords(diaryEntries);
            dbHelp.close();

            navigateSubmitted();

        } else {
            System.out.println("Diary entry is invalid.");
        }
    }

    //This simple method determines whether or not the diary entry is valid
    private boolean isValidDiaryEntry() {
        EditText txtDiaryEntry = findViewById(R.id.txtDiaryEntry);
        return !(txtDiaryEntry.getText().toString().isEmpty() || txtDiaryEntry.getText().toString().equals(" ") || txtDiaryEntry.getText().toString().equals("\n"));
    }

    //Navigates to the submitted page
    private void navigateSubmitted() {
        Intent gotoSubmitted = new Intent(this, submittedActivity.class);
        gotoSubmitted.putExtra(EXTRA_MESSAGE, SUBMITTED_DOMAIN);
        startActivity(gotoSubmitted);
    }
}
