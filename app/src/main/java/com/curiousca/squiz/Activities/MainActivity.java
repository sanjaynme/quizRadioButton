package com.curiousca.squiz.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.curiousca.squiz.DataClasses.Category;
import com.curiousca.squiz.DataClasses.Question;
import com.curiousca.squiz.DataClasses.QuizDbHelper;
import com.curiousca.squiz.GKDataSource;
import com.curiousca.squiz.R;
import com.curiousca.squiz.model.ApiObject;
import com.curiousca.squiz.retrofit.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private GKDataSource gkDataSource;

    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";

    private TextView textViewHighscore;
    private Spinner spinnerCategory;
    private int highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewHighscore = findViewById(R.id.text_View_highscore);
        spinnerCategory = findViewById(R.id.spinner_category);
        gkDataSource = new GKDataSource(MainActivity.this);

        loadCategories();
        loadHighscore();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("LOADING");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        ApiService.getServiceClass().getAllPost().enqueue(new Callback<List<ApiObject>>() {
            @Override
            public void onResponse(Call<List<ApiObject>> call, Response<List<ApiObject>> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    setQuestionsTable(response);
                }
            }

            @Override
            public void onFailure(Call<List<ApiObject>> call, Throwable t) {
                Log.d("", "Error msg is :::" + t.getMessage());
            }
        });

        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });
    }

    public void setQuestionsTable(Response<List<ApiObject>> response) {
        gkDataSource.open();
        Question question = new Question();
        for (int i = 0; i < response.body().size(); i++) {
            int id = response.body().get(i).getId();
            question.setId(id);
            question.setAnswerNr(Integer.parseInt(response.body().get(i).getAnswer()));
            question.setOption1(response.body().get(i).getOpta());
            question.setOption2(response.body().get(i).getOptb());
            question.setOption3(response.body().get(i).getOptc());
            question.setQuestion(response.body().get(i).getQuestion());
            question.setCategoryID(response.body().get(i).getCategory());
            gkDataSource.insertOrUpdateGkQuestion(question);
        }
        gkDataSource.close();
    }

    private void startQuiz() {
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String categoryName = selectedCategory.getName();

        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryID);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                if (score > highscore) {
                    updateHighscore(score);
                }
            }
        }
    }

    private void loadCategories() {
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        List<Category> categories = dbHelper.getAllCategories();

        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spinnerCategory.setAdapter(adapterCategories);
    }

    private void loadHighscore() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        textViewHighscore.setText("Highscore: " + highscore);
    }

    private void updateHighscore(int highscoreNew) {
        highscore = highscoreNew;
        textViewHighscore.setText("Highscore: " + highscore);

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }
}
