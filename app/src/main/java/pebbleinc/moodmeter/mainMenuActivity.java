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

        databaseHelper dbHelper;
        dbHelper = new databaseHelper(this);
    }

    //Navigates to the Daily Quiz (Question 1) page
    public void navigateDailyQuiz(View view) {
        Intent gotoDailyQuiz = new Intent(this, dq1Activity.class);
        startActivity(gotoDailyQuiz);

        databaseHelper dbHelp;

        dbHelp = new databaseHelper(this);
        dbHelp.deleteDailyQuizRecord(1);
        dbHelp.close();

        dbHelp = new databaseHelper(this);
        dbHelp.deleteDiaryEntryRecord(1);
        dbHelp.close();
    }

    //Navigates to the Helplines page
    public void navigateHelplines(View view) {
        Intent gotoHelplines = new Intent(this, helplinesActivity.class);
        startActivity(gotoHelplines);

        databaseHelper dbHelp;

        dbHelp = new databaseHelper(this);
        Cursor quizzes = dbHelp.getDailyQuizRecord(-1);
        dbHelp.printDailyQuizRecords(quizzes);
        dbHelp.close();

        dbHelp = new databaseHelper(this);
        Cursor diary = dbHelp.getDiaryEntryRecord(-1);
        dbHelp.printDiaryEntryRecords(diary);
        dbHelp.close();
    }
}
