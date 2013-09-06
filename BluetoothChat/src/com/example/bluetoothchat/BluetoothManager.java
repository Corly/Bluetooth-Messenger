package com.example.bluetoothchat;

import java.util.ArrayList;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.ArrayAdapter;

public class BluetoothManager
{
	private BluetoothAdapter bluetoothAdapter;
	private Context context;
	private ArrayAdapter<String> list;
	private ArrayList<BluetoothDevice> foundDevices;
	public BluetoothManager(Context context , ArrayAdapter<String> list)
	{
		this.context = context;
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		foundDevices = new ArrayList<BluetoothDevice>();
		this.list = list;
	}
	
	public boolean checkDevices()
	{
		if (bluetoothAdapter == null)
		{
			// check if the device has Bluetooth
			return false;
		}
		if (!bluetoothAdapter.isEnabled())
		{
			// check if the bluetooth is enabled
			Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			context.startActivity(enableBluetooth);			
		}
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
		if (pairedDevices.size() > 0)
		{
			for (BluetoothDevice device : pairedDevices)
			{
				foundDevices.add(device);
				list.add("Name: " + device.getName() + "\n" + "Adress: " + device.getAddress());
			}
			list.notifyDataSetChanged();
		}
		bluetoothAdapter.startDiscovery();
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		context.registerReceiver(receiver, filter);
		return true;		
	}
	
	private final BroadcastReceiver receiver = new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action))
			{
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				foundDevices.add(device);
				list.add("Name: " + device.getName() + "\n" + "Adress: " + device.getAddress());
				list.notifyDataSetChanged();
				Log.d("BLT" , "Name: " + device.getName() + "\n" + "Adress: " + device.getAddress());
			}
		}
		
	};
	
	public void stopDiscovery()
	{
		bluetoothAdapter.cancelDiscovery();
	}
	
	public BluetoothDevice getDevice(int index)
	{
		return foundDevices.get(index);
	}

}
