#Files 

##Almacenamiento interno
Es posible guardar archivos en el almacenamiento interno del dispositivo. Por defecto, todos los archivos que se guarden en el almacenamiento interno son privados para la aplicación. Otras aplicaciones no podrán acceder a ellos así como tampoco el usuario. Cuando la aplicación es desinstalada los archivos también se borrarán.
Para crear un archivo privado en el almacenamiento interno es necesario realizar lo siguiente:

 1. Llamar al `openFileOutput()` pasando el nombre del archivo y el modo de operación como parámetros. Esto devolverá un `FileOutputStream`.
 2. Realizar las operaciones de escritura en el archivo con el método `write()`.
 3. Finalmente, cerrar el flujo con `close()`.

Ejemplo:

```java
String FILENAME = "hello_file";
String content = "hello world!";

FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
fos.write(content.getBytes());
fos.close();
```

MODE_PRIVATE creará el archivo (o lo reemplazará si ya existe) y lo hará privado para la aplicación. Otros modos disponibles son MODE_APPEND, MODE_WORLD_READABLE y MODE_WORLD_WRITEABLE.

Para leer un archivo desde el almacenamiento interno se debe hacer lo siguiente:

 1. Llamar al `openFileInput()` pasando el nombre del archivo y el modo de operación como parámetros. Esto devolverá un `FileInputStream`.
 2. Leer los bytes del archivo con `read()`.
 3. Finalmente, cerrar el flujo con `close()`.

###Algunos métodos útiles

 - getFilesDir() obtiene la ruta absoluta al directorio de sistemas de archivos donde se guardan los archivos internos.
 - getDir() crea (o abre si es que existe) un directorio dentro del espacio de almacenamiento interno de la aplicación.
 - deleteFile() quita un archivo guardado en el almacenamiento interno.
 - fileList() muestra un conjunto de archivos que la aplicación guarda actualmente.

##Almacenamiento externo
Todo dispositivo compatible con Android admite un “almacenamiento externo” compartido, que puedes usar para guardar archivos. Los archivos guardados en el almacenamiento externo pueden ser leídos por cualquier usuario y este puede modificarlos cuando habilita el almacenamiento masivo de USB para transferir archivos a una computadora.

###Como obtener acceso al almacenamiento externo
Es necesario especificar los permisos de sistema del READ_EXTERNAL_STORAGE o WRITE_EXTERNAL_STORAGE para poder realizar acciones de lectura - escritura en archivos del almacenamiento externo.

```xml
<manifest ...>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    ...
</manifest>
```

###Comprobar la disponibilidad a los medios
Antes de comenzar a trabajar con archivos en el almacenamiento externo es necesario si el medio esta disponible. Para esto usamos el método getExternalStorageState(). 

```java
/* Chequea si almacenamiento externo esta disponible para lectura y escritura */
public boolean isExternalStorageWritable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state)) {
        return true;
    }
    return false;
}

/* Chequea si almacenamiento externo esta disponible al menos para poder ser leido*/
public boolean isExternalStorageReadable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state) ||
        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
        return true;
    }
    return false;
}
```

###Guardar archivos disponibles para otras aplicaciones
Generalmente los archivos que el usuario obtenga mediante la aplicación deben guardarse en una ubicación publica del dispositivo, la cual pueda ser accedida por otras aplicaciones y el usuario pueda copiarlos fácilmente desde el dispositivo.
Para esto, podemos hacer uso de alguno de los directorios públicos compartidos, como Music/, Pictures/ y Ringtones/.

A continuación, se describe un método que crea un directorio para un nuevo álbum de fotografías en el directorio público de imágenes:

```java
public File getAlbumStorageDir(String albumName) {
    // Get the directory for the user's public pictures directory.
    File file = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), albumName);
    if (!file.mkdirs()) {
        Log.e(LOG_TAG, "Directory not created");
    }
    return file;
}
```

