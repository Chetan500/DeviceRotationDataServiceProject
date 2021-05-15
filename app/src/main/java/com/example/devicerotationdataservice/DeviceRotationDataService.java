package com.example.devicerotationdataservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.devicerotationdatasdk.IDeviceRotationData;

public class DeviceRotationDataService extends Service
{
    private SensorManager sensorManager = null;
    private Sensor sensor = null;
    IDeviceRotationData.Stub deviceRotationDataServiceImpl = new IDeviceRotationData.Stub()
    {
        @Override
        public String getDeviceRotationData() throws RemoteException
        {
            sensorManager = (SensorManager) DeviceRotationDataService.this.getSystemService(Context.SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            return sensor.toString();
        }
    };

    @Override
    public IBinder onBind(Intent intent)
    {
        return deviceRotationDataServiceImpl;
    }
}
