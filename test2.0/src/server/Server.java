package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import client.ClientObject;
import client.ServerObject;



public class Server {
	
	ArrayList<ObjectOutputStream> clientOutputStreams;
	
	ArrayList<String> usernames; //0
	ArrayList<String> messages; //1
	ArrayList<Integer> xCoordinates; //2
	ArrayList<Integer> yCoordinates; //3
	ArrayList<Boolean> faceDowns; //4
	ArrayList<Boolean> faceUps; //5
	ArrayList<Boolean> faceLefts; //6
	ArrayList<Boolean> faceRights; //7
	ArrayList<Boolean> crosses; //8
	ArrayList<Integer> xMoves;
	ArrayList<Integer> yMoves;
	
	ArrayList<String> deltaMessages;
	ArrayList<Boolean> deltaCrosses;
	
	private String messenger;

	ArrayList<ServerObject> serverObjects;
	ArrayList<ClientObject> clientObjects;
	
	ObjectOutputStream outStream;
	ServerObject serverObject;
	
	int port = 0;
	DatagramPacket packet = null;
	InetAddress address = null;
	ArrayList<InetAddress> addresses = new ArrayList<InetAddress>();
	ArrayList<Integer> ports = new ArrayList<Integer>();
	
	protected DatagramSocket datagramSocket;
	
	Thread timeThread;
	
	
	public String getMessenger() {
		return messenger;
	}
	
	public static void main(String[] args){
		//ApplicationContext factory = new ClassPathXmlApplicationContext("spring.xml");
		//new ChatServer().go();  //Old way of doing it.
		//Server server = new Server();	//New and improved way
		Server server = new Server();
		server.go();
	}
	     

	public class ClientHandler implements Runnable {
		ObjectInputStream inStream;
		
