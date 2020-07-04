

/**
 *  This program was taken as an example presented in
 *  Head First JAVA 2 ed  K Sierra and Bert Bates
 *  Many added modifications were done to allow its readability
 *  TCU Fall 2019
 *  Assumes port 5001 for the connections
 *  
 *  Note that this program uses an ArrayList rather than a Vector
 *   also uses an inner local class ( i.e. a class within a class)  rather than external class   (  Client Handler ) 
 *  modified by Antonio Sanchez         
 *  
 */    

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;   

public class ChatServer extends JFrame  implements WindowListener
{    int portSocket = 5001;
    ArrayList<PrintWriter> clientOutputStreams;
    BufferedReader reader;
    Socket sock;
    public final boolean verbose = true;
    public  JTextArea  result = new JTextArea(20,30);
    public JScrollPane jsp = new JScrollPane(result);
    public Font f = new Font("Helvetica", Font.BOLD,16);
    JLabel timeLabel = new JLabel("Date and Time ", JLabel.RIGHT);
    JTextField timeField = new JTextField("");
    JPanel  displayTime = new JPanel(new GridLayout(1,2));
    JPanel  p = new JPanel(new BorderLayout());
    int year,month,day,hour,min,sec;    
    String todayS, timeS, minS, secS;
    Calendar now;
    Color     TCUColors   = new Color(77,25,121);
    public static void main(String args[]) {
		System.out.println("Chat Service");
	    new ChatServer();
	     	
	} 
    
        
    public String processTime(int option)
	   {    now = Calendar.getInstance();
	        year = now.get(Calendar.YEAR); 
            month = now.get(Calendar.MONTH)+1; 
            day = now.get(Calendar.DAY_OF_MONTH);
            hour = now.get(Calendar.HOUR);
            min =  now.get(Calendar.MINUTE);	  
            sec =  now.get(Calendar.SECOND);
            if (min < 10 )  minS =  "0" + min ;  else  minS = "" + min;
            if (sec < 10 )  secS =  "0" + sec ;  else  secS = "" + sec;
            todayS =  month + " / " + day + " / " + year;  
            timeS  = hour + " : " + minS + " : " + secS; 
            switch(option) {
            case (0):  return todayS  ; 
            case (1):  return timeS;
            case (2):  return todayS + " @ " + timeS ; 
           } 
           return null;  // should not get here
        }
     
    public ChatServer() {
    	setBounds(100,100,500,400);
		   displayTime.add(timeLabel);displayTime.add(timeField);
		   p.add(displayTime,BorderLayout.NORTH);
		   p.add(jsp,BorderLayout.CENTER);
		   p.setBackground(TCUColors);
		   timeLabel.setFont(f);
		   result.setFont(f);
		   timeLabel.setForeground(Color.WHITE);
		   displayTime.setBackground(TCUColors);
		   setLayout(new FlowLayout());
		   setTitle("Chat Server");
		   add(p);
		   addWindowListener(this);
           setVisible(true); 
        clientOutputStreams = new ArrayList<PrintWriter>();
        try {
            ServerSocket serverSock = new ServerSocket(portSocket);
             if (verbose) System.out.println(" Server IP 127.0.0.1. ready at port 5001 " );
                if (verbose) System.out.println(" ___________________"
                    +"________________________________________________"  ); 
            result.append("Server started on " + processTime(2)+"\n");
            if (verbose) { System.out.println ("\nStart Server on " + processTime(2)); 
             timeField.setText(processTime(2));
             }   
            while(true) {
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);  
                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();  // to execute the run() method of ClientHandler
                if(verbose) System.out.println("got a connection");
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    
    }
    
    public void tellEveryone(String message) {
        Iterator<PrintWriter> it = clientOutputStreams.iterator();
        result.append(message + " broadcasted on " + processTime(2)+ "\n");
      
        while (it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                System.out.println("coded message" + message);
                writer.println(message);
                writer.flush();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
    
    
    
    /** Internal class to handle the Client Handler
    The benefit of using an internal class is that we have access to the various objects of the 
    external class
    
    */
     public class ClientHandler  implements Runnable { 
    	 
      /**
       * Constructor of the inner class
       * @param clientSocket
       */
       public ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
                if (verbose) System.out.println("new client " );
                if (verbose) System.out.println(" new client at address "
                  +  sock.getLocalAddress() + " at port " +  sock.getLocalPort()); 
                if (verbose) System.out.println(" Client at address " 
                    +  sock.getInetAddress() + " at port " +  sock.getPort()  );
                if (verbose) System.out.println(" ___________________"
                    +"________________________________________________"  ); 

                
            } catch (Exception ex) { ex.printStackTrace(); }
        }
        /**
         * Method RUN to be executed when thread.start() is executed
         */
        public void run() {
            String message; 
            try {
                while ((message = reader.readLine()) != null) {
                	if(verbose) System.out.println("coded " + message); 
                
                    result.append("message received " + message + " on " + processTime(2)+"\n");
                    tellEveryone(message);
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    } 
     
	public void windowOpened(WindowEvent e) { }
	public void windowClosing(WindowEvent e) { System.exit(1); }
	public void windowClosed(WindowEvent e) { }
	public void windowIconified(WindowEvent e) { }
	public void windowDeiconified(WindowEvent e) { }
	public void windowActivated(WindowEvent e) { }
	public void windowDeactivated(WindowEvent e) { }
	
}
