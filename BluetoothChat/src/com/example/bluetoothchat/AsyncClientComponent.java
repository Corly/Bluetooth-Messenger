package com.example.bluetoothchat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncClientComponent extends AsyncTask<Void, String, Void>
{
	private BluetoothSocket mDataSocket;
	private final BluetoothDevice mDevice;
	private final UILink mUpdater;
	private ConnectionManager mManager;

	public AsyncClientComponent(BluetoothDevice device, UILink UIUpdater)
	{
		BluetoothSocket tmp = null;
		mUpdater = UIUpdater;
		mDevice = device;
		try
		{
			tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
		}
		catch (IOException er)
		{
		}
		mDataSocket = tmp;
	}

	protected Void doInBackground(Void... params)
	{
		try
		{
			mDataSocket.connect();
		}
		catch (Exception connectEr)
		{
			try
			{
				Log.d("BLT", connectEr.getMessage());
				this.publishProgress("Connection to " + mDataSocket.getRemoteDevice().getName() + " has failed!");
				mDataSocket.close();
				return null;
			}
			catch (IOException closeEr)
			{
				Log.d("BLT", closeEr.getMessage());
				this.publishProgress("Connection to " + mDataSocket.getRemoteDevice().getName() + " has failed!");
				return null;
			}
		}
		mManager = new ConnectionManager(mDataSocket, mUpdater);
		mManager.execute();
		this.publishProgress("Connection established to " + mDataSocket.getRemoteDevice().getName());
		
		return null;
	}

	public void onProgressUpdate(String... strings)
	{
		if (mUpdater != null)
			mUpdater.useData(strings);
	}

	public void closeSockets()
	{
		try
		{
			mManager.stop();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void write(String data)
	{
		mManager.write(data);
	}
}
