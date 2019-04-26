package pebbleinc.moodmeter;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Calendar;

public class authenticateActivity extends AppCompatActivity {

    public static final int[] DEFAULT_DAILY_QUIZ_RESPONSES = {6, 6, 6, 6, 6, 6}; //These responses are invalid and translate to "-" in Track

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        handleProfileSetup();
        handlePlaceholders();
        if(!isPasswordProtected()) {
            //Not password-protected - proceed directly to Main Menu
            navigateMainMenu();
        }
    }

    public void authenticate(View view) {
        EditText txtPassword = findViewById(R.id.txtPassword);
        if(isAuthenticated(txtPassword.getText().toString())) {
            //Password match
            displayToast("Login Successful!");
            navigateMainMenu();
        } else {
            //No password match
            displayToast("Invalid Password!");
        }
    }

    private boolean isAuthenticated(String password) {
        databaseHelper dbHelp;
        dbHelp = new databaseHelper(this);
        Cursor profile = dbHelp.getProfileRecordByPassword(hashString(password));
        if(dbHelp.printProfileRecords(profile)) {
            System.out.println("Password Match!");
            return true;
        } else {
            System.out.println("No Password Match!");
            return false;
        }
    }

    //Takes a string and returns the hashed SHA-256 equivalent
    private String hashString(String str) {
        String hashed = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            hashed = String.format( "%064x", new BigInteger(1, digest));
            System.out.println("Hashing successful!");
            return hashed;
        } catch(NoSuchAlgorithmException nsae) {
            System.out.println("Hashing error!");
            return hashed;
        }
    }

    private void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private boolean isPasswordProtected() {
        String storedPassword = getProfileData(databaseContract.tableProfile.COLUMN_NAME_P_MY_KEY);
        if(!storedPassword.isEmpty()) {
            System.out.println("This installation is password-protected");
            return true;
        } else {
            System.out.println("This installation is not password-protected");
            return false;
        }
    }

    private String getProfileData(String column) {
        databaseHelper dbHelp;
        dbHelp = new databaseHelper(this);
        Cursor profile = dbHelp.getProfileRecord(1);
        try {
            profile.moveToFirst();
            return profile.getString(profile.getColumnIndexOrThrow(column));
        } catch(SQLiteException sqle) {
            return "";
        }
    }

    //Navigates to the The Main Menu page
    private void navigateMainMenu() {
        Intent gotoMainMenu = new Intent(this, mainMenuActivity.class);
        startActivity(gotoMainMenu);
    }

    //Initial startup housekeeping database methods
    //*********************************************

    //Inserts the initial profile record with default values
    private void insertDefaultProfile() {
        databaseHelper dbHelp;

        dbHelp = new databaseHelper(this);
        dbHelp.insertProfileRecord("User", "", 1);
        dbHelp.close();
    }

    //Decides whether or not first-time startup operations are necessary
    //Triggers these operations if necessary
    private void handleProfileSetup() {
        try {
            if(profileSetup()) {
                System.out.println("Profile is setup");
            } else {
                System.out.println("Profile table does not exist - first-time startup!");

                databaseHelper dbHelp;

                dbHelp = new databaseHelper(this);
                dbHelp.createDatabase();
                dbHelp.close();
                insertDefaultProfile();
            }
        } catch(SQLiteException sqle) {
            System.out.println("Profile table does not exist - first-time startup!");

            databaseHelper dbHelp;

            dbHelp = new databaseHelper(this);
            dbHelp.createDatabase();
            dbHelp.close();
            insertDefaultProfile();
        }
    }

    //Checks whether or not a profile has been setup
    //Returns true if it has, false otherwise
    private boolean profileSetup() {
        databaseHelper dbHelp = new databaseHelper(this);
        Cursor profile = dbHelp.getProfileRecord(1);
        return dbHelp.printProfileRecords(profile);
    }

    //Decides whether or not new placeholders need to be created for today to prevent displaying null values
    //Inserts these placeholders if necessary
    private void handlePlaceholders() {
        if (!dailyQuizDoneToday()) {
            System.out.println("Daily Quiz not yet completed; inserting for today with default responses.");

            databaseHelper dbHelp;

            dbHelp = new databaseHelper(this);
            dbHelp.insertDailyQuizRecord(DEFAULT_DAILY_QUIZ_RESPONSES);
            dbHelp.close();

            dbHelp = new databaseHelper(this);
            dbHelp.insertDiaryEntryRecord("I launched the MoodMeter app!");
            dbHelp.close();
        } else {
            System.out.println("Daily Quiz already exists for today.");
        }
    }

    //Checks whether or not a daily quiz has already been completed for today
    //Returns true if it has, false otherwise
    private boolean dailyQuizDoneToday() {
        databaseHelper dbHelp = new databaseHelper(this);
        Cursor quizzesToday = dbHelp.getDailyQuizRecordByDate(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
        boolean doneToday = dbHelp.printDailyQuizRecords(quizzesToday);
        dbHelp.close();
        return doneToday;
    }
}
