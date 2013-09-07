package com.example.bluetoothchat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

public class AsyncClientComponent extends AsyncTask<Void,String,Void>
{
	private BluetoothSocket dataSocket;
	private BluetoothDevice device;
	private InputStream input = null;
	private OutputStream output = null;
	private UILink updater;
	public AsyncClientComponent(BluetoothDevice device , UILink UIUpdater)
	{
		BluetoothSocket tmp = null;
		updater = UIUpdater;
		this.device = device;
		try
		{
			tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("65497178-f0ac-4c37-b619-eecd39ab947c"));
		}
		catch(IOException er)
		{}
		dataSocket = tmp;
	}
	
	protected Void doInBackground(Void... params)
	{
		try
		{
			dataSocket.connect();			
			input = dataSocket.getInputStream();
			output = dataSocket.getOutputStream();
			byte[] data = new byte[100];
			while (true)
			{
				int bytes = input.read(data);
				this.publishProgress(new String(data));
				Thread.sleep(20);
			}
		}
		catch (Exception connectEr)
		{
			try
			{
				dataSocket.close();
			}
			catch(IOException closeEr)
			{
				return null;
			}
		}
		return null;
	}
	
	protected void onProgressUpdate(String... strings)
	{
		if (updater != null) updater.useData(strings);
	}
	
	public void closeSockets()
	{
		try
		{
			dataSocket.close();
			dataSocket = null;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void write(String data) throws Exception
	{
		output.write(data.getBytes());
		output.flush();		
	}
}
