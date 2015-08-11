package client;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class InGame{

	private JTextArea incoming;
	private JTextField outgoing;
	private JPanel panel;
	private Client networkStartup;
	private PlayerMob player;
	public Background background;
	private JLayeredPane layeredPane;
	private JLayeredPane keyListenerLayer;
	private ArrayList<PlayerMob> players;
	private int playerIndex;
	private UI userInterface;

	public InGame(){
		layeredPane = new ZoomLayeredPane();
		background = new Background();
		layeredPane.add(background,99);
	}
	

	
	public void chat(JPanel panel){
		this.panel = panel;
		userInterface = new UI(player);
		userInterface.chat(panel);	
		
		AddKeyListener keyListener = new AddKeyListener();
		keyListener.setPlayer(player);

		keyListenerLayer = new JLayeredPane();
		keyListenerLayer.add(keyListener, 10);
		
		panel.add(BorderLayout.CENTER, keyListenerLayer);
		keyListener.setFocusable(true);
		keyListener.requestFocusInWindow();
		
		panel.add(BorderLayout.CENTER, layeredPane);
		panel.add(BorderLayout.SOUTH, userInterface.getChatPanel());
		panel.validate();
		panel.repaint();

		
		//initialize
		networkStartup.InGameChatInitialize(userInterface.getOutgoing(),userInterface.getIncoming());

		panel.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				keyListener.setFocusable(true);
				keyListener.requestFocusInWindow();
			}
		});

		userInterface.getOutgoing().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	networkStartup.InGameChatSendButtonListener(userInterface.getOutgoing(),userInterface.getIncoming());
        }});

	}
	
	public void setPlayer(PlayerMob player){
		this.player = player;
	}

	public void drawFromServer(PlayerMob plyr){
		layeredPane.add(plyr, 100);
		layeredPane.moveToBack(background);
	}

	public void removeFromServer(PlayerMob plyr){
		layeredPane.remove(plyr);
	}

	public void setPlayers(ArrayList<PlayerMob> allPlayers,int playerIndex){
		players = allPlayers;
		this.playerIndex = playerIndex;

	}
	
	public void setNetObject(Client networkStartup){
		this.networkStartup = networkStartup;
	}
	
	public void drawPanel(){
		try{
				
				panel.remove(layeredPane);

				for(int i = 0; i < players.size();i ++){
					players.get(i).setXCoordinate(networkStartup.xCoordinates.get(i));
					players.get(i).setYCoordinate(networkStartup.yCoordinates.get(i));
					players.get(i).updateFace(i);
				}
				background.move();
				
				panel.add(BorderLayout.CENTER,layeredPane);
				panel.validate();
				panel.repaint();

				collisionDetection();

			
		}catch (NullPointerException ed){
			System.err.println("for loop null catch");

		}
		catch(Exception ev){
			System.err.println("for loop catch");
			ev.printStackTrace();	
		}
	}
	
	public void collisionDetection() throws IndexOutOfBoundsException{
		
		for(int i=0;  i< players.size(); i++){
			if(players.get(playerIndex).getCross()){
				if (playerIndex != i && players.get(playerIndex).getBounds().intersects(players.get(i).getBounds())){
					players.get(i).setKnockedOut(true);
				}
			}
		}
		
	}
	
	public class SendButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			networkStartup.InGameChatSendButtonListener(outgoing,incoming);
		}
	}

}