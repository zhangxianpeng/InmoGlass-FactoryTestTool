package com.inmoglass.factorytools.sensortest;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inmoglass.factorytools.AbstractTestActivity;
import com.inmoglass.factorytools.R;
import com.inmoglass.factorytools.view.CompassVerticalView;
import com.inmoglass.factorytools.view.CompassView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指南针测试项
 *
 * @author Administrator
 * @date 2021-11-18
 */
public class CompassSensorTestActivity extends AbstractTestActivity {
    private static final String TAG = CompassSensorTestActivity.class.getSimpleName();
    public static final String PREFS_NAME = "com.yunos.alicompass";
    public static final String VALUE_HAS_MAEKED = "value has marked";
    public static final String VALUE_PRESSURE = "pressure";
    // public static final String URL_PRESSURE_HEAD =
    // "http://api.worldweatheronline.com/free/v1/weather.ashx?key=9cnu9mhtxns4ubdqtkrnjefm&q=48.85,2.35&fx=no&format=json";
    public static final String URL_PRESSURE_HEAD = "http://api.worldweatheronline.com/free/v1/weather.ashx?key=9cnu9mhtxns4ubdqtkrnjefm&q=";
    public static final String URL_PRESSURE_TEAR = "&fx=no&format=json";
    private final float MAX_ROATE_DEGREE = 4.0f;// rotate 4° every time
    private final float VALUE_STANDAND_PRESSURE = 1013.25f;
    private long mRequestLocation = 1000 * 600;// query address when 10 seconds
    private boolean mHorizontal = true;
    private boolean mHasPressure = false;
    private boolean mHasMarked = false;
    private boolean mCheckInfo = false;
    private boolean mInitView = true;
    private SensorManager mSensorManager;
    private Sensor mOrientationSensor;
    private Sensor mPressureSensor;
    private LocationManager mLocationManager;
    private String mLocationProvider;
    private float mDirection;
    private float mTargetDirection;
    private AccelerateInterpolator mInterpolator;
    protected final Handler mHandler = new Handler();
    private boolean mStopDrawing;
    private AlertDialog mDialog;

    private CompassView mPointer;// the horizontal view of compass
    private CompassVerticalView mVerticalPointer;// the vertical view of compass
    private Location mLocation;
    private TextView mAngleText;
    private TextView mAngleDirection;
    private LinearLayout mLonlatLayout;
    private LinearLayout mAltPreLayout;
    private TextView mLongitude;
    private TextView mLatitude;
    private TextView mCity;
    private TextView mPressure;
    private TextView mAltitude;
    private RelativeLayout mHorizontalView;
    private RelativeLayout mVerticalView;
    private TextView mVerticalDirectionEn;
    private TextView mVerticalDirectionCh;
    private Vibrator mVibrator;
    private String mStrPressure;
    private String mStrAltitude;
    private float mLastDirection;
    private float mStandandPressure = SensorManager.PRESSURE_STANDARD_ATMOSPHERE;

