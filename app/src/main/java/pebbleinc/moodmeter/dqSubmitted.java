package pebbleinc.moodmeter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class dqSubmitted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dq_submitted);
    }

    //Navigates to the Options page
    public void navigateMainMenu(View view) {
        Intent gotoMainMenu = new Intent(this, mainMenuActivity.class);
        startActivity(gotoMainMenu);
    }
}
