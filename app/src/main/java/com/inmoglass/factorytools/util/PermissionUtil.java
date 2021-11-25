package com.inmoglass.factorytools.util;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;
import java.util.List;

/**
 * @author Administrator
 * @github : https://192.168.3.113:8443/IMC-ROM/setting.git
 * @time : 2019/09/13
 * @desc : 权限检测
 */
public class PermissionUtil {

    /**
     * @param context
     * @return AccessibilityService permission check
     */
    public static boolean isAccessibilityServiceEnable(Context context) {
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        assert accessibilityManager != null;
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(
                        AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().contains(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSettingsCanWrite(Context context) {
        return Settings.System.canWrite(context);
    }

    public static boolean isCanDrawOverlays(Context context) {
        return Settings.canDrawOverlays(context);
    }

}