		public ClientHandler(Socket clientSocket){
			try{
				Socket sock = clientSocket;
				inStream = new ObjectInputStream(sock.getInputStream());
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		//The object comes in from a client
		public void run(){
			Object fromClientObject = null;
			
			try{
				while ((fromClientObject = inStream.readUnshared()) != null){
					//System.out.println(System.currentTimeMillis());
					ClientObject clientObject = (ClientObject) fromClientObject;
					//the casted fressh new object that came in
					
					serverObject = getServerObject(clientObject);
					serverObject.setArrayList(usernames);
					Boolean undefinedUser = serverObject.getUsername().equals("undefined");
					int clientIndex = usernames.indexOf(serverObject.getUsername());

					if(!undefinedUser && serverObject.getArrayList().indexOf(serverObject.getUsername()) < 0){
						//serverObject = getServerObject(clientObject);
						System.out.println("New User Logged in: " + serverObject.getUsername());

						usernames.add(serverObject.getUsername());
						clientIndex = usernames.indexOf(serverObject.getUsername()); //update clientIndex since we just added to usernames
						
						serverObjects.add(serverObject);
						addCoordinates(serverObject);// add all arraylists
					}
					else if(!undefinedUser && clientIndex >= 0){
						//serverObject = serverObjects.get(clientIndex);
						updateClientsInfo(clientIndex, serverObject); // update all array lists
					}
					serverObject.setArrayList(usernames);

					//tellEveryone();
				}
			}
			catch(SocketException e){
				System.err.println("User Logged out");
				removeLoggedOutUsers();

			}
			catch(IndexOutOfBoundsException ex){
				System.err.println("IndexOutOfBoundsException caught");
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			

		}
		
		
		
		public ServerObject getServerObject(ClientObject clientObject){
			ServerObject serverObject = null;
			
			
			String message = 	clientObject.getMessage();
			String username = 	clientObject.getUsername();
			Boolean r = 		clientObject.getR();
			Boolean down = 		clientObject.getDown();
			Boolean up = 		clientObject.getUp();
			Boolean left = 		clientObject.getLeft();
			Boolean right = 	clientObject.getRight();
			
			if(usernames.contains(username)){
				int objIndex = usernames.indexOf(username);
				serverObject = serverObjects.get(objIndex);
			}
			else{
				serverObject = new ServerObject();
			}
			
			serverObject.setUsername(username);
			serverObject.setMessage(message);
			serverObject.setCross(r);
			int speed = 2;
			
			if(up){
				serverObject.setYMove(-speed);
				
				serverObject.setFaceDown(false);
				serverObject.setFaceRight(false);
				serverObject.setFaceLeft(false);
				serverObject.setFaceUp(true);
			}
			if(down){
				serverObject.setYMove(speed);
				
				serverObject.setFaceDown(true);
				serverObject.setFaceRight(false);
				serverObject.setFaceLeft(false);
				serverObject.setFaceUp(false);
			}
			if(right){
				serverObject.setXMove(speed);
				
				serverObject.setFaceDown(false);
				serverObject.setFaceRight(true);
				serverObject.setFaceLeft(false);
				serverObject.setFaceUp(false);
			}
			if(left){
				serverObject.setXMove(-speed);
				
				serverObject.setFaceDown(false);
				serverObject.setFaceRight(false);
				serverObject.setFaceLeft(true);
				serverObject.setFaceUp(false);
			}
			if(!(right || left)){
				serverObject.setXMove(0);
			}
			if(!(up || down)){
				serverObject.setYMove(0);
			}
			
			return serverObject;
		}
		
		public void updateClientsInfo(int clientIndex, ServerObject serverObject){
			serverObjects.set(clientIndex, serverObject);
			xCoordinates.set(clientIndex,serverObject.getXCoordinate());
			yCoordinates.set(clientIndex,serverObject.getYCoordinate());
				deltaMessages.set(clientIndex, messages.get(clientIndex));
			messages.set(clientIndex,serverObject.getMessage());
			faceDowns.set(clientIndex, serverObject.getFaceDown()); //4
			faceUps.set(clientIndex, serverObject.getFaceUp()); //5
			faceLefts.set(clientIndex, serverObject.getFaceLeft()); //6
			faceRights.set(clientIndex, serverObject.getFaceRight()); //7
				deltaCrosses.set(clientIndex,crosses.get(clientIndex));
			crosses.set(clientIndex, serverObject.getCross()); //8
			xMoves.set(clientIndex, serverObject.getXMove());
			yMoves.set(clientIndex, serverObject.getYMove());
		}
		
		public void addCoordinates(ServerObject serverObject){
			xCoordinates.add(serverObject.getXCoordinate());
			yCoordinates.add(serverObject.getYCoordinate());
			messages.add(serverObject.getMessage());
				deltaMessages.add("null");
			faceDowns.add(serverObject.getFaceDown()); //4
			faceUps.add(serverObject.getFaceUp()); //5
			faceLefts.add(serverObject.getFaceLeft()); //6
			faceRights.add(serverObject.getFaceRight()); //7
			crosses.add(serverObject.getCross()); //8
				deltaCrosses.add(false);
			xMoves.add(serverObject.getXMove());
			yMoves.add(serverObject.getYMove());
		}

	}
	
	
		public void go(){
			clientOutputStreams = new ArrayList<ObjectOutputStream>();
			usernames = new ArrayList<String>();
			serverObjects = new ArrayList<ServerObject>();
			xCoordinates = new ArrayList<Integer>();
			yCoordinates = new ArrayList<Integer>();
			messages = new ArrayList<String>();
			faceDowns = new ArrayList<Boolean>(); //4
			faceUps = new ArrayList<Boolean>(); //5
			faceLefts = new ArrayList<Boolean>(); //6
			faceRights = new ArrayList<Boolean>(); //7
			crosses = new ArrayList<Boolean>(); //8
			xMoves = new ArrayList<Integer>();
			yMoves = new ArrayList<Integer>();
			
			deltaMessages = new ArrayList<String>();
			deltaCrosses = new ArrayList<Boolean>();
			
			try {
				datagramSocket = new DatagramSocket(7777);
				
			} catch (SocketException e) {
				e.printStackTrace();
			}

			try{
				@SuppressWarnings("resource")
				ServerSocket serverSock = new ServerSocket(5000);

				System.out.println("Server Up");
				Boolean timeSingleton = true;
				while(true){
					Socket clientSocket = serverSock.accept();
		            outStream = new ObjectOutputStream(clientSocket.getOutputStream());
		            clientOutputStreams.add(outStream);
					Thread t = new Thread(new ClientHandler(clientSocket));
					t.start();
					if(timeSingleton){
					try{
						timeThread = new Thread(new Time());
						timeThread.start();
						timeSingleton = false;
					}
					catch(Exception ex){
						System.err.println("making the thread catch");
						ex.printStackTrace();
					}
					}
				}
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}

		public class Time implements Runnable{
			public void run(){
				
				
				Boolean sendSingleton = true;

				
				while(true){
					sleepDelay(50);
					
					moveEveryone();
					
					
					
					
					try{
						byte[] buf = new byte[512];
						
						packet = new DatagramPacket(buf,buf.length);
						System.out.println("before receiving");
						
						//datagramSocket.receive(packet);
						System.out.println("after receiving");
						
						address = packet.getAddress();
						port = packet.getPort();
					
						String received = new String(packet.getData(), 0, packet.getLength());
						//if(received.equals("newClient")) loadNewUser(packet,address,port);
						
						System.out.println("The address: " + address + " The port: " + port);
						
						
						if(!(addresses.contains(address) && ports.contains(port))){
							addresses.add(address);
							ports.add(port);
						}
						
						System.out.println(addresses);
					
						/*
						byte[] buf2 = new byte[512];
						buf2 = messenger.getBytes();
					
						for(int i=0; i<ports.size();i++){
							System.out.println("sending to:" + addresses.get(i));
							packet = new DatagramPacket(buf2,buf2.length, addresses.get(i), ports.get(i));
							System.out.println("before sending");
							datagramSocket.send(packet);
							System.out.println("after sending");
						}
						*/
					
					}catch(Exception e){
						
					}
					
					if(sendSingleton  && ports.size() > 0){
						try{
							Thread sendThread = new Thread(new Send());
							sendThread.start();
							sendSingleton = false;
						}catch(Exception ex){
							//do nothing
						}
					}
					
					callTellEveryone();
					
					emptyMessenger();
					/*for(ServerObject obj:serverObjects){
						System.out.println("xCoordinates: " + obj.getXCoordinate() + "yCoordinates: " + obj.getYCoordinate());
					}*/
					//System.out.println("xCoordinates: " + xCoordinates + "---" + "yCoordinates: " + yCoordinates);
					
				}
			}
			
			public void sleepDelay(int delayTime){
				try{
					Thread.sleep(delayTime);
				} catch (InterruptedException e){
					e.printStackTrace();
				}			
			}
			
			public void callTellEveryone(){
				try {
					if(serverObject != null) tellEveryone();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			public void moveEveryone(){
				Iterator<ServerObject> serverObj = serverObjects.iterator();
				ServerObject aObject = null;
				for(int i = 0; i < serverObjects.size(); i++){
					
					serverObjects.get(i).setXCoordinate(serverObjects.get(i).getXCoordinate() + serverObjects.get(i).getXMove());
					serverObjects.get(i).setYCoordinate(serverObjects.get(i).getYCoordinate() + serverObjects.get(i).getYMove());
					xCoordinates.set(i, serverObjects.get(i).getXCoordinate());
					yCoordinates.set(i, serverObjects.get(i).getYCoordinate());
				}
				while(serverObj.hasNext()){
					aObject = (ServerObject) serverObj.next();
					aObject.setXCoordinate(aObject.getXCoordinate() + aObject.getXMove());
					aObject.setYCoordinate(aObject.getYCoordinate() + aObject.getYMove());
				}
				
			}
			
			
			
			public void loadNewUser(DatagramPacket packet, InetAddress address, int port) throws IOException{
				byte[] bigBuf = new byte[512];
				String bigBufString = "";
				for(int i = 0; i < usernames.size(); i++){
					bigBufString += "0" + usernames.get(i) + ",";
					bigBufString += "2" + xCoordinates.get(i) + ",";
					bigBufString += "3" + yCoordinates.get(i) + ",";
					bigBufString += "4" + faceDowns.get(i) + ",";
					bigBufString += "5" + faceUps.get(i) + ",";
					bigBufString += "6" + faceLefts.get(i) + ",";
					bigBufString += "7" + faceRights.get(i) + ",";
					bigBufString += "8" + crosses.get(i) + ",";
				}
				bigBuf = bigBufString.getBytes();
				
				packet = new DatagramPacket(bigBuf, bigBuf.length, address, port);
				datagramSocket.send(packet);
			}
		}
		
		public class Send implements Runnable{
			public Send(){
				
			}
			public void run(){
				while(true){
					
					fillMessenger();
					
					byte[] buf2 = new byte[512];
					buf2 = messenger.getBytes();
					
					
					
					synchronized(packet){
					for(int i=0; i<ports.size();i++){
						System.out.println("sending to:" + addresses.get(i));
						try {
							InetAddress hardCoded = InetAddress.getByName("192.168.2.21");
							packet = new DatagramPacket(buf2,buf2.length, hardCoded, 7778);
						} catch (UnknownHostException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						System.out.println("before sending");
						try {
							datagramSocket.send(packet);
						} catch (IOException e) {}
						System.out.println("after sending");
					}
					}
					
					emptyMessenger();
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		public void fillMessenger(){
			for(int i = 0; i < usernames.size(); i++){
				
				if(xMoves.get(i) != 0 || yMoves.get(i) != 0 || !deltaCrosses.get(i).equals(crosses.get(i))){
					messenger += "0" + usernames.get(i) + ",";
					if(xMoves.get(i) != 0 || yMoves.get(i) != 0){
						messenger += "4" + faceDowns.get(i) + ",";
						messenger += "5" + faceUps.get(i) + ",";
						messenger += "6" + faceLefts.get(i) + ",";
						messenger += "7" + faceRights.get(i) + ",";
						if(xMoves.get(i) != 0){
							messenger += "2" + xCoordinates.get(i) + ",";
						}
						if(yMoves.get(i) != 0){
							messenger += "3" + yCoordinates.get(i) + ",";
						}
					}	
					
					if(!deltaCrosses.get(i).equals(crosses.get(i))){
						messenger += "8" + crosses.get(i) + ",";
					}
					
				}
				
			}
		}
		
		public void emptyMessenger(){
			messenger = "";
		}
		
		public void removeLoggedOutUsers(){
				Iterator<ObjectOutputStream> it = clientOutputStreams.iterator();
				ObjectOutputStream out = null;
				ServerObject conTest = new ServerObject();

					try{

							while(it.hasNext()){
								out = (ObjectOutputStream) it.next();
								int removedIndex = clientOutputStreams.indexOf(out);
								conTest.setArrayList(usernames);
								if(usernames.size() < removedIndex) 
								{
									conTest.setUsername(usernames.get(removedIndex));
									conTest.setXCoordinate(xCoordinates.get(removedIndex));
									conTest.setYCoordinate(yCoordinates.get(removedIndex));
								}
									
								try{
									synchronized(out){out.writeUnshared(conTest);}
								}catch(SocketException e){
									System.err.println("Removing terminated user from clientOutputStreams the index is:" + clientOutputStreams.indexOf(out));

									
								//if(usernames.size() < removedIndex){
									usernames.remove(removedIndex);;
									serverObjects.remove(removedIndex);;
									xCoordinates.remove(removedIndex);;
									yCoordinates.remove(removedIndex);;
									//}
									clientOutputStreams.remove(out);

								}
								out.reset();
							}
						
					}
					catch(SocketException e){

					}
					catch(Exception e){
						e.printStackTrace();
					}
		}
		
		//The object gets sent out to every client
		public void tellEveryone() throws IOException{
			Iterator<ObjectOutputStream> it = clientOutputStreams.iterator();
			ObjectOutputStream out = null;

			while(it.hasNext()){
				out = (ObjectOutputStream) it.next();
				synchronized(out){out.writeUnshared(messenger);
				out.reset();
				}
				
			}

		}
}