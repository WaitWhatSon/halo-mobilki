package pl.edu.pb.libraryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import lombok.val;

public class BookDetailsActivity extends AppCompatActivity {

    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

    public static final String EXTRA_BOOK_TITLE = "pb.edu.pl.BOOK_TITLE";
    public static final String EXTRA_BOOK_AUTHORS = "pb.edu.pl.BOOK_AUTHORS";
    public static final String EXTRA_BOOK_COVER = "pb.edu.pl.BOOK_COVER";

    private TextView titleTextView;
    private TextView authorsTextView;
    private ImageView coverImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        titleTextView = findViewById(R.id.book_title);
        authorsTextView = findViewById(R.id.book_authors);
        coverImageView = findViewById(R.id.book_cover);


        titleTextView.setText(getIntent().getStringExtra(EXTRA_BOOK_TITLE));
        authorsTextView.setText(getIntent().getStringExtra(EXTRA_BOOK_AUTHORS));

        if (getIntent().getStringExtra(EXTRA_BOOK_COVER) != null) {
            Picasso.with(this.getApplicationContext())
                    .load(IMAGE_URL_BASE +getIntent().getStringExtra(EXTRA_BOOK_COVER) + "-L.jpg")
                    .placeholder(R.drawable.ic_baseline_book_24).into(coverImageView);
        }
        else
        {
            coverImageView.setImageResource(R.drawable.ic_baseline_book_24);
        }


        final Button button = findViewById(R.id.button_return);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });

    }
}