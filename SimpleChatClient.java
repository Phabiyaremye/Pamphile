

/**
 *  This program was taken as an example presented in
 *  Head First JAVA 2 ed  K Sierra and Bert Bates
 *  Many added modifications were done to allow its readability
 *  TCU Fall 2019
 *  Assumes port 5001 for the connections
 *  
 *  Note it uses an inner local class ( i.e. a class within a class)  rather than external class   (  Incoming Reader ) 
 *  modified by Antonio Sanchez
 *  
 */

import java.io.*;
import java.net.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SimpleChatClient extends JFrame implements WindowListener, ActionListener
{  	      
		
	//Creates an instance of the L3Model class since we will be using methods from that class.
		private static final long serialVersionUID = 1L;
		
		//Initializes and declares an ecryption key.
		private final int[] key = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9};
		
		//Initializes and declares options.
		private final int encryptOption = 0;
		private final int decryptOption = 1;
		private final int writeOption = 2; 
		
		//Creates a Vector of type Byte.
		//This Vector contains all bytes of a file to be encrypted/decrypted before it is written to the hard disk.
		private Vector<Byte> data = new Vector<Byte>();
		
		
 final boolean verbose = true;
    int portSocket = 5001;  
    JPanel p = new JPanel(new GridLayout(3,1));  
    JButton sendButton = new JButton("Send The Message");   
    JLabel mL = new JLabel("Message to send ", JLabel.RIGHT);
    JTextArea incoming;
    JScrollPane qScroller;
    JTextField outgoing; 
    JTextArea console = new JTextArea(20,20); 
    
    JLabel nameLabel = new JLabel("Name", JLabel.RIGHT);
    JLabel toLabel = new JLabel("To", JLabel.RIGHT); 
    
    JFrame frame = new JFrame();
	
    

    
    
   
	
	JCheckBox encryBox = new JCheckBox(" Encryption"); 
	JCheckBox fancyBox = new JCheckBox("Fancy Display");
	
	JTextField nameField = new JTextField(20);
	JTextField toField = new JTextField(20);
	
	
	Color color;  
	JPanel imageP = new JPanel(new BorderLayout()); 
	ImageIcon icon1 = new ImageIcon("D60.JPG");  
	JTextArea tfFrame = new JTextArea(3,3); 
	
	//creating panels
	JPanel northP = new JPanel(new GridLayout(4,2));  
	JPanel southP = new JPanel (new BorderLayout(5,3));  
	JPanel centerP = new JPanel (new FlowLayout(7)); 
	JPanel disPanel = new JPanel(new BorderLayout(5,3)); 
    
    
	
	
    
    BufferedReader reader; 
    PrintWriter writer; 
    Socket sock;   
    
    
    
    
    String todayS, timeS, minS, secS;
    Calendar now; int year,month,day,hour,min,sec;
	private Object shiftFactor =0;
    public static void main(String args[]) {
		System.out.println("Simple Chat Client");
         new SimpleChatClient();
	   
	   }
    public SimpleChatClient() {
       
        setLayout(new BorderLayout());
        add(p);
        int sec = Integer.parseInt(processTime(3));
	    Random rn = new Random(sec);
	    int number =  rn.nextInt((50 - 1) + 1) + 0;
        setTitle("Simple Chat Client " + number); 
        Color c=Color.GRAY;
        if( number%4 == 0  ) c = Color.RED;
         else if(number%4 == 1 ) c = Color.YELLOW;
           else if( number%4 == 2 ) c = Color.MAGENTA;
             else if( number%4 == 3 ) c = Color.red;
        p.setBackground(c);
        incoming = new JTextArea(20, 70);
       incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        incoming.setText("Client logged on "+processTime(2) +"\n");
        qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        outgoing = new JTextField( 20);   
        
       
        northP.add(nameLabel);
        northP.add(nameField);
        northP.add(toLabel);
        northP.add(toField);
        northP.add(encryBox);
        northP.add(fancyBox); 
        northP.add(mL);
        northP.add(outgoing);
        
        northP.setBackground(color.GREEN); 
        p.setBackground(color.ORANGE);  
        console.setBackground(color.CYAN);
        
        p.add(sendButton); 
        p.add(qScroller); 
        p.add(console); 
        
        add(northP, BorderLayout.NORTH);
        add(p, BorderLayout.CENTER);
       
        setUpNetworking(); 
       
        Thread readerThread = new Thread(new IncomingReader());  // thread to read messages
        readerThread.start();
        addWindowListener(this);
        sendButton.addActionListener(this); 
        setBounds(400+5*number,8*number,650,320);
        setVisible(true);
        
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
         case (3): return secS;
        } 
        return null;  // should not get here
     }
    
    private void setUpNetworking() {
        try {if (verbose) System.out.println("Openning socket at port 5001");
            sock = new Socket("127.0.0.1",  portSocket);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("networking protocol established");
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
        public void actionPerformed(ActionEvent ev) { 
        	Object ae =  ev.getSource(); 
        	if(ae == sendButton) {
        		send();
        	
        	}else if(ae== sendButton && encryBox.isSelected()) { 
        	encrypt(reader); 	
        	} 
        	
        }
               
        private void send() {  
        	String send = nameField.getText(); 
        	try {  
        		
        		
        		
               	String message=  outgoing.getText();
             	if (verbose) System.out.println("Sending coded message => " + message); 
             	if(verbose)  System.out.println("Yes"+ send); 
             		writer.println(message); 
                writer.flush(); 
                incoming.setText("From" + send + "Message"+  message);
                
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }  
			
		
		private void encrypt(BufferedReader in) {
			int option = 0;
			{
				//Since the whole method is dealing with FileIO, a try/catch surrounds all code.
				try {
					//Any data left over gets cleared.
					data.clear();
					
					//Declares and initializes a byte for later use.
					byte a = 0;
					byte b = 0;
					
					//For loop that iterates through each element in the Array key.
					for(int i = 0; i < key.length; i++)
					{
						//Gets the next byte of the file.
						int next = in.read();
						
						//If there are no more bytes left, end the process.
						if(next == -1)
							break;
						else
							//Otherwise, cast next to a byte.
							a = (byte)next;
						
						//Depending on the parameter...
						if(option == encryptOption)
							
							//Either add the encryption key, encrypt the byte.
							b = (byte)(a + key[i]);
						else if(option == decryptOption)
							
							//Of subtract the encryption key, decrypt the byte.
							b = (byte)(a - key[i]);
						
						//Add the modified byte to the Vector.
						data.add(b);
						
						//To avoid OutOfBoundsException, check if the loop has iterated through all elements.
						if(i == key.length - 1)
							
							//And if it has, set the counter to 0, to begin at the beginning of the Array key.
							i = 0;
					}
				} catch(Exception e) {
					//If an error occurs, print it to the console.
					e.printStackTrace();
				}
			}	
		}	
            //Your answer is the String inside of encrypted 
           
    
	/** Internal class to handle the IncomingReader Handler
    The benefit of using an internal class is that we have access to the various objects of the 
    external class
    
    */  
      
    
    class IncomingReader implements Runnable {
    	/**
         * Method RUN to be executed when thread.start() is executed
         */
    	
        public void run() { 
            String sender = nameField.getText().trim();
            
 
            String message = outgoing.getText().trim();   
            
            incoming.setText("From " + sender + " MESSAGE" + message+ "\n"); 
              
            try { 
            	
                while ((message = reader.readLine()) != null) {
                	 if (verbose) System.out.println("Encoded received =>  " + message); 
                	 incoming.setText("From " + sender + " MESSAGE" + message+ "\n"); 
               	 
                }
            } catch (IOException ex) 
            {
                ex.printStackTrace();
            }
        }
    }
    
    
	@Override
	public void windowOpened(WindowEvent e) {
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(1);
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}

