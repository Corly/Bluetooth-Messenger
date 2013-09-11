package com.example.bluetoothchat;

import java.io.IOException;
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
	private BluetoothServerSocket mServerSocket;
	private final BluetoothAdapter mBltAdapter;
	private final Context mContext;
	private final UILink mUpdater;
	private ConnectionManager mManager;

	public AsyncServerComponent(Context context, UILink UIUpdater)
	{
		mContext = context;
		mUpdater = UIUpdater;
		BluetoothServerSocket tmp = null;
		mBltAdapter = BluetoothAdapter.getDefaultAdapter();

		if (mBltAdapter == null)
			return;

		if (mBltAdapter.isEnabled())
		{
			Intent discoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			mContext.startActivity(discoverable);
		}
		try
		{
			tmp = mBltAdapter.listenUsingRfcommWithServiceRecord("BLT", UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

		}
		catch (IOException er)
		{

		}

		mServerSocket = tmp;
	}

	@Override
	protected Void doInBackground(Void... arg0)
	{
		BluetoothSocket socket = null;
		while (true)
		{
			try
			{
				socket = mServerSocket.accept();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				break;
			}
			if (socket != null)
			{
				try
				{
					mServerSocket.close();
					mManager = new ConnectionManager(socket , mUpdater);
					mManager.execute();
					break;
				}
				catch (IOException e)
				{
					break;
				}
			}
			try
			{
				Thread.sleep(20);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		this.publishProgress(socket.getRemoteDevice().getName() + " has connected!");

		return null;
	}

	protected void onProgressUpdate(String... strings)
	{
		if (mUpdater != null)
			mUpdater.useData(strings);
	}

	protected void closeSockets()
	{
		try
		{
			mManager.stop();
			mServerSocket.close();
		}
		catch (Exception er)
		{

		}
	}
	
	public void write(String data)
	{
		mManager.write(data);
	}
}
