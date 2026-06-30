import java.io.*;
import java.net.*;

public class MathServer {
    //Επιλέχθηκε η θύρα 5555 απο την εκφώνηση
    private static final int PORT = 5555;

    public static void main(String[] args) {
        System.out.println("O Server ξεκίνησε στην θύρα " + PORT);
        System.out.println("Περιμένει πελάτες...");

        //Δημιουργεία ServerSocket για την αποδοχή TCP
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                //Η accept() μπλοκάρει την δημιουργεία socket μέχρι να συνδεθεί κάποιος client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Νέος πελάτης συνδέθηκε: " + clientSocket.getInetAddress());
                
                //Δημιουργεία thread για κάθε πελάτη ξεχωριστά
                //ώστε να γίνεται ταυτόχρονη εξυπηρέτηση όταν υπάρχει πάνω απο ένας
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}