package com.luiz.server.packets;

import com.luiz.server.Server;

public class Packet02Move extends Packet {
	private String username;
	
	public Packet02Move(byte[] data) {
		super(02);
		this.username = readData(data);
	}
	
	public Packet02Move(String username) {
		super(02);
		this.username = username;
	}

	public void writeData(Server server) {
		server.sendMessageToAllClient(getData());
	}

	public byte[] getData() {
		return ("02" + this.username).getBytes();
	}
	
	public String getUsername() {
		return username;
	}
}
