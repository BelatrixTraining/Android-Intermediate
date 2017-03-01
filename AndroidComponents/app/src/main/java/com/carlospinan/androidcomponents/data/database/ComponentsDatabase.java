package com.carlospinan.androidcomponents.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carlospinan.androidcomponents.common.Globals;
import com.carlospinan.androidcomponents.data.provider.ComponentsProviderContract;

/**
 * @author Carlos Piñan
 */

public class ComponentsDatabase extends SQLiteOpenHelper {

    public ComponentsDatabase(Context context) {
        super(context, Globals.DATABASE_NAME, null, Globals.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createPokemonTable =
                "CREATE TABLE " + ComponentsProviderContract.PokemonTable.TABLE + " ("
                        + ComponentsProviderContract.PokemonTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + ComponentsProviderContract.PokemonTable.POKEMON_NAME + " TEXT NOT NULL,"
                        + ComponentsProviderContract.PokemonTable.POKEMON_TYPE + " TEXT NOT NULL, "
                        + ComponentsProviderContract.PokemonTable.POKEMON_PICTURE + " TEXT"
                        + ");";
        db.execSQL(createPokemonTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ComponentsProviderContract.PokemonTable.TABLE);
    }
}
