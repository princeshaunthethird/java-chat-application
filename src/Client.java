import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame {
    private JTextArea messageArea;
    private JTextField inputField;
    private JButton sendButton;
    private JLabel statusLabel;
    private Socket socket;
    private BufferedReader br;
    private PrintWriter out;
    private Connection con;
    private String clientName;
    private final String serverName = "Server";
    private boolean isConnected = false;
    private String serverIP;

    public Client() {
        // Get server IP address
        serverIP = JOptionPane.showInputDialog(this, 
            "Enter Server IP Address:\n(Use 'localhost' for same computer)", 
            "Server IP", 
            JOptionPane.PLAIN_MESSAGE);
        
        if (serverIP == null || serverIP.trim().isEmpty()) {
            serverIP = "localhost";
        }
        serverIP = serverIP.trim();

        // Get username from user
        clientName = JOptionPane.showInputDialog(this, "Enter your username:", "Username", JOptionPane.PLAIN_MESSAGE);
        if (clientName == null || clientName.trim().isEmpty()) {
            clientName = "Client";
        }
        clientName = clientName.trim();

        setupUI();
        setVisible(true);
        startClient();
    }

    private void setupUI() {
        setTitle("Chat Client - " + clientName);
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Message area with better styling
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBackground(new Color(240, 240, 240));
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.addActionListener(e -> sendMessage());
        
        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(70, 130, 180));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.addActionListener(e -> sendMessage());

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Status bar
        statusLabel = new JLabel("Connecting to " + serverIP + "...");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(255, 255, 200));

        // Layout
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);

        // Window closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
            }
        });
    }

    private void startClient() {
        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/chat_application", 
                "root", 
                "shaun@2004"
            );

            // Socket connection
            socket = new Socket(serverIP, 7777);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            isConnected = true;
            updateStatus("Connected to " + serverIP, Color.GREEN);
            appendMessage("=== Connected to server at " + serverIP + " ===", Color.BLUE);

            // Load message history
            loadMessageHistory();

            // Start reading messages
            startReading();

        } catch (Exception e) {
            updateStatus("Connection Failed", Color.RED);
            appendMessage("Error: Could not connect to server at " + serverIP, Color.RED);
            appendMessage("Make sure the server is running and the IP address is correct.", Color.RED);
            e.printStackTrace();
        }
    }

    private void startReading() {
        Thread readerThread = new Thread(() -> {
            try {
                while (isConnected && !socket.isClosed()) {
                    String msg = br.readLine();
                    if (msg == null || msg.equals("exit")) {
                        appendMessage("=== Server disconnected ===", Color.RED);
                        disconnect();
                        break;
                    }
                    String timestamp = getCurrentTime();
                    appendMessage("[" + timestamp + "] " + serverName + ": " + msg, Color.BLACK);
                }
            } catch (Exception e) {
                if (isConnected) {
                    appendMessage("=== Connection lost ===", Color.RED);
                    disconnect();
                }
            }
        });
        readerThread.setDaemon(true);
        readerThread.start();
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty() && isConnected) {
            out.println(message);
            String timestamp = getCurrentTime();
            saveMessage(clientName, serverName, message);
            appendMessage("[" + timestamp + "] You: " + message, new Color(0, 100, 0));
            inputField.setText("");

            if (message.equalsIgnoreCase("exit")) {
                disconnect();
            }
        }
    }

    private void loadMessageHistory() {
        try {
            String query = "SELECT sender, receiver, content, timestamp FROM messages " +
                          "ORDER BY timestamp DESC LIMIT 50";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            java.util.List<String> messages = new java.util.ArrayList<>();
            while (rs.next()) {
                String sender = rs.getString("sender");
                String content = rs.getString("content");
                Timestamp ts = rs.getTimestamp("timestamp");
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String time = sdf.format(ts);
                
                String displayName = sender.equals(clientName) ? "You" : sender;
                messages.add("[" + time + "] " + displayName + ": " + content);
            }
            
            if (!messages.isEmpty()) {
                appendMessage("=== Previous messages ===", Color.GRAY);
                for (int i = messages.size() - 1; i >= 0; i--) {
                    appendMessage(messages.get(i), Color.GRAY);
                }
                appendMessage("=== End of history ===\n", Color.GRAY);
            }
        } catch (SQLException e) {
            System.err.println("Error loading message history");
            e.printStackTrace();
        }
    }

    private void saveMessage(String sender, String receiver, String content) {
        try {
            String query = "INSERT INTO messages (sender, receiver, content) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, sender);
            pstmt.setString(2, receiver);
            pstmt.setString(3, content);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving message to database");
            e.printStackTrace();
        }
    }

    private void disconnect() {
        isConnected = false;
        updateStatus("Disconnected", Color.RED);
        sendButton.setEnabled(false);
        inputField.setEnabled(false);
        
        try {
            if (out != null) out.close();
            if (br != null) br.close();
            if (socket != null && !socket.isClosed()) socket.close();
            if (con != null && !con.isClosed()) con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appendMessage(String message, Color color) {
        SwingUtilities.invokeLater(() -> {
            if (color != null && color != Color.BLACK) {
                messageArea.append(message + "\n");
            } else {
                messageArea.append(message + "\n");
            }
            messageArea.setCaretPosition(messageArea.getDocument().getLength());
        });
    }

    private void updateStatus(String status, Color color) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(status);
            statusLabel.setBackground(color.equals(Color.GREEN) ? new Color(200, 255, 200) : 
                                     color.equals(Color.RED) ? new Color(255, 200, 200) : 
                                     new Color(255, 255, 200));
        });
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Client());
    }
}