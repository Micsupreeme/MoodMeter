package pebbleinc.moodmeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class dq3Activity extends AppCompatActivity {

    Intent gotoDQ3;
    Intent gotoDQ4;
    int[] dqResponses;
    public static final String EXTRA_MESSAGE = "pebbleinc.MoodMeter.MESSAGE"; //defines the "extra" prefix for sending messages, ensuring that it's unique

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dq3);

        //Receiving the previous response
        gotoDQ3 = getIntent();
        dqResponses = gotoDQ3.getIntArrayExtra(EXTRA_MESSAGE);
        System.out.println("Received response from q2: " + dqResponses[1]);

        //Preparing to send the next response
        gotoDQ4 = new Intent(this, dq4Activity.class);
    }

    //Handles the event when the user responds with option 1
    public void handleOption1(View view) {
        dqResponses[2] = 1;
        navigateDQ4();
    }

    //Handles the event when the user responds with option 2
    public void handleOption2(View view) {
        dqResponses[2] = 2;
        navigateDQ4();
    }

    //Handles the event when the user responds with option 3
    public void handleOption3(View view) {
        dqResponses[2] = 3;
        navigateDQ4();
    }

    //Handles the event when the user responds with option 4
    public void handleOption4(View view) {
        dqResponses[2] = 4;
        navigateDQ4();
    }

    //Handles the event when the user responds with option 5
    public void handleOption5(View view) {
        dqResponses[2] = 5;
        navigateDQ4();
    }

    //Navigates to the Daily Quiz (Question 4) page
    private void navigateDQ4() {
        gotoDQ4.putExtra(EXTRA_MESSAGE, dqResponses);
        startActivity(gotoDQ4);
    }
}
