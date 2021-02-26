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
import java.io.*;
import java.util.*;

public class Client {
	
	static boolean active = true;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		// making a new scanner
		Scanner scanner = new Scanner(System.in);
		
		// making a socket
		Socket socket = new Socket("localhost", 5000);
		
		DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
		DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

		// output for the Server
		// asking for the username, ackowledging the connection was successful, and welcoming the user
		System.out.println("Enter username: ");
		String username = scanner.nextLine();
		System.out.println("");
		System.out.println("Connection successful " + socket);
		System.out.println("Welcome " + username);
		
		try {
			dataOutputStream.writeUTF(username);
		} // end of the try

		catch (IOException e) {
		} // end of the catch

		Thread send = new Thread(new Runnable()
		{	

			@Override
			public void run() {
				while (active) {
					try {
						String msg = scanner.nextLine();
						if(msg.equals("bye")) {
							active = false;
							dataOutputStream.writeUTF("bye");
							socket.close();
							return;
						} // if statement

						dataOutputStream.writeUTF(msg);
					} // end of the try

					catch (IOException e) {
					} // end of the catch

				} // while loop

			} // end of override run method in the send thread

		} // end of the send thread
		);
		
		Thread recieve = new Thread(new Runnable() {
			@Override
			public void run() {
				while(active) {
					try {
						String msg = dataInputStream.readUTF();
						System.out.println(msg);
					} // end of the try

					catch (IOException e) {
					} // end of the catch

				} // end while loop

			} // end of Override run method

		}); // end of the receive thread
		
		send.start();
		recieve.start();
		
		
	} // end main method
    
} // end class Client
