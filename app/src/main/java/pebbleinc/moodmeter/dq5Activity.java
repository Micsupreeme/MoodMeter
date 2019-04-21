package pebbleinc.moodmeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class dq5Activity extends AppCompatActivity {

    Intent gotoDQ5;
    Intent gotoDQ6;
    int[] dqResponses;
    public static final String EXTRA_MESSAGE = "pebbleinc.MoodMeter.MESSAGE"; //defines the "extra" prefix for sending messages, ensuring that it's unique

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dq5);

        //Receiving the previous response
        gotoDQ5 = getIntent();
        dqResponses = gotoDQ5.getIntArrayExtra(EXTRA_MESSAGE);
        System.out.println("Received response from q4: " + dqResponses[3]);

        //Preparing to send the next response
        gotoDQ6 = new Intent(this, dq6Activity.class);
    }

    //Handles the event when the user responds with option 1
    public void handleOption1(View view) {
        dqResponses[4] = 1;
        navigateDQ6();
    }

    //Handles the event when the user responds with option 2
    public void handleOption2(View view) {
        dqResponses[4] = 2;
        navigateDQ6();
    }

    //Handles the event when the user responds with option 3
    public void handleOption3(View view) {
        dqResponses[4] = 3;
        navigateDQ6();
    }

    //Handles the event when the user responds with option 4
    public void handleOption4(View view) {
        dqResponses[4] = 4;
        navigateDQ6();
    }

    //Handles the event when the user responds with option 5
    public void handleOption5(View view) {
        dqResponses[4] = 5;
        navigateDQ6();
    }

    //Navigates to the Daily Quiz (Question 4) page
    private void navigateDQ6() {
        gotoDQ6.putExtra(EXTRA_MESSAGE, dqResponses);
        startActivity(gotoDQ6);
    }
}
