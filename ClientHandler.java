import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class ClientHandler extends Thread {
    private Socket socket;

    //Μεταβλητές κάταστασης ανά πελάτη
    private int requestCount = 0; //Πλήθος αιτημάτων
    private double lastResult = 0.0; //Τελευταίο αποτέλασμα
    private List<String> history = new ArrayList<>(); //Λίστα ιστορικού στη μνήμη
    private static final int K = 5; //Μέγεθος ιστορικού (τελευταίες 5 εντολές)

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            //Ανάγνωση και εγγραφή δεδομένων μέσω του Socket
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            String inputLine;
            //Διάβασμα εντολών, βρόγχος μέχρι ο χρήστης να τον σπάσει με εντολή
            while ((inputLine = in.readLine()) != null) {

                //Καταγραφή στο server.log
                logRequest(socket.getInetAddress().toString(), inputLine);
                
                //Επεξεργασία εντολής και δημιουργεία απάντησης
                String response = processCommand(inputLine);
                out.println(response); //Εμφάνιση απάντησης

                //Τερματισμός σύνδεσης/while loop με εντολή BYE
                if (response.equals("BYE")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Ο πελάτης αποσυνδέθηκε βίαια.");
        } finally {
            try { socket.close(); } catch (IOException e) {}
        }
    }

    //Επεξεργασία εντολών
    private String processCommand(String command) {
        //Έλεγχος εντολής
        if (command == null || command.trim().isEmpty()) return "ERROR Δεν υπάρχει εντολή";
        
        requestCount++; //Αύξηση μετρητή αιτημάτων
        
        addToHistory(command); //Προσθήκη εντολής στο ιστορικό

        //Κάθε εντολή έχει το πρώτο κομμάτι που είναι γράμματα και μετά αν είναι αριθμιτική πράξη τους αριθμούς
        //Χρήση split για να πάρουμε το κομμάτη της εντολής και του αριθμούς
        String[] parts = command.trim().split("\\s+");
        String cmd = parts[0].toUpperCase();

        try {
            switch (cmd) {
                
                case "ADD":
                    if (parts.length < 3) return "ERROR Usage: ADD x y";
                    lastResult = Double.parseDouble(parts[1]) + Double.parseDouble(parts[2]);
                    return "OK " + lastResult;

                case "SUB":
                    if (parts.length < 3) return "ERROR Usage: SUB x y";
                    lastResult = Double.parseDouble(parts[1]) - Double.parseDouble(parts[2]);
                    return "OK " + lastResult;

                case "MUL":
                    if (parts.length < 3) return "ERROR Usage: MUL x y";
                    lastResult = Double.parseDouble(parts[1]) * Double.parseDouble(parts[2]);
                    return "OK " + lastResult;

                case "DIV":
                    if (parts.length < 3) return "ERROR Usage: DIV x y";
                    double div2 = Double.parseDouble(parts[2]);
                    if (div2 == 0) return "ERROR Division by zero";
                    lastResult = Double.parseDouble(parts[1]) / div2;
                    return "OK " + lastResult;

                case "POW":
                    if (parts.length < 3) return "ERROR Usage: POW x y";
                    lastResult = Math.pow(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
                    return "OK " + lastResult;

                case "LAST":
                    return "OK " + lastResult;

                case "COUNT":
                    return "OK " + requestCount;

                case "HIST":
                    return "OK History: " + history.toString();

                case "HELP":
                    return "OK Commands: ADD, SUB, MUL, DIV, POW, LAST, COUNT, HIST, EXIT";

                case "EXIT":
                    return "BYE";

                default:
                    return "ERROR Unknown command";
            }
        } catch (NumberFormatException e) {
            return "ERROR Invalid numbers";
        } catch (Exception e) {
            return "ERROR " + e.getMessage();
        }
    }

    //Ιστορικό
    private void addToHistory(String cmd) {
        //Λίστα FIFO οπότε αν περάσει τις Κ=5 εγγραφές το παλαιότερο στοιχείο σβήνεται
        if (history.size() >= K) {
            history.remove(0);
        }
        history.add(cmd);
    }

    //Καταγραφή των αιτημάτων με synchronized ώστε αν την ιδια στιγμή υπάρξουν 2 αιτήματα να μην καταχωρηθουν σε μία γραμμή
    private synchronized void logRequest(String ip, String cmd) {
        //Τρέχουσα ώρα
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        
        //Formating στο κέιμενο που θα καταγραφεί
        String logEntry = timeStamp + " | IP: " + ip + " | CMD: " + cmd;
        
        //Γράψιμο στο αρχείο με append mode
        try (PrintWriter logWriter = new PrintWriter(new FileWriter("server.log", true))) {
            logWriter.println(logEntry);
        } catch (IOException e) {
            System.err.println("Σφάλμα εγγραφής στο log: " + e.getMessage());
        }
    }
}