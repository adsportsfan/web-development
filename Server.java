/**
 *	Alexander Dicharry
 *	
 *	Professor Nur
 *
 *	4311-CSCI-Computer Networks and Telecommunication
 *
 *	16 October 2020
 */

import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;
	
public class Server {		
	static ArrayList<ClientHandler> clientList = new ArrayList();
	
	public static void main(String[] args) throws IOException {
			
		// creating a new server socket
		ServerSocket serverSocket = new ServerSocket(5000);

		// formatting the time
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

		Socket socket;
			
		while(true) {
			socket = serverSocket.accept();
				
			DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

			ClientHandler clientHandler = new ClientHandler(socket, dataInputStream, dataOutputStream);
			

			LocalDateTime now = LocalDateTime.now();

			System.out.println(">>" + dtf.format(now)+ " Welcome: " + clientHandler.username); 
			Thread thread = new Thread(clientHandler);
				
			for(ClientHandler arr: clientList) {
				now = LocalDateTime.now();
				if(arr.active)
				    arr.dataOutputStream.writeUTF(">>" + dtf.format(now)+" Welcome: " + clientHandler.username);
			}
				
			clientList.add(clientHandler);

			thread.start();
			} // end while loop

		} // end main method

		public static char[] listOfClients(DataInputStream dataInputStream) {
			return null;
		}

	} // end class server

	// clientHandler implementing Runnable
	class ClientHandler implements Runnable{

		// building a scanner
		Scanner scanner = new Scanner(System.in);
		
		public String username;
	    public Socket socket;

	    final DataInputStream dataInputStream;
	    final DataOutputStream dataOutputStream;
	    final String time;

	    // creatinga boolean named active, setting it to true
	    boolean active = true;

	    // here I create the dating format on how to display the date and time of the responses
	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	    DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("EEE, dd MMM  HH:mm:ss 'CST' yyyy", Locale.US);
	    
	    // here is the constructor setting up the multiple instantiations, and setting our parameters
	    // also throwing an IOException
	    public ClientHandler(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
	    	
	    	this.dataInputStream = dataInputStream;
	    	this.dataOutputStream= dataOutputStream;
	    	this.socket = socket;
	    	this.username =  dataInputStream.readUTF();
			LocalDateTime now = LocalDateTime.now();
	    	this.time = dtf1.format(now);
		}

		// getter method to get the time
		public String getTime() {
			return time;
		}
	    
	    // getter method to get the username
	    public String getUsername() {
			return username;
		}
	    
	    public boolean getActive() {
	    	return this.active;
	    }
	    
	    // getter method to get the output stream
		public DataOutputStream getDataOutputStream() {
			return dataOutputStream;
		}

	 	// here we override the run method
		@Override
		public void run() {
			String received;
			while(true) {
				try {
					received = dataInputStream.readUTF();
					if(received.equals("bye")) {
						LocalDateTime now = LocalDateTime.now();
		            	System.out.println( dtf.format(now) + "Goodbye  " + username);
		            	System.out.println( dtf.format(now) + "Server: Goodbye " + username);
		            	
						active = false;
						for(ClientHandler clientHandler: Server.clientList) {
							if( clientHandler.active == true)
								clientHandler.dataOutputStream.writeUTF(">> " + dtf.format(now) + " Server: Goodbye " +  username );			
							} // for loop

						break;	
					} // end if statement
					
					else if(received.equals("AllUsers")){
						
						LocalDateTime now = LocalDateTime.now();
						dataOutputStream.writeUTF( "Users: " + dtf.format(now));
						for(int i = 0 ; i < Server.clientList.size(); i++) {
							if(Server.clientList.get(i).getActive()) {
								dataOutputStream.writeUTF(">> " + i+") " + Server.clientList.get(i).getUsername() + " since " +  Server.clientList.get(i).getTime());
							} // end if-statement

						} // for-loop

					} // end else
				
					else {
						LocalDateTime now = LocalDateTime.now();
						
		            	System.out.println( dtf.format(now) + " "+ username +": "+ received);
		            	
						for(ClientHandler clientHandler: Server.clientList) {
							if( clientHandler.active == true)
								clientHandler.dataOutputStream.writeUTF(">> " + dtf.format(now) + " "+ username +": "+ received);
						} // for-loop

					} // end else 

				} // end of the try

				catch (IOException e) {
				} // end of the catch

			} // end while loop

		} // end run method

	} // end of ClientHandler

