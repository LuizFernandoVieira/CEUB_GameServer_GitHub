package com.luiz.client;

import java.net.InetAddress;

public class Client {

	public InetAddress ipAddress;
	public int port;
	public String username;
	
	public int id;
	public static int nextId = 0;
	
	public Client(InetAddress ipAddress, int port, String username) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.username = username;
		this.id = nextId;
		this.nextId++;
	}
	
}
