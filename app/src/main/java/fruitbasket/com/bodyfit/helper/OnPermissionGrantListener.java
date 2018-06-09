package fruitbasket.com.bodyfit.helper;

public interface OnPermissionGrantListener {
    void onPermissionGranted(PermissionHelper.Permission... grantedPermissions);

    void onPermissionsDenied(PermissionHelper.Permission... deniedPermissions);

    abstract class OnPermissionGrantListenerAdapter implements OnPermissionGrantListener {
        @Override
        public void onPermissionGranted(PermissionHelper.Permission... grantedPermissions) {

        }

        @Override
        public void onPermissionsDenied(PermissionHelper.Permission... deniedPermissions) {

        }
    }
}
