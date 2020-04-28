
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
	public static int portNumber = 1500;
	public static boolean connect=true;
	public static ArrayList<ClientThread> al;
	public static  int uniqueId=0;
	public static String room_name;

	public static void main(String[] args) {


		try
		{
			al=new ArrayList<ClientThread>();
			// the socket used by the server
			System.out.println("Enter name of chat room : ");
			room_name=new Scanner(System.in).nextLine();
			ServerSocket serverSocket = new ServerSocket(portNumber);
	System.out.println("Chat room "+room_name+" created...\nWaiting for clients to connect...\n");
			// infinite loop to wait for connections ( till server is active )
			while(connect)
			{
				Socket socket = serverSocket.accept();
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				if(!connect)
					break;
				// if client is connected, create its thread
				ClientThread t = new ClientThread(socket,dis,dos,++uniqueId,room_name,al);

				al.add(t);

				t.start();
			}

				serverSocket.close();
				for(int i = 0; i < al.size(); ++i) {
					ClientThread tc = al.get(i);
					try {
					// close all data streams and socket
					tc.dis.close();
					tc.dos.close();
					tc.socket.close();
					}
					catch(IOException ioE) {
					}
				}


		}
		catch(Exception e) {
			e.printStackTrace();
		}



}
}

class ClientThread extends Thread{


	Socket socket;
    DataInputStream dis;
	DataOutputStream dos;
 ArrayList al;
	int id;

	String username;

	ClientThread(Socket s,DataInputStream dis,DataOutputStream dos,int id,String chatroom,ArrayList al){
		this.socket=s;
		this.id = id;
		this.al=al;
		this.dis=dis;
		this.dos=dos;
		this.socket = socket;

		//Creating both Data Stream
		try{
			username=dis.readUTF();
		System.out.println(username+" connected to chatroom...");
		dos.writeUTF(chatroom);dos.flush();
	}

	catch(Exception e){
		e.printStackTrace();
	}

	}


 public void run(){

	 while(true){
 		try{
 		String read=dis.readUTF();

      if(read.equals("exit")){
 			System.out.println("User "+username+" went out from chatroom...");
	                al.remove(this);
			break;
 		}

		if(read.equals("broadcast")){
			System.out.println(username+" is on group chat");
			while(true){
		   String mes=dis.readUTF();
			 if(mes.equals("exit"))
			 break;
			 if(mes.equals(""))
			 continue;
			 System.out.println("[group -"+username+']'+" : "+mes);

			 Iterator itr=al.iterator();
			 while(itr.hasNext()){
				 ClientThread ct=(ClientThread)itr.next();
			 if(ct.id!=id)
			 ct.send('['+username+']'+" : "+mes);
		 }
		 }
		}

		if(read.equals("personal")){
			String uname=dis.readUTF();
			while(true){
				if(uname.equals(""))
				uname=dis.readUTF();
				System.out.println(username+" is on private chat with :"+uname);
			 String mes=dis.readUTF();
			 if(mes.equals("exit"))
			 break;
			 if(mes.equals(""))
			 continue;


			 Iterator itr=al.iterator();
			 while(itr.hasNext()){
				 ClientThread ct=(ClientThread)itr.next();
			 if(ct.username.equals(uname) && ct.id!=id)
			 ct.send("[ Private- "+username+']'+" : "+mes);
		 }
		 }
			}

		}
 	catch(Exception e){
 		break;
 	}
 	}
 }

  void send(String s){
 	 try{
 	 dos.writeUTF(s);dos.flush();
  }
  catch(Exception e){
 	 e.printStackTrace();
  }
 }

}
