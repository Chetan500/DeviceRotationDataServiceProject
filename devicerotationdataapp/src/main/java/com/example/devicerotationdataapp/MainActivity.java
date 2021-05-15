package com.example.devicerotationdataapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devicerotationdatasdk.IDeviceRotationData;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements ServiceConnection
{
    private TextView tvDeviceRotationData;
    private Button btnDeviceRotationData;
    protected IDeviceRotationData iDeviceRotationData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListeners();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        initConnection();
    }

    private void init()
    {
        tvDeviceRotationData = (TextView) findViewById(R.id.txtDeviceRotationData);
        btnDeviceRotationData = (Button) findViewById(R.id.btnDeviceRotationData);
    }

    private void setListeners()
    {
        btnDeviceRotationData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDeviceRotationData();
            }
        });
    }

    private void initConnection()
    {
        if (iDeviceRotationData == null)
        {
            Intent intent = new Intent("com.example.devicerotationdataservice.DeviceRotationDataService");
            intent.setPackage("com.example.devicerotationdataservice");
            bindService(intent, this, Service.BIND_AUTO_CREATE);
        }
    }

    private void showDeviceRotationData()
    {
        Timer timer_sensor = new Timer();

        TimerTask task_process_sensor = new TimerTask()
        {
            public void run()
            {
                new GetDeviceRotationDataTask().execute();
            }
        };

        timer_sensor.schedule(task_process_sensor, 0, 8000);
    }

    public class GetDeviceRotationDataTask extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... voids)
        {
            String deviceRotationData = "";
            try
            {
                deviceRotationData = iDeviceRotationData.getDeviceRotationData();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return deviceRotationData;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            tvDeviceRotationData.setText(s);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service)
    {
        iDeviceRotationData = IDeviceRotationData.Stub.asInterface((IBinder) service);
        Toast.makeText(getApplicationContext(), "Device Rotation Data Service Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceDisconnected(ComponentName name)
    {
        iDeviceRotationData = null;
        Toast.makeText(getApplicationContext(), "Device Rotation Data Service Disconnected", Toast.LENGTH_SHORT).show();
    }
}
