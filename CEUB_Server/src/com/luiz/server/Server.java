package com.luiz.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.luiz.client.Client;
import com.luiz.server.packets.Packet;
import com.luiz.server.packets.Packet.PacketTypes;
import com.luiz.server.packets.Packet00Login;
import com.luiz.server.packets.Packet01Disconnect;

public class Server extends Thread {
	
	public List<Client> connectedClients = new ArrayList<Client>();
	private DatagramSocket socket;
	
	public Server() {
		try {
			this.socket = new DatagramSocket(1331);
			System.out.println("SERVER> started");
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while(true) {
			byte[] data = new byte[1024]; 
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}
	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		Packet packet = null;
		Client client;
		
		switch(type) {
			default:
			case INVALID:
				break;
			case LOGIN:
				packet = new Packet00Login(data);
				System.out.println("["+address.getHostAddress()+":"+
						port+"] "+((Packet00Login) packet).getUsername()+
							" has joined the game...");
				client = new Client(address, port, ((Packet00Login)packet).getUsername());
				this.addConnection(client, (Packet00Login)packet);
				break;
			case DISCONNECT:
				packet = new Packet01Disconnect(data);
				System.out.println("["+address.getHostAddress()+":"+
						port+"] "+((Packet01Disconnect) packet).getUsername()+
							" has left the game...");
				
				for(Client c : this.connectedClients) {
					if(c.ipAddress.equals(address)) {
						if(c.username.equalsIgnoreCase(((Packet01Disconnect)packet).getUsername())) {
							client = c;
							this.removeConnection(client, (Packet01Disconnect)packet);
							return;
						}
					}
				}
				System.out.println("error: client doesnt exit");
				break;
		}
	}
	
	public void closeSocket() {
		socket.close();
	}
	
	public void addConnection(Client client, Packet00Login packet) {
		for(Client c : this.connectedClients) {
			//tells other clients that the current client has connected
			sendMessage(packet.getData(), c.ipAddress, c.port);
			//tells the current client that other clients existed b4 he logged in
			Packet00Login other_packet = new Packet00Login(c.username);
			sendMessage(other_packet.getData(), client.ipAddress, client.port);
			
		}
		this.connectedClients.add(client);
	}
	
	public void removeConnection(Client client, Packet01Disconnect packet) {
		for(Client c : this.connectedClients) {
			if(!c.username.equalsIgnoreCase(client.username))
			//tells other clients that the current one has connected
			sendMessage(packet.getData(), c.ipAddress, c.port);
		}
		this.connectedClients.remove(client);
	}
	
	public void sendMessage(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessageToAllClient(byte[] data) {
		//Sending data to all clients
		for(Client c : connectedClients) {
			sendMessage(data, c.ipAddress, c.port);
		}
	}
}
