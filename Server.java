// A Java program for a Server
import java.net.*;
import java.io.*;

class MyServerClientThread extends Thread{
	static int delays[]={10,5,8};
	int clientID;
	int result;
	int tokenNumber;
    int windowNumber;
	String timestamp;
	Socket s_clientSocket;

	static int ClassNoOfWindows=3;
	static boolean ClassWindowsIsBusyStatus[]=new boolean[ClassNoOfWindows]; 

	public static boolean[] getClassWindowsIsBusyStatus() {
		return ClassWindowsIsBusyStatus;
	}
	
	MyServerClientThread(Socket _clientSocket, int _clientID){
		s_clientSocket=_clientSocket;
		clientID=_clientID;
	}
	@Override
	public void run(){
		try
		{
			DataInputStream inStream = new DataInputStream(s_clientSocket.getInputStream());
			DataOutputStream outStream = new DataOutputStream(s_clientSocket.getOutputStream());
			String clientMessage[];
			String serverMessage="";
			//
			//System.out.println(" >> " + "Client No:" + clientID + " assigned to thread: "+Thread.currentThread().getId());
			serverMessage="Client No:" + clientID + " assigned to thread: "+Thread.currentThread().getId();
			outStream.writeUTF(serverMessage);
			outStream.flush();

			{
				String tmpStr=inStream.readUTF().toString();

				clientMessage=tmpStr.split("#");
				//
				/*for(String s:clientMessage)
				{
					System.out.println(s);
				}*/
				tokenNumber=Integer.parseInt(clientMessage[0]);
				timestamp=clientMessage[2];
				windowNumber=Integer.parseInt(clientMessage[3]);

				if(ClassWindowsIsBusyStatus[windowNumber]==true)
				{
					serverMessage="BUSY";
					outStream.writeUTF(serverMessage);
					outStream.flush();

					while(ClassWindowsIsBusyStatus[windowNumber]==true){
						System.out.print("");
					}
				}

				System.out.println("Token "+tokenNumber+" servicing at window "+windowNumber);
				serverMessage="PROCESSING";
				ClassWindowsIsBusyStatus[windowNumber]=true;
				outStream.writeUTF(serverMessage);
				outStream.flush();

				int expectedTimeofCompletion=(int)(5000+Math.random()*10000);
				System.out.println("expected Time of Completion for Token "+ tokenNumber+" = " +expectedTimeofCompletion+"ms");
				Thread.sleep(expectedTimeofCompletion);
				
				//serverMessage="Token #"+tokenNumber+" serviced successuflly!";
				serverMessage="DONE";
				ClassWindowsIsBusyStatus[windowNumber]=false;

				//System.out.println("toekn "+tokenNumber+" set "+ClassWindowsIsBusyStatus[windowNumber]+"\n");

				outStream.writeUTF(serverMessage);
				outStream.flush();
				//System.out.println("Result sent!!");
			}
			inStream.close();
			outStream.close();
			s_clientSocket.close();
		}catch(Exception ex){
			System.out.println(ex);
		}finally{
			ClassWindowsIsBusyStatus[windowNumber]=false;
			System.out.println("Patient " + tokenNumber + " successfully vaccinated!! ");
		}
	}

	int getDelay(int i)
	{
		return delays[i]*1000;
	}
}
public class Server
{
	public static void main(String[] args) throws Exception
	{
		try{
			ServerSocket server=new ServerSocket(8889);
			int clientID=0;
      		System.out.println("Server Started ....");

			while(true){
			Socket serverClient=server.accept();  //server accept the client connection request
			//System.out.println(" >> " + "Client No:" + clientID + " started!");
			MyServerClientThread sct = new MyServerClientThread(serverClient,clientID); //send  the request to a separate thread
			sct.start();
			clientID++;
			}
		}catch(Exception e){
			System.out.println(e);
		  }
	}
}
