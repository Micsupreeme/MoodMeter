package pebbleinc.moodmeter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pebbleinc.moodmeter.databaseContract.tableDailyQuiz;
import pebbleinc.moodmeter.databaseContract.tableDiaryEntry;
import pebbleinc.moodmeter.databaseContract.tableProfile;

/**Use this class to manipulate the local database
 * Use an ID to specify a single record in a table, or use "-1" to specify all records in a table
 *
 * Methods available:
 * Profile table:
 * - INSERT
 * - GET (by ID)
 * - UPDATE
 * - DELETE
 * - PRINT
 *
 * Diary Entry table:
 * - INSERT
 * - GET (by ID)
 * - DELETE
 * - PRINT
 *
 * Daily Quiz table:
 * - INSERT
 * - GET (by ID)
 * - GET (by DATE)
 * - UPDATE
 * - DELETE
 * - PRINT*/

public class databaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MoodMeter.db";

    /**SQL CREATE scripts*/
    private static final String SQL_CREATE_DAILY_QUIZ =
            "CREATE TABLE " + tableDailyQuiz.TABLE_NAME + " (" +
                    tableDailyQuiz.COLUMN_NAME_DQ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    tableDailyQuiz.COLUMN_NAME_DQ_DATE + " TEXT, " +
                    tableDailyQuiz.COLUMN_NAME_DQ_Q1 + " INTEGER, " +
                    tableDailyQuiz.COLUMN_NAME_DQ_Q2 + " INTEGER, " +
                    tableDailyQuiz.COLUMN_NAME_DQ_Q3 + " INTEGER, " +
                    tableDailyQuiz.COLUMN_NAME_DQ_Q4 + " INTEGER, " +
                    tableDailyQuiz.COLUMN_NAME_DQ_Q5 + " INTEGER, " +
                    tableDailyQuiz.COLUMN_NAME_DQ_Q6 + " INTEGER)";
    private static final String SQL_CREATE_DIARY_ENTRY =
            "CREATE TABLE " + tableDiaryEntry.TABLE_NAME + " (" +
                    tableDiaryEntry.COLUMN_NAME_DE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    tableDiaryEntry.COLUMN_NAME_DE_DATETIME + " TEXT, " +
                    tableDiaryEntry.COLUMN_NAME_DE_BODY + " TEXT)";
    private static final String SQL_CREATE_PROFILE =
            "CREATE TABLE " + tableProfile.TABLE_NAME + " (" +
                    tableProfile.COLUMN_NAME_P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    tableProfile.COLUMN_NAME_P_NAME + " TEXT, " +
                    tableProfile.COLUMN_NAME_P_MY_KEY + " TEXT, " +
                    tableProfile.COLUMN_NAME_P_THEME + " INTEGER)";

    /**SQL DROP scripts*/
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

    /**Functions for the PROFILE table*/

    //GETs 1 or all of the records from the PROFILE table.
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
        String sortOrder = tableProfile.COLUMN_NAME_P_ID + " ASC";

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
                        null //Limiting (LIMIT BY)
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
                        null //Limiting (LIMIT BY)
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
        System.out.println("New record inserted into PROFILE.");
    }

    //Updates an existing record in the PROFILE table
    public void updateProfileRecord(int id, String newName, String newMyKey, int newTheme) {
        SQLiteDatabase db = getWritableDatabase(); //Obtains write access

        //Defines values for the UPDATE operation
        ContentValues values = new ContentValues();
        values.put(tableProfile.COLUMN_NAME_P_NAME, newName);
        values.put(tableProfile.COLUMN_NAME_P_MY_KEY, newMyKey);
        values.put(tableProfile.COLUMN_NAME_P_THEME, newTheme);

        //Defines the record that will be updated
        String[] selectionArgs = new String[1];
        String strId = "" + id;
        String selection = tableProfile.COLUMN_NAME_P_ID + " = ?";
        selectionArgs[0] = strId;

        //Update the record with the values provided
        db.update(tableProfile.TABLE_NAME, values, selection, selectionArgs);
        db.close();
        System.out.println("Record " + id + " updated in PROFILE.");
    }

    //Deletes an existing record in the PROFILE table
    public void deleteProfileRecord(int id) {
        boolean hasSpecifiedRecord = false;
        SQLiteDatabase db = getWritableDatabase(); //Obtains write access

        String selection = "";
        String[] selectionArgs = new String[1];
        //Defines the record that will be deleted
        if(id != -1) {
            hasSpecifiedRecord = true;
            String strId = "" + id;
            selection = tableProfile.COLUMN_NAME_P_ID + " = ?";
            selectionArgs[0] = strId;
        }

        if(hasSpecifiedRecord){ //Deletes a single record
            db.delete(tableProfile.TABLE_NAME, selection, selectionArgs);
        } else { //Deletes all records
            db.delete(tableProfile.TABLE_NAME, null, null);
        }
        db.close();
        System.out.println("Record " + id + " deleted from PROFILE.");
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
        do {
            try{
                long pId = records.getLong(records.getColumnIndexOrThrow(tableProfile.COLUMN_NAME_P_ID));
                pIds.add(pId);
                String pName = records.getString(records.getColumnIndexOrThrow(tableProfile.COLUMN_NAME_P_NAME));
                pNames.add(pName);
                String pMyKey = records.getString(records.getColumnIndexOrThrow(tableProfile.COLUMN_NAME_P_MY_KEY));
                pMyKeys.add(pMyKey);
                int pTheme = records.getInt(records.getColumnIndexOrThrow(tableProfile.COLUMN_NAME_P_THEME));
                pThemes.add(pTheme);
                System.out.println(pId + "\t\t\t" + pName + "\t\t" + pMyKey + "\t\t\t" + pTheme);
            } catch(CursorIndexOutOfBoundsException cioobe) {
                System.out.println("Warning: attempted to read an empty record.");
            }
        } while (records.moveToNext());
        System.out.println("Printed records from PROFILE.");
    }

    /**Functions for the DIARY_ENTRY table*/

    //Inserts a record into the DIARY_ENTRY table
    public void insertDiaryEntryRecord(String body) {
        SQLiteDatabase db = getWritableDatabase(); //Obtains write access

        //Defines values for the INSERT INTO operation
        ContentValues values = new ContentValues();
        values.put(tableDiaryEntry.COLUMN_NAME_DE_DATETIME, DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
        values.put(tableDiaryEntry.COLUMN_NAME_DE_BODY, body);

        //Insert the new row with the next automatically determined auto-increment SCORE_ID
        db.insert(tableDiaryEntry.TABLE_NAME, tableDiaryEntry.COLUMN_NAME_DE_ID, values);
        db.close();
        System.out.println("New record inserted into DIARY_ENTRY.");
    }

    //GETs 1 or all of the records from the DIARY_ENTRY table.
    public Cursor getDiaryEntryRecord(int id) {
        boolean hasSpecifiedRecord = false;
        SQLiteDatabase db = getReadableDatabase(); //Obtains read access

        //Returns the following columns
        String[] projection = {
                tableDiaryEntry.COLUMN_NAME_DE_ID,
                tableDiaryEntry.COLUMN_NAME_DE_DATETIME,
                tableDiaryEntry.COLUMN_NAME_DE_BODY
        };
        //Returns records that match the following conditions
        String selection = "";
        String[] selectionArgs = new String[1];
        if(id != -1) {
            hasSpecifiedRecord = true;
            String strId = "" + id;
            selection = tableDiaryEntry.COLUMN_NAME_DE_ID + " = ?";
            selectionArgs[0] = strId;
        }
        //Sorts the records
        String sortOrder = tableDiaryEntry.COLUMN_NAME_DE_ID + " ASC";

        Cursor results;
        if(hasSpecifiedRecord) { //Only include the selection arguments if a single record is being returned
            try{
                results = db.query(
                        tableDiaryEntry.TABLE_NAME, //Target table (FROM TABLE)
                        projection, //Projection (SELECT...)
                        selection, //Selection (WHERE...)
                        selectionArgs, //Selection arguments (COLUMN = X)
                        null, //Row grouping?
                        null, //Filter by row grouping?
                        sortOrder, //Ordering (ORDER BY)
                        null //Limiting (LIMIT BY)
                );
                System.out.println("Single record returned from DIARY_ENTRY.");
                return results;
            } catch(SQLiteException e) {
                e.printStackTrace();
            }
        } else {
            try {
                results = db.query(
                        tableDiaryEntry.TABLE_NAME, //Target table (FROM TABLE)
                        projection, //Projection (SELECT...)
                        null, //Selection (WHERE...)
                        null, //Selection arguments (COLUMN = X)
                        null, //Row grouping?
                        null, //Filter by row grouping?
                        sortOrder, //Ordering (ORDER BY)
                        null //Limiting (LIMIT BY)
                );
                System.out.println("All records returned from DIARY_ENTRY.");
                return results;
            } catch(SQLiteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //Deletes an existing record in the DIARY_ENTRY table
    public void deleteDiaryEntryRecord(int id) {
        boolean hasSpecifiedRecord = false;
        SQLiteDatabase db = getWritableDatabase(); //Obtains write access

        String selection = "";
        String[] selectionArgs = new String[1];
        //Defines the record that will be deleted
        if(id != -1) {
            hasSpecifiedRecord = true;
            String strId = "" + id;
            selection = tableDiaryEntry.COLUMN_NAME_DE_ID + " = ?";
            selectionArgs[0] = strId;
        }

        if(hasSpecifiedRecord){ //Deletes a single record
            db.delete(tableDiaryEntry.TABLE_NAME, selection, selectionArgs);
        } else { //Deletes all records
            db.delete(tableDiaryEntry.TABLE_NAME, null, null);
        }
        db.close();
        System.out.println("Record " + id + " deleted from DIARY_ENTRY.");
    }

    //Prints records that have been retrieved from the DIARY_ENTRY table to the console
    public void printDiaryEntryRecords(Cursor records) {
        try {
            records.moveToFirst();
        } catch(NullPointerException e) {
            System.out.println("No records to print from DIARY_ENTRY.");
            return;
        }
        System.out.println("DE_ID\t\tDE_DATETIME\t\t\t\t\tDE_BODY");
        List<Long> deIds = new ArrayList<>();
        List<String> deDatetimes = new ArrayList<>();
        List<String> deBodies = new ArrayList<>();
        do {
            try{
                long deId = records.getLong(records.getColumnIndexOrThrow(tableDiaryEntry.COLUMN_NAME_DE_ID));
                deIds.add(deId);
                String deDatetime = records.getString(records.getColumnIndexOrThrow(tableDiaryEntry.COLUMN_NAME_DE_DATETIME));
                deDatetimes.add(deDatetime);
                String deBody = records.getString(records.getColumnIndexOrThrow(tableDiaryEntry.COLUMN_NAME_DE_BODY));
                deBodies.add(deBody);
                System.out.println(deId + "\t\t\t" + deDatetime + "\t\t" + deBody);
            } catch(CursorIndexOutOfBoundsException cioobe) {
                System.out.println("Warning: attempted to read an empty record.");
            }
        } while (records.moveToNext());
        System.out.println("Printed records from DIARY_ENTRY.");
    }

    /**Functions for the DAILY_QUIZ table*/

    //Inserts a record into the DAILY_QUIZ table
    public void insertDailyQuizRecord(int[] responses) {
        if(responses.length == 6) { //Only add a new record if there are 6 answers supplied, responding to the 6 questions
            SQLiteDatabase db = getWritableDatabase(); //Obtains write access

            //Defines values for the INSERT INTO operation
            ContentValues values = new ContentValues();
            values.put(tableDailyQuiz.COLUMN_NAME_DQ_DATE, DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
            values.put(tableDailyQuiz.COLUMN_NAME_DQ_Q1, responses[0]);
            values.put(tableDailyQuiz.COLUMN_NAME_DQ_Q2, responses[1]);
            values.put(tableDailyQuiz.COLUMN_NAME_DQ_Q3, responses[2]);
            values.put(tableDailyQuiz.COLUMN_NAME_DQ_Q4, responses[3]);
            values.put(tableDailyQuiz.COLUMN_NAME_DQ_Q5, responses[4]);
            values.put(tableDailyQuiz.COLUMN_NAME_DQ_Q6, responses[5]);

            //Insert the new row with the next automatically determined auto-increment SCORE_ID
            db.insert(tableDailyQuiz.TABLE_NAME, tableDailyQuiz.COLUMN_NAME_DQ_ID, values);
            db.close();
            System.out.println("New record inserted into DAILY_QUIZ.");
        } else {
            System.out.println("Error: Incorrect of responses supplied.");
        }
    }

    //GETs 1 or all of the records from the DAILY_QUIZ table.
    public Cursor getDailyQuizRecord(int id) {
        boolean hasSpecifiedRecord = false;
        SQLiteDatabase db = getReadableDatabase(); //Obtains read access

        //Returns the following columns
        String[] projection = {
                tableDailyQuiz.COLUMN_NAME_DQ_ID,
                tableDailyQuiz.COLUMN_NAME_DQ_DATE,
                tableDailyQuiz.COLUMN_NAME_DQ_Q1,
                tableDailyQuiz.COLUMN_NAME_DQ_Q2,
                tableDailyQuiz.COLUMN_NAME_DQ_Q3,
                tableDailyQuiz.COLUMN_NAME_DQ_Q4,
                tableDailyQuiz.COLUMN_NAME_DQ_Q5,
                tableDailyQuiz.COLUMN_NAME_DQ_Q6
        };
        //Returns records that match the following conditions
        String selection = "";
        String[] selectionArgs = new String[1];
        if(id != -1) {
            hasSpecifiedRecord = true;
            String strId = "" + id;
            selection = tableDailyQuiz.COLUMN_NAME_DQ_ID + " = ?";
            selectionArgs[0] = strId;
        }
        //Sorts the records
        String sortOrder = tableDailyQuiz.COLUMN_NAME_DQ_ID + " ASC";

        Cursor results;
        if(hasSpecifiedRecord) { //Only include the selection arguments if a single record is being returned
            try{
                results = db.query(
                        tableDailyQuiz.TABLE_NAME, //Target table (FROM TABLE)
                        projection, //Projection (SELECT...)
                        selection, //Selection (WHERE...)
                        selectionArgs, //Selection arguments (COLUMN = X)
                        null, //Row grouping?
                        null, //Filter by row grouping?
                        sortOrder, //Ordering (ORDER BY)
                        null //Limiting (LIMIT BY)
                );
                System.out.println("Single record returned from DAILY_QUIZ.");
                return results;
            } catch(SQLiteException e) {
                e.printStackTrace();
            }
        } else {
            try {
                results = db.query(
                        tableDailyQuiz.TABLE_NAME, //Target table (FROM TABLE)
                        projection, //Projection (SELECT...)
                        null, //Selection (WHERE...)
                        null, //Selection arguments (COLUMN = X)
                        null, //Row grouping?
                        null, //Filter by row grouping?
                        sortOrder, //Ordering (ORDER BY)
                        null //Limiting (LIMIT BY)
                );
                System.out.println("All records returned from DAILY_QUIZ.");
                return results;
            } catch(SQLiteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //GETs all of the records in the DAILY_QUIZ table that are made on a specified date.
    public Cursor getDailyQuizRecordByDate(String date) {
        SQLiteDatabase db = getReadableDatabase(); //Obtains read access

        //Returns the following columns
        String[] projection = {
                tableDailyQuiz.COLUMN_NAME_DQ_ID,
                tableDailyQuiz.COLUMN_NAME_DQ_DATE,
                tableDailyQuiz.COLUMN_NAME_DQ_Q1,
                tableDailyQuiz.COLUMN_NAME_DQ_Q2,
                tableDailyQuiz.COLUMN_NAME_DQ_Q3,
                tableDailyQuiz.COLUMN_NAME_DQ_Q4,
                tableDailyQuiz.COLUMN_NAME_DQ_Q5,
                tableDailyQuiz.COLUMN_NAME_DQ_Q6
        };
        //Returns records that match the following conditions
        String selection = "";
        String[] selectionArgs = new String[1];
        selection = tableDailyQuiz.COLUMN_NAME_DQ_DATE + " = ?";
        selectionArgs[0] = date;

        //Sorts the records
        String sortOrder = tableDailyQuiz.COLUMN_NAME_DQ_ID + " ASC";

        Cursor results;
        try{
            results = db.query(
                    tableDailyQuiz.TABLE_NAME, //Target table (FROM TABLE)
                    projection, //Projection (SELECT...)
                    selection, //Selection (WHERE...)
                    selectionArgs, //Selection arguments (COLUMN = X)
                    null, //Row grouping?
                    null, //Filter by row grouping?
                    sortOrder, //Ordering (ORDER BY)
                    null //Limiting (LIMIT BY)
            );
            System.out.println("Records returned from DAILY_QUIZ.");
            return results;
        } catch(SQLiteException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Updates an existing record in the DAILY_QUIZ table
    public void updateDailyQuizRecord(long id, int[] newResponses) {
        if(newResponses.length == 6) { //Only update the record if there are 6 answers supplied, responding to the 6 questions
            SQLiteDatabase db = getWritableDatabase(); //Obtains write access

            //Defines values for the UPDATE operation
            ContentValues values = new ContentValues();
            values.put(tableDailyQuiz.COLUMN_NAME_DQ_DATE, DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
            values.put(tableDailyQuiz.COLUMN_NAME_DQ_Q1, newResponses[0]);
            values.put(tableDailyQuiz.COLUMN_NAME_DQ_Q2, newResponses[1]);
            values.put(tableDailyQuiz.COLUMN_NAME_DQ_Q3, newResponses[2]);
            values.put(tableDailyQuiz.COLUMN_NAME_DQ_Q4, newResponses[3]);
            values.put(tableDailyQuiz.COLUMN_NAME_DQ_Q5, newResponses[4]);
            values.put(tableDailyQuiz.COLUMN_NAME_DQ_Q6, newResponses[5]);

            //Defines the record that will be updated
            String[] selectionArgs = new String[1];
            String strId = "" + id;
            String selection = tableDailyQuiz.COLUMN_NAME_DQ_ID + " = ?";
            selectionArgs[0] = strId;

            //Update the record with the values provided
            db.update(tableDailyQuiz.TABLE_NAME, values, selection, selectionArgs);
            db.close();
            System.out.println("Record " + id + " updated in DAILY_QUIZ.");
        } else {
            System.out.println("Error: Incorrect of responses supplied.");
        }
    }

    //Deletes an existing record in the DAILY_QUIZ table
    public void deleteDailyQuizRecord(int id) {
        boolean hasSpecifiedRecord = false;
        SQLiteDatabase db = getWritableDatabase(); //Obtains write access

        String selection = "";
        String[] selectionArgs = new String[1];
        //Defines the record that will be deleted
        if(id != -1) {
            hasSpecifiedRecord = true;
            String strId = "" + id;
            selection = tableDailyQuiz.COLUMN_NAME_DQ_ID + " = ?";
            selectionArgs[0] = strId;
        }

        if(hasSpecifiedRecord){ //Deletes a single record
            db.delete(tableDailyQuiz.TABLE_NAME, selection, selectionArgs);
        } else { //Deletes all records
            db.delete(tableDailyQuiz.TABLE_NAME, null, null);
        }
        db.close();
        System.out.println("Record " + id + " deleted from DAILY_QUIZ.");
    }

    //Prints records that have been retrieved from the DAILY_QUIZ table to the console
    public void printDailyQuizRecords(Cursor records) {
        try {
            records.moveToFirst();
        } catch(NullPointerException e) {
            System.out.println("No records to print from DAILY_QUIZ.");
            return;
        }
        System.out.println("DQ_ID\t\tDQ_DATE\t\t\t\tDQ_Q1\t\tDQ_Q2\t\tDQ_Q3\t\tDQ_Q4\t\tDQ_Q5\t\tDQ_Q6");
        List<Long> dqIds = new ArrayList<>();
        List<String> dqDates = new ArrayList<>();
        List<Integer> dqQ1s = new ArrayList<>();
        List<Integer> dqQ2s = new ArrayList<>();
        List<Integer> dqQ3s = new ArrayList<>();
        List<Integer> dqQ4s = new ArrayList<>();
        List<Integer> dqQ5s = new ArrayList<>();
        List<Integer> dqQ6s = new ArrayList<>();
        do {
            try{
                long dqId = records.getLong(records.getColumnIndexOrThrow(tableDailyQuiz.COLUMN_NAME_DQ_ID));
                dqIds.add(dqId);
                String dqDate = records.getString(records.getColumnIndexOrThrow(tableDailyQuiz.COLUMN_NAME_DQ_DATE));
                dqDates.add(dqDate);
                int dqQ1 = records.getInt(records.getColumnIndexOrThrow(tableDailyQuiz.COLUMN_NAME_DQ_Q1));
                dqQ1s.add(dqQ1);
                int dqQ2 = records.getInt(records.getColumnIndexOrThrow(tableDailyQuiz.COLUMN_NAME_DQ_Q2));
                dqQ2s.add(dqQ2);
                int dqQ3 = records.getInt(records.getColumnIndexOrThrow(tableDailyQuiz.COLUMN_NAME_DQ_Q3));
                dqQ3s.add(dqQ3);
                int dqQ4 = records.getInt(records.getColumnIndexOrThrow(tableDailyQuiz.COLUMN_NAME_DQ_Q4));
                dqQ4s.add(dqQ4);
                int dqQ5 = records.getInt(records.getColumnIndexOrThrow(tableDailyQuiz.COLUMN_NAME_DQ_Q5));
                dqQ5s.add(dqQ5);
                int dqQ6 = records.getInt(records.getColumnIndexOrThrow(tableDailyQuiz.COLUMN_NAME_DQ_Q6));
                dqQ6s.add(dqQ6);
                System.out.println(dqId + "\t\t\t" + dqDate + "\t\t" + dqQ1 + "\t\t\t" + dqQ2 + "\t\t\t" + dqQ3 + "\t\t\t" + dqQ4 + "\t\t\t" + dqQ5 + "\t\t\t" + dqQ6);
            } catch(CursorIndexOutOfBoundsException cioobe) {
                System.out.println("Warning: attempted to read an empty record.");
            }
        } while (records.moveToNext());
        System.out.println("Printed records from DAILY_QUIZ.");
    }
}