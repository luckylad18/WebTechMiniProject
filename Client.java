

import java.net.*;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {

	private static DataInputStream dis;
	private static DataOutputStream dos;
	private static Socket socket;

	private static String server="localhost";
	private static String username;
	private static int port=1500;
 	static reading r;
	static Scanner in=new Scanner(System.in);

	public static void main(String[] args) throws IOException {

		System.out.println("Enter the username: ");
		username = in.nextLine();

		try {
			socket = new Socket(server, port);
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			dos.writeUTF(username);dos.flush();


		}
		// exception handler if it failed
		catch(Exception ec) {
			System.out.println("Error connectiong to server:" + ec);
		}
		System.out.println("Joined to chat room : "+dis.readUTF());

      r = new reading(dis,socket);
			r.start();
    int choice=-1;
		do{
			System.out.println("Press 1 to type broadcast message...\nPress 2 to type personal message...\nPress 3 to send files...\nPress 0 to exit...");
			choice=in.nextInt();
			switch(choice){
				case 0:dos.writeUTF("exit");dos.flush();dis.close();dos.close();socket.close();break;
				case 1:dos.writeUTF("broadcast");dos.flush();broadcast();break;
				case 2:dos.writeUTF("personal");dos.flush();personal();break;
				case 3:dos.writeUTF("file");dos.flush();file();break;
				default:System.out.println("Enter valid choice ...");
			}
		}while(choice!=0);


	}

	static void personal(){
				try{
		System.out.println("Enter username :");
		dos.writeUTF(in.next());dos.flush();
		System.out.println("Type exit for go to menu...");

		while(true){
			String mes=in.nextLine();
			if(mes.equals("exit")){
				dos.writeUTF("exit");dos.flush();
			break;
		}
			dos.writeUTF(mes);dos.flush();
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	}
	static  void file(){

	}

	static  void broadcast(){
		try{
		System.out.println("Type exit for go to menu...");

		while(true){
			String mes=in.nextLine();
			if(mes.equals("exit")){
				dos.writeUTF("exit");dos.flush();
			break;
		}
			dos.writeUTF(mes);dos.flush();
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	}

}

class reading extends Thread{
	DataInputStream dis;
	Socket socket;
	reading(DataInputStream dis,Socket socket){
		this.dis=dis;
		this.socket=socket;

	}
	public void run(){
		try{
		while(true && !socket.isClosed()){
			String read=dis.readUTF();
			System.out.println(read);
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
}


}
