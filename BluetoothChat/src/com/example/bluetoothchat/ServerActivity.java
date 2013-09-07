package com.example.bluetoothchat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class ServerActivity extends Activity
{
	private AsyncServerComponent server;
	private EditText text;
	
	private UILink asdf = new UILink()
	{
		@Override
		public void useData(String... args)
		{
			text.append(args[0]+"\n");
		}		
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		text = (EditText)findViewById(R.id.editText1);
		server = new AsyncServerComponent(this , asdf);
		server.execute();
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
		server.cancel(true);
		super.onDestroy();
	}

}
