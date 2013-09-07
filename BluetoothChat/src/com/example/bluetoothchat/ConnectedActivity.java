package com.example.bluetoothchat;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class ConnectedActivity extends Activity
{
	private int deviceIndex;
	private BluetoothDevice deviceToConnect;
	private AsyncClientComponent client;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connected);
		Bundle extras = this.getIntent().getExtras();
		deviceIndex = extras.getInt("index");
		Log.d("BLT",deviceIndex + " ");
		deviceToConnect = ClientActivity.getDevice(deviceIndex);
		client = new AsyncClientComponent(deviceToConnect);
		client.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.connected, menu);
		return true;
	}
	
	public void onDestroy()
	{
		client.cancel(true);
		super.onDestroy();
	}

}
