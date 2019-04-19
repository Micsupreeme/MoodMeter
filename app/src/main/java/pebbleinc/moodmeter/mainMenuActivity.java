package pebbleinc.moodmeter;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

//todo: catch "android.database.sqlite.SQLiteException: no such table" in case database is bad, cause it to re-create

public class mainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    //Navigates to the Daily Quiz (Question 1) page
    public void navigateDailyQuiz(View view) {
        Intent gotoDailyQuiz = new Intent(this, dq1Activity.class);
        startActivity(gotoDailyQuiz);
    }

    //Navigates to the New Diary Entry page
    public void navigateNewDiaryEntry(View view) {
        Intent gotoNewDiaryEntry = new Intent(this, newDiaryEntryActivity.class);
        startActivity(gotoNewDiaryEntry);
    }

    //Navigates to the Track page
    public void navigateTrack(View view) {
        Intent gotoTrack = new Intent(this, trackActivity.class);
        startActivity(gotoTrack);
    }

    //Navigates to the Helplines page
    public void navigateHelplines(View view) {
        Intent gotoHelplines = new Intent(this, helplinesActivity.class);
        startActivity(gotoHelplines);
    }

    //Navigates to the Options page
    public void navigateOptions(View view) {
        Intent gotoOptions = new Intent(this, optionsActivity.class);
        startActivity(gotoOptions);
    }
}
