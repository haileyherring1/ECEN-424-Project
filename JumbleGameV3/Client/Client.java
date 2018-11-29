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
import java.util.Random;
import java.io.*;
import java.net.*;






//Class that creates Client
public class Client {




    
        //////////// INITIALIZING VARIABLES ////////////////////////////
        Random randomChar = new Random();



        /////////////////////VARIABLES END///////////////////////////


    ///////////////////FUNCTIONS////////////////////////////
/*
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
    }*/

    ////////////////////FUNCTIONS END//////////////////////////////////


    //Initialize the information that will be given to the server:
    static String ServerIP; //The server's IP address that the client wants to connect to
    static int PortNumber;  //The server's port number that the client wants to connect to
    
    //Initialize the Socket:
    static Socket ClientSock;
    
    //Main:
    public static void main(String[] arg) {
        long time = 0;

        long start = System.nanoTime();
        long end = 0;    
        int numGuesses = 0;

        String[] WORDS_DATABASE = new String[] {
            "aggies","engineering","program","reveille","football","howdy"
        };
        String[] SWORDS_DATABASE = new String[] {
            "ieggsa","graormp","veiellre","oobtllaf","wdyoh"
        };
        Random random = new Random();
        int rPos = random.nextInt(SWORDS_DATABASE.length);
        String randomWord = WORDS_DATABASE[rPos];
        String randomSWord = SWORDS_DATABASE[rPos];

        String shuffledWord = randomSWord; //Client.getShuffledWord(randomWord);
        

        

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
            //System.out.println("Client Information");
            
            //Get Client IP address:
            InetAddress ClientIP;
            try {
                ClientIP = InetAddress.getLocalHost();
                //System.out.println("\tClient IP Address: " + ClientIP.getHostAddress() + "\n");
            }
            catch (UnknownHostException ex) {
                ex.printStackTrace();
            }
            
            //System.out.println("Client information being sent...\n");
            
            //Read information coming in and going out:
            InputStreamReader Input = new InputStreamReader(ClientSock.getInputStream());
            BufferedReader ServerInput = new BufferedReader(Input);
            
            //Get messages from server:
            System.out.println("Connected to the server successfully.");
            
            String input = null;
            int k = 0;
            while((input = ServerInput.readLine()) != null) {
                System.out.println(input);
                k++;
                //System.out.println(k);
                //if((input.contains("Congratulations") == false)) {
                //    break;
              //  }
                if(k == 4){
                    break;
                }
            }

            System.out.println(" Your shuffled word is: " + " " + shuffledWord + "\n");
            start = System.nanoTime();
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
                //start = 0;
                System.out.print("Your Message: ");
                String message = ClientInput.nextLine();
                //System.out.println(ServerInput.readLine());

               /* while(true){

                    end = System.nanoTime();
                    //end = 1000000003;
                    if((end-start) > 1000000000)
                    break;                  
                    System.out.println(ServerInput.readLine());
                }*/
                
            
               /* try{
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
                    //String inputtest = null;
                    //System.out.println(inputtest);
                    // PRINT GUESS
                    //Client.processInput(input,randomWord);
                    end = System.nanoTime();
                    time = end - start;
                    time = time / 1000000000;
                    switch(message){
                        
                        case ("aggies"):  
                            System.out.println("\n Congratulations! You've guessed the word in " + time +" seconds!");
                            System.out.println("You guessed in " + numGuesses + " number of times");

                            System.out.println("\n Exiting game...");
                            System.exit(0);
                            numGuesses++;
                        case ("program"):  
                        System.out.println("\n Congratulations! You've guessed the word in " + time + " seconds!");
                        System.out.println("You guessed in " + numGuesses + " number of times");

                        System.out.println("\n Exiting game...");
                        System.exit(0);
                        numGuesses++;

                        case ("howdy"):  
                        System.out.println("\n Congratulations! You've guessed the word in " + time +" seconds!");
                        System.out.println("You guessed in " + numGuesses + " number of times");

                        System.out.println("\n Exiting game...");
                        System.exit(0);
                        numGuesses++;

                        case ("reveille"):  
                        System.out.println("\n Congratulations! You've guessed the word in " + time +" seconds!");
                        System.out.println("You guessed in " + numGuesses + " number of times");

                        System.out.println("\n Exiting game...");
                            System.exit(0);
                            numGuesses++;

                        case ("football"):  
                        System.out.println("\n Congratulations! You've guessed the word in " + time +" seconds!");
                        System.out.println("You guessed in " + numGuesses + " number of times");

                        System.out.println("\n Exiting game...");
                        System.exit(0);
                        numGuesses++;

                        default:
                        numGuesses++;

                        System.out.println("Incorrect. Please try again.");


                    }

                  /*  if(!message.equals("aggies")) {
                        System.out.println("Incorrect. Please try again.");
                    }*/
                   /* else if(!message.equals("reveille")){
                        System.out.println("Incorrect. Please try again.");
                    }
                    else if(!message.equals("football")){
                        System.out.println("Incorrect. Please try again.");
                    }                    
                    else if(!message.equals("howdy")){
                        System.out.println("Incorrect. Please try again.");
                    }  
                    else{
                        System.out.println("Congradulations! You've guessed the word in 7.2351 seconds!");
                        System.out.println("Exiting game...");
                        System.exit(0);
                    }*/

                    //System.out.println(processInput(input,randomWord));

                   /* try{
                        TimeUnit.SECONDS.sleep(1);
                        String inputtest = null; 
                        //inputtest = ServerInput.readLine();
                        System.out.println(inputtest);
                    } catch (InterruptedException ie){
                        System.out.println("sleep execption");
                    }*/
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

