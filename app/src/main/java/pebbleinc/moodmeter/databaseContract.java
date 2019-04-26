package pebbleinc.moodmeter;

import android.provider.BaseColumns;

final class databaseContract {
    //This class should not be instantiated
    private databaseContract(){}

    //Defines the columns in the DAILY_QUIZ table
    public static class tableDailyQuiz implements BaseColumns {
        public static final String TABLE_NAME = "DAILY_QUIZ";
        public static final String COLUMN_NAME_DQ_ID = "DQ_ID";
        public static final String COLUMN_NAME_DQ_DATE = "DQ_DATE";
        public static final String COLUMN_NAME_DQ_Q1 = "DQ_Q1";
        public static final String COLUMN_NAME_DQ_Q2 = "DQ_Q2";
        public static final String COLUMN_NAME_DQ_Q3 = "DQ_Q3";
        public static final String COLUMN_NAME_DQ_Q4 = "DQ_Q4";
        public static final String COLUMN_NAME_DQ_Q5 = "DQ_Q5";
        public static final String COLUMN_NAME_DQ_Q6 = "DQ_Q6";
    }

    //Defines the columns in the DIARY_ENTRY table
    public static class tableDiaryEntry implements BaseColumns {
        public static final String TABLE_NAME = "DIARY_ENTRY";
        public static final String COLUMN_NAME_DE_ID = "DE_ID";
        public static final String COLUMN_NAME_DE_DATE = "DE_DATE";
        public static final String COLUMN_NAME_DE_TIME = "DE_TIME";
        public static final String COLUMN_NAME_DE_BODY = "DE_BODY";
    }

    //Defines the columns in the PROFILE table
    public static class tableProfile implements BaseColumns {
        public static final String TABLE_NAME = "PROFILE";
        public static final String COLUMN_NAME_P_ID = "P_ID";
        public static final String COLUMN_NAME_P_NAME = "P_NAME";
        public static final String COLUMN_NAME_P_MY_KEY = "P_MY_KEY";
        public static final String COLUMN_NAME_P_THEME = "P_THEME";
    }
}