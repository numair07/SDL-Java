import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Scanner;

public class MessageProcessor implements Runnable{

    protected Socket ss = null;
    protected String serverText   = null;
    Hashtable <Integer, Record> hpatS = new Hashtable <Integer, Record>();
	Hashtable <Integer, Doctor> hdocS = new Hashtable <Integer, Doctor>();

    public MessageProcessor(Socket ss, Hashtable <Integer, Record> hpatS) {
        this.ss = ss; 
        this.hpatS   = hpatS;
    }

    public void run () {
    	Scanner Sc = null;
    	try {
			Sc = new Scanner(ss.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintStream p = null;
		try {
			p = new PrintStream(ss.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Scanner S1 = null; 
		S1 = new Scanner(System.in);
		
		ObjectInputStream in = null; 
		ObjectOutputStream out = null;
		try {
			in = new ObjectInputStream(ss.getInputStream());
			out = new ObjectOutputStream(ss.getOutputStream());
		}
		catch (Exception E) {
			
		}
		String Conf = Sc.nextLine();
		System.out.println(Conf);
		
		try {
			out.writeObject(hpatS);
			out.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Record Rec = new Record();
		int flag = 1;
		while(true) {
			if(flag != 1) {
				System.out.println("Current Thread : " + Thread.currentThread().getId()%4 + " Accessed Ny Patient No :  " + Rec.recno); 
			}
			else {
				flag=0;
			}
			String Msg = Sc.nextLine();
			if(Msg.equals("Appointment")) {
				System.out.println("The Patient is Requesting an appointment, Authorize? [1] [0]");
				int auth = S1.nextInt();
				if(auth==1) {
					p.println("Yes");
				}
				else {
					p.println("No");
				}
			}
			else if(Msg.equals("Exit")) {
				System.out.println("Patient No :- " + Rec.recno + " Has Disconnected from Thread Number " + Thread.currentThread().getId()%5);
				p.println("Thank You, GoodBye");
				break;
			}
			else if(Msg.equals("Data")) {
				Record temp;
				try {
					try {
						temp = (Record) in.readObject();
						temp.GetData();
						Rec=temp;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					p.println("Recieved");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			else {
				//String SS = Sc.next();
				System.out.println("Message from Patient No : " + " " + Rec.recno + " ---- " + Msg);
				System.out.print("Enter Reply : \t");
				String pp = S1.nextLine();
				p.println(pp);
			}
			p.flush();
		}
	}
    /*public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            PrintStream printStream = new PrintStream(output);
            InputStreamReader inputStream = new InputStreamReader(input);
            long time = System.currentTimeMillis();
            
            BufferedReader bufferedReader = new BufferedReader(inputStream);
            String message = null;
            message = bufferedReader.readLine();
            
            System.out.println("message received from client: \n\t"+message);
            processingDelay(1000);
            System.out.println("Send back the same message "+message);  
            

            output.write(message.getBytes());
            output.close();
            input.close();
            System.out.println("Request processed: " + time);
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
    */
}
