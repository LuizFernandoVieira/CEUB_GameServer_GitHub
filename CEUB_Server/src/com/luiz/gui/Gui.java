package com.luiz.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.luiz.server.Server;

public class Gui extends JFrame implements ActionListener {
	
	Server server;
	boolean serverStarted = false;

	public Gui() {
		this.setLayout(new BorderLayout());
		this.setTitle("Title");
	    this.setSize(250, 250);
	    this.setResizable(false);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    
	    Panel center_panel = new Panel();
	    center_panel.setLayout(new BorderLayout());
	    this.add(center_panel, BorderLayout.CENTER);
	    
	    Panel top_panel = new Panel();
	    this.add(top_panel, BorderLayout.NORTH);
	    
	    Panel botton_panel = new Panel();
	    this.add(botton_panel, BorderLayout.SOUTH);
	    
	    Panel left_panel = new Panel();
	    this.add(left_panel, BorderLayout.LINE_START);
	    
	    Panel right_panel = new Panel();
	    this.add(right_panel, BorderLayout.LINE_END);
	    
	    Button button = new Button("Start Server");
	    button.setSize(200, 200);
	    center_panel.add(button);
	    button.addActionListener(this);
	    
	    this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(!serverStarted) {
			server = new Server();
			server.start();
			this.setBackground(Color.GREEN);
			this.serverStarted = true;
		} else {
			System.out.println("SERVER> finished");
			if(server != null) {
				server.closeSocket();
			}
			this.setBackground(Color.RED);
			this.serverStarted = false;
		}
	}
}
