package pebbleinc.moodmeter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class trackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
    }

    //Navigates to the Diary page
    public void navigateDiary(View view) {
        Intent gotoDiary = new Intent(this, diaryActivity.class);
        startActivity(gotoDiary);
    }
}
