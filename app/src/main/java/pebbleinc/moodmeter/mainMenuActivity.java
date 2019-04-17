package pebbleinc.moodmeter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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

    //Navigates to the Helplines page
    public void navigateHelplines(View view) {
        Intent gotoHelplines = new Intent(this, helplinesActivity.class);
        startActivity(gotoHelplines);
    }
}
