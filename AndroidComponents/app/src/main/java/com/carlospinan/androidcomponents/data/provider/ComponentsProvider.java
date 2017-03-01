package com.carlospinan.androidcomponents.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.carlospinan.androidcomponents.data.database.ComponentsDatabase;

/**
 * @author cpinan
 */

public class ComponentsProvider extends ContentProvider {

    // MATCHES CONSTANTS
    private static final int MATCHES = 100;
    private static final int MATCHES_WITH_ID = 101;
    private static final int MATCHES_WITH_POKEMON = 102;
    // END MATCHES CONSTANTS

    // FILTER
    private static final String POKEMON_BY_ID =
            ComponentsProviderContract.PokemonTable._ID + " ?";

    private static final String POKEMON_BY_NAME =
            ComponentsProviderContract.PokemonTable.POKEMON_NAME + " LIKE ?";
    // END FILTERS

    private static ComponentsDatabase database;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ComponentsProviderContract.BASE_CONTENT_URI.toString();
        matcher.addURI(authority, null, MATCHES);
        matcher.addURI(authority, ComponentsProviderContract.PokemonTable._ID, MATCHES_WITH_ID);
        matcher.addURI(authority, ComponentsProviderContract.PokemonTable.POKEMON_NAME, MATCHES_WITH_POKEMON);
        return matcher;
    }

    /**
     * Helper method to validate if the uri exists.
     *
     * @param uri
     * @return
     */
    private int matchUri(Uri uri) {
        String link = uri.toString();
        if (link.contentEquals(ComponentsProviderContract.BASE_CONTENT_URI.toString())) {
            return MATCHES;
        } else if (link.contentEquals(ComponentsProviderContract.PokemonTable.buildPokemonWithId().toString())) {
            return MATCHES_WITH_ID;
        } else if (link.contentEquals(ComponentsProviderContract.PokemonTable.buildPokemonWithName().toString())) {
            return MATCHES_WITH_POKEMON;
        }
        return -1;
    }

    @Override
    public boolean onCreate() {
        database = new ComponentsDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        int match = matchUri(uri);
        switch (match) {
            case MATCHES:
                cursor = database.getReadableDatabase().query(
                        ComponentsProviderContract.PokemonTable.TABLE,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MATCHES_WITH_ID:
                cursor = database.getReadableDatabase().query(
                        ComponentsProviderContract.PokemonTable.TABLE,
                        projection,
                        POKEMON_BY_ID,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MATCHES_WITH_POKEMON:
                cursor = database.getReadableDatabase().query(
                        ComponentsProviderContract.PokemonTable.TABLE,
                        projection,
                        POKEMON_BY_NAME,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = matchUri(uri);
        switch (match) {
            case MATCHES:
                return ComponentsProviderContract.PokemonTable.CONTENT_TYPE;
            case MATCHES_WITH_ID:
                return ComponentsProviderContract.PokemonTable.CONTENT_ITEM_TYPE;
            case MATCHES_WITH_POKEMON:
                return ComponentsProviderContract.PokemonTable.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = database.getWritableDatabase();
        int match = matchUri(uri);
        Uri returnUri;
        switch (match) {
            case MATCHES_WITH_POKEMON:
                long _id = db.insert(ComponentsProviderContract.PokemonTable.TABLE, null, values);
                if (_id > 0) {
                    returnUri = ComponentsProviderContract.PokemonTable.buildPokemonWithName();
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
