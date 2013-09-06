package com.example.bluetoothchat;

import java.util.ArrayList;
import java.util.HashMap;
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
	private HashMap<String, Integer> hash;

	public BluetoothManager(Context context, ArrayAdapter<String> list)
	{
		this.context = context;
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		foundDevices = new ArrayList<BluetoothDevice>();
		hash = new HashMap<String, Integer>();
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
				list.add("Name: " + device.getName() + "\n" + "Adress: " + device.getAddress() + "\nPaired and not in range");
				hash.put(device.getAddress(), hash.size());
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
				if (!hash.containsKey(device.getAddress()))
				{
					foundDevices.add(device);
					list.add("Name: " + device.getName() + "\n" + "Adress: " + device.getAddress() + "\nNot paired and in range");
					list.notifyDataSetChanged();
					Log.d("BLT", "Name: " + device.getName() + "\n" + "Adress: " + device.getAddress());
				}
				else
				{
					int index = hash.get(device.getAddress());
					String value = list.getItem(index);
					list.remove(value);
					value = ("Name: " + device.getName() + "\n" + "Adress: " + device.getAddress() + "\nPaired and in range");
					list.insert(value, index);
					list.notifyDataSetChanged();
				}
			}
		}

	};

	public void stopDiscovery()
	{
		bluetoothAdapter.cancelDiscovery();
	}

	public void destroy()
	{
		bluetoothAdapter = null;
		foundDevices.clear();
		foundDevices = null;
		list.clear();
		list = null;
		hash.clear();
		hash = null;
	}

	public BluetoothDevice getDevice(int index)
	{
		return foundDevices.get(index);
	}

}
