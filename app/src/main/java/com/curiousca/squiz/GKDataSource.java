package com.curiousca.squiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.curiousca.squiz.DataClasses.Question;
import com.curiousca.squiz.DataClasses.QuizContract;
import com.curiousca.squiz.DataClasses.QuizDbHelper;


public class GKDataSource {
    private static final String LOGTAG = "GKDataSource";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    public GKDataSource(Context context) {
        dbhelper = new QuizDbHelper(context);
    }

    public void open() {
        Log.d(LOGTAG, "Database opened!");
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        Log.d(LOGTAG, "Database closed!");
        dbhelper.close();
    }

    public void insertOrUpdateGkQuestion(Question question) {
        long id = question.getId();
        Cursor c = database.rawQuery("SELECT * FROM " + QuizContract.QuestionsTable.TABLE_NAME + " WHERE " + QuizContract.QuestionsTable._ID + "=" + id, null);
        if (c.getCount() == 0) {
            //if the row is not present,then insert the row
            ContentValues values = new ContentValues();
            values.put(QuizContract.QuestionsTable._ID, id);
            values.put(QuizContract.QuestionsTable.COLUMN_QUESTION, question.getQuestion());
            values.put(QuizContract.QuestionsTable.COLUMN_OPTION1, question.getOption1());
            values.put(QuizContract.QuestionsTable.COLUMN_OPTION2, question.getOption2());
            values.put(QuizContract.QuestionsTable.COLUMN_OPTION3, question.getOption3());
            values.put(QuizContract.QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
            values.put(QuizContract.QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());


            database.insert(QuizContract.QuestionsTable.TABLE_NAME, null, values);
        } else {
            //else update the row
            ContentValues updatedValues = new ContentValues();
            updatedValues.put(QuizContract.QuestionsTable._ID, id);
            updatedValues.put(QuizContract.QuestionsTable.COLUMN_QUESTION, question.getQuestion());
            updatedValues.put(QuizContract.QuestionsTable.COLUMN_OPTION1, question.getOption1());
            updatedValues.put(QuizContract.QuestionsTable.COLUMN_OPTION2, question.getOption2());
            updatedValues.put(QuizContract.QuestionsTable.COLUMN_OPTION3, question.getOption3());
            updatedValues.put(QuizContract.QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());

            database.update(QuizContract.QuestionsTable.TABLE_NAME, updatedValues, QuizContract.QuestionsTable._ID + "=" + id, null);
        }
    }


    /*public List<ApiObject> getData() {
        database = dbhelper.getReadableDatabase();
        String[] columns = {QuizDbHelper.COLUMN_ITEM_ID,
                QuizDbHelper.COLUMN_QUESTION,
                QuizDbHelper.COLUMN_OPTION_A,
                QuizDbHelper.COLUMN_OPTION_B,
                QuizDbHelper.COLUMN_OPTION_C,
                QuizDbHelper.COLUMN_OPTION_ANSWER};
        Cursor cursor = database.query(QuizDbHelper.TABLE_NAME_GK, columns, null, null, null, null, null);
        List<ApiObject> questionList = new ArrayList<>();

        while (cursor.moveToNext()) {
            ApiObject q = new ApiObject();
            q.setId(cursor.getInt(0));
            q.setQuestion(cursor.getString(1));
            q.setOpta(cursor.getString(2));
            q.setOptb(cursor.getString(3));
            q.setOptc(cursor.getString(4));
            q.setAnswer(cursor.getString(5));

            questionList.add(q);
        }
        return questionList;
    }
*/
}
