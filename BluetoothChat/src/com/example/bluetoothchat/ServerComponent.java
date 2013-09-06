package com.example.bluetoothchat;

import java.io.IOException;
import java.util.UUID;

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
			Log.d("BLT","WOHOOO");
			
		} catch (IOException er)
		{

		}

		serverSocket = tmp;
	}

	public void run()
	{
		BluetoothSocket socket = null;
		while (true)
		{
			try
			{
				socket = serverSocket.accept();
			} catch (IOException er)
			{
				break;
			}
			if (socket != null)
			{
				try
				{
					serverSocket.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				break;
			}
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
