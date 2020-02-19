package Tool;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionsChecker {

    /**
     * author: WangYiKai
     * date: on 2019/4/15.
     * describe:检查权限的工具类
     */
        private final Context mContext;

        public PermissionsChecker(Context context) {
            mContext = context.getApplicationContext();
        }

        // 判断权限集合
        public boolean lacksPermissions(String... permissions) {
            for (String permission : permissions) {
                if (lacksPermission(permission)) {
                    return true;
                }
            }
            return false;
        }

        // 判断是否缺少权限
        private boolean lacksPermission(String permission) {
            return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED;
        }

}
