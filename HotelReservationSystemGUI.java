import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Room {
    int roomNumber;
    String category;
    boolean isAvailable;

    public Room(int roomNumber, String category) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.isAvailable = true;
    }

    public void bookRoom() {
        this.isAvailable = false;
    }

    public void vacateRoom() {
        this.isAvailable = true;
    }
}

class Reservation {
    String guestName;
    Room room;
    String paymentStatus;

    public Reservation(String guestName, Room room) {
        this.guestName = guestName;
        this.room = room;
        this.paymentStatus = "Pending";
    }

    public void processPayment() {
        this.paymentStatus = "Paid";
    }

    @Override
    public String toString() {
        return "Reservation for " + guestName + " in room \n" + room.roomNumber + "(" + room.category + ").Payment status: " + paymentStatus;
    }
}

class Account {
    String accountNumber;
    String password;
    double balance;

    public Account(String accountNumber, String password, double balance) {
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return accountNumber + "," + password + "," + balance;
    }
}

public class HotelReservationSystemGUI extends JFrame {
    List<Room> rooms = new ArrayList<>();
    List<Reservation> reservations = new ArrayList<>();
    List<Account> accounts = new ArrayList<>();

    private JTextArea outputArea;
    private JTextField nameField;
   
    public HotelReservationSystemGUI() {
        // Initialize some rooms
        rooms.add(new Room(101, "Single"));
        rooms.add(new Room(102, "Double"));
        rooms.add(new Room(103, "Suite"));
        rooms.add(new Room(104, "Single"));
        rooms.add(new Room(105, "Double"));

        setTitle("Hotel Reservation System");
        setSize(900, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 1));

        Font font1 = new Font("SansSerif", Font.ITALIC, 20);
        outputArea = new JTextArea();
        outputArea.setFont(new Font("SansSerif", Font.BOLD, 15));
        outputArea.setBackground(new Color(28, 125, 144));
        outputArea.setForeground(new Color(255,255,255));
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        inputPanel.add(scrollPane);

        JButton searchButton = new JButton("Search Rooms");
        searchButton.addActionListener(new SearchRoomsListener());
        inputPanel.add(searchButton);
        searchButton.setFont(font1);
        searchButton.setForeground(new Color(29, 87, 145));

        JButton bookButton = new JButton("Make Reservation");
        bookButton.addActionListener(new BookRoomListener());
        inputPanel.add(bookButton);
        bookButton.setFont(font1);
        bookButton.setForeground(new Color(29, 87, 145));

        JButton viewButton = new JButton("View Reservations");
        viewButton.addActionListener(new ViewReservationsListener());
        inputPanel.add(viewButton);
        viewButton.setFont(font1);
        viewButton.setForeground(new Color(29, 87, 145));

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new CreateAccountListener());
        inputPanel.add(createAccountButton);
        createAccountButton.setFont(font1);
        createAccountButton.setForeground(new Color(29, 87, 145));

        JButton paymentButton = new JButton("Process Payment");
        paymentButton.addActionListener(new ProcessPaymentListener());
        inputPanel.add(paymentButton);
        paymentButton.setFont(font1);
        paymentButton.setForeground(new Color(29, 87, 145));

        setLocationRelativeTo(null);
        ImageIcon imageIcon = new ImageIcon("hh.png");
   
             
             Image image = imageIcon.getImage(); // transform it 
             Image newimg = image.getScaledInstance(600, 600,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        
             
             ImageIcon resizedImageIcon = new ImageIcon(newimg);
        
             
             JLabel label = new JLabel(resizedImageIcon);
        
        inputPanel.setBackground(new Color(28, 125, 144));
        add(inputPanel, BorderLayout.CENTER);
        add(label,BorderLayout.EAST);
        nameField=new JTextField();

        
    }

    private class SearchRoomsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            StringBuilder sb = new StringBuilder("Available rooms:\n");
            for (Room room : rooms) {
                if (room.isAvailable) {
                    sb.append("Room ").append(room.roomNumber).append(" (").append(room.category).append(")\n");
                }
            }
            outputArea.setText(sb.toString());
        }
    }

    private class BookRoomListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
             String guestName = JOptionPane.showInputDialog("Enter your name:");
             nameField.setText(guestName);
            try {
                int roomNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter room number:"));
                Room roomToBook = null;
                for (Room room : rooms) {
                    if (room.roomNumber == roomNumber && room.isAvailable) {
                        roomToBook = room;
                        break;
                    }
                }

                if (roomToBook != null) {
                    roomToBook.bookRoom();
                    Reservation reservation = new Reservation(guestName, roomToBook);
                    reservations.add(reservation);
                    outputArea.setText("Reservation made successfully.");
                } else {
                    outputArea.setText("Room not available or does not exist.");
                }
            } catch (NumberFormatException ex) {
                outputArea.setText("Invalid room number format.");
            }
        }
    }

    private class ViewReservationsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            StringBuilder sb = new StringBuilder("Current reservations:\n");
            for (Reservation reservation : reservations) {
                sb.append(reservation).append("\n");
            }
            outputArea.setText(sb.toString());
        }
    }

    private class CreateAccountListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            
                    
            String accountNumber =  JOptionPane.showInputDialog("Enter your account number:");
            String password = JOptionPane.showInputDialog("Enter your password:");
            double deposit;
            try {
                deposit = Double.parseDouble(JOptionPane.showInputDialog("Enter your deposit:"));
            } catch (NumberFormatException ex) {
                outputArea.setText("Invalid deposit amount.");
                return;
            }

            Account newAccount = new Account(accountNumber, password, deposit);
            accounts.add(newAccount);
            saveAccountsToFile();
            outputArea.setText("Account created successfully.");
        }
    }

    private class ProcessPaymentListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String guestName = nameField.getText();
            boolean reservationFound = false;

            for (Reservation reservation : reservations) {
                if (reservation.guestName.equals(guestName) && reservation.paymentStatus.equals("Pending")) {
                    reservationFound = true;

                    String accountNumber = JOptionPane.showInputDialog("Enter your account number:");
                    String password = JOptionPane.showInputDialog("Enter your password:");

                    boolean paymentSuccessful = processAccountPayment(accountNumber, password);

                    if (paymentSuccessful) {
                        reservation.processPayment();
                        outputArea.setText("Payment processed for " + guestName);
                    } else {
                        outputArea.setText("Payment failed. Please create\nan account & deposit some money.");
                    }
                    break;
                }
            }

            if (!reservationFound) {
                outputArea.setText("No pending payment found for " + guestName);
            }
        }

        private boolean processAccountPayment(String accountNumber, String password) {
            loadAccountsFromFile();
            for (Account account : accounts) {
                if (account.accountNumber.equals(accountNumber) && account.password.equals(password)) {
                    if (account.balance >= 5000) { 
                        account.balance -= 5000;
                        saveAccountsToFile();
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return false;
        }
    }

    private void saveAccountsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.txt"))) {
            for (Account account : accounts) {
                writer.write(account.toString());
                writer.write("\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadAccountsFromFile() {
        accounts.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String accountNumber = parts[0];
                    String password = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    accounts.add(new Account(accountNumber, password, balance));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HotelReservationSystemGUI().setVisible(true);
            }
        });
    }
}
