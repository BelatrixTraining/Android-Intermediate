package com.belatrixsf.bobbyproject.commons;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * @author Carlos Piñan
 * @date 3/13/17
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
            if (grantResults.length > 0) {
                boolean grantedPermissions = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        grantedPermissions = false;
                        break;
                    }
                }
                if (grantedPermissions) {
                    this.callback.onPermissionSuccess();
                } else {
                    this.callback.onPermissionDeny();
                }
            } else {
                this.callback.onPermissionDeny();
            }
        }
    }

}