    // the thread for compass rotation， update the compass when 20ms
    protected Runnable mCompassViewUpdater = new Runnable() {
        public void run() {
            if (mPointer != null && !mStopDrawing) {
                if (mDirection != mTargetDirection) {

                    // calculate the short routine
                    float to = mTargetDirection;
                    if (to - mDirection > 180) {
                        to -= 360;
                    } else if (to - mDirection < -180) {
                        to += 360;
                    }

                    // limit the max speed to MAX_ROTATE_DEGREE
                    float distance = to - mDirection;
                    if (Math.abs(distance) > MAX_ROATE_DEGREE) {
                        distance = distance > 0 ? MAX_ROATE_DEGREE
                                : (-1.0f * MAX_ROATE_DEGREE);
                    }

                    if (mHorizontal) {
                        if (mInitView || View.VISIBLE == mVerticalView.getVisibility()) {
                            Map<String, String> lProperties = new HashMap<String, String>();
                            lProperties.put("mode", "horizontal");
                        }
                        mHorizontalView.setVisibility(View.VISIBLE);
                        mVerticalView.setVisibility(View.GONE);
                        // need to slow down if the distance is short
                        mDirection = normalizeDegree(mDirection + ((to - mDirection) * mInterpolator.getInterpolation(Math.abs(distance) > MAX_ROATE_DEGREE ? 0.5f : 0.4f)));
                        mPointer.updateDirection(mDirection);
                    } else {
                        if (View.VISIBLE == mHorizontalView.getVisibility()) {
                            Map<String, String> lProperties = new HashMap<String, String>();
                            lProperties.put("mode", "vertical");
                        }
                        mHorizontalView.setVisibility(View.GONE);
                        mVerticalView.setVisibility(View.VISIBLE);
                        mVerticalPointer
                                .updateDirection((mTargetDirection) % 360);
                    }
                    mInitView = false;
                }

                updateDirection();

                mHandler.postDelayed(mCompassViewUpdater, 20);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass_sensor_test);
        initResources();
        initServices();
        mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLocationProvider != null) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mLocationManager.requestLocationUpdates(mLocationProvider, mRequestLocation, 1000, mLocationListener);
                }
            }, 100);
        } else {
        }
        if (mOrientationSensor != null) {
            mSensorManager.registerListener(mOrientationSensorEventListener,
                    mOrientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mPressureSensor != null) {
            mHasPressure = mSensorManager.registerListener(
                    mPressureEventListener, mPressureSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        mHasMarked = this.containsPreference(VALUE_HAS_MAEKED);

        mStopDrawing = false;
        mHandler.postDelayed(mCompassViewUpdater, 20);
    }

    protected void onPause() {
        super.onPause();
        mStopDrawing = true;
        if (mOrientationSensor != null) {
            mSensorManager.unregisterListener(mOrientationSensorEventListener);
        }
        if (mPressureSensor != null) {
            mSensorManager.unregisterListener(mPressureEventListener);
        }
        if (mLocationProvider != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    private void initResources() {
        mDirection = 0.0f;
        mTargetDirection = 0.0f;
        mInterpolator = new AccelerateInterpolator();
        mStopDrawing = true;

        mPointer = (CompassView) findViewById(R.id.compass_detail);
        mVerticalPointer = (CompassVerticalView) findViewById(R.id.compass_detail_vertical);
        mAngleText = (TextView) findViewById(R.id.angle_number);
        mAngleDirection = (TextView) findViewById(R.id.angle_direction);
        mLongitude = (TextView) findViewById(R.id.longitude);
        mLatitude = (TextView) findViewById(R.id.latitude);
        mLonlatLayout = (LinearLayout) findViewById(R.id.lonlat_layout);
        mAltPreLayout = (LinearLayout) findViewById(R.id.altpre_layout);
        mCity = (TextView) findViewById(R.id.address);
        mAltitude = (TextView) findViewById(R.id.altitude);
        mPressure = (TextView) findViewById(R.id.pressure);
        mVerticalDirectionEn = (TextView) findViewById(R.id.direction_vertical_en);
        mVerticalDirectionCh = (TextView) findViewById(R.id.direction_vertical_ch);
        mHorizontalView = (RelativeLayout) findViewById(R.id.horizontal_view);
        mVerticalView = (RelativeLayout) findViewById(R.id.vertical_view);
        mStrPressure = getString(R.string.sensor_test_pressure);
        mStrAltitude = getString(R.string.sensor_test_altitude);
        mCheckInfo = this.readPreference(VALUE_HAS_MAEKED, false);
    }

    private void initServices() {
        // sensor manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mPressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        // location manager
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationProvider = LocationManager.NETWORK_PROVIDER;
    }

    private void updateDirection() {
        if (mHorizontal) {
            int direction = calculatorDegree(mTargetDirection * -1.0f);
            if (isVibrator(direction)) {
                mVibrator.vibrate(100);
            }
            mAngleText.setText(direction + "°");
            if (direction >= 337.5f || direction <= 22.5f) {
                mAngleDirection.setText(getString(R.string.north));
            } else if (direction > 22.5f && direction < 67.5f) {
                mAngleDirection.setText(getString(R.string.east_north));
            } else if (direction >= 67.5f && direction <= 112.5f) {
                mAngleDirection.setText(getString(R.string.east));
            } else if (direction > 112.5f && direction < 157.5f) {
                mAngleDirection.setText(getString(R.string.east_south));
            } else if (direction >= 157.5f && direction <= 202.5f) {
                mAngleDirection.setText(getString(R.string.south));
            } else if (direction > 202.5 && direction < 247.5f) {
                mAngleDirection.setText(getString(R.string.west_south));
            } else if (direction >= 247.5f && direction <= 292.5f) {
                mAngleDirection.setText(getString(R.string.west));
            } else if (direction > 292.5f && direction < 337.5f) {
                mAngleDirection.setText(getString(R.string.west_north));
            }
        } else {
            float direction = mTargetDirection;
            if (isVibrator(direction)) {
                mVibrator.vibrate(100);
            }
            if (direction >= 337.5f || direction <= 22.5f) {
                mVerticalDirectionCh.setText(getString(R.string.ch_vertical_north));
                mVerticalDirectionEn.setText(getString(R.string.en_vertical_north));
            } else if (direction > 22.5f && direction < 67.5f) {
                mVerticalDirectionCh.setText(getString(R.string.ch_vertical_east_north));
                mVerticalDirectionEn.setText(getString(R.string.en_vertical_east_north));
            } else if (direction >= 67.5f && direction <= 112.5f) {
                mVerticalDirectionCh.setText(getString(R.string.ch_vertical_east));
                mVerticalDirectionEn.setText(getString(R.string.en_vertical_east));
            } else if (direction > 112.5f && direction < 157.5f) {
                mVerticalDirectionCh.setText(getString(R.string.ch_vertical_east_south));
                mVerticalDirectionEn.setText(getString(R.string.en_vertical_east_south));
            } else if (direction >= 157.5f && direction <= 202.5f) {
                mVerticalDirectionCh.setText(getString(R.string.ch_vertical_south));
                mVerticalDirectionEn.setText(getString(R.string.en_vertical_south));
            } else if (direction > 202.5 && direction < 247.5f) {
                mVerticalDirectionCh.setText(getString(R.string.ch_vertical_west_south));
                mVerticalDirectionEn.setText(getString(R.string.en_vertical_west_south));
            } else if (direction >= 247.5f && direction <= 292.5f) {
                mVerticalDirectionCh.setText(getString(R.string.ch_vertical_west));
                mVerticalDirectionEn.setText(getString(R.string.en_vertical_west));
            } else if (direction > 292.5f && direction < 337.5f) {
                mVerticalDirectionCh.setText(getString(R.string.ch_vertical_west_north));
                mVerticalDirectionEn.setText(getString(R.string.en_vertical_west_north));
            }
        }
    }

    private boolean isVibrator(float direction) {
        boolean isVibrator = false;
        if (mLastDirection < 0 && direction >= 0 || mLastDirection > 0
                && direction <= 0 || mLastDirection < 90 && direction >= 90
                || mLastDirection > 90 && direction <= 90
                || mLastDirection < 180 && direction >= 180
                || mLastDirection > 180 && direction <= 180
                || mLastDirection < 270 && direction >= 270
                || mLastDirection > 270 && direction <= 270) {
            isVibrator = true;
        }
        mLastDirection = direction;
        return isVibrator;
    }

    private void updateLocation(Location location) {
        if (location == null) {
        } else {
            mLocation = location;
            mLonlatLayout.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            if (latitude >= 0.0f) {
                sb.append(getString(R.string.location_north)).append(
                        getLocationString(latitude));
            } else {
                sb.append(getString(R.string.location_south)).append(
                        getLocationString(-1.0 * latitude));
            }
            mLatitude.setText(sb.toString());
            sb = new StringBuilder();

            if (longitude >= 0.0f) {
                sb.append(getString(R.string.location_east)).append(
                        getLocationString(longitude));
            } else {
                sb.append(getString(R.string.location_west)).append(
                        getLocationString(-1.0 * longitude));
            }
            mLongitude.setText(sb.toString());

            if (!mHasMarked && !checkNetwork()) {
                showMobileDialog(location);
            } else {
                if ((mCheckInfo && !checkNetwork()) || checkNetwork()) {
                    if (mCity.getVisibility() == View.GONE) {
                        downloadCityAsyncTask(mLocation);
                        downloadPressureAsyncTask(mLocation);
                    }
                }
            }
        }
    }

    private void downloadCityAsyncTask(final Location location) {
        new AsyncTask<Location, Void, List<Address>>() {
            @Override
            protected List<Address> doInBackground(Location... params) {
                Geocoder gc = new Geocoder(CompassSensorTestActivity.this);
                List<Address> addresses = null;
                try {
                    addresses = gc.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return addresses;
            }

            @Override
            protected void onPostExecute(List<Address> addresses) {
                super.onPostExecute(addresses);
                if (addresses != null && addresses.size() > 0) {
                    String string = addresses.get(0).getAdminArea();
                    if ((addresses.get(0).getAdminArea() != null)
                            && (addresses.get(0).getLocality() != null)
                            && !(addresses.get(0).getAdminArea())
                            .equals(addresses.get(0).getLocality())) {
                        string += " " + addresses.get(0).getLocality();
                    }
                    mCity.setText(string);
                    mCity.setVisibility(View.VISIBLE);
                }
            }
        }.execute(new Location[]{});
    }

    private void downloadPressureAsyncTask(final Location location) {
        new AsyncTask<Location, Void, String>() {
            @Override
            protected String doInBackground(Location... params) {
                StringBuilder uri = new StringBuilder(URL_PRESSURE_HEAD);
                uri.append(location.getLatitude()).append(",")
                        .append(location.getLongitude())
                        .append(URL_PRESSURE_TEAR);
                System.out.println("bcz uri = " + uri.toString());
                return getWebContent(uri.toString());
            }

            @Override
            protected void onPostExecute(String addresses) {
                super.onPostExecute(addresses);
                try {
                    if (addresses != null) {
                        JSONObject jsonObject = new JSONObject(addresses)
                                .getJSONObject("data");
                        if (jsonObject != null) {
                            JSONArray jsonArray = jsonObject
                                    .getJSONArray("current_condition");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                JSONObject jsonObject2 = (JSONObject) jsonArray
                                        .opt(0);
                                if (jsonObject2 != null) {
                                    String pressure = jsonObject2
                                            .getString(VALUE_PRESSURE);
                                    Log.d("", "pressure : " + pressure);
                                    if (pressure != null) {
                                        mStandandPressure = Float
                                                .parseFloat(pressure);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute(new Location[]{});
    }

    public String getWebContent(String url) {
//        HttpGet request = new HttpGet(url);
//        HttpParams params = new BasicHttpParams();
//        HttpConnectionParams.setConnectionTimeout(params, 3000);
//        HttpConnectionParams.setSoTimeout(params, 5000);
//        HttpClient httpClient = new DefaultHttpClient(params);
//        try {
//            HttpResponse response = httpClient.execute(request);
//            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                String content = EntityUtils.toString(response.getEntity());
//                return content;
//            } else {
//                // Toast.makeText(context, "网络访问失败，请检查您机器的联网设备!",
//                // Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            httpClient.getConnectionManager().shutdown();
//        }
        return null;
    }

    // change the longitude and latitude
    private String getLocationString(double input) {
        int du = (int) input;
        int fen = (((int) ((input - du) * 3600))) / 60;
        int miao = (((int) ((input - du) * 3600))) % 60;
        return " " + String.valueOf(du) + "°" + String.valueOf(fen) + "′"
                + String.valueOf(miao) + "″";
    }

    private SensorEventListener mOrientationSensorEventListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent event) {
            float direction = event.values[0] * -1.0f;
            float temp = normalizeDegree(direction);
            if (Math.abs(temp - mTargetDirection) > 0.9) {
                if (Math.abs(event.values[1]) > 60
                        && Math.abs(event.values[1]) < 120
                        || Math.abs(event.values[1]) > 240
                        && Math.abs(event.values[1]) < 300) {
                    mHorizontal = false;
                    mTargetDirection = event.values[0] % 360;
                } else {
                    mHorizontal = true;
                    mTargetDirection = temp;
                }
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private SensorEventListener mPressureEventListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent event) {
            double pressure = Math.round(event.values[0] * 10) / 10.0;
            // mAltPreLayout.setVisibility(View.VISIBLE);
            // mPressure.setText(mStrPressure + pressure + " hPa");
            // double height =
            // Math.round(SensorManager.getAltitude(mStandandPressure,
            // (float)pressure) * 10) / 10.0;
            // mAltitude.setText(mStrAltitude + height + " M");
            // int pressure = (int)event.values[0];
            mAltPreLayout.setVisibility(View.VISIBLE);
            mPressure.setText(mStrPressure + pressure + " hPa");
            int height = (int) SensorManager.getAltitude(mStandandPressure,
                    (float) (Math.round(event.values[0] * 10) / 10.0));
            mAltitude.setText(mStrAltitude + height + " M");
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public boolean checkNetwork() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()
                && ConnectivityManager.TYPE_MOBILE == networkinfo.getType()) {
            return false;
        }
        return true;
    }

    public void showMobileDialog(Location location) {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        mDialog = new AlertDialog.Builder(this)
                .setMessage(
                        this.mHasPressure ? R.string.ali_dlg_confirm_mobile
                                : R.string.ali_dlg_confirm_mobile_1)
                .setPositiveButton(R.string.dlg_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mCheckInfo = true;
                                mHasMarked = true;
                                writePreference(VALUE_HAS_MAEKED, mCheckInfo);
                                downloadCityAsyncTask(mLocation);
                                downloadPressureAsyncTask(mLocation);
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                mCheckInfo = false;
                                mHasMarked = true;
                                writePreference(VALUE_HAS_MAEKED, mCheckInfo);
                            }
                        }).create();
        mDialog.show();
    }

    // adjust the value from sensor
    private float normalizeDegree(float degree) {
        return (degree + 720) % 360;
    }

    // change the value to int
    private int calculatorDegree(float degree) {
        return (int) (((degree + 360) % 360));
    }

    // update the location information
    LocationListener mLocationListener = new LocationListener() {

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }

        public void onLocationChanged(Location location) {
            updateLocation(location);
        }
    };

    private boolean containsPreference(String item) {
        SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, 0);
        return settings.contains(item);
    }

    protected void writePreference(String item, boolean flag) {
        SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(item, flag);
        editor.commit();
    }

    public boolean readPreference(String item, boolean flag) {
        SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(item, flag);
    }
}
