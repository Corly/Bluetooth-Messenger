package com.example.bluetoothchat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncServerComponent extends AsyncTask<Void, String, Void>
{
	private BluetoothServerSocket serverSocket;
	private BluetoothSocket dataSocket = null;
	private BluetoothAdapter bltAdapter;
	private Context context;
	private UILink updater;

	public AsyncServerComponent(Context cnt, UILink UIUpdater)
	{
		this.context = cnt;
		updater = UIUpdater;
		BluetoothServerSocket tmp = null;
		bltAdapter = BluetoothAdapter.getDefaultAdapter();
		serverSocket = null;

		if (bltAdapter == null)
			return;

		if (!bltAdapter.isEnabled())
		{
			Intent discoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			context.startActivity(discoverable);
			Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			context.startActivity(enableBluetooth);
		} else
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

	@Override
	protected Void doInBackground(Void... arg0)
	{
		InputStream input;
		Log.d("BLT", "Listening for connection...");
		try
		{
			dataSocket = serverSocket.accept();
			serverSocket.close();
			Log.d("BLT", "Someone connected");
			if (dataSocket == null)
				return null;
			input = dataSocket.getInputStream();
			Log.d("BLT", "Streams initialized");
			byte[] data = new byte[100];
			while (true)
			{
				int bytes = input.read(data);
				Log.d("BLT", new String(data));
				this.publishProgress(new String(data));
				Thread.sleep(20);
			}
		} catch (Exception er)
		{
			Log.d("BLT", er.getMessage() + " " + er.getLocalizedMessage());
		}
		return null;
	}

	protected void onProgressUpdate(String... strings)
	{
		Log.d("BLT", "update in UI");
		if (updater != null)
			updater.useData(strings);
	}

	protected void closeSockets()
	{
		try
		{
			dataSocket.close();
			serverSocket.close();
			dataSocket = null;
			serverSocket = null;
		} catch (Exception er)
		{

		}
	}

	public void write(String data) throws Exception
	{
		OutputStream output = dataSocket.getOutputStream();
		output.write(data.getBytes());
		output.flush();
	}
}
