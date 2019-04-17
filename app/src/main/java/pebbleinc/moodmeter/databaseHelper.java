package pebbleinc.moodmeter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import pebbleinc.moodmeter.databaseContract.tableDailyQuiz;
import pebbleinc.moodmeter.databaseContract.tableDiaryEntry;
import pebbleinc.moodmeter.databaseContract.tableProfile;

public class databaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MoodMeter.db";

    //SQL CREATE scripts
    private static final String SQL_CREATE_DAILY_QUIZ =
            "CREATE TABLE " + tableDailyQuiz.TABLE_NAME + " (" +
            tableDailyQuiz.COLUMN_NAME_DQ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + tableDailyQuiz.COLUMN_NAME_DQ_DATE + " NUMERIC, " +
            tableDailyQuiz.COLUMN_NAME_DQ_Q1 + " INTEGER, " +
            tableDailyQuiz.COLUMN_NAME_DQ_Q2 + " INTEGER, " +
            tableDailyQuiz.COLUMN_NAME_DQ_Q3 + " INTEGER, " +
            tableDailyQuiz.COLUMN_NAME_DQ_Q4 + " INTEGER, " +
            tableDailyQuiz.COLUMN_NAME_DQ_Q5 + " INTEGER, " +
            tableDailyQuiz.COLUMN_NAME_DQ_Q6 + " INTEGER)";
    private static final String SQL_CREATE_DIARY_ENTRY =
            "CREATE TABLE " + tableDiaryEntry.TABLE_NAME + " (" +
            tableDiaryEntry.COLUMN_NAME_DE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + tableDiaryEntry.COLUMN_NAME_DE_DATETIME + " NUMERIC, " +
            tableDiaryEntry.COLUMN_NAME_DE_BODY + " TEXT)";
    private static final String SQL_CREATE_PROFILE =
            "CREATE TABLE " + tableProfile.TABLE_NAME + " (" +
            tableProfile.COLUMN_NAME_P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + tableProfile.COLUMN_NAME_P_NAME + " TEXT, " +
            tableProfile.COLUMN_NAME_P_MY_KEY + " TEXT, " +
            tableProfile.COLUMN_NAME_P_THEME + " INTEGER)";

    //SQL DROP scripts
    private static final String SQL_DROP_DAILY_QUIZ = "DROP TABLE IF EXISTS " + tableDailyQuiz.TABLE_NAME;
    private static final String SQL_DROP_DIARY_ENTRY = "DROP TABLE IF EXISTS " + tableDiaryEntry.TABLE_NAME;
    private static final String SQL_DROP_PROFILE = "DROP TABLE IF EXISTS " + tableProfile.TABLE_NAME;

    databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Automatically creates the database
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DAILY_QUIZ);
        db.execSQL(SQL_CREATE_DIARY_ENTRY);
        db.execSQL(SQL_CREATE_PROFILE);
    }

    //Automatically re-creates the database
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_PROFILE);
        db.execSQL(SQL_DROP_DIARY_ENTRY);
        db.execSQL(SQL_DROP_DAILY_QUIZ);
        onCreate(db);
    }

    //Creates the database
    public void createDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_CREATE_DAILY_QUIZ);
        db.execSQL(SQL_CREATE_DIARY_ENTRY);
        db.execSQL(SQL_CREATE_PROFILE);
        db.close();
        System.out.println("Database created!");
    }

    //Deletes the database
    public void dropDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DROP_PROFILE);
        db.execSQL(SQL_DROP_DIARY_ENTRY);
        db.execSQL(SQL_DROP_DAILY_QUIZ);
        db.close();
        System.out.println("Database dropped!");
    }

    //Functions for the PROFILE table

    //GETs 1 or all of the records from the PROFILE table.
    //To get a single record, pass in the record's "p_id".
    //To get all records, pass in -1.
    public Cursor getProfileRecord(int id) {
        boolean hasSpecifiedRecord = false;
        SQLiteDatabase db = getReadableDatabase(); //Obtains read access

        //Returns the following columns
        String[] projection = {
                tableProfile.COLUMN_NAME_P_ID,
                tableProfile.COLUMN_NAME_P_NAME,
                tableProfile.COLUMN_NAME_P_MY_KEY,
                tableProfile.COLUMN_NAME_P_THEME
        };
        //Returns records that match the following conditions
        String selection = "";
        String[] selectionArgs = new String[1];
        if(id != -1) {
            hasSpecifiedRecord = true;
            String strId = "" + id;
            selection = tableProfile.COLUMN_NAME_P_ID + " = ?";
            selectionArgs[0] = strId;
        }
        //Sorts the records
        String sortOrder = tableProfile.COLUMN_NAME_P_ID + " DESC";
        //Limits the number of records
        String limitBy = null;

        Cursor results;
        if(hasSpecifiedRecord) { //Only include the selection arguments if a single record is being returned
            try{
                results = db.query(
                        tableProfile.TABLE_NAME, //Target table (FROM TABLE)
                        projection, //Projection (SELECT...)
                        selection, //Selection (WHERE...)
                        selectionArgs, //Selection arguments (COLUMN = X)
                        null, //Row grouping?
                        null, //Filter by row grouping?
                        sortOrder, //Ordering (ORDER BY)
                        limitBy //Limiting (LIMIT BY)
                );
                System.out.println("Single record returned from PROFILE.");
                return results;
            } catch(SQLiteException e) {
                e.printStackTrace();
            }
        } else {
            try {
                results = db.query(
                        tableProfile.TABLE_NAME, //Target table (FROM TABLE)
                        projection, //Projection (SELECT...)
                        null, //Selection (WHERE...)
                        null, //Selection arguments (COLUMN = X)
                        null, //Row grouping?
                        null, //Filter by row grouping?
                        sortOrder, //Ordering (ORDER BY)
                        limitBy //Limiting (LIMIT BY)
                );
                System.out.println("All records returned from PROFILE.");
                return results;
            } catch(SQLiteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //Inserts a record into the PROFILE table
    public void insertProfileRecord(String name, String myKey, int theme) {
        SQLiteDatabase db = getWritableDatabase(); //Obtains write access

        //Defines values for the INSERT INTO operation
        ContentValues values = new ContentValues();
        values.put(tableProfile.COLUMN_NAME_P_NAME, name);
        values.put(tableProfile.COLUMN_NAME_P_MY_KEY, myKey);
        values.put(tableProfile.COLUMN_NAME_P_THEME, theme);

        //Insert the new row with the next automatically determined auto-increment SCORE_ID
        db.insert(tableProfile.TABLE_NAME, tableProfile.COLUMN_NAME_P_ID, values);
        db.close();
        System.out.println("Record inserted into PROFILE.");
    }

    //Prints records that have been retrieved from the PROFILE table to the console
    public void printProfileRecords(Cursor records) {
        try {
            records.moveToFirst();
        } catch(NullPointerException e) {
            System.out.println("No records to print from PROFILE.");
            return;
        }
        System.out.println("P_ID\t\tP_NAME\t\tP_MY_KEY\t\tP_THEME");
        List<Long> pIds = new ArrayList<>();
        List<String> pNames = new ArrayList<>();
        List<String> pMyKeys = new ArrayList<>();
        List<Integer> pThemes = new ArrayList<>();
        while(records.moveToNext()) {
            long pId = records.getLong(records.getColumnIndexOrThrow(tableProfile.COLUMN_NAME_P_ID));
            pIds.add(pId);
            String pName = records.getString(records.getColumnIndexOrThrow(tableProfile.COLUMN_NAME_P_NAME));
            pNames.add(pName);
            String pMyKey = records.getString(records.getColumnIndexOrThrow(tableProfile.COLUMN_NAME_P_MY_KEY));
            pMyKeys.add(pMyKey);
            int pTheme = records.getInt(records.getColumnIndexOrThrow(tableProfile.COLUMN_NAME_P_THEME));
            pThemes.add(pTheme);
            System.out.println(pId + "\t\t\t" + pName + "\t\t\t" + pMyKey + "\t\t\t" + pTheme);
        }
        System.out.println("Printed records from PROFILE.");
    }
}