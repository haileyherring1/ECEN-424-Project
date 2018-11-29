import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.lang.String;


//Class that creates Client
public class Client {
    //Initialize the information that will be given to the server:
    static String ServerIP; //The server's IP address that the client wants to connect to
    static int PortNumber;  //The server's port number that the client wants to connect to
    
    //Initialize the Socket:
    static Socket ClientSock;
    
    //Main:
    public static void main(String[] arg) {
        //The client will take as a console input the IP address and the port number of the server it is trying to connect to.
        //Try/Catches are set up for each integer input wanted:
        try {
            String PortIn = arg[1];
            PortNumber = Integer.parseInt(PortIn);
        }
        catch (Exception e) {
            System.out.println("The given port number is not a valid input. Exiting server...");
            System.exit(1);
        }
        
        //The IP address input does not need a try/catch because it is a string.
        String ServerIP  = arg[0];
        
        //Creating the client connection to the server:
        try {
            ClientSock = new Socket(ServerIP, PortNumber);      //Creates client with given server IP and port
            
            //Output that the client is connected:
            System.out.println("\nClient has connected to the server with IP " + ServerIP + " and port " + PortNumber + ".\n");
            System.out.println("Client Information");
            
            //Get Client IP address:
            InetAddress ClientIP;
            try {
                ClientIP = InetAddress.getLocalHost();
                System.out.println("\tClient IP Address: " + ClientIP.getHostAddress() + "\n");
            }
            catch (UnknownHostException ex) {
                ex.printStackTrace();
            }
            
            System.out.println("Client information being sent...\n");
            
            //Read information coming in and going out:
            InputStreamReader Input = new InputStreamReader(ClientSock.getInputStream());
            BufferedReader ServerInput = new BufferedReader(Input);
            
            //Get messages from server:
            System.out.println("Receiving message from Server...");
            
            String input = null;
            int k = 0;
            while((input = ServerInput.readLine()) != null) {
                System.out.println(input);
                k++;
                //System.out.println(k);
                //if((input.contains("Congratulations") == false)) {
                //    break;
              //  }
                if(k == 5){
                    break;
                }
        }

            /*    String serverResponse = null;
            for(int i = 0; i<=6; i++){
                serverResponse = ServerInput.readLine();
            } */
           //String input = ServerInput.readLine();
           //System.out.println(input);


            //Send message from client to server:
            Scanner ClientInput = new Scanner(System.in);
            PrintWriter ClientMessage = new PrintWriter(ClientSock.getOutputStream());
            
            //Sending input from the client and disconnecting if needed:
            do {
                System.out.print("Client's Message: ");
                String message = ClientInput.nextLine();

            /*
                try{
                    TimeUnit.SECONDS.sleep(1); 
                    System.out.println(ServerInput.readLine());
                } catch (InterruptedException ie){
                    System.out.println("sleep execption");
                }*/
                //String inputResponse = ServerInput.readLine();
               // System.out.println(inputResponse);
                if(message.equals("\\disconnect")) {
                    ClientMessage.write(message + "\n");
                    ClientMessage.flush();
                    break;
                }
                else if(message.equals(" ")) {
                    
                }
                else {
                    ClientMessage.write(message + "\n");
                    ClientMessage.flush();
                }

            } while(true);

        }


        catch (UnknownHostException Unk) {
            System.out.println("Cound not connect to server with IP " + ServerIP + " and port " + PortNumber + ". Exiting client...");
            System.exit(1);
        }
        catch (IOException s)
        {
            s.printStackTrace();
            System.exit(1);
        }

    } //main
} //Class