#Content Provider

Los Content Providers son un mecanismo de Android que hace posible acceder a data de otras aplicaciones. Estos soportan las 4 operaciones básicas (CRUD).

##Preparación
Primero debemos crear las clases de contrato que definirán el esquema de la base de datos. Para el ejemplo crearemos una base de datos de películas. Dentro de nuestra clase `MovieContract` definiremos algunas propiedades: 

```java
    /**
     * El Content Authority es un nombre que se le da a todo el Content Provider. 
     * Un nombre conveniente es el paquete de la aplicación, ya que 
     * está garantizado que será único en el dispositivo
     */
    public static final String CONTENT_AUTHORITY = "com.androidessence.moviedatabase";

    /**
     * El content authority se usa para crear la base de todas las URIs las cuales 
     * las aplicaciones usarán para contactar a este content provider.
     */
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Una lista de posibles rutas que serán agregadas a la URI base para cada tabla
     */
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_GENRE = "genre";
```
Para cada tabla se debe crear una clase que extienda de la clase `BaseColumns`, la cual incluye un String `_ID`  que es usado para el auto incremento del id de cada tabla. Además es necesario definir una URI para la tabla, un tipo MIME para la respuesta de las consultas y un método para construir una URI para una fila individual de la tabla. A continuación hay ejemplos de MovieTable y GenreTable:

```java
    public static final class MovieEntry implements BaseColumns {
        // Content URI representa la ubicación de la tabla
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        // Estos son tipos especiales de prefijos que especifican si una 
        //URI devuelve una lista o un item individual.
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_MOVIE;

        // Se define el esquema de la tabla
        public static final String TABLE_NAME = "movieTable";
        public static final String COLUMN_NAME = "movieName";
        public static final String COLUMN_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_GENRE = "movieGenre";

        // Definir una funciona para construir una URI que 
        // encuentre una película por su identificador
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class GenreEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_GENRE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_GENRE;

        public static final String TABLE_NAME = "genreTable";
        public static final String COLUMN_NAME = "genreName";

        public static Uri buildGenreUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
```

Necesitamos, además, implementar una clase que extienda de SQLiteOpenHelper y sobreescribir sus métodos `onCreate()` y `onUpgrade()`.

```java
    public class MovieDBHelper extends SQLiteOpenHelper{
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "movieList.db"; 
 
        public MovieDBHelper(Context context){
           super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
    }
@Override
    public void onCreate(SQLiteDatabase db) {
        addGenreTable(db);
        addMovieTable(db);
    }

   
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//TODO
    }
```

Por último, escribimos el código SQL para la creación de las tablas:

```java
    private void addGenreTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + MovieContract.GenreEntry.TABLE_NAME + " (" +
                        MovieContract.GenreEntry._ID + " INTEGER PRIMARY KEY, " +
                        MovieContract.GenreEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL);"
        );
    }


    private void addMovieTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                        MovieContract.MovieEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_GENRE + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" + MovieContract.MovieEntry.COLUMN_GENRE + ") " +
                        "REFERENCES " + MovieContract.GenreEntry.TABLE_NAME + " (" + MovieContract.GenreEntry._ID + "));"
        );
    }
```

##Content Provider

Una vez hecho lo anterior tenemos todo listo para la creación del content provider. Veremos paso a paso su impementación:

Primero, debemos crear un entero por cada URI que tengamos:

```java
public class MovieProvider extends ContentProvider {
    // Se usa un entero diferente por cada URI que vayamos a correr.
    private static final int GENRE = 100;
    private static final int GENRE_ID = 101;
    private static final int MOVIE = 200;
    private static final int MOVIE_ID = 201;
}
```

Luego definimos nuestras otras variables como por ejemplo `SQLiteOpenHelper` que se usará para acceder a la base de datos y `URIMatcher` la cual usará cada URI que hemos creado y las relacionará con los enteros que también creamos anteriormente:

```java
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    /**
     * Construye un UriMatcher que se usará para determinar que request de BD ha sido hecho.
     */
    public static UriMatcher buildUriMatcher(){
        String content = MovieContract.CONTENT_AUTHORITY;

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, MovieContract.PATH_GENRE, GENRE);
        matcher.addURI(content, MovieContract.PATH_GENRE + "/#", GENRE_ID);
        matcher.addURI(content, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(content, MovieContract.PATH_MOVIE + "/#", MOVIE_ID);

        return matcher;
    }
```

####El método getType

Este método es usado para definir el tipo MIME del resultado:

```java
    @Override
    public String getType(Uri uri) {
        switch(sUriMatcher.match(uri)){
            case GENRE:
                return MovieContract.GenreEntry.CONTENT_TYPE;
            case GENRE_ID:
                return MovieContract.GenreEntry.CONTENT_ITEM_TYPE;
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
```

###El método Query

Este método lleva 5 parámetros:
- uri: El URI que debe ser consultado.
- projection: Un arreglo de String de las columnas que serán devueltas en el resultado.
- selection: Un String definiendo el criterio para seleccionar los resultados.
- selectionArgs: Argumentos que forman parte del parametro anterior.
- sortOrder: Un String que indica el orden del resultado.

```java
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){
            case GENRE:
                retCursor = db.query(
                        MovieContract.GenreEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case GENRE_ID:
                long _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        MovieContract.GenreEntry.TABLE_NAME,
                        projection,
                        MovieContract.GenreEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE:
                retCursor = db.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }
```

###El método Insert

Este metodo tiene como parametro un objeto `ContentValues` el cual lleva los nombres de las columnas y valores a ser insertados:

```java
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch(sUriMatcher.match(uri)){
            case GENRE:
                _id = db.insert(MovieContract.GenreEntry.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri =  MovieContract.GenreEntry.buildGenreUri(_id);
                } else{
                    throw new UnsupportedOperationException("No se pudo insertar filas en esta URI: " + uri);
                }
                break;
            case MOVIE:
                _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                } else{
                    throw new UnsupportedOperationException("No se pudo insertar filas en esta URI: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("URI desconocida: " + uri);
        }

        // Se notifica el cambio
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }
```

###Los métodos Update y Delete
Los métodos update y delete tienen como parámetro un String y un array de Strings que definen que filas deben ser modificadas o eliminadas.

```java
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows; // Numero de filas afectadas

        switch(sUriMatcher.match(uri)){
            case GENRE:
                rows = db.delete(MovieContract.GenreEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE:
                rows = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("URI desconocida: " + uri);
        }

        if(selection == null || rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows;

        switch(sUriMatcher.match(uri)){
            case GENRE:
                rows = db.update(MovieContract.GenreEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MOVIE:
                rows = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("URI desconocida: " + uri);
        }

        if(rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }
```

##Usando el Content Provider

Para poder usarlo, aun incluso desde nuestra aplicación, es necesario especificarlo en el Manifest:

```xml
     <provider
            android:name=".MovieProvider"
            android:authorities="com.androidessence.moviedatabase"
            android:exported="false"
            android:protectionLevel="signature"
            android:syncable="true"/>
```

##Referencias

 - https://github.com/androidessence/MovieDatabase
 - http://developer.android.com/guide/topics/providers/content-providers.html
