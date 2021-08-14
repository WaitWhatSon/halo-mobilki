package pl.edu.pb.libraryapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final BookAdapter adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

        private ImageView bookCover;
        private TextView bookTitleTextView;
        private TextView bookAuthorTextView;

        private Book currentBook;

        public BookHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.book_list_item, parent, false));
            itemView.setOnClickListener(this);

            bookCover = itemView.findViewById(R.id.img_cover);
            bookTitleTextView = itemView.findViewById(R.id.book_title);
            bookAuthorTextView = itemView.findViewById(R.id.book_author);
        }

        public void bind(Book book) {
            if (book != null && checkNullOrEmpty(book.getTitle()) && book.getAuthors() != null)
            {
                currentBook = book;
                bookTitleTextView.setText(book.getTitle());
                bookAuthorTextView.setText(TextUtils.join(", ", book.getAuthors()));
                if (book.getCover() != null) {
                    Picasso.with(itemView.getContext())
                            .load(IMAGE_URL_BASE + book.getCover() + "-S.jpg")
                            .placeholder(R.drawable.ic_baseline_book_24).into(bookCover);
                }
                else
                {
                    bookCover.setImageResource(R.drawable.ic_baseline_book_24);
                }
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, BookDetailsActivity.class);
            String bookTitle = currentBook.getTitle();
            intent.putExtra(BookDetailsActivity.EXTRA_BOOK_TITLE, bookTitle);
            String bookAuthors = TextUtils.join(", ", currentBook.getAuthors());
            intent.putExtra(BookDetailsActivity.EXTRA_BOOK_AUTHORS, bookAuthors);
            String bookCover = currentBook.getCover();
            intent.putExtra(BookDetailsActivity.EXTRA_BOOK_COVER, bookCover);
            startActivity(intent);
        }
    }

    private class BookAdapter extends RecyclerView.Adapter<BookHolder> {
        private List<Book> books;

        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position) {
            if (books != null) {
                Book book = books.get(position);
                holder.bind(book);
            }
            else {
                Log.d("MainActivity", "No books");
            }
        }

        @Override
        public int getItemCount() {
            if (books != null)
            {
                return books.size();
            }
            else {
                return 0;
            }
        }

        void setBooks(List<Book> books) {
            this.books = books;
            notifyDataSetChanged();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.book_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchBooksData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }


    private void setupBookListView(List<Book> books) {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BookAdapter adapter = new BookAdapter();
        adapter.setBooks(books);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchBooksData(String query) {
        String finalQuery = prepareQuery(query);
        BookService bookService = RetrofitInstance.getRetrofitInstance().create(BookService.class);

        Call<BookContainer> booksApiCall = bookService.findBooks(finalQuery);

        booksApiCall.enqueue(new Callback<BookContainer>() {
            @Override
            public void onResponse(Call<BookContainer> call, Response<BookContainer> response) {
                setupBookListView(response.body().getBookList());
            }

            @Override
            public void onFailure(Call<BookContainer> call, Throwable t) {
                Snackbar.make(findViewById(R.id.main_layout), "Something went wrong... Please try later!",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private String prepareQuery(String query) {
        String[] queryParts = query.split("\\s+");
        return TextUtils.join("+", queryParts);
    }

    private boolean checkNullOrEmpty(String text) {
        return text != null && !TextUtils.isEmpty(text);
    }
}