package com.luiz.server.packets;

import com.luiz.server.Server;

public class Packet01Disconnect extends Packet {
	private String username;
	
	public Packet01Disconnect(byte[] data) {
		super(00);
		this.username = readData(data);
	}
	
	public Packet01Disconnect(String username) {
		super(00);
		this.username = username;
	}

	public void writeData(Server server) {
		server.sendMessageToAllClient(getData());
	}

	public byte[] getData() {
		return ("01" + this.username).getBytes();
	}
	
	public String getUsername() {
		return username;
	}
}
