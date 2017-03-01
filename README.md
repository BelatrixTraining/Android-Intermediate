# Android-Intermediate

## Aplicación de componentes de Android

<img src="https://image.slidesharecdn.com/android1-10-110913042326-phpapp01/95/android-services-3-728.jpg?cb=1315887904" />

 - En caso te hayas olvidado, aquí tienes la referencia: 

 	https://github.com/BelatrixTraining/Android-Fundamentals/tree/Lesson2

 	Debido a que ya explicamos parte de esto en el módulo 1, en esta sección
 	nos enfocaremos en resaltar Servicios y Content Providers

  ### Ventajas de los content providers

  	-	Ofrecen control al acceso de datos.
  	-	Puede ser accedido desde otras aplicaciones.
  	-	Se usa para la implementación de widgets de escritorio que requieran comunicarse con información de la app. (Debe estar exportado)
  	-	Puedes configurar diferentes permisos de lectura y escritura.

  	<img src = "https://developer.android.com/guide/topics/providers/images/content-provider-tech-stack.png" />

  	A continuación, una imagen de ejemplo (obtenida de Android Developers) con respecto a como funciona el acceso a un provider:

  	<img src ="https://developer.android.com/guide/topics/providers/images/content-provider-interaction.png" />

  ## Sobre los servicios

  	<img src="https://www.tutorialspoint.com/android/images/services.jpg" />

```
package com.carlospinan.androidcomponents.data.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.carlospinan.androidcomponents.R;

/**
 * @author Carlos Piñan
 */

public class SoundService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.unknown_sound);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}

```	 

## Referencias

  - https://developer.android.com/guide/topics/providers/content-providers.html
  - https://developer.android.com/guide/topics/providers/content-provider-creating.html
  - https://github.com/cpinan/AndroidNanodegree/tree/master/SuperDuo
  - https://github.com/TeclaLabsPeruTraining/Android-Fundamentals-GMD/blob/Lesson2/AppComponents/app/src/main/java/com/gmd/lessons/services/SoundService.java
