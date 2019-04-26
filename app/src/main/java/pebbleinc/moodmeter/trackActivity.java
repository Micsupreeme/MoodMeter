package pebbleinc.moodmeter;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class trackActivity extends AppCompatActivity {

    private static final int MINIMUM_DAILY_QUIZ_ID = 1;
    int latestDailyQuizId = 1;
    int currentDailyQuizId = 1;
    boolean isDisplayingMantra = true;
    String currentDate;
    String[] dqDetailedFeedback = new String[6];
    public static final String EXTRA_MESSAGE = "pebbleinc.MoodMeter.MESSAGE"; //defines the "extra" prefix for sending messages, ensuring that it's unique

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        databaseHelper dbHelp;

        dbHelp = new databaseHelper(this);
        Cursor quizzes = dbHelp.getDailyQuizRecord(-1, true, 1);
        dbHelp.printDailyQuizRecords(quizzes);
        dbHelp.close();

        latestDailyQuizId = getId(quizzes);
        currentDailyQuizId = latestDailyQuizId;

        displayTip(generateMantra(), isDisplayingMantra);
        int[] dqResponses = getResponses(quizzes);
        display(getDate(quizzes), interpretResponses(dqResponses));
    }

    //This method fires when the user taps the "<" button
    public void handleBackDate(View view) {
        if(currentDailyQuizId <= MINIMUM_DAILY_QUIZ_ID) {
            System.out.println("Warning: attempted to read a record that doesn't exist.");
        } else {
            currentDailyQuizId -= 1;
            databaseHelper dbHelp;

            dbHelp = new databaseHelper(this);
            Cursor quizzes = dbHelp.getDailyQuizRecord(currentDailyQuizId, false, 1);
            dbHelp.printDailyQuizRecords(quizzes);
            dbHelp.close();

            int[] dqResponses = getResponses(quizzes);
            display(getDate(quizzes), interpretResponses(dqResponses));
        }
    }

    //This method fires when the user taps the ">" button
    public void handleForwardDate(View view) {
        if(currentDailyQuizId >= latestDailyQuizId) {
            System.out.println("Warning: attempted to read a record that doesn't exist.");
        } else {
            currentDailyQuizId += 1;
            databaseHelper dbHelp;

            dbHelp = new databaseHelper(this);
            Cursor quizzes = dbHelp.getDailyQuizRecord(currentDailyQuizId, false, 1);
            dbHelp.printDailyQuizRecords(quizzes);
            dbHelp.close();

            int[] dqResponses = getResponses(quizzes);
            display(getDate(quizzes), interpretResponses(dqResponses));
        }
    }

    //Takes a raw data int array of responses and returns a concise string rating array
    private String[] interpretResponses(int[] dqResponses) {
        String[] dqFeedback = new String[6];

        try{
            //Interpret response to question 1 (General mood)
            switch(dqResponses[0]) {
                case 1:
                    dqFeedback[0] = "Amazing";
                    dqDetailedFeedback[0] = "";
                    break;
                case 2:
                    dqFeedback[0] = "Good";
                    dqDetailedFeedback[0] = "";
                    break;
                case 3:
                    dqFeedback[0] = "Okay";
                    dqDetailedFeedback[0] = "";
                    break;
                case 4:
                    dqFeedback[0] = "Inadequate";
                    dqDetailedFeedback[0] = "Sometimes it is OK not to be OK.";
                    break;
                case 5:
                    dqFeedback[0] = "Lousy";
                    dqDetailedFeedback[0] = "Sorry you are feeling that way; perhaps see the Helplines for assistance.";
                    break;
                default:
                    dqFeedback[0] = "--";
                    dqDetailedFeedback[0] = "--";
                    break;
            }

            //Interpret response to question 2 (sleep)
            switch(dqResponses[1]) {
                case 1:
                    dqFeedback[1] = "Not Enough";
                    dqDetailedFeedback[1] = "Try going to bed a little earlier and turn off screens.";
                    break;
                case 2:
                    dqFeedback[1] = "Insufficient";
                    dqDetailedFeedback[1] = "Try going to bed a little earlier and turn off screens.";
                    break;
                case 3:
                    dqFeedback[1] = "Ideal";
                    dqDetailedFeedback[1] = "";
                    break;
                case 4:
                    dqFeedback[1] = "Excessive";
                    dqDetailedFeedback[1] = "Try waking up a little earlier; you will feel better.";
                    break;
                case 5:
                    dqFeedback[1] = "Too Much";
                    dqDetailedFeedback[1] = "Try waking up a little earlier; you will feel better.";
                    break;
                default:
                    dqFeedback[1] = "--";
                    dqDetailedFeedback[1] = "--";
                    break;
            }

            //Interpret response to question 3 (Work)
            switch(dqResponses[2]) {
                case 1:
                    dqFeedback[2] = "None";
                    dqDetailedFeedback[2] = "Try to apply yourself within a designated work time.";
                    break;
                case 2:
                    dqFeedback[2] = "Not Much";
                    dqDetailedFeedback[2] = "Try to apply yourself within a designated work time.";
                    break;
                case 3:
                    dqFeedback[2] = "Ideal";
                    dqDetailedFeedback[2] = "";
                    break;
                case 4:
                    dqFeedback[2] = "Excessive";
                    dqDetailedFeedback[2] = "Try setting aside some time for yourself to relax.";
                    break;
                case 5:
                    dqFeedback[2] = "Too Much";
                    dqDetailedFeedback[2] = "Try setting aside some time for yourself to relax.";
                    break;
                default:
                    dqFeedback[2] = "--";
                    break;
            }

            //Interpret response to question 4 (Healthy Eating)
            switch(dqResponses[3]) {
                case 1:
                    dqFeedback[3] = "Amazing";
                    dqDetailedFeedback[3] = "";
                    break;
                case 2:
                    dqFeedback[3] = "Good";
                    dqDetailedFeedback[3] = "";
                    break;
                case 3:
                    dqFeedback[3] = "Okay";
                    dqDetailedFeedback[3] = "Consider eating a little more fruit and vegetables.";
                    break;
                case 4:
                    dqFeedback[3] = "Inadequate";
                    dqDetailedFeedback[3] = "Consider eating a little more fruit and vegetables.";
                    break;
                case 5:
                    dqFeedback[3] = "Lousy";
                    dqDetailedFeedback[3] = "Try to eat more fruit and vegetables and substitute sugary drinks for water.";
                    break;
                default:
                    dqFeedback[3] = "--";
                    dqDetailedFeedback[3] = "--";
                    break;
            }


            //Interpret response to question 5 (Exercise)
            switch(dqResponses[4]) {
                case 1:
                    dqFeedback[4] = "Incredible";
                    dqDetailedFeedback[4] = "";
                    break;
                case 2:
                    dqFeedback[4] = "Excellent";
                    dqDetailedFeedback[4] = "";
                    break;
                case 3:
                    dqFeedback[4] = "Great";
                    dqDetailedFeedback[4] = "";
                    break;
                case 4:
                    dqFeedback[4] = "Improvable";
                    dqDetailedFeedback[4] = "Walking is a good way to help clear your mind.";
                    break;
                case 5:
                    dqFeedback[4] = "Not Enough";
                    dqDetailedFeedback[4] = "Jogging is a good way to burn calories and clear your mind.";
                    break;
                default:
                    dqFeedback[4] = "--";
                    dqDetailedFeedback[4] = "--";
                    break;
            }

            //Interpret response to question 6 (Social)
            switch(dqResponses[5]) {
                case 1:
                    dqFeedback[5] = "Amazing";
                    dqDetailedFeedback[5] = "";
                    break;
                case 2:
                    dqFeedback[5] = "Good";
                    dqDetailedFeedback[5] = "";
                    break;
                case 3:
                    dqFeedback[5] = "Okay";
                    dqDetailedFeedback[5] = "Phone a friend or a family member online or on the phone. You’ll make your day and their day better.";
                    break;
                case 4:
                    dqFeedback[5] = "Inadequate";
                    dqDetailedFeedback[5] = "Phone a friend or a family member online or on the phone. You’ll make your day and their day better.";
                    break;
                case 5:
                    dqFeedback[5] = "Lousy";
                    dqDetailedFeedback[5] = "Phone a friend or a family member online or on the phone. You’ll make your day and their day better.";
                    break;
                default:
                    dqFeedback[5] = "--";
                    dqDetailedFeedback[5] = "--";
                    break;
            }
        } catch(NullPointerException npe) {
            System.out.println("No records found");
        }

        displayTip(prioritiseTip(), isDisplayingMantra);
        return dqFeedback;
    }

    //Prioritises feedback to show in the tip box, returns a mantra if there's no feedback to show
    private String prioritiseTip() {
        isDisplayingMantra = false;
        try {
            if (!dqDetailedFeedback[1].equals("") && !dqDetailedFeedback[1].equals("--")) {
                return dqDetailedFeedback[1]; //Sleep
            } else if (!dqDetailedFeedback[3].equals("") && !dqDetailedFeedback[3].equals("--")) {
                return dqDetailedFeedback[3]; //Diet
            } else if (!dqDetailedFeedback[5].equals("") && !dqDetailedFeedback[5].equals("--")) {
                return dqDetailedFeedback[5]; //Social
            } else if (!dqDetailedFeedback[4].equals("") && !dqDetailedFeedback[4].equals("--")) {
                return dqDetailedFeedback[4]; //Exercise
            } else if (!dqDetailedFeedback[2].equals("") && !dqDetailedFeedback[2].equals("--")) {
                return dqDetailedFeedback[2]; //Work
            } else if (!dqDetailedFeedback[0].equals("") && !dqDetailedFeedback[0].equals("--")) {
                return dqDetailedFeedback[0]; //General Mood
            } else {
                isDisplayingMantra = true;
                return generateMantra();
            }
        } catch(NullPointerException npe) {
            System.out.println("Warning: attempted to read blank feedback");
            isDisplayingMantra = true;
            return generateMantra();
        }
    }

    //Takes a Cursor and returns the ID
    private int getId(Cursor quizzes) {
        try {
            quizzes.moveToFirst();
            return quizzes.getInt(quizzes.getColumnIndexOrThrow(databaseContract.tableDailyQuiz.COLUMN_NAME_DQ_ID));
        } catch(Exception e) {
            System.out.println("No records found");
            return 0;
        }
    }

    //Takes a Cursor and returns the date to display
    private String getDate(Cursor quizzes) {
        try {
            quizzes.moveToFirst();
            currentDate = quizzes.getString(quizzes.getColumnIndexOrThrow(databaseContract.tableDailyQuiz.COLUMN_NAME_DQ_DATE));
            return currentDate;
        } catch(Exception e) {
            System.out.println("No records found");
            return null;
        }
    }

    //Takes a Cursor and returns the ratings to display
    private int[] getResponses(Cursor quizzes) {
        int[] displayRatings = new int[6];
        try {
            quizzes.moveToFirst();
            displayRatings[0] = quizzes.getInt(quizzes.getColumnIndexOrThrow(databaseContract.tableDailyQuiz.COLUMN_NAME_DQ_Q1));
            displayRatings[1] = quizzes.getInt(quizzes.getColumnIndexOrThrow(databaseContract.tableDailyQuiz.COLUMN_NAME_DQ_Q2));
            displayRatings[2] = quizzes.getInt(quizzes.getColumnIndexOrThrow(databaseContract.tableDailyQuiz.COLUMN_NAME_DQ_Q3));
            displayRatings[3] = quizzes.getInt(quizzes.getColumnIndexOrThrow(databaseContract.tableDailyQuiz.COLUMN_NAME_DQ_Q4));
            displayRatings[4] = quizzes.getInt(quizzes.getColumnIndexOrThrow(databaseContract.tableDailyQuiz.COLUMN_NAME_DQ_Q5));
            displayRatings[5] = quizzes.getInt(quizzes.getColumnIndexOrThrow(databaseContract.tableDailyQuiz.COLUMN_NAME_DQ_Q6));
            return displayRatings;
        } catch(NullPointerException e) {
            System.out.println("No records found");
            return null;
        } catch(CursorIndexOutOfBoundsException cioobe) {
            System.out.println("Out of bounds");
            return null;
        }
    }

    //Updates the GUI with the specified values
    private void display(String date, String[] dqRatings) {
        TextView lblDate = findViewById(R.id.lblDate);
        lblDate.setText(date);

        TextView lblMoodRating = findViewById(R.id.lblMoodRating);
        lblMoodRating.setText(dqRatings[0]);
        TextView lblSleepRating = findViewById(R.id.lblSleepRating);
        lblSleepRating.setText(dqRatings[1]);
        TextView lblWorkRating = findViewById(R.id.lblWorkRating);
        lblWorkRating.setText(dqRatings[2]);
        TextView lblDietRating = findViewById(R.id.lblDietRating);
        lblDietRating.setText(dqRatings[3]);
        TextView lblExerciseRating = findViewById(R.id.lblExerciseRating);
        lblExerciseRating.setText(dqRatings[4]);
        TextView lblSocialRating = findViewById(R.id.lblSocialRating);
        lblSocialRating.setText(dqRatings[5]);
    }

    //Updates the main tip with the specified text and background colour
    private void displayTip(String body, boolean isGreen) {
        TextView lblTip = findViewById(R.id.lblTip);
        if(isGreen) {
            //Green colour for non-specific mantras
            lblTip.setBackgroundColor(Color.rgb(111, 177, 102));
        } else {
            //Purple colour for targeted tips
            lblTip.setBackgroundColor(Color.rgb(170, 102, 204));
        }
        lblTip.setText(body);
    }

    //Navigates to the Diary page
    public void navigateDiary(View view) {
        Intent gotoDiary = new Intent(this, diaryActivity.class);
        gotoDiary.putExtra(EXTRA_MESSAGE, currentDate);
        startActivity(gotoDiary);
    }

    //Randomly generates and returns a mantra string from 187 preset mantras
    private String generateMantra() {
        String mantra;

        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(186);

        switch (randomIndex) {
            case 0:
                mantra = "Today, you are perfect.";
                break;
            case 1:
                mantra = "Just keep moving.";
                break;
            case 2:
                mantra = "You are the sky, everything else is just the weather.";
                break;
            case 3:
                mantra = "You are strong. You are beautiful. You are enough.";
                break;
            case 4:
                mantra = "You are fulfilled. You are fearless.";
                break;
            case 5:
                mantra = "Less is more.";
                break;
            case 6:
                mantra = "Take it easy.";
                break;
            case 7:
                mantra = "You have to face the truth straight on.";
                break;
            case 8:
                mantra = "Take a step and stride on.";
                break;
            case 9:
                mantra = "In front of your eyes, a path opens to a future of hope.";
                break;
            case 10:
                mantra = "Whatever awaits you'll overcome.";
                break;
            case 11:
                mantra = "There is no strength without strife.";
                break;
            case 12:
                mantra = "Life does not get easier, you just get stronger.";
                break;
            case 13:
                mantra = "You're never a loser until you quit trying.";
                break;
            case 14:
                mantra = "Remember that guy that gave up? Neither do I.";
                break;
            case 15:
                mantra = "Failure and rejection are steps towards succeeding.";
                break;
            case 16:
                mantra = "It never gets easier, you just get better.";
                break;
            case 17:
                mantra = "Be patient.";
                break;
            case 18:
                mantra = "Work happily; money will come";
                break;
            case 19:
                mantra = "Do not let others destroy your inner peace.";
                break;
            case 20:
                mantra = "Be your own friend, not your own enemy.";
                break;
            case 21:
                mantra = "Go forwards and just think positive.";
                break;
            case 22:
                mantra = "Trust in and listen to your gut instincts.";
                break;
            case 23:
                mantra = "Be genuine. Life is too short for facades.";
                break;
            case 24:
                mantra = "Surround yourself with the people that make you happy.";
                break;
            case 25:
                mantra = "Live long and prosper.";
                break;
            case 26:
                mantra = "Let it out. Don't bottle up your feelings.";
                break;
            case 27:
                mantra = "Close your eyes and take a deep breath.";
                break;
            case 28:
                mantra = "Don't obsess over your past.";
                break;
            case 29:
                mantra = "Worrying accomplishes nothing.";
                break;
            case 30:
                mantra = "Don't worry about what the future holds.";
                break;
            case 31:
                mantra = "Nobody knows what the future holds.";
                break;
            case 32:
                mantra = "Live for the present. The now. Right now.";
                break;
            case 33:
                mantra = "Care for others, and they will care for you.";
                break;
            case 34:
                mantra = "Be kind.";
                break;
            case 35:
                mantra = "Be there for the people you care about.";
                break;
            case 36:
                mantra = "There is always something to be thankful for.";
                break;
            case 37:
                mantra = "Actions speak louder than words.";
                break;
            case 38:
                mantra = "You don't need a reason to love something.";
                break;
            case 39:
                mantra = "You don't owe anyone an argument.";
                break;
            case 40:
                mantra = "It will all make sense some day.";
                break;
            case 41:
                mantra = "Always believe in your abilities.";
                break;
            case 42:
                mantra = "Don't let others pressure you";
                break;
            case 43:
                mantra = "Anything worth doing is worth doing well.";
                break;
            case 44:
                mantra = "It may not be easy, but it will be worth it.";
                break;
            case 45:
                mantra = "You, yes you, right there, you are not alone.";
                break;
            case 46:
                mantra = "Just reach out; you'll find that you're not alone.";
                break;
            case 47:
                mantra = "Make the most of what you have.";
                break;
            case 48:
                mantra = "Take a moment to appreciate where you are.";
                break;
            case 49:
                mantra = "Take a moment to appreciate the beauty of nature.";
                break;
            case 50:
                mantra = "Just keep on keeping on.";
                break;
            case 51:
                mantra = "Keep calm.";
                break;
            case 52:
                mantra = "You cannot control everything.";
                break;
            case 53:
                mantra = "You cannot change people.";
                break;
            case 54:
                mantra = "You are one-of-a-kind.";
                break;
            case 55:
                mantra = "You are not a thing to be used.";
                break;
            case 56:
                mantra = "You are a person to love and be loved.";
                break;
            case 59:
                mantra = "It's ok to stand still, just don't move backwards.";
                break;
            case 60:
                mantra = "It's ok to feel frustrated.";
                break;
            case 61:
                mantra = "It's ok to feel sad.";
                break;
            case 62:
                mantra = "Be there for others and they will be there for you.";
                break;
            case 63:
                mantra = "Don't live just to impress others.";
                break;
            case 64:
                mantra = "It's too easy to focus on the negatives.";
                break;
            case 65:
                mantra = "There is always a silver lining.";
                break;
            case 66:
                mantra = "There is beauty all around you.";
                break;
            case 67:
                mantra = "Challenges make life meaningful.";
                break;
            case 68:
                mantra = "Do your favourite things.";
                break;
            case 69:
                mantra = "Love yourself for who you are.";
                break;
            case 70:
                mantra = "Time may be money, but money cannot buy time.";
                break;
            case 71:
                mantra = "Spend lots of time with the people you love.";
                break;
            case 72:
                mantra = "People are more important than money.";
                break;
            case 73:
                mantra = "Your job does not define you.";
                break;
            case 74:
                mantra = "You are a product of your decisions.";
                break;
            case 75:
                mantra = "Everyone needs time to themselves.";
                break;
            case 76:
                mantra = "Let go of the past.";
                break;
            case 77:
                mantra = "Show people that you care.";
                break;
            case 78:
                mantra = "Be humble.";
                break;
            case 79:
                mantra = "Never stop learning.";
                break;
            case 80:
                mantra = "Make time for the important things in life.";
                break;
            case 81:
                mantra = "Nothing grows in a comfort zone.";
                break;
            case 82:
                mantra = "Compliment others, you just might make their day.";
                break;
            case 83:
                mantra = "Obstacles are your stepping stones.";
                break;
            case 84:
                mantra = "Make yourself a priority.";
                break;
            case 85:
                mantra = "Make lots of memories. They are yours to keep forever.";
                break;
            case 86:
                mantra = "Fear is a mile high, a mile wide, but paper thin.";
                break;
            case 87:
                mantra = "It's okay not to be okay.";
                break;
            case 88:
                mantra = "Life doesn't come with an instruction manual.";
                break;
            case 89:
                mantra = "Nobody is perfect.";
                break;
            case 90:
                mantra = "Love those who accept your imperfections.";
                break;
            case 91:
                mantra = "You are worth it.";
                break;
            case 92:
                mantra = "Even the darkest night will end when the Sun rises.";
                break;
            case 93:
                mantra = "Your life is a movie. Make sure it's a good one.";
                break;
            case 94:
                mantra = "Be in the moment.";
                break;
            case 95:
                mantra = "Time waits for nobody.";
                break;
            case 96:
                mantra = "Be proud of everything you have overcome.";
                break;
            case 97:
                mantra = "If someone reaches out to you, take their hand.";
                break;
            case 98:
                mantra = "Be trustworthy.";
                break;
            case 99:
                mantra = "Better to be late than to miss out.";
                break;
            case 100:
                mantra = "Feed your heart and mind, not your ego.";
                break;
            case 101:
                mantra = "A sound soul dwells within a sound mind and a sound body.";
                break;
            case 102:
                mantra = "The only thing we have to fear is fear itself.";
                break;
            case 103:
                mantra = "It's ok if Plan A doesn't work; there are 26 letters.";
                break;
            case 104:
                mantra = "Try to be a rainbow in someone's cloud.";
                break;
            case 105:
                mantra = "Never regret.";
                break;
            case 106:
                mantra = "True strength lies in rising after the fall.";
                break;
            case 107:
                mantra = "Take it one step at a time.";
                break;
            case 108:
                mantra = "Carve your own path through life.";
                break;
            case 109:
                mantra = "There is no need to compare yourself to others.";
                break;
            case 110:
                mantra = "Make the choice, to take the chance, to bring change.";
                break;
            case 111:
                mantra = "It may take time, but everything will work out.";
                break;
            case 112:
                mantra = "You are important.";
                break;
            case 113:
                mantra = "This too, shall pass.";
                break;
            case 114:
                mantra = "You deserve great things.";
                break;
            case 115:
                mantra = "You are loveable.";
                break;
            case 116:
                mantra = "The best is yet to come.";
                break;
            case 117:
                mantra = "Creativity is intelligence having fun.";
                break;
            case 118:
                mantra = "Optimism leads to success.";
                break;
            case 119:
                mantra = "If your plan fails, change the plan, not the goal.";
                break;
            case 120:
                mantra = "Destroy your enemies by making them your friends.";
                break;
            case 121:
                mantra = "You can't always save people, but you can love them.";
                break;
            case 122:
                mantra = "Happiness is in the journey, not the destination.";
                break;
            case 123:
                mantra = "You are made of stardust.";
                break;
            case 124:
                mantra = "Life is too short to hold onto anger.";
                break;
            case 125:
                mantra = "Be the change you wish to see in the world.";
                break;
            case 126:
                mantra = "Don't seek validation from others.";
                break;
            case 127:
                mantra = "Your feelings are valid.";
                break;
            case 128:
                mantra = "Do it with passion, or not at all.";
                break;
            case 129:
                mantra = "Create a life that you want to live.";
                break;
            case 130:
                mantra = "Sometimes you win. Sometimes you learn.";
                break;
            case 131:
                mantra = "To avoid failure is to avoid progress.";
                break;
            case 132:
                mantra = "If you're not happy, something needs to change.";
                break;
            case 133:
                mantra = "Stick with people that love you for you.";
                break;
            case 134:
                mantra = "Hold on. Pain ends.";
                break;
            case 135:
                mantra = "Nobody can take your memories from you.";
                break;
            case 136:
                mantra = "Only you can make your memories";
                break;
            case 137:
                mantra = "Stay strong.";
                break;
            case 138:
                mantra = "Stand up. Have a voice.";
                break;
            case 139:
                mantra = "The good in this world is worth fighting for.";
                break;
            case 140:
                mantra = "Never leave the people that care about you.";
                break;
            case 141:
                mantra = "You are in charge of your own happiness.";
                break;
            case 142:
                mantra = "Smile. You don't own all of the world's problems.";
                break;
            case 143:
                mantra = "Avoid overthinking things.";
                break;
            case 144:
                mantra = "Worry less. Live more.";
                break;
            case 145:
                mantra = "Think less. Do more.";
                break;
            case 146:
                mantra = "It doesn't matter what other people think of you.";
                break;
            case 147:
                mantra = "Express yourself.";
                break;
            case 148:
                mantra = "Release negative emotions, don't suppress them.";
                break;
            case 149:
                mantra = "Don't be afraid to ask for help.";
                break;
            case 150:
                mantra = "Speak the truth, even if your voice shakes.";
                break;
            case 151:
                mantra = "Enjoy the little things in life.";
                break;
            case 152:
                mantra = "Take pride in your work.";
                break;
            case 153:
                mantra = "Make your mark, and make it a good one.";
                break;
            case 154:
                mantra = "Just relax.";
                break;
            case 155:
                mantra = "Stay cool.";
                break;
            case 156:
                mantra = "Stay awesome.";
                break;
            case 157:
                mantra = "Let go of fear.";
                break;
            case 158:
                mantra = "Take care of yourself, then of others.";
                break;
            case 159:
                mantra = "It goes on.";
                break;
            case 160:
                mantra = "Sometimes you just need someone to talk to.";
                break;
            case 161:
                mantra = "Sometimes you just need to take a deep breath.";
                break;
            case 162:
                mantra = "You are enough.";
                break;
            case 163:
                mantra = "Live a life full of love.";
                break;
            case 164:
                mantra = "Live and love.";
                break;
            case 165:
                mantra = "Always choose life.";
                break;
            case 166:
                mantra = "Be what you want to be.";
                break;
            case 167:
                mantra = "Everyone has their own specialities.";
                break;
            case 168:
                mantra = "There is only time if you make time.";
                break;
            case 169:
                mantra = "Keep pressing forward.";
                break;
            case 170:
                mantra = "You are empowered.";
                break;
            case 171:
                mantra = "Be good company.";
                break;
            case 172:
                mantra = "Sometimes you just need someone to listen.";
                break;
            case 173:
                mantra = "Don't promise, just prove.";
                break;
            case 174:
                mantra = "Live live to express, not to impress.";
                break;
            case 175:
                mantra = "Sm;)e";
                break;
            case 176:
                mantra = "You've got this.";
                break;
            case 177:
                mantra = "You can't please everyone.";
                break;
            case 178:
                mantra = "You are amazing.";
                break;
            case 179:
                mantra = "What matters is that you tried.";
                break;
            case 180:
                mantra = "Always do your best.";
                break;
            case 181:
                mantra = "Go for it.";
                break;
            case 182:
                mantra = "Embrace change.";
                break;
            case 183:
                mantra = "I believe in you. I know you can too.";
                break;
            case 184:
                mantra = "Nobody has everything.";
                break;
            case 185:
                mantra = "You can do it.";
                break;
            default:
                mantra = "If it doesn't feel right, go left.";
                break;
        }
        return mantra;
    }
}
