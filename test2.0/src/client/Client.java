package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client {

	private JTextArea incoming;
	private JTextField outgoing;
	private String username;
	private Socket sock;
	@SuppressWarnings("unused")
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private ClientObject clientObject;
	private Login loginPage;
	private InGame inGame;
	private PlayerMob player;
	private ArrayList<PlayerMob> players;
	private Client networkStartup;
	private int usersIn = 0;
	private final String HOST_ADDRESS = "192.168.2.21";

	ArrayList<String> usernames; // 0
	ArrayList<String> messages; // 1
	ArrayList<Integer> xCoordinates; // 2
	ArrayList<Integer> yCoordinates; // 3
	ArrayList<Boolean> faceDowns; // 4
	ArrayList<Boolean> faceUps; // 5
	ArrayList<Boolean> faceLefts; // 6
	ArrayList<Boolean> faceRights; // 7
	ArrayList<Boolean> crosses; // 8

	private DatagramSocket datagramSocket;

	public void startUp(Client netStart, JPanel panel) {

		networkStartup = netStart;
		loginPage = new Login(); //Make new object loginPage

		loginPage.setPanel(panel);
		loginPage.setNetObject(netStart); // Send the ChatClient object to
											// loginPage
		clientObject = new ClientObject();
		player = new PlayerMob(netStart);
		players = new ArrayList<PlayerMob>(); // Make an array list to hold all
												// other players from server
		usernames = new ArrayList<String>(); //0
		messages = new ArrayList<String>(); // 1
		xCoordinates = new ArrayList<Integer>(); // 2
		yCoordinates = new ArrayList<Integer>(); // 3
		faceDowns = new ArrayList<Boolean>(); // 4
		faceUps = new ArrayList<Boolean>(); // 5
		faceLefts = new ArrayList<Boolean>(); // 6
		faceRights = new ArrayList<Boolean>(); // 7
		crosses = new ArrayList<Boolean>(); // 8
		
	
		//This is for testing purposes with two players
		crosses.add(false);
		crosses.add(false);
		
		inGame = new InGame();
		inGame.setPlayer(player);
		inGame.setNetObject(netStart); // Send the ChatClient object to inGame
		setUpNetworking();
		loginPage.login();
	}

	private void setUpNetworking() {

		try {
			datagramSocket = new DatagramSocket();

			sock = new Socket(HOST_ADDRESS, 5000);
			outputStream = new ObjectOutputStream(sock.getOutputStream());
			inputStream = new ObjectInputStream(sock.getInputStream());
			
			setUpIncomingReader();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void setUpIncomingReader() {
		try {
			Thread remote = new Thread(new IncomingReader());
			remote.start();
		} catch (Exception e) {
			// do nothing
		}
	}

	public void LoginPageLoginButtonListener(String usr, JPanel panel) {
		username = usr;
		try {

			if (username.length() > 0) {
				initializeUsernames();
				Thread.sleep(100); // This sleep gives time for the Incoming
									// reader class to update the usernames from
									// the server
				Boolean duplicateUser = usernames.contains(username);

				if (!duplicateUser) {
					createNewUser();
					startGame(panel);
				} else {
					loginPage.duplicateUserMsg(username);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void initializeUsernames() throws IOException {
		outputStream.writeUnshared(clientObject); // Sending the ChatObject to
													// the server
		outputStream.flush();
	}

	public void createNewUser() throws IOException {
		clientObject.setUsername(username);
		outputStream.writeUnshared(clientObject); // Sending the ChatObject to
													// the server
		outputStream.flush();
	}

	public void startGame(JPanel panel) {
		panel.removeAll();
		inGame.chat(panel);
	}

	public void InGameChatInitialize(JTextField out, JTextArea in) {
		incoming = in;
		outgoing = out;
		try {
			clientObject.setMessage(username + " has joined!");
			outputStream.writeUnshared(clientObject);
			outputStream.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void InGameChatSendButtonListener(JTextField out, JTextArea in) {
		outgoing = out;
		incoming = in;

		try {
			clientObject.setMessage(username + ": " + outgoing.getText());
			outputStream.writeUnshared(clientObject);
			outputStream.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		outgoing.setText("");
		outgoing.requestFocus();
	}

	public void keyPressedR(Boolean cross) {
		try {
			clientObject.setR(cross);

			clientObject.setMessage(null);
			outputStream.writeUnshared(clientObject);
			outputStream.flush();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void keyReleasedR(Boolean cross) {
		try {
			clientObject.setR(cross);

			clientObject.setMessage(null);
			outputStream.writeUnshared(clientObject);
			outputStream.flush();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void keyPressed(Boolean vertMove, int yMove, int yCoordinate) {
		try {
			if (yMove > 0) {
				clientObject.setDown(true);
			} else if (yMove < 0) {
				clientObject.setUp(true);
			}

			clientObject.setMessage(null);
			outputStream.writeUnshared(clientObject);
			outputStream.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void keyPressed(int xMove, Boolean horMove, int xCoordinate) {
		try {
			if (xMove > 0) {
				clientObject.setRight(true);
			} else if (xMove < 0) {
				clientObject.setLeft(true);
			}

			clientObject.setMessage(null);
			outputStream.writeUnshared(clientObject);
			outputStream.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void keyReleased(Boolean vertMove, int yMove, int yCoordinate) {
		try {
			if (yMove == 0) {
				clientObject.setUp(false);
				clientObject.setDown(false);
			}

			clientObject.setMessage(null);
			outputStream.writeUnshared(clientObject);
			outputStream.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void keyReleased(int xMove, Boolean horMove, int xCoordinate) {

		try {
			if (xMove == 0) {
				clientObject.setRight(false);
				clientObject.setLeft(false);
			}

			clientObject.setMessage(null);
			outputStream.writeUnshared(clientObject);
			outputStream.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void moveBackground(ServerObject serverObject, int indexOfPlayer) {
		try {
			inGame.background.readMove(serverObject, indexOfPlayer);
		} catch (NullPointerException ex) {
			// do nothing
		}
	}

	public class IncomingReader implements Runnable {
		// reads object from server through inputStream if the object is read,
		// we cast it as a ChatObject
		// then we append message inside chat object to the text area called
		// incoming

		public void run() {
			int indexOfPlayer = 0;
			@SuppressWarnings("unused")
			Object objFromInStream = null;
			byte[] buf = new byte[512];
			byte[] buf2 = new byte[512];
			String clientString = "newClient";

			try {
					int size = 0;
					Boolean newUser = false;
					while (true) {
						buf = clientString.getBytes();
						InetAddress address = InetAddress.getByName(HOST_ADDRESS);
						DatagramPacket packet = new DatagramPacket(buf,buf.length,address,7777);
						//for(int i = 0; i<30;i++){
							datagramSocket.send(packet);
							
						//}
							clientString = "loaded";
						packet = new DatagramPacket(buf2, buf2.length);
						
						datagramSocket.receive(packet);
						System.out.println("The address: " + packet.getAddress() + " The port: " + packet.getPort());
						
						String messenger = new String(packet.getData(),0,packet.getLength());
						
						
						System.out.println("The Messenger: " + messenger);
						
						size = usernames.size();
						indexOfPlayer = updateArrayLists(messenger);
						if(usernames.size() != 0){
						if(usernames.size() > size)  newUser = true;
						size = usernames.size();
						
						if (indexOfPlayer >= 0) {
							//appendMessageIfNotNull(serverObject);
							/*if (incoming != null && messages.get(indexOfPlayer) != null)
								incoming.append(messages.get(indexOfPlayer) + "\n");
*/
							try {
								//indexOfPlayer = usernames.indexOf(serverObject.getUsername());
								//logoutHandler(serverObject);
							} catch (NullPointerException ex) {	}

							
							if (newUser) {
								newUser = false;

								PlayerMob aPlayer = new PlayerMob(networkStartup);
								players.add(aPlayer);
								inGame.drawFromServer(aPlayer);
								
								/*players.get(indexOfPlayer).updateOthersCoordinates(
												xCoordinates.get(indexOfPlayer) - getXInWorld(indexOfPlayer),
												yCoordinates.get(indexOfPlayer) - getYInWorld(indexOfPlayer));
								*/
							} else {
								//if (thisUser(indexOfPlayer)) moveEveryoneElse(indexOfPlayer,serverObject);
								//otherPlayerMove(indexOfPlayer, serverObject);
							}

							
							players.get(indexOfPlayer).setClientServUsername(clientObject, indexOfPlayer);
							
							inGame.setPlayers(players, indexOfPlayer);
							inGame.drawPanel();

							System.out.println("");
							System.out.println("");
							System.out.println("");
							System.out.println("");
							System.out.println("");
							System.out.println(usernames);
							System.out.println(messages);
							System.out.println(xCoordinates);
							System.out.println(yCoordinates);
							System.out.println(faceDowns);
							System.out.println(faceUps);
							System.out.println(faceLefts);
							System.out.println(faceRights);
							System.out.println(crosses);
						}

					}

				}
				
			}

			catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		private int updateArrayLists(String messenger) {
			int index = 0;
			
			String pattern = "(\\d)(.*?),";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(messenger);
			String user;
			
			while(m.find()){
				if(m.group(1).equals("0")){
					user = m.group(2);
					if(!usernames.contains(m.group(2))) usernames.add(user);
					index = usernames.indexOf(user);
				}
				
				if(m.group(1).equals("2")){
					if(index < xCoordinates.size()) xCoordinates.set(index,Integer.parseInt(m.group(2)));
		    		else xCoordinates.add(index,Integer.parseInt(m.group(2)));
				}
				
				if(m.group(1).equals("3")){
					if(index < yCoordinates.size()) yCoordinates.set(index,Integer.parseInt(m.group(2)));
		    		else yCoordinates.add(index,Integer.parseInt(m.group(2)));
				}
				
				if(m.group(1).equals("4")){
					if(index < faceDowns.size()) faceDowns.set(index,Boolean.valueOf(m.group(2)));
		    		else faceDowns.add(index,Boolean.valueOf(m.group(2)));
				}
				
				if(m.group(1).equals("5")){
					if(index < faceUps.size()) faceUps.set(index,Boolean.valueOf(m.group(2)));
		    		else faceUps.add(index,Boolean.valueOf(m.group(2)));
				}
				
				if(m.group(1).equals("6")){
					if(index < faceLefts.size()) faceLefts.set(index,Boolean.valueOf(m.group(2)));
		    		else faceLefts.add(index,Boolean.valueOf(m.group(2)));
				}
				
				if(m.group(1).equals("7")){
					if(index < faceRights.size()) faceRights.set(index,Boolean.valueOf(m.group(2)));
		    		else faceRights.add(index,Boolean.valueOf(m.group(2)));
				}
				
				if(m.group(1).equals("8")){
					if(index < crosses.size()) crosses.set(index,Boolean.valueOf(m.group(2)));
		    		else crosses.add(index,Boolean.valueOf(m.group(2)));
				}
				
				
			}
			

			return index;
		}

		public int getXInWorld(int indexOfPlayer) {
			int xInWorld = 0;
			if (players.size() > 1 && thisUserIndex() >= 0) {
				// xInWorld = players.get(0).getXCoordinate() -
				// players.get(indexOfPlayer).getXCoordinate();
				xInWorld = players.get(thisUserIndex()).getXCoordinate() - 400;
				System.out
						.println("----getXInWorld thisUserIndex()-----\n thisUserIndex(): "
								+ thisUserIndex()
								+ players.get(thisUserIndex()).getXCoordinate()
								+ "  "
								+ players.get(indexOfPlayer).getXCoordinate()
								+ "\n" + xInWorld);
			}
			return xInWorld;
		}

		public int getYInWorld(int indexOfPlayer) {
			int yInWorld = 0;
			if (players.size() > 1 && thisUserIndex() >= 0) {
				yInWorld = players.get(thisUserIndex()).getYCoordinate() - 200;
				// yInWorld = players.get(0).getYCoordinate() -
				// players.get(indexOfPlayer).getYCoordinate();
			}
			return yInWorld;
		}

		public boolean thisUser(int indexOfPlayer) {
			return clientObject.getUsername().equals(
					usernames.get(indexOfPlayer));
		}

		public int thisUserIndex() {
			int index = 0;
			for (String username : usernames) {
				if (clientObject.getUsername().equals(username)) {
					return index;
				}
				index++;
			}
			return -1;
		}

		public void moveEveryoneElse(int indexOfPlayer,
				ServerObject serverObject) throws IndexOutOfBoundsException {
			for (PlayerMob eryElse : players) {
				if (eryElse == players.get(indexOfPlayer)) {
					players.get(indexOfPlayer).readMove(serverObject,
							indexOfPlayer);
				} else {
					eryElse.worldMove(serverObject, indexOfPlayer);
				}
			}
		}

		public void otherPlayerMove(int indexOfPlayer, ServerObject serverObject)
				throws IndexOutOfBoundsException {
			players.get(indexOfPlayer).readMove(serverObject, indexOfPlayer);
		}

		public void addNewUser(ServerObject serverObject) {
			//usernames.add(serverObject.getUsername());
			PlayerMob aPlayer = new PlayerMob(networkStartup);
			players.add(aPlayer);
			inGame.drawFromServer(aPlayer);
		}

		public void appendMessageIfNotNull(ServerObject serverObject) {
			if (incoming != null && serverObject.getMessage() != null)
				incoming.append(serverObject.getMessage() + "\n");
		}

		public void logoutHandler(ServerObject serverObject) {
			if (serverObject.getArrayList().size() > usernames.size()
					|| serverObject.getArrayList().size() < usernames.size()) {
				usersIn = 1;
			}

			if (usersIn == 1
					&& serverObject.getArrayList().size() < usernames.size()) {
				ArrayList<String> tempUsers = new ArrayList<String>();
				tempUsers = usernames;
				tempUsers.removeAll(serverObject.getArrayList());

				int removeIndex = usernames.indexOf(tempUsers.get(0));
				usernames = serverObject.getArrayList();

				inGame.removeFromServer(players.get(removeIndex));
				players.remove(removeIndex);
				inGame.setPlayers(players, removeIndex);

				usersIn = 0;
			}
		}

	}// End of Incoming Reader (the thread) class
}// End of Client class
