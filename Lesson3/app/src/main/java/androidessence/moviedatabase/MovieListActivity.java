package androidessence.moviedatabase;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MovieListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        //insertarData();
        obtenerData();
    }

    private void obtenerData() {
        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
        Log.e("Pablo", "" + cursor.getCount());
    }

    private void insertarData() {
        Genre terror = new Genre("Terror");
        Genre suspenso = new Genre("Suspenso");
        Genre musical = new Genre("Musical");

        Uri terrorUri = getContentResolver().insert(MovieContract.GenreEntry.CONTENT_URI, MovieContract.GenreEntry.getContentValue(terror));
        Uri suspensoUri = getContentResolver().insert(MovieContract.GenreEntry.CONTENT_URI, MovieContract.GenreEntry.getContentValue(suspenso));
        Uri musicalUri = getContentResolver().insert(MovieContract.GenreEntry.CONTENT_URI, MovieContract.GenreEntry.getContentValue(musical));


        Movie movie1 = new Movie("LALALA", ContentUris.parseId(musicalUri), "2-08-2016");
        Movie movie2 = new Movie("JOJOJO", ContentUris.parseId(musicalUri), "2-08-2016");
        Movie movie3 = new Movie("Sexto Sentido", ContentUris.parseId(suspensoUri), "2-08-2016");
        Movie movie4 = new Movie("Suspensoo", ContentUris.parseId(suspensoUri), "2-08-2016");
        Movie movie5 = new Movie("Freddy Kruger", ContentUris.parseId(terrorUri), "2-08-2016");
        Movie movie6 = new Movie("Exorcista", ContentUris.parseId(terrorUri), "2-08-2016");

        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry.getContentValue(movie1));
        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry.getContentValue(movie2));
        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry.getContentValue(movie3));
        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry.getContentValue(movie4));
        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry.getContentValue(movie5));
        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry.getContentValue(movie6));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
