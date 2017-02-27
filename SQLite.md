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

La forma de acceder a una instancia de nuestra base de datos sería la siguiente:

```java
PostsDatabaseHelper helper = PostsDatabaseHelper.getInstance(this);
```

##Definir los modelos

Para este ejemplo definiremos dos clases Post.java y User.java, con el fin de poder acceder a la data más fácilmente.

```java
public class Post {
    public User user;
    public String text;
}
```

```java
public class User {
    public String userName;
    public String profilePictureUrl;
}
```

##Operaciones CRUD

Ahora haremos ejemplos de inserción, lectura, actualización y eliminación de posts y users en nuestra base de datos.

###Insertando nueva data 

```java
public class PostsDatabaseHelper extends SQLiteOpenHelper {
    // ...metodos existentes...

    // Inserta un post en la base de datos
    public void addPost(Post post) {
        // crea o abre la BD para escritura
        SQLiteDatabase db = getWritableDatabase();

        // Se recomienda usar una transacción ya que de esta manera 
        //nos aseguramos de la consistencia de la data.
        db.beginTransaction();
        try {
          
            long userId = addOrUpdateUser(post.user);

            ContentValues values = new ContentValues();
            values.put(KEY_POST_USER_ID_FK, userId);
            values.put(KEY_POST_TEXT, post.text);

            // No se especifica la Primary Key ya que SQLite la autoincrementa.
            db.insertOrThrow(TABLE_POSTS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Ocurrió un error al guardar un post en la base de datos");
        } finally {
            db.endTransaction();
        }
    }

    // Inserta o actualiza un usuario en la BD.
    public long addOrUpdateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_NAME, user.userName);
            values.put(KEY_USER_PROFILE_PICTURE_URL, user.profilePictureUrl);

            // Primero tratar de actualizar el usuario en caso este ya exista en la BD.
            // Se asume que el nombre de usuario es único
            int rows = db.update(TABLE_USERS, values, KEY_USER_NAME + "= ?", new String[]{user.userName});

            // Verificar si el update fue exitoso
            if (rows == 1) {
                // Obtener el PK del usuario actualizado
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER_ID, TABLE_USERS, KEY_USER_NAME);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.userName)});
                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // El usuario con este userName no existe asi que es necesario crear un nuevo usuario
                userId = db.insertOrThrow(TABLE_USERS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error al agregar o actualizar un usuario");
        } finally {
            db.endTransaction();
        }
        return userId;
    }
}
```

###Leyendo la data

```java
public class PostsDatabaseHelper extends SQLiteOpenHelper {
    // ...métodos existentes...

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s = %s.%s",
                        TABLE_POSTS,
                        TABLE_USERS,
                        TABLE_POSTS, KEY_POST_USER_ID_FK,
                        TABLE_USERS, KEY_USER_ID);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    User newUser = new User();
                    newUser.userName = cursor.getString(cursor.getColumnIndex(KEY_USER_NAME));
                    newUser.profilePictureUrl = cursor.getString(cursor.getColumnIndex(KEY_USER_PROFILE_PICTURE_URL));

                    Post newPost = new Post();
                    newPost.text = cursor.getString(cursor.getColumnIndex(KEY_POST_TEXT));
                    newPost.user = newUser;
                    posts.add(newPost);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "error al traer los posts de la BD");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return posts;
    }
}
```

###Actualizando data
```java
public class PostsDatabaseHelper extends SQLiteOpenHelper {
    // ...métodos existentes...

    // Actualiza la url de la imagen del profile del usuario
    public int updateUserProfilePicture(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_PROFILE_PICTURE_URL, user.profilePictureUrl);

        return db.update(TABLE_USERS, values, KEY_USER_NAME + " = ?",
                new String[] { String.valueOf(user.userName) });
    }
}
```

###Eliminando data

```java
public class PostsDatabaseHelper extends SQLiteOpenHelper {
    // ...métodos existentes...

    public void deleteAllPostsAndUsers() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_POSTS, null, null);
            db.delete(TABLE_USERS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error al momento de eliminar");
        } finally {
            db.endTransaction();
        }
    }
}
```

##Como usar el gestor de base de datos?

Ahora podemos utilizar el gestor que hemos creado para insertar y leer usuarios y posts desde nuestra base de datos.

```java
public class SQLiteExampleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_example);

        User sampleUser = new User();
        sampleUser.userName = "Steph";
        sampleUser.profilePictureUrl = "https://i.imgur.com/tGbaZCY.jpg";

        Post samplePost = new Post();
        samplePost.user = sampleUser;
        samplePost.text = "Won won!";

        // obtiene la instancia singleton del databaseHelper
        PostsDatabaseHelper databaseHelper = PostsDatabaseHelper.getInstance(this);
        
        databaseHelper.addPost(samplePost);

        List<Post> posts = databaseHelper.getAllPosts();
        for (Post post : posts) {
            // TODO
        }
    }
}
```

## SQLite Database Debugging 

Cuando trabajemos con SQLite, abrir e inspeccionar la base de datos siempre sera útil para debugear problemas. Podemos usar las siguientes herramientas para obtener la data:

### En un emulador

Usar `SQLite3` para obtener la data en un emulador:

```bash
cd /path/to/my/sdk/platform-tools
./adb shell
run-as <app package name>
cd /data/data/<app package name>/databases
ls
chmod 666 <database file name>
sqlite3 <database file name>
> (semi-colon terminated commands can be run here to query the data)
> .exit
(copy full database path)
exit
```

Para examinarla más a fondo podemos descargar la BD de la siguiente manera:

```
./adb wait-for-device pull /data/data/<app package name>/databases/<database file name>
```

### En un dispositivo

No existe un ejecutable `SQLite3` en el dispositivo así que nuestra única opción es descargar la BD de la siguiente manera:

```bash
./adb shell run-as <app package name> chmod 666 /data/data/<app package name>/databases/<database file name>
./adb shell cp /data/data/<app package name>/databases/<database file name> /sdcard/
./adb pull /sdcard/<database file name>
```

### Usando el *Android Device Monitor*

Este se encuentra en `Tools`->`Android Device Monitor`->`File Explorer`.
Buscar dentro de `/data/<app package name>/databases` y descargar el archivo.

##Referencias

 - https://developer.android.com/guide/topics/data/data-storage.html#db
 - http://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html
