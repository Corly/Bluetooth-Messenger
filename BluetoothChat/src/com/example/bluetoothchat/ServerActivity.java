package com.example.bluetoothchat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class ServerActivity extends Activity
{
	private AsyncServerComponent server;
	private EditText chatText;
	private EditText inputText;

	private UILink asdf = new UILink()
	{
		@Override
		public void useData(String... args)
		{
			Log.d("BLT", "de aci " + args[0]);
			chatText.append(args[0] + "\n");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		chatText = (EditText) findViewById(R.id.serverEditText);
		inputText = (EditText) findViewById(R.id.serverInput);
		server = new AsyncServerComponent(this, asdf);
		server.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.server, menu);
		return true;
	}

	@Override
	public void onDestroy()
	{
		server.closeSockets();
		server.cancel(true);
		super.onDestroy();
	}

	public void SendClick(View view)
	{
		try
		{
			String text = inputText.getText().toString();
			chatText.append(MyDeviceData.name + ": " + text + "\n");
			server.write(MyDeviceData.name + ": " + text + "\n");
			inputText.setText("");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
