package pl.edu.pb.wi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PromptActivity extends AppCompatActivity {

    private Button showHintButton;
    private TextView answerTextView;
    private boolean correctAnswer;
    public static final String KEY_EXTRA_ANSWER_SHOWN = "answerShown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        showHintButton = findViewById(R.id.button_show_hint);
        answerTextView = findViewById(R.id.answer_text_view);

        correctAnswer = getIntent().getBooleanExtra(MainActivity.KEY_EXTRA_ANSWER, true);


        showHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int answer = correctAnswer ? R.string.button_true : R.string.button_false;
                answerTextView.setText(answer);
                setAnswerShownResult(true);
            }
        });

    }

    private void setAnswerShownResult(boolean hintWasShown) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EXTRA_ANSWER_SHOWN, hintWasShown);
        setResult(RESULT_OK, resultIntent);
    }

}