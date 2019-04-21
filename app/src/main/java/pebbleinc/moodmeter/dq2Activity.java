package pebbleinc.moodmeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class dq2Activity extends AppCompatActivity {

    Intent gotoDQ2;
    Intent gotoDQ3;
    int[] dqResponses;
    public static final String EXTRA_MESSAGE = "pebbleinc.MoodMeter.MESSAGE"; //defines the "extra" prefix for sending messages, ensuring that it's unique

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dq2);

        //Receiving the previous response
        gotoDQ2 = getIntent();
        dqResponses = gotoDQ2.getIntArrayExtra(EXTRA_MESSAGE);
        System.out.println("Received response from q1: " + dqResponses[0]);

        //Preparing to send the next response
        gotoDQ3 = new Intent(this, dq3Activity.class);
    }

    //Handles the event when the user responds with option 1
    public void handleOption1(View view) {
        dqResponses[1] = 1;
        navigateDQ3();
    }

    //Handles the event when the user responds with option 2
    public void handleOption2(View view) {
        dqResponses[1] = 2;
        navigateDQ3();
    }

    //Handles the event when the user responds with option 3
    public void handleOption3(View view) {
        dqResponses[1] = 3;
        navigateDQ3();
    }

    //Handles the event when the user responds with option 4
    public void handleOption4(View view) {
        dqResponses[1] = 4;
        navigateDQ3();
    }

    //Handles the event when the user responds with option 5
    public void handleOption5(View view) {
        dqResponses[1] = 5;
        navigateDQ3();
    }

    //Navigates to the Daily Quiz (Question 3) page
    private void navigateDQ3() {
        gotoDQ3.putExtra(EXTRA_MESSAGE, dqResponses);
        startActivity(gotoDQ3);
    }

}