package com.carlospinan.androidcomponents;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.carlospinan.androidcomponents.data.provider.ComponentsProviderContract;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AndroidComponentsTest {

    @Test
    public void provider() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        appContext.getContentResolver().delete(
                ComponentsProviderContract.PokemonTable.buildPokemonWithName(),
                null,
                null
        );

        appContext.getContentResolver().insert(
                ComponentsProviderContract.PokemonTable.buildPokemonWithName(),
                getPokemonValues("Charmander", "Fuego")
        );

        Cursor cursor1 = appContext.getContentResolver().query(
                ComponentsProviderContract.PokemonTable.buildPokemon(),
                null,
                null,
                null,
                null
        );
        Assert.assertNotNull(cursor1);
        Assert.assertTrue(cursor1.getCount() > 0);

        appContext.getContentResolver().insert(
                ComponentsProviderContract.PokemonTable.buildPokemonWithName(),
                getPokemonValues("Bulbasaur", "Hierba")
        );

        appContext.getContentResolver().insert(
                ComponentsProviderContract.PokemonTable.buildPokemonWithName(),
                getPokemonValues("Squirtle", "Agua")
        );

        Cursor cursor2 = appContext.getContentResolver().query(
                ComponentsProviderContract.PokemonTable.buildPokemon(),
                null,
                null,
                new String[]{"Bulbasaur"},
                null
        );
        Assert.assertNotNull(cursor2);
        Assert.assertTrue(cursor2.getCount() > 0);


    }

    private ContentValues getPokemonValues(String name, String type) {
        final ContentValues values = new ContentValues();
        values.put(ComponentsProviderContract.PokemonTable.POKEMON_NAME, name);
        values.put(ComponentsProviderContract.PokemonTable.POKEMON_TYPE, type);
        values.put(ComponentsProviderContract.PokemonTable.POKEMON_PICTURE, "http://www.google.com");
        return values;
    }
}
