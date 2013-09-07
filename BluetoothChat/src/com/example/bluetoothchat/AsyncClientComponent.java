package com.example.bluetoothchat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

public class AsyncClientComponent extends AsyncTask<Void,Void,Void>
{
	private BluetoothSocket dataSocket;
	private BluetoothDevice device;
	private InputStream input = null;
	private OutputStream output = null;
	
	public AsyncClientComponent(BluetoothDevice device)
	{
		BluetoothSocket tmp = null;
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
			while (true)
			{
				String ping_message = "(Ping) " + device.getName() + ".";
				output.write(ping_message.getBytes());
				output.flush();
				Thread.sleep(1000);
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
	
	public void write(String data) throws Exception
	{
		OutputStream output = dataSocket.getOutputStream();
		output.write(data.getBytes());
		output.flush();
		
	}
}
