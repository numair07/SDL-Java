import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HospServer {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException, ClassNotFoundException {

		Hashtable <Integer, Record> hpat = new Hashtable <Integer, Record>();
		Hashtable <Integer, Doctor> hdoc = new Hashtable <Integer, Doctor>();
		
		System.out.println("Retrieving all data from Database...");
		try {  
			
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/_31158?autoReconnect=true&useSSL=false","root","Numair@2000");  
			
			Statement stmt=con.createStatement();  
			
			ResultSet rs=stmt.executeQuery("select * from patient");
			
			while(rs.next()) {  
				Record P = new Record(rs.getInt(1), rs.getString(2), rs.getString(6), rs.getInt(3), rs.getInt(4), rs.getInt(5));
				hpat.put(rs.getInt(1), P);
			}
			
			rs=stmt.executeQuery("select Doctor.*, doctor_pass.password from doctor left join doctor_pass on Doctor.doct_id=doctor_pass.doct_id");
			while(rs.next()) {
				Doctor Doc = new Doctor(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
				hdoc.put(rs.getInt(1), Doc);
			}
			
		}
		catch(Exception e) { 
			System.out.println(e);
		}
		
		Set <Integer> ex = hdoc.keySet();
		System.out.println("Doctor ID\tFirstName\tLastName\tSpecialization");
		
		for(Integer key:ex) {
			Doctor Doc = new Doctor();
			Doc = hdoc.get(key);
			
			System.out.println(key+"\t"+Doc.doct_fname+"\t"+Doc.doct_lname+"\t"+Doc.specialization);
		}
		
		Scanner Scan = new Scanner (System.in);
		
		System.out.print("Enter Your Doctor's ID : \t");
		int Doc_id = Scan.nextInt();
		
		Doctor Temp = new Doctor(); 
		Temp = hdoc.get(Doc_id);
		
		System.out.print("Enter Your Password :\t");
		String Pass = Scan.next();
		
		if(Pass.equals(Temp.password)) {
			System.out.println("Welcome Dr. " + Temp.doct_fname + " " + Temp.doct_lname);
		}
		else {
			System.out.println("Sorry, Wrong Password. Try Again Later...");
			System.exit(0);
		}
		
		boolean flagger = false;
		
		long startTime = System.currentTimeMillis();
		
		ServerSocket S = null;
		int serverPort = 5129;
		
		S = new ServerSocket(serverPort);
		System.out.println("Server Created on Server Port : " + serverPort);
		Socket ss=null;
		
		ExecutorService threadPool = Executors.newFixedThreadPool(5);
		
		while(true) {
			if((System.currentTimeMillis()-startTime)>200000) {
				break; // server program will end after time runs out (200 seconds)
			}
			try {
				if(flagger == false) {
					System.out.println("Awaiting Connection(s)...");
					flagger = true;
				}
				
				ss = S.accept();
				
            } catch (IOException e) {
                System.out.println("Error: cannot accept client request. Exit program");
                return;
            }
			threadPool.execute(new MessageProcessor(ss,hpat));
		}
		
		ss.close();
		threadPool.shutdown();
	}
}






// OLD CODE

/*
Scanner Scan = new Scanner (System.in);
System.out.print("Username :- \t");
String User = Scan.nextLine();
if(User.equals("Doctor") || User.equals("doctor")) {
System.out.print("Enter Password :- \t");
String pass = Scan.next();
if(pass.equals("doctor") || pass.equals("Doctor")) {
	
	
	
}
}
Scan.close();
*/

/*

while(true){
            Socket clientSocket = null;
            try {
                //start listening to incoming client request (blocking function)
                System.out.println("[ECHO Server] waiting for the incoming request ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Error: cannot accept client request. Exit program");
                return;
            }
            try {
                //create a new thread for each incoming message
                new Thread(new MessageProcessorRunnable(clientSocket, "Multithreaded Server")).start();
            } catch (Exception e) {
                //log exception and go on to next request.
            }
        }


System.out.println("Retrieving all data from Database...");
		try {  
			
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/_31158?autoReconnect=true&useSSL=false","root","Numair@2000");  
			
			Statement stmt=con.createStatement();  
			
			ResultSet rs=stmt.executeQuery("select * from patient");
			
			while(rs.next()) {  
				Record P = new Record(rs.getInt(1), rs.getString(2), rs.getString(6), rs.getInt(3), rs.getInt(4), rs.getInt(5));
				hpat.put(rs.getInt(1), P);
			}
			
			rs=stmt.executeQuery("select Doctor.*, doctor_pass.password from doctor left join doctor_pass on Doctor.doct_id=doctor_pass.doct_id");
			while(rs.next()) {
				Doctor Doc = new Doctor(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
				hdoc.put(rs.getInt(1), Doc);
			}
			
		}
		catch(Exception e) { 
			System.out.println(e);
		}

*/