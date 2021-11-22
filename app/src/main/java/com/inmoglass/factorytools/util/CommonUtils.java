package com.inmoglass.factorytools.util;

import android.os.SystemProperties;
import android.util.Log;

public class CommonUtils {

    private static final String TAG = CommonUtils.class.getSimpleName();

    public static boolean isBoardISharkL210c10() {
        String board = SystemProperties.get("ro.product.board", "unknown");
        Log.d(TAG, "isBoardISharkL210c10 board=" + board);
        if (board != null && board.equals("sp9853i_10c10_vmm")) {
            return true;
        }
        if (board != null && board.equals("sp9832e_10c10_32b")) {
            return true;
        }
        return false;
    }
}
