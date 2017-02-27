#SQLite

Android ofrece compatibilidad total con las bases de datos SQLite. Cualquier clase dentro de una aplicación podrá acceder por nombre a cualquier base de datos que crees.

A continuación veremos como trabajar con bases de datos SQLite haciendo uso de la clase `SQLiteOpenHelper` para ejecutar comandos SQL y manejar la base de datos local.

##Creando un gestor de base de datos

Necesitamos implementar una clase que extienda de `SQLiteOpenHelper` para poder manejar operaciones de creación, actualización, escritura y lectura de información en la base de datos.

```java
public class PostsDatabaseHelper extends SQLiteOpenHelper {
    // Información de la BD
    private static final String DATABASE_NAME = "postsDatabase";
    private static final int DATABASE_VERSION = 1;

    // Nombres de tablas
    private static final String TABLE_POSTS = "posts";
    private static final String TABLE_USERS = "users";

    // Columnas tabla POSTS
    private static final String KEY_POST_ID = "id";
    private static final String KEY_POST_USER_ID_FK = "userId";
    private static final String KEY_POST_TEXT = "text";

    // Columnas tabla USER
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PROFILE_PICTURE_URL = "profilePictureUrl";

    public PostsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    // Se llama cuando la conexión a la base de datos está siendo configurada
    // Se configura la BD para cosas como soporte de Foreign Keys, write-ahead logging, etc
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Se llama cuando la base de datos es creada por primera vez.
    // Si una base de datos existe con el mismo nombre, este método no será llamado.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_POSTS +
                "(" +
                    KEY_POST_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                    KEY_POST_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USERS + "," + // Define a foreign key
                    KEY_POST_TEXT + " TEXT" +
                ")";

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                    KEY_USER_ID + " INTEGER PRIMARY KEY," +
                    KEY_USER_NAME + " TEXT," +
                    KEY_USER_PROFILE_PICTURE_URL + " TEXT" +
                ")";

        db.execSQL(CREATE_POSTS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
    }
    
    // Se llama cuando la base de datos necesita ser actualizada.
    // Este método solo se llamara si existe una base de datos con el mismo nombre 
    //y el DATABASE_VERSION es distinto.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // La implementación más simple es borrar todas las viejas tablas y recrearlas.
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }
}
```

Tener en cuenta que la base de datos SQLite es inicializada en *lazy mode* de manera que esta no será creada hasta la primera llamada a `getReadableDatabase()` o `getWriteableDatabase()`. Es por ello que las llamadas a estos métodos deben ser hechas en un hilo secundario.

##Aplicando el patrón Singleton

Normalmente una base de datos SQLite será usada a través de toda la aplicación como por ejemplo dentro de servicios, actividades, fragmentos, etc. Para esto, las buenas prácticas recomiendan que se haga uso del patrón Singleton con el fin de evitar desperdicio de memoria y creación de instancias innecesarias.

```java
public class PostsDatabaseHelper extends SQLiteOpenHelper { 
  private static PostsDatabaseHelper sInstance;

  // ...

  public static synchronized PostsDatabaseHelper getInstance(Context context) {
    // Se recomienda usar el application context para evitar el leak de un contexto de Actividad
    if (sInstance == null) {
      sInstance = new PostsDatabaseHelper(context.getApplicationContext());
    }
    return sInstance;
  }

  /**
   * El constructor debe ser privado para evitar creaciones de instancias directas.
   */
  private PostsDatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }
}
```

##Referencias

 - https://developer.android.com/guide/topics/data/data-storage.html#db
