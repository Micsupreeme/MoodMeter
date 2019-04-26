package pebbleinc.moodmeter;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class optionsActivity extends AppCompatActivity {
    public static final String DEFAULT_NAME = "User";
    public static final String DEFAULT_PASSWORD_WARNING = "No Password Set";
    public static final String UPDATED_MESSAGE = "Your profile has been updated!";
    public static final String CLEAR_MESSAGE = "All of your data has been cleared!";
    public static final int[] DEFAULT_DAILY_QUIZ_RESPONSES = {6, 6, 6, 6, 6, 6}; //These responses are invalid and translate to "-" in Track

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        updateFields();
    }

    private void updateFields() {
        String displayName = getProfileData(databaseContract.tableProfile.COLUMN_NAME_P_NAME);
        String displayPassword = getProfileData(databaseContract.tableProfile.COLUMN_NAME_P_MY_KEY);
        if(displayPassword.isEmpty()) {
            setPasswordWarning(DEFAULT_PASSWORD_WARNING);
        } else {
            setPasswordWarning("");
        }
        int displayTheme = Integer.parseInt(getProfileData(databaseContract.tableProfile.COLUMN_NAME_P_THEME));

        setDisplayName(displayName);
        //setDisplayPassword(displayPassword); displaying the hashed password is confusing to users
        setDisplayTheme(displayTheme);
    }

    private String getProfileData(String column) {
        databaseHelper dbHelp;
        dbHelp = new databaseHelper(this);
        Cursor profile = dbHelp.getProfileRecord(1);
        try {
            profile.moveToFirst();
            return profile.getString(profile.getColumnIndexOrThrow(column));
        } catch(SQLiteException sqle) {
            return DEFAULT_NAME;
        }
    }

    private int getThemeSelection() {
        Spinner spnTheme = findViewById(R.id.spnTheme);
        System.out.println("Item: " + spnTheme.getSelectedItem().toString());
        switch(spnTheme.getSelectedItem().toString()) {
            case "Classic":
                //Classic
                return 0;
            case "Dark":
                //Dark
                return 1;
            default:
                return 0;
        }
    }

    private void setDisplayName(String name) {
        EditText txtName = findViewById(R.id.txtName);
        txtName.setText(name);
    }

    private void setDisplayPassword(String password) {
        EditText txtPassword = findViewById(R.id.txtPassword);
        txtPassword.setText(password);
    }

    private void setDisplayTheme(int theme) {
        Spinner spnTheme = findViewById(R.id.spnTheme);
        spnTheme.setSelection(theme);
    }

    private void setPasswordWarning(String warning) {
        TextView lblPasswordWarn = findViewById(R.id.lblPasswordWarn);
        lblPasswordWarn.setText(warning);
    }

    private void updateProfileData() {
        EditText txtName = findViewById(R.id.txtName);
        EditText txtPassword = findViewById(R.id.txtPassword);

        databaseHelper dbHelp;

        dbHelp = new databaseHelper(this);
        if(!txtPassword.getText().toString().isEmpty()) { //"Nothing" ("") still creates a hash value, so if the password field is updated to "" (I would like to remove my password) then we need to bypass the hashing method and just insert "" into the database
            dbHelp.updateProfileRecord(1, txtName.getText().toString(), hashString(txtPassword.getText().toString()), getThemeSelection());
        } else {
            dbHelp.updateProfileRecord(1, txtName.getText().toString(), "", getThemeSelection());
        }
        dbHelp.close();

        dbHelp = new databaseHelper(this);
        Cursor profile = dbHelp.getProfileRecord(1);
        dbHelp.printProfileRecords(profile);
        dbHelp.close();

        updateFields();
    }

    /**
     * Uses an AlertDialog to get the user's confirmation as to whether or not they would like to
     * clear all data. Only clears the data if the user confirms their intentions.
     * @param view The control that triggered this method.
     */
    public void confirmResetHighScores(View view) {
        AlertDialog.Builder resetHighScoresConfirmation = new AlertDialog.Builder(this);
        resetHighScoresConfirmation.setMessage("Are you sure you want to clear all data? This operation cannot be undone.");

        resetHighScoresConfirmation.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //User responded "Yes", proceed with clearing data
                        clearData();
                        insertDefaultProfile();
                        insertPlaceholders();
                        updateFields();
                        displayToast(CLEAR_MESSAGE);
                        dialog.cancel();
                    }
                });

        resetHighScoresConfirmation.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //User responded "No", don't clear data
                        dialog.cancel();
                    }
                });

        AlertDialog resetHighScoresAlert = resetHighScoresConfirmation.create();
        resetHighScoresAlert.show();
    }

    private void clearData() {
        databaseHelper dbHelp;

        dbHelp = new databaseHelper(this);
        dbHelp.dropDatabase();
        dbHelp.close();

        dbHelp = new databaseHelper(this);
        dbHelp.createDatabase();
        dbHelp.close();
    }

    private void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    //Inserts the initial profile record with default values
    private void insertDefaultProfile() {
        databaseHelper dbHelp;

        dbHelp = new databaseHelper(this);
        dbHelp.insertProfileRecord("User", "", 1);
        dbHelp.close();
    }

    private void insertPlaceholders() {
        databaseHelper dbHelp;

        dbHelp = new databaseHelper(this);
        dbHelp.insertDailyQuizRecord(DEFAULT_DAILY_QUIZ_RESPONSES);
        dbHelp.close();

        dbHelp = new databaseHelper(this);
        dbHelp.insertDiaryEntryRecord("I launched the MoodMeter app!");
        dbHelp.close();
    }

    public void updateProfile(View view) {
        updateProfileData();
        displayToast(UPDATED_MESSAGE);
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
}
