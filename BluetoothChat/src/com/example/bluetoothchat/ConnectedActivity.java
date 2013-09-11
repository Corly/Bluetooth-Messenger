package com.example.bluetoothchat;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class ConnectedActivity extends Activity
{
	private int deviceIndex;
	private BluetoothDevice deviceToConnect;
	private AsyncClientComponent client;
	private EditText chatText;
	private EditText inputText;

	private UILink updater = new UILink()
	{
		@Override
		public void useData(String... args)
		{
			chatText.append(args[0] + "\n");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connected);
		chatText = (EditText) findViewById(R.id.clientEditText);
		inputText = (EditText) findViewById(R.id.clientInput);
		Bundle extras = this.getIntent().getExtras();
		deviceIndex = extras.getInt("index");
		deviceToConnect = ClientActivity.getDevice(deviceIndex);
		client = new AsyncClientComponent(deviceToConnect, updater);
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
		client.closeSockets();
		client.cancel(true);
		super.onDestroy();
	}

	public void SendClick(View view)
	{
		try
		{
			String text = inputText.getText().toString();
			chatText.append(MyDeviceData.name + ": " + text + "\n");
			client.write(MyDeviceData.name + ": " + text + "\n");
			inputText.setText("");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
