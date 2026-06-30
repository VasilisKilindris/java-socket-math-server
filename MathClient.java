import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MathClient {
    public static void main(String[] args) {
        //Σύνδεση με τον server τον υπολογιστή και την θύρα 5555
        try (Socket socket = new Socket("localhost", 5555)) {

            //Ροή εξόδου: Δημιουργεία printwriter για να στλενουμε κείμενο
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            //Μετατροπή byte σε κείμενο
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            //Αντικείμενο για είσοδο κείμεου
            Scanner scanner = new Scanner(System.in);

            //Επιβεβαίωση επιτυχημένης σύνδεσης
            System.out.println("Συνδεθήκατε στον Math Server.");
            System.out.println("Δώστε εντολή (π.χ. ADD 10 5) ή HELP για βοήθεια:");

            //Βρόγχος για να πολλές εντολές μέχρι να φύγει ο χρήστης
            while (true) {
                System.out.print("> "); //Αναμονή εντολής
                String command = scanner.nextLine(); //Καταχώρηση εντολ΄΄ης
                
                //Η απάντηση καταγράφεται παέι στον clienthandler μέσω της in.readline
                out.println(command); 

                String response = in.readLine();

                //Αν ο server κλείσει απότομα βγάζει το ανάλογο μήνυμα
                if (response == null){
                    System.out.println("Η σύνδεση έκλεισε από τον Server.");
                    break;
                }  
                
                System.out.println("Server: " + response);

                //Αν ο χρήσεις δώσει την εντολή BYE ο βρόγχος σπάει κααι κλείνει το πρόγραμμα
                if (response.equals("BYE")) {
                    break;
                }
                
            }
        } catch (IOException e) {
            System.out.println("Δεν είναι δυνατή η σύνδεση στον Server. Είναι ανοιχτός;");
        }
    }
}