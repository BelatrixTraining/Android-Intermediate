# Shared Preferences

La clase `SharedPreferences` ofrece la capacidad de guardar data a través de pares clave-valor de tipos de datos primitivos tales como `int`, `boolean`, `float`, `long` y `String`. Estos datos se conservaran a través de distintas sesiones de usuarios y aún si es que la aplicación ha finalizado.

Existen dos maneras de obtener un objeto `SharedPreferences`:

 1. Usando el método `getSharedPreferences()` se podrá obtener un archivo de preferencias identificado por nombre (primer parámetro).
 2. Usando el método `getPreferences()` se podrá obtener un archivo de preferencias exclusivo de la actividad.

Una vez que tengamos la instancia de nuestro objeto `SharedPreferences` podemos comenzar a escribir valores, para esto necesitamos hacer lo siguiente:

 1. Llamar al `edit()` para obtener un objeto `SharedPreferences.Editor.`
 2. Agregar los valores usando los métodos `putString()`, `putFloat()`, etc.
 3. Confirmar los nuevos valores con `commit()`*.
 
 *Considerar el uso de `apply()` en lugar de `commit()` ya que este último efectúa los cambios de inmediato mientras que `apply()` lo maneja en background. 

Para leer los valores solo es necesario tener una referencia del SharedPreferences y usar los métodos `getBoolean()`, `getString()`, etc.

A continuación un ejemplo de como usar el SharedPreference.

```java
public class MyActivity extends Activity {
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle state){
       super.onCreate(state);
       . . .

       // Restore preferences
       SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
       boolean silent = settings.getBoolean("silentMode", false);
       setSilent(silent);
    }

    @Override
    protected void onStop(){
       super.onStop();

      
      SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = settings.edit();
      editor.putBoolean("silentMode", mSilentMode);
      editor.commit();
    }
}

```

