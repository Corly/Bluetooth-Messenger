package com.example.bluetoothchat;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ServerActivity extends Activity
{
	private ServerComponent server;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		server = new ServerComponent(this);
		server.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.server, menu);
		return true;
	}
	
	@Override
	public void onDestroy()
	{
		server.cancel();
		super.onDestroy();
	}

}
