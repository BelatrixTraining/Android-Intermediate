# Android-Intermediate

## Runtime Permissions

Comencemos con la parte de Runtime permissions, esto aplica a partir del API level 23 o Android M. Hay que tener en cuenta que no todos los permisos deben ser implementados usando runtime permissions (como internet por ejemplo), así que veremos una lista de los permisos, un ejemplo y luego la parte de GPS.

<img src="https://inthecheesefactory.com/uploads/source/blog/mpermission/runtimepermissioncrash.jpg"/>

Al igual que cuando intentas hacer una petición a una url sin incluir el permiso de internet genera una excepción. Es el mismo caso a partir de Android M, si vas a utilizar un permiso que requiera el consentimiento del usuario, pues debes pedirlo manualmente y también tenerlo registrado en el AndroidManifest.xml

<img src="https://inthecheesefactory.com/uploads/source/blog/mpermission/permissionsrevoke.jpg" />

Además, puedes entrar a los settings de tu app y modificar los permisos manualmente. A continuación, una tabla con los permisos que requieren el consentimiento del usuario:

<img src="https://inthecheesefactory.com/uploads/source/blog/mpermission/permgroup.png" />

Ejemplo de la página de Android Developers:

```java
int permissionCheck = ContextCompat.checkSelfPermission(thisActivity,
        Manifest.permission.WRITE_CALENDAR);
```

```java
// Here, thisActivity is the current activity
if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.READ_CONTACTS)
        != PackageManager.PERMISSION_GRANTED) {

    // Should we show an explanation?
    if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
            Manifest.permission.READ_CONTACTS)) {

        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.

    } else {

        // No explanation needed, we can request the permission.

        ActivityCompat.requestPermissions(thisActivity,
                new String[]{Manifest.permission.READ_CONTACTS},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
        // app-defined int constant. The callback method gets the
        // result of the request.
    }
}
```

Ejemplo de una librería simple creada:

```java
package com.carlospinan.inapbilling.presentation.common.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * @author Carlos Piñan
 */

public class AndroidMPermission {

    public interface Callback {
        void onPermissionSuccess();

        void onPermissionDeny();

        void onShowRationale(String permissionName);
    }

    // STATIC
    private static AndroidMPermission instance;

    // ATTRIBUTES
    private Integer permissionRequestCode;
    private Callback callback;

    private AndroidMPermission() { /* UNUSED */ }

    public static AndroidMPermission get() {
        if (instance == null) {
            instance = new AndroidMPermission();
        }
        return instance;
    }

    public boolean checkPermissions(Activity activity, String... permissions) {
        for (String permission : permissions) {
            int selfPermission = ContextCompat.checkSelfPermission(activity, permission);
            if (selfPermission != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void askForPermissions(
            Callback callback,
            int permissionRequestCode,
            Activity activity,
            String... permissions
    ) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback cannot be null.");
        }
        this.callback = callback;
        this.permissionRequestCode = permissionRequestCode;
        if (!checkPermissions(activity, permissions)) {
            boolean showRationale = false;
            String permissionName = null;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    permissionName = permission;
                    showRationale = true;
                    break;
                }
            }
            if (permissionName != null && showRationale) {
                this.callback.onShowRationale(permissionName);
            } else {
                ActivityCompat.requestPermissions(activity, permissions, permissionRequestCode);
            }
        }
    }

    public void permissionSuccess(int permissionRequestCode, int... grantResults) {
        if (this.callback != null &&
                this.permissionRequestCode != null &&
                this.permissionRequestCode == permissionRequestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.callback.onPermissionSuccess();
            } else {
                this.callback.onPermissionDeny();
            }
        }
    }
}
```


## Geolocalización

Aquí solo veremos Geolocalización sin mapas, la próxima clase se vera la parte de Google maps.

Vamos a ver dos:

1. Location Listener, que es el que estoy habituado a usar.
2. Fused Location, usando el api de Google.

Asi que vayamos al código.


# Referencias
- https://developer.android.com/training/permissions/requesting.html
- https://developer.android.com/training/permissions/index.html
- https://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en
- https://github.com/cpinan/Android-In-App-Billing-Example
- https://developer.android.com/preview/features/runtime-permissions.html#normal
- https://github.com/sitepoint-editors/RuntimePermissions
- http://hotchemi.github.io/PermissionsDispatcher/
- https://developer.android.com/training/location/retrieve-current.html
- https://developer.android.com/training/location/index.html