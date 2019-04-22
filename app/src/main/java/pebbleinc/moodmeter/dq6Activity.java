package pebbleinc.moodmeter;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.text.DateFormat;
import java.util.Calendar;

public class dq6Activity extends AppCompatActivity {

    //TODO: go to a thank you for submitting screen after submitting
    Intent gotoDQ6;
    int[] dqResponses;
    public static final String EXTRA_MESSAGE = "pebbleinc.MoodMeter.MESSAGE"; //defines the "extra" prefix for sending messages, ensuring that it's unique

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dq6);

        //Receiving the previous response
        gotoDQ6 = getIntent();
        dqResponses = gotoDQ6.getIntArrayExtra(EXTRA_MESSAGE);
        System.out.println("Received response from q5: " + dqResponses[4]);
    }

    //Handles the event when the user responds with option 1
    public void handleOption1(View view) {
        dqResponses[5] = 1;
        submitDailyQuiz();
    }

    //Handles the event when the user responds with option 2
    public void handleOption2(View view) {
        dqResponses[5] = 2;
        submitDailyQuiz();
    }

    //Handles the event when the user responds with option 3
    public void handleOption3(View view) {
        dqResponses[5] = 3;
        submitDailyQuiz();
    }

    //Handles the event when the user responds with option 4
    public void handleOption4(View view) {
        dqResponses[5] = 4;
        submitDailyQuiz();
    }

    //Handles the event when the user responds with option 5
    public void handleOption5(View view) {
        dqResponses[5] = 5;
        submitDailyQuiz();
    }

    //Handles the submission of the daily quiz
    private void submitDailyQuiz() {
        databaseHelper dbHelp;
        int numberOfQuizzesToday;
        long todayQuizId = -1;

        dbHelp = new databaseHelper(this);
        Cursor quizzesToday = dbHelp.getDailyQuizRecordByDate(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
        dbHelp.printDailyQuizRecords(quizzesToday);
        numberOfQuizzesToday = quizzesToday.getCount();

        if(numberOfQuizzesToday > 0) {
            quizzesToday.moveToFirst();
            todayQuizId = quizzesToday.getLong(quizzesToday.getColumnIndexOrThrow(databaseContract.tableDailyQuiz.COLUMN_NAME_DQ_ID));
            System.out.println("Today's quiz ID is " + todayQuizId);
        }

        dbHelp.close();

        if(numberOfQuizzesToday > 0) {
            //quiz has already been completed today, update it
            System.out.println("Update record ID " + todayQuizId);
            updateDailyQuiz(todayQuizId);
        } else {
            //quiz has not been completed today, insert a new one
            System.out.println("Insert new record");
            insertDailyQuiz();
        }

        navigateDQSubmitted();
    }

    //Inserts a new daily quiz record because there is no daily quiz record submitted for today
    private void insertDailyQuiz() {
        databaseHelper dbHelp;

        dbHelp = new databaseHelper(this);
        dbHelp.insertDailyQuizRecord(dqResponses);
        dbHelp.close();

        dbHelp = new databaseHelper(this);
        Cursor quizzes = dbHelp.getDailyQuizRecord(-1, false, -1);
        dbHelp.printDailyQuizRecords(quizzes);
        dbHelp.close();
    }

    //Updates a specified daily quiz record because there is already a daily quiz record for today
    private void updateDailyQuiz(long id) {
        databaseHelper dbHelp;

        dbHelp = new databaseHelper(this);
        dbHelp.updateDailyQuizRecord(id, dqResponses);
        dbHelp.close();

        dbHelp = new databaseHelper(this);
        Cursor quizzes = dbHelp.getDailyQuizRecord(-1, false, -1);
        dbHelp.printDailyQuizRecords(quizzes);
        dbHelp.close();
    }

    //Navigates to the Daily Quiz Submitted page
    private void navigateDQSubmitted() {
        Intent gotoDQSubmitted = new Intent(this, dqSubmitted.class);
        startActivity(gotoDQSubmitted);
    }
}