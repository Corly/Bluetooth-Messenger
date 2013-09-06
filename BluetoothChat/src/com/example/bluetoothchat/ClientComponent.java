package com.example.bluetoothchat;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class ClientComponent extends Thread
{
	private BluetoothSocket socket;
	private BluetoothDevice device;
	
	public ClientComponent(BluetoothDevice device)
	{
		BluetoothSocket tmp = null;
		this.device = device;
		try
		{
			tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("65497178-f0ac-4c37-b619-eecd39ab947c"));
		}
		catch(IOException er)
		{}
		socket = tmp;
	}
	
	public void run()
	{
		try
		{
			socket.connect();
		}
		catch (IOException connectEr)
		{
			try
			{
				socket.close();
			}
			catch(IOException closeEr)
			{
				return;
			}
		}
	}
	
	public void cancel()
	{
		try
		{
			socket.close();
		}
		catch (Exception er) {}
	}
	
}
