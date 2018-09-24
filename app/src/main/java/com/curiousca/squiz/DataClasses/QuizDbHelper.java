package com.curiousca.squiz.DataClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.curiousca.squiz.model.ApiObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static com.curiousca.squiz.DataClasses.Category.GEOGRAPHY;
import static com.curiousca.squiz.DataClasses.Category.MATH;
import static com.curiousca.squiz.DataClasses.Category.PROGRAMMING;
import static com.curiousca.squiz.DataClasses.QuizContract.CategoriesTable;
import static com.curiousca.squiz.DataClasses.QuizContract.QuestionsTable;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "KoolQuiz.db";
    private static final int DATABASE_VERSION = 1;
    SQLiteOpenHelper dbhelper;

    private static QuizDbHelper instance;

    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + " ( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT, " +
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
//        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category("Programming");
        addCategory(c1);
        Category c2 = new Category("Geography");
        addCategory(c2);
        Category c3 = new Category("Math");
        addCategory(c3);
    }

    private void addCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    public void setQuestionsTable(Response<List<ApiObject>> response) {
        db = dbhelper.getWritableDatabase();
        Question question = new Question();
        for (int i = 0; i < response.body().size(); i++) {
            int id = response.body().get(i).getId();
            question.setId(id);
            question.setAnswerNr(1);
            question.setOption1(response.body().get(i).getOpta());
            question.setOption2(response.body().get(i).getOptb());
            question.setOption3(response.body().get(i).getOptc());
            question.setQuestion(response.body().get(i).getQuestion());
            question.setCategoryID(GEOGRAPHY);
            insertOrUpdateGkQuestion(question);
        }
        dbhelper.close();
    }

    private void insertOrUpdateGkQuestion(Question question) {
        long id = question.getId();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME + " WHERE " + QuestionsTable._ID + "=" + id, null);
        if (c.getCount() == 0) {
            //if the row is not present,then insert the row
            ContentValues values = new ContentValues();
            values.put(QuestionsTable._ID, id);
            values.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
            values.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
            values.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
            values.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
            values.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
            values.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());


            db.insert(QuestionsTable.TABLE_NAME, null, values);
        } else {
            //else update the row
            ContentValues updatedValues = new ContentValues();
            updatedValues.put(QuestionsTable._ID, id);
            updatedValues.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
            updatedValues.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
            updatedValues.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
            updatedValues.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
            updatedValues.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());

            db.update(QuestionsTable.TABLE_NAME, updatedValues, QuestionsTable._ID + "=" + id, null);
        }
    }

    public void fillQuestionsTable() {

       /* Question q7 = new Question(response.body().get(0).getQuestion(),
                response.body().get(0).getOpta(),
                response.body().get(0).getOptb(),
                response.body().get(0).getOptc(),
                1,
                Question.DIFFICULTY_EASY,
                PROGRAMMING);
        addQuestion(q7);*/

        Question q1 = new Question("Programming, Easy: A is correct",
                "A", "B", "C", 1,
                PROGRAMMING);
        addQuestion(q1);
        Question q2 = new Question("Geography, Medium: B is correct",
                "A", "B", "C", 2,
                GEOGRAPHY);
        addQuestion(q2);
        Question q3 = new Question("Math, Hard: C is correct",
                "A", "B", "C", 3,
                MATH);
        addQuestion(q3);
        Question q4 = new Question("Math, Easy: B is correct",
                "A", "B", "C", 2,
                MATH);
        addQuestion(q4);
        Question q5 = new Question("Non existing, Easy: A is correct",
                "A", "B", "C", 1,
                4);
        addQuestion(q5);
        Question q6 = new Question("Non existing, Medium: B is correct",
                "A", "B", "C", 2,
                5);
        addQuestion(q6);
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
//        cv.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex(CategoriesTable._ID)));
                category.setName(cursor.getString(cursor.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return categoryList;
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getInt(cursor.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(cursor.getInt(cursor.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
//                question.setDifficulty(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(cursor.getInt(cursor.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(int categoryID) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? ";

        String[] selectionArgs = new String[]{
                String.valueOf(categoryID)};
        Cursor cursor = db.query(
                QuestionsTable.TABLE_NAME, null,
                selection, selectionArgs,
                null, null, null);

//        String[] selectionArgs = new String[]{difficulty};
//        Cursor cursor = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME +
//                " WHERE " + QuestionsTable.COLUMN_DIFFICULTY + " = ?", selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getInt(cursor.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(cursor.getInt(cursor.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
//                question.setDifficulty(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(cursor.getInt(cursor.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }

}
