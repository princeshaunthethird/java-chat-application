# 💬 Java Chat Application

A simple yet powerful real-time chat application built with Java Swing and MySQL. Chat with anyone on your local network with a modern, user-friendly interface.

## 👨‍💻 Author
**Shaun Dias**

## ✨ Features

- 🌐 **Local Network Support** - Chat with anyone on the same WiFi
- 💾 **Message Persistence** - All messages saved to MySQL database
- 📜 **Chat History** - Automatically loads previous conversations
- ⏰ **Timestamps** - See exactly when messages were sent
- 🎨 **Modern UI** - Clean, color-coded interface with status indicators
- 👤 **Custom Usernames** - Set your own display name
- 🔄 **Real-time Messaging** - Instant message delivery
- 🚀 **Easy to Use** - Simple setup and intuitive interface

## 🖼️ Screenshots

Server displays IP address for easy connection:
```
╔════════════════════════════════════════╗
║     SERVER IP ADDRESS INFORMATION     ║
╠════════════════════════════════════════╣
║  Local IP:  192.168.1.100              ║
║  Port:      7777                       ║
╠════════════════════════════════════════╣
║  Share this IP with clients to connect ║
╚════════════════════════════════════════╝
```

## 🛠️ Technology Stack

- **Language:** Java (Swing for GUI)
- **Database:** MySQL
- **Networking:** Java Socket Programming
- **JDBC:** MySQL Connector/J

## 📋 Prerequisites

Before running this application, make sure you have:

- Java JDK 8 or higher
- MySQL Server (5.7 or higher)
- MySQL Connector/J (JDBC Driver)

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/shaundias/java-chat-application.git
cd java-chat-application
```

### 2. Set Up MySQL Database

Run the following SQL commands in MySQL:

```sql
CREATE DATABASE IF NOT EXISTS chat_application;

USE chat_application;

CREATE TABLE IF NOT EXISTS messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender VARCHAR(100) NOT NULL,
    receiver VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_timestamp (timestamp),
    INDEX idx_sender (sender),
    INDEX idx_receiver (receiver)
);
```

### 3. Configure Database Connection

Edit the database credentials in both `Server.java` and `Client.java`:

```java
con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/chat_application", 
    "root",        // Your MySQL username
    "your_password" // Your MySQL password
);
```

### 4. Add MySQL Connector

Download MySQL Connector/J and add it to your classpath:
- [Download MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)

**For Eclipse/IntelliJ:**
- Right-click project → Build Path → Add External JARs → Select `mysql-connector-java-x.x.xx.jar`

**For Command Line:**
```bash
javac -cp ".:mysql-connector-java-x.x.xx.jar" Server.java Client.java
```

### 5. Compile and Run

**Compile:**
```bash
javac Server.java
javac Client.java
```

**Run Server:**
```bash
java Server
```

**Run Client:**
```bash
java Client
```

## 📖 How to Use

### Starting a Chat

1. **Start the Server**
   - Run `Server.java`
   - Note the IP address displayed (e.g., `192.168.1.100`)
   - Enter your username when prompted

2. **Connect Client(s)**
   - Run `Client.java` on another computer (or same computer)
   - Enter the server's IP address when prompted
   - Enter your username
   - Start chatting!

### Connection Options

- **Same Computer:** Use `localhost` or `127.0.0.1`
- **Local Network:** Use the IP shown by server (e.g., `192.168.1.100`)
- **Mobile Hotspot:** Usually `192.168.43.1` or check hotspot settings

### Chat Commands

- Type your message and press **Enter** or click **Send**
- Type `exit` to close the connection
- Close the window to disconnect

## 🔥 Features in Detail

### Message History
- Automatically loads last 50 messages on connection
- Color-coded history (gray text)
- Chronologically ordered

### Visual Indicators
- 🟢 **Green:** Connected
- 🟡 **Orange:** Waiting for connection
- 🔴 **Red:** Disconnected or error

### Message Colors
- **Green:** Your messages
- **Black:** Received messages
- **Blue:** System messages
- **Gray:** History

## 🔧 Troubleshooting

### Can't Connect?

1. **Check Firewall**
   - Allow Java through firewall
   - Open port 7777

2. **Verify IP Address**
   - Make sure client uses correct server IP
   - Both devices on same network

3. **Check MySQL**
   - MySQL server running
   - Database created
   - Credentials correct

### Common Issues

**"Connection refused"**
- Server not running or wrong IP address

**"SQLException"**
- Check database credentials
- Ensure database and table exist
- MySQL server running

**Messages not saving**
- Check database connection
- Verify table structure includes `timestamp` column

## 📁 Project Structure

```
java-chat-application/
├── 📁 src
│   ├── Server.java
│   └── Client.java
├── 📁 database
│   └── database_setup.sql
└── README.md
```

## 🔐 Security Notes

⚠️ **This is a educational/local network application. For production use, consider:**
- Encrypting messages (SSL/TLS)
- Hashing passwords
- Using environment variables for credentials
- Adding authentication
- Input validation and sanitization

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 Future Enhancements

- [ ] Multiple client support
- [ ] File sharing
- [ ] Emojis and rich text
- [ ] Voice/video calls
- [ ] End-to-end encryption
- [ ] User profiles with avatars
- [ ] Group chats
- [ ] Message search
- [ ] Dark mode

## 📧 Contact

**Shaun Dias**

- GitHub: [@princeshaunthethird](https://github.com/princeshaunthethird)
- Project Link: [https://github.com/princeshaunthethird/java-chat-application](https://github.com/princeshaunthethird/java-chat-application)
## 🙏 Acknowledgments

- Thanks to the Java Swing community
- MySQL for reliable database management
- All contributors and users

---

⭐ If you found this project helpful, please give it a star!

**Made with ❤️ by Shaun Dias**
