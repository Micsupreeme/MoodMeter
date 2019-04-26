package pebbleinc.moodmeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class dq1Activity extends AppCompatActivity {

    Intent gotoDQ2;
    int[] dqResponses;
    public static final String EXTRA_MESSAGE = "pebbleinc.MoodMeter.MESSAGE"; //defines the "extra" prefix for sending messages, ensuring that it's unique

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dq1);

        gotoDQ2 = new Intent(this, dq2Activity.class);
        dqResponses = new int[6];
    }

    //Handles the event when the user responds with option 1
    public void handleOption1(View view) {
        dqResponses[0] = 1;
        navigateDQ2();
    }

    //Handles the event when the user responds with option 2
    public void handleOption2(View view) {
        dqResponses[0] = 2;
        navigateDQ2();
    }

    //Handles the event when the user responds with option 3
    public void handleOption3(View view) {
        dqResponses[0] = 3;
        navigateDQ2();
    }

    //Handles the event when the user responds with option 4
    public void handleOption4(View view) {
        dqResponses[0] = 4;
        navigateDQ2();
    }

    //Handles the event when the user responds with option 5
    public void handleOption5(View view) {
        dqResponses[0] = 5;
        navigateDQ2();
    }

    //Navigates to the Daily Quiz (Question 2) page
    private void navigateDQ2() {
        gotoDQ2.putExtra(EXTRA_MESSAGE, dqResponses);
        startActivity(gotoDQ2);
    }
}
