package com.carlospinan.androidcomponents.data.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Carlos Piñan
 */

public class ComponentsProviderContract {

    public static final String CONTENT_AUTHORITY = "carlos.pinan.bobby.com";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class PokemonTable implements BaseColumns {

        public static final String TABLE = "pokemon";

        public static final String PATH = "pokemon";

        public static final String POKEMON_NAME = "name";
        public static final String POKEMON_TYPE = "type";
        public static final String POKEMON_PICTURE = "picture";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                        CONTENT_AUTHORITY + "/" +
                        PATH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                        CONTENT_AUTHORITY + "/" +
                        PATH;

        public static Uri buildPokemon() {
            return BASE_CONTENT_URI.buildUpon().build();
        }

        public static Uri buildPokemonWithId() {
            return BASE_CONTENT_URI.buildUpon().appendPath(_ID).build();
        }

        public static Uri buildPokemonWithName() {
            return BASE_CONTENT_URI.buildUpon().appendPath(POKEMON_NAME).build();
        }

    }

}
