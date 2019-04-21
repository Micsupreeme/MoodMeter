package pebbleinc.moodmeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class dq4Activity extends AppCompatActivity {

    Intent gotoDQ4;
    Intent gotoDQ5;
    int[] dqResponses;
    public static final String EXTRA_MESSAGE = "pebbleinc.MoodMeter.MESSAGE"; //defines the "extra" prefix for sending messages, ensuring that it's unique

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dq4);

        //Receiving the previous response
        gotoDQ4 = getIntent();
        dqResponses = gotoDQ4.getIntArrayExtra(EXTRA_MESSAGE);
        System.out.println("Received response from q3: " + dqResponses[2]);

        //Preparing to send the next response
        gotoDQ5 = new Intent(this, dq5Activity.class);
    }

    //Handles the event when the user responds with option 1
    public void handleOption1(View view) {
        dqResponses[3] = 1;
        navigateDQ5();
    }

    //Handles the event when the user responds with option 2
    public void handleOption2(View view) {
        dqResponses[3] = 2;
        navigateDQ5();
    }

    //Handles the event when the user responds with option 3
    public void handleOption3(View view) {
        dqResponses[3] = 3;
        navigateDQ5();
    }

    //Handles the event when the user responds with option 4
    public void handleOption4(View view) {
        dqResponses[3] = 4;
        navigateDQ5();
    }

    //Handles the event when the user responds with option 5
    public void handleOption5(View view) {
        dqResponses[3] = 5;
        navigateDQ5();
    }

    //Navigates to the Daily Quiz (Question 4) page
    private void navigateDQ5() {
        gotoDQ5.putExtra(EXTRA_MESSAGE, dqResponses);
        startActivity(gotoDQ5);
    }
}
