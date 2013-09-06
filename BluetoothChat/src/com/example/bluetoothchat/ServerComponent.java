package com.example.bluetoothchat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServerComponent extends Thread
{
	private BluetoothServerSocket serverSocket;
	private BluetoothAdapter bltAdapter;
	private Context context;
	private boolean isRunning;

	public ServerComponent(Context context)
	{
		this.context = context;
		BluetoothServerSocket tmp = null;
		bltAdapter = BluetoothAdapter.getDefaultAdapter();
		serverSocket = null;

		if (bltAdapter == null)
			return;

		if (!bltAdapter.isEnabled())
		{
			Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			context.startActivity(enableBluetooth);
		}

		if (bltAdapter.isEnabled())
		{
			Intent discoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			context.startActivity(discoverable);
		}

		try
		{
			tmp = bltAdapter.listenUsingRfcommWithServiceRecord("BLT", UUID.fromString("65497178-f0ac-4c37-b619-eecd39ab947c"));

		} catch (IOException er)
		{

		}

		serverSocket = tmp;
	}

	public void run()
	{
		BluetoothSocket socket = null;
		InputStream input;
		OutputStream output;
		Log.d("BLT" , "Listening for connection...");
		try
		{
			socket = serverSocket.accept();
			Log.d("BLT","Someone connected");
			if (socket == null) return;
			input = socket.getInputStream();
			output = socket.getOutputStream();
			Log.d("BLT","Streams initialized");
			byte[] data = new byte[10];
			while (socket != null)
			{
				Log.d("BLT","reading...");
				int bytes = input.read(data);
				Log.d("BLT",new String(data));
			}
		}
		catch (Exception er)
		{
			Log.d("BLT",er.getMessage() + " " + er.getLocalizedMessage());
		}
		
	}

	public void cancel()
	{
		try
		{
			serverSocket.close();
			
		} catch (IOException er)
		{
		}
	}
	
}
