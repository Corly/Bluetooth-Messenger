package com.example.bluetoothchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class FrontActivity extends Activity
{
	private BluetoothManager bltManager;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_front);
		context = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.front, menu);
		return true;
	}
	
	public void clientClick(View v)
	{
		Intent clt = new Intent(FrontActivity.this, ClientActivity.class);
		startActivity(clt);
	}
	
	public void serverClick(View v)
	{
		Intent srv = new Intent(FrontActivity.this, ServerActivity.class);
		startActivity(srv);
	}

}
