
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.NullPointerException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

//import java.lang;

//Class that Creates Server
public class Server {
    //Initialize the information that will be given to the server:
    static int PortNumber;       //The port number on which clients would connect to the server
    static int MaxClients;    //The maximum number of clients that can connect simultaneously
    static String Input;   //The message to be passed as a string from the server to the clients
    
    //Initialize the Server itself and its socket for the client:
    static ServerSocket Server23;
    static Socket ClientSock;
    
    //Main:
    public static void main(String[] arg) {
        //The server will take as a console input the port number, the maximum number of clients, and the message to be passed to the client.
        //Try/Catches are set up for each integer input wanted:
        try {
            String PortIn = arg[0];                 //Get port from input
            PortNumber = Integer.parseInt(PortIn);
        }
        catch (Exception e) {
            System.out.println("The given port number is not a valid input. Exiting Server...");
            System.exit(1);
        }
        try {
            String MaxClientsIn = arg[1];           //Get maximum number of clients from input
            MaxClients = Integer.parseInt(MaxClientsIn);
        }
        catch (Exception e) {
            System.out.println("The given maximum number of clients is not a valid input. Exiting Server...");
            System.exit(1);
        }
        

        
        //Output that the server is working:
        System.out.println("\nGroup17's Server has been created.");
        System.out.println("It can take a maximum of " + MaxClients + " client(s).");
        System.out.println("The port " + PortNumber + " will be used to connect to the server.");
        
        //Get Server IP address:
        InetAddress ServerIP;
        try {
            ServerIP = InetAddress.getLocalHost();
            System.out.println("The server's IP address is " + ServerIP.getHostAddress() + "\n");
        }
        catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        
        //Only creates threads for allowed clients:
        for(int j = 0; j < MaxClients; j++) {
            //Creating the Server connection to the client:
            try {
                Server23 = new ServerSocket(PortNumber); //Creates server with given Port
                int ClientsConnected = 0;
                /**The minimum number of possible clients is 0. Therefore, a for loop is
                 created that will loop until there is a thread for each of the maximum amount of
                 clients allowed on the server. There will be multiple clients, but one socket
                 being used.**/
                while(ClientsConnected < MaxClients) {
                    ClientSock = Server23.accept();
                    Thread ClientThread = new Thread(new ServerThread());
                    ClientThread.start();
                    ClientsConnected++;
                    System.out.println("\nClient number " + ClientsConnected + " attempting to connect...");
                }
                Thread ClientThread = new Thread(new ServerThread());
                ClientThread.join();
            }
            catch (Exception e) {
            }
        }
        System.out.println("\nMaximum number of clients connected.\n");
    } //main
} //class





//Class that runs threads for server
class ServerThread implements Runnable {
    
    //Bring values from Server Class over to the Thread:
    Server ServerValues = new Server();
    Random randomChar = new Random();
    int numGuesses = 0;
    long start = System.nanoTime();
    long end = 0;
    long time = 0;

    public String swapCharacters(String shuffledWord, int position1, int position2) {
        char[] charArray = shuffledWord.toCharArray();
        char temp = charArray[position1];
        charArray[position1] = charArray[position2];
        charArray[position2] = temp;
        return new String(charArray);
    }
    public String getShuffledWord(String original) {
        String shuffledWord = original; // start with original
        int wordSize = original.length();
        int shuffleCount = 10; // let us randomly shuffle letters 10 times
        for(int i=0;i<shuffleCount;i++) {
            //swap letters in two indexes
            int position1 = randomChar.nextInt(wordSize);
            int position2 = randomChar.nextInt(wordSize);
            shuffledWord = swapCharacters(shuffledWord,position1,position2);
        }
        return shuffledWord;
    }

    
    public String processInput(String theInput, String randomWord) {
        String theOutput = null;

        if (theInput.equalsIgnoreCase(randomWord)) {
                //theOutput = "Congratulations! You've guessed the word!";
                end = System.nanoTime();
                time = end - start;
                theOutput = "Congratulations! You've guessed the word!"+ "\n" +"You found the word in " +numGuesses+ " guesses and " +time+ " nanoseconds.";
                numGuesses++;
        }
        else {
                theOutput = "Incorrect. Please try again.";
                numGuesses++;
                //Test time to compute
            }
                                    
        return theOutput;
    }

    @Override
    public void run() {
        
        //Find out what thread we are on:
        long checker = Thread.currentThread().getId();
        int i = (int) checker;
        i = i - 8;


        String[] WORDS_DATABASE = new String[] {
            "aggies","engineering","program","reveille","football","howdy"
        };

        Random random = new Random();
        int rPos = random.nextInt(WORDS_DATABASE.length);
        String randomWord = WORDS_DATABASE[rPos];

        String shuffledWord = getShuffledWord(randomWord);

        //String original = selectRandomWord();

        
        //Copy values from Server class:
        int PortNumber = ServerValues.PortNumber;
        int MaxClients = ServerValues.MaxClients;
        String Message = ServerValues.Input;
        Socket ClientSock = ServerValues.ClientSock;
        
        //Get the client's address:
        String ClientAddress = ClientSock.getRemoteSocketAddress().toString();
        
        try {
            //Connect client:
            System.out.println("Client number " + i + " connected.");
            System.out.println("Client Information");
            System.out.println("\tClient IP Address: " + ClientAddress);
            
            //Send message from Server to client:
            PrintWriter ServerMessage = new PrintWriter(ClientSock.getOutputStream());
            ServerMessage.write("\n" + " Unjumble the following word in as few guesses as possible!" + "\n" + "\n" +
            " Type your guess and hit enter to guess, you have unlimited guesses!" + "\n");

            ServerMessage.write(" Your shuffled word is: " + " " + shuffledWord + "\n");
            //ServerMessage.write(processInput(input,randomWord));

            ServerMessage.flush();
            //System.out.println("\tMessage Recieved from Server: True\n");
            //ServerMessage.write(null);
            
            //Read information coming in and going out:
            InputStreamReader Input = new InputStreamReader(ClientSock.getInputStream());
            BufferedReader ClientInput = new BufferedReader(Input);

            
            //Taking input from the client and disconnecting if needed:
            do {
                try
                {
                    String input = ClientInput.readLine();
                    System.out.println(ClientAddress + " says: " + input);
                    //Process word from client
                    System.out.println(processInput(input,randomWord));
                    ServerMessage.write(processInput(input,randomWord));
                    /*try{
                        TimeUnit.SECONDS.sleep(1); 
                        ServerMessage.write(processInput(input,randomWord));
                        //ServerMessage.flush();

                    } catch (InterruptedException ie){
                        System.out.println("sleep execption");
                    }*/
                    



                    if (input.equals("\\disconnect"))
                    {
                        ClientSock.close();
                        System.out.println("Client number " + i + " disconnected.\n");
                        break;
                    }
                }
                catch (SocketException t) {
                    System.out.println("Client number " + i + " disconnected.\n");
                    break;
                }
                catch (NullPointerException n) {
                    ClientSock.close();
                    System.out.println("Client number " + i + " disconnected.\n");
                    break;
                }
            }while(true);
        }
        catch(IOException s) {
           s.printStackTrace();
        }
    } //run
} //Class
