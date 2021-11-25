package com.inmoglass.factorytools.cameratest;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;

/**
 * 闪光灯测试项
 *
 * @author Administrator
 * @date 2021-11-22
 */
public class FlashLightTestActivity extends AbstractTestActivity {

    private static final String TAG = FlashLightTestActivity.class.getSimpleName();
    private Button openFlashLightBtn;
    private Button closeFlashLightBtn;
    private TextView statusTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_light_test);
        openFlashLightBtn = findViewById(R.id.btn_open_flash_light);
        closeFlashLightBtn = findViewById(R.id.btn_close_flash_light);
        statusTv = findViewById(R.id.tv_flash_light_status);
        initListener();
    }

    private void initListener() {
        openFlashLightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFlashLight(true);
                statusTv.setText("开");
            }
        });

        closeFlashLightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFlashLight(false);
                statusTv.setText("关");
            }
        });
    }

    private void switchFlashLight(boolean isEnabled) {
        try {
            CameraManager mCameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
            String[] ids = mCameraManager.getCameraIdList();
            for (String id : ids) {
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(id);
                Boolean flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                Integer lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (flashAvailable != null && flashAvailable && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                    mCameraManager.setTorchMode(id, isEnabled);
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
