package server;

public class Main {

		public static void main (String[] args){
			//new ChatServer().go();  //Old way of doing it.
			Server server = new Server();	//New and improved way
			server.go();
		}

}