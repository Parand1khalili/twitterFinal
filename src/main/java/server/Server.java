package server;

import common.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.sql.*;
import java.util.concurrent.Executors;
import java.util.Date;


public class Server {

    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private boolean isDone;

    private static Connection connection;

    public static Connection getConnection(){
        if (connection == null) {
            synchronized (Server.class) {
                if (connection == null) {
                    try {
                        connection = DriverManager.getConnection("jdbc:sqlite:jdbc.db");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return connection;
    }

    public Server() {
        this.isDone = false;
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.serverSocket = new ServerSocket(9898);
            while (!server.isDone){
                System.out.println("waiting for client...");
                Socket client = server.serverSocket.accept();
                System.out.println("client accepted...");
                ClientHandler clientHandler = new ClientHandler(client);
                server.executorService = Executors.newCachedThreadPool();
                server.executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
class ClientHandler implements Runnable{

    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private Socket client;
    @Override
    public void run()  {
        java.sql.Connection connection = Server.getConnection();
        try {
            
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

        } catch (  IOException e) {
            throw new RuntimeException(e);
        }
        String command;
        while(true) {
            try {
                if ((command = (String) in.readObject()).equals("exit")) break;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            switch (command) {
                case "sign-up":
                    System.out.println("sign up done");
                    try {
                        signUpServer((User) in.readObject());
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "sign-in":
                    System.out.println("sign in done");
                    try {
                        signInServer((User) in.readObject());
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "get-user":
                    try {
                        getUser((String) in.readObject());
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "get-profile": {
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    User y = null;
                    try {
                        y = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    getProfile(x, y);
                    break;
                }
                case "edit-avatar": {
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    String y = null;
                    try {
                        y = (String) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    editProf(x, y, 4);
                    break;
                }
                case "edit-header": {
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    String y = null;
                    try {
                        y = (String) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    editProf(x, y, 5);
                    break;
                }
                case "edit-bio": {
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    String y = null;
                    try {
                        y = (String) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    editProf(x, y, 1);
                    break;
                }
                case "edit-location": {
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    String y = null;
                    try {
                        y = (String) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    editProf(x, y, 2);
                    break;
                }
                case "edit-web": {
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    String y = null;
                    try {
                        y = (String) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    editProf(x, y, 3);
                    break;
                }
                case "follow": {
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    String y = null;
                    try {
                        y = (String) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    follow(x, y);
                    break;
                }
                case "search": {
                    String x = null;
                    try {
                        x = (String) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    search(x);
                    break;
                }
                case "unfollow": {
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    String y = null;
                    try {
                        y = (String) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    unfollow(x, y);
                    break;
                }
                case "new-tweet": {
                    Tweet x = null;
                    try {
                        x = (Tweet) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    newTweet(x);
                    break;
                }
                case "timeline": {
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    timeline(x);
                    break;
                }
                case "like": {
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    Tweet y = null;
                    try {
                        y = (Tweet) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    like(x, y);
                    break;
                }
                case "hashtag": {
                    String x = null;
                    try {
                        x = (String) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    searchHashtag(x);
                    break;
                }
                case "block": {
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    User y = null;
                    try {
                        y = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    block(x, y);
                    break;
                }
                case "unblock": {
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    User y = null;
                    try {
                        y = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    unblock(x, y);
                    break;
                }
                case "retweet": {
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    Tweet y = null;
                    try {
                        y = (Tweet) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    retweet(x, y);
                    break;
                }
                case "comment": {
                    //reply
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    Tweet y = null;
                    try {
                        y = (Tweet) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    String z = null;
                    try {
                        z = (String) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    addComment(x, y, z);
                    break;
                }
                case "show-comments": {
                    Tweet x = null;
                    try {
                        x = (Tweet) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    showComments(x);
                    break;
                }
                case "check-like": {
                    Tweet x = null;
                    try {
                        x = (Tweet) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    User y = null;
                    try {
                        y = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    checkLike(x, y);
                    break;
                }
                case "check-retweet": {
                    Tweet x = null;
                    try {
                        x = (Tweet) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    User y = null;
                    try {
                        y = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    checkRetweet(x, y);
                    break;
                }
                case "followers-list":{
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    followerList(x);
                    break;
                }
                case "followings-list":{
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    followingList(x);
                    break;
                }
                case "check-follow": {
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    User y = null;
                    try {
                        y = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    checkFollow(x, y);
                    break;
                }
                case "check-block": {
                    User x = null;
                    try {
                        x = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    User y = null;
                    try {
                        y = (User) in.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    checkBlock(x, y);
                    break;
                }
            }
        }
    }
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:jdbc.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    public ClientHandler(Socket client) {
        this.client=client;
    }
    public static void signUpServer(User theUser) {
        System.out.println("method started");
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = Server.getConnection();
        try {
             statement = connection.createStatement();
             resultSet = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            System.out.println("sign-up : sqLite exp");
        }
        String respond;
        if(theUser.getEmail()==null && theUser.getPhoneNumber()==null){
            respond = "empty-field";
            try {
                out.writeObject(respond);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if(resultSet == null){
            System.out.println("resultSet empty");
            return;
        }
        while(true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString("id").equals(theUser.getId())){
                    respond = "duplicate-id";
                    out.writeObject(respond);
                    return;
                }
                else if(resultSet.getString("email")!=null&&!resultSet.getString("email").equals("")&&resultSet.getString("email").equals(theUser.getEmail())){
                    respond = "duplicate-email";
                    out.writeObject(respond);
                    return;
                }
                else if(resultSet.getString("phoneNumber")!=null&&!resultSet.getString("phoneNumber").equals("")&&resultSet.getString("phoneNumber").equals(theUser.getPhoneNumber())){
                    respond = "duplicate-number";
                    out.writeObject(respond);
                    return;
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            statement.close();
            resultSet.close();

            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO user(id,firstName,lastName,email,phoneNumber,password,country,birthDate,registerDate,profile,header,bio,location,web) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1,theUser.getId());
            pstmt.setString(2,theUser.getFirstName());
            pstmt.setString(3,theUser.getLastName());
            pstmt.setString(4,theUser.getEmail());
            pstmt.setString(5,theUser.getPhoneNumber());
            pstmt.setString(6,theUser.getPassword());
            pstmt.setString(7,theUser.getCountry());
            pstmt.setString(8,theUser.getBirthDate());
            pstmt.setString(9,theUser.getRegisterDate().toString());
            pstmt.setString(10,theUser.getProfPicName());
            pstmt.setString(11,theUser.getHeaderPicName());
            pstmt.setString(12,theUser.getBio());
            pstmt.setString(13,theUser.getLocation());
            pstmt.setString(14,theUser.getWeb());
            pstmt.executeUpdate();
            System.out.println("inserted");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        respond = "success";
        try {
            out.writeObject(respond);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void signInServer(User theUser)  {
        Connection connection = Server.getConnection();
        ResultSet resultSet;

        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String respond;
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString("id").equals(theUser.getId())){
                    try {
                        if(!resultSet.getString("password").equals(theUser.getPassword())){
                            respond="wrong-pass";
                            out.writeObject(respond);
                            return;
                        }
                        else{
                            respond="success";
                            out.writeObject(respond);
                            return;
                        }
                    } catch (SQLException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        respond="not-found";
        try {
            out.writeObject(respond);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void getUser(String id)  {
        Connection connection = Server.getConnection();
 
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString("id").equals(id)){
                    User theUser= null;
                    theUser = new User(resultSet.getString("id"),resultSet.getString("firstName"),
                            resultSet.getString("lastName"),resultSet.getString("email"),resultSet.getString("phoneNumber")
                            ,resultSet.getString("password"),resultSet.getString("country"),resultSet.getString("birthDate"),resultSet.getString("registerDate")
                            ,resultSet.getString("header"),resultSet.getString("profile"),resultSet.getString("bio"),resultSet.getString("location"),
                            resultSet.getString("web"),resultSet.getString("followers"),resultSet.getString("followings"),
                            resultSet.getInt("follower"),resultSet.getInt("following"),resultSet.getString("blacklist"));
                    out.writeObject(theUser);
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void getProfile(User theUser,User wanted) {
        Connection connection = Server.getConnection();
       
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;//wantedUser
        try {
            resultSet = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet1 = null;//theUser
        try {
            resultSet1 = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        while (true){
            try {
                if (!resultSet1.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet1.getString("id").equals(theUser.getId())){
                    if(resultSet1.getString("blacklist")==null || !resultSet1.getString("blacklist").contains(wanted.getId())){
                            if(resultSet.getString("id").equals(wanted.getId())){
                                Profile theProfile = new Profile(resultSet.getString("profile"),resultSet.getString("header"),
                                        resultSet.getString("bio"),resultSet.getString("location"),resultSet.getString("web"),
                                        resultSet.getInt("follower"),resultSet.getInt("following"));
                                out.writeObject(theProfile);
                            }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void editProf(User theUser,String text,int com)  {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(com==1){
            //bio
            while (true){
                try {
                    if (!resultSet.next()) break;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if(resultSet.getString("id").equals(theUser.getId())){
                        PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET bio = ? WHERE id = ?");
                        updateStatement.setString(1,text);
                        updateStatement.setString(2,theUser.getId());
                        updateStatement.executeUpdate();
                        Profile theProfile = new Profile(resultSet.getString("profile"), resultSet.getString("header"),
                                resultSet.getString("bio"), resultSet.getString("location"), resultSet.getString("web"),
                                resultSet.getInt("follower"), resultSet.getInt("following"));
                        out.writeObject("success");
                        return;
                    }
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else if(com==2){
            //loc
            while (true){
                try {
                    if (!resultSet.next()) break;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if(resultSet.getString("id").equals(theUser.getId())){
                        PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET location = ? WHERE id = ?");
                        try {
                            updateStatement.setString(1,text);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        updateStatement.setString(2,theUser.getId());
                        updateStatement.executeUpdate();
                        Profile theProfile = new Profile(resultSet.getString("profile"), resultSet.getString("header"),
                                resultSet.getString("bio"), resultSet.getString("location"), resultSet.getString("web"),
                                resultSet.getInt("follower"), resultSet.getInt("following"));
                        out.writeObject("success");
                        return;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else if(com==3){
            //web
            while (true){
                try {
                    if (!resultSet.next()) break;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if(resultSet.getString("id").equals(theUser.getId())){
                        PreparedStatement updateStatement = null;
                        try {
                            updateStatement = connection.prepareStatement("UPDATE user SET web = ? WHERE id = ?");
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            updateStatement.setString(1,text);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        updateStatement.setString(2,theUser.getId());
                        try {
                            updateStatement.executeUpdate();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        Profile theProfile = new Profile(resultSet.getString("profile"), resultSet.getString("header"),
                                resultSet.getString("bio"), resultSet.getString("location"), resultSet.getString("web"),
                                resultSet.getInt("follower"), resultSet.getInt("following"));
                        out.writeObject("success");
                        return;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else if(com==4){
            //profile
            while (true) {
                try {
                    if (!resultSet.next()) break;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (resultSet.getString("id").equals(theUser.getId())) {
                        PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET profile = ? WHERE id = ?");
                        updateStatement.setString(1, text);
                        updateStatement.setString(2, theUser.getId());
                        updateStatement.executeUpdate();

                        Profile theProfile = new Profile(resultSet.getString("profile"), resultSet.getString("header"),
                                resultSet.getString("bio"), resultSet.getString("location"), resultSet.getString("web"),
                                resultSet.getInt("follower"), resultSet.getInt("following"));

                        out.writeObject("success");
                        return;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else if(com==5){
            //header
            while (true) {
                try {
                    if (!resultSet.next()) break;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (resultSet.getString("id").equals(theUser.getId())) {
                        PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET header = ? WHERE id = ?");
                        updateStatement.setString(1, text);
                        updateStatement.setString(2, theUser.getId());
                        updateStatement.executeUpdate();

                        Profile theProfile = new Profile(resultSet.getString("profile"), resultSet.getString("header"),
                                resultSet.getString("bio"), resultSet.getString("location"), resultSet.getString("web"),
                                resultSet.getInt("follower"), resultSet.getInt("following"));

                        out.writeObject("success");
                        return;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public static void follow(User theUser,String followingId) {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String respond;
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString("id").equals(theUser.getId())){
                    if(resultSet.getString("followings").contains(followingId)){
                        respond="already-followed";
                        out.writeObject(respond);
                        return;
                    }
                }
                else{
                    PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET followings = ? WHERE id = ?");
                    updateStatement.setString(1,theUser.getFollowing()+"="+followingId);
                    updateStatement.setString(2,theUser.getId());
                    updateStatement.executeUpdate();

                    PreparedStatement updateStatement2 = connection.prepareStatement("UPDATE user SET followingNum = ? WHERE id = ?");
                    updateStatement2.setInt(1, Integer.parseInt(theUser.getFollowing())+1);
                    updateStatement2.setString(2,theUser.getId());
                    updateStatement2.executeUpdate();

                    while (resultSet.next()){
                        if(resultSet.getString("id").equals(followingId)){
                            PreparedStatement updateStatement3 = connection.prepareStatement("UPDATE user SET followers = ? WHERE id = ?");
                            updateStatement3.setString(1,resultSet.getString("followers")+"="+theUser.getId());
                            updateStatement3.setString(2,followingId);
                            updateStatement3.executeUpdate();

                            PreparedStatement updateStatement4 = connection.prepareStatement("UPDATE user SET followerNum = ? WHERE id = ?");
                            updateStatement4.setInt(1,Integer.parseInt(resultSet.getString("follower"))+1);
                            updateStatement4.setString(2,followingId);
                            updateStatement4.executeUpdate();

                            respond="success";
                            out.writeObject(respond);
                            return;
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void search(String text)  {
        ArrayList <User> res=new ArrayList<>();
        java.sql.Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String respond;
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString("id").contains(text)||resultSet.getString("firstName").contains(text)
                        ||resultSet.getString("lastName").contains(text)){
                    User newUser = new User(resultSet.getString("id"),resultSet.getString("firstName"),
                            resultSet.getString("lastName"),resultSet.getString("email"),resultSet.getString("phoneNumber"),
                            resultSet.getString("password"),resultSet.getString("country"),resultSet.getString("birthDate"),resultSet.getString("header")
                    ,resultSet.getString("profile"));
                    res.add(newUser);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(res.isEmpty()){
            respond="not-found";
            try {
                out.writeObject(respond);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            respond="found";
            try {
                out.writeObject(respond);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                out.writeObject(res);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void unfollow(User theUser,String followingId)  {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String respond;
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString("id").equals(theUser.getId())){
                    if(!resultSet.getString("followings").contains(followingId)){
                        respond="is not followed";
                        out.writeObject(respond);
                        return;
                    }
                    else {
                        int i;
                        String[] following=resultSet.getString("followings").split("=");
                        ArrayList<String> list = new ArrayList<String>(Arrays.asList(following));
                        for( i=0;i<list.size();i++){
                            if(list.equals(followingId)){
                                break;
                            }
                            i++;
                        }
                        list.remove(i);
                        PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET followings = ? WHERE id = ?");
                        updateStatement.setString(1,list.toString());
                        updateStatement.setString(2,theUser.getId());
                        updateStatement.executeUpdate();
                        PreparedStatement updateStatement2 = connection.prepareStatement("UPDATE user SET following = ? WHERE id = ?");
                        updateStatement2.setInt(1,theUser.getFollowingNum()-1);
                        updateStatement2.setString(2,theUser.getId());
                        updateStatement2.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString("id").equals(followingId)){
                    int i;
                    String[] follower=resultSet.getString("followers").split("=");
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(follower));
                    for(i=0;i<list.size();i++){
                        if(list.equals(theUser.getId())){
                            break;
                        }
                        i++;
                    }
                    list.remove(i);
                    PreparedStatement updateStatement3 = connection.prepareStatement("UPDATE user SET followers = ? WHERE id = ?");
                    updateStatement3.setString(1,list.toString());
                    updateStatement3.setString(2,followingId);
                    updateStatement3.executeUpdate();
                    PreparedStatement updateStatement4 = connection.prepareStatement("UPDATE user SET follower = ? WHERE id = ?");
                    updateStatement4.setInt(1,theUser.getFollowerNum()-1);
                    updateStatement4.setString(2,followingId);
                    updateStatement4.executeUpdate();
                    respond="success";
                    out.writeObject(respond);
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void newTweet(Tweet tweet) {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            PreparedStatement pstmt = null;
            try {
                pstmt = connection.prepareStatement("INSERT INTO Tweet(text,piclink,userId,likes,retweets,comments,date,isFavStar,likesIds,commentText,retweetIds) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                pstmt.setString(1,tweet.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                pstmt.setString(2,tweet.getPicLink());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                pstmt.setString(3,tweet.getUserId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                pstmt.setInt(4,tweet.getLikes());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                pstmt.setInt(5,tweet.getRetweet());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                pstmt.setInt(6,tweet.getComment());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                pstmt.setString(7, String.valueOf(tweet.getDate()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                pstmt.setInt(8,tweet.getIsFavStar());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                pstmt.setString(9,tweet.getLikesIds());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                pstmt.setString(10,tweet.getCommentText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                pstmt.setString(11,tweet.getRetweetIds());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String respond="success";
        try {
            out.writeObject(respond);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    public static void timeline(User theUser)  {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSetTweet = null;
        try {
            resultSetTweet = statement.executeQuery("SELECT * FROM Tweet");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ArrayList <Tweet> res=new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //check user
        while (true){
            try {
                if (!resultSetTweet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSetTweet.getString("userId").equals(theUser.getId())){
                    Tweet theTweet = new Tweet(resultSetTweet.getString("text"),resultSet.getString("piclink"),
                            resultSet.getString("userId"),resultSet.getInt("likes"),resultSet.getInt("retweets"),
                            resultSet.getInt("comments"), resultSet.getString("date") ,resultSet.getInt("isFavStar"),
                            resultSet.getString("commentText"),resultSet.getString("retweetIds"));
                    System.out.println("tweet retweets : " + theTweet.getRetweetIds());
                    res.add(theTweet);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        //check followings
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString(1).equals(theUser.getId())){
                    while (resultSetTweet.next()){
                        if(resultSet.getString("followings").contains(resultSetTweet.getString("userId"))){
                            Tweet theTweet = new Tweet(resultSetTweet.getString("text"),resultSet.getString("piclink"),
                                    resultSet.getString("userId"),resultSet.getInt("likes"),resultSet.getInt("retweets"),
                                  resultSet.getInt("comments"), resultSet.getString("date") ,resultSet.getInt("isFavStar"),
                                    resultSet.getString("commentText"),resultSet.getString("retweetIds"));
                            res.add(theTweet);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        //check favstars and blocks
        while (true){
            try {
                if (!resultSetTweet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSetTweet.getInt("isFavStar")==1){
                    Tweet theTweet = new Tweet(resultSetTweet.getString("text"),resultSet.getString("piclink"),
                            resultSet.getString("userId"),resultSet.getInt("likes"),resultSet.getInt("retweets"),
                            resultSet.getInt("comments"),resultSet.getString("date") ,resultSet.getInt("isFavStar"),
                            resultSet.getString("commentText"),resultSet.getString("retweetIds"));
                    if(!res.contains(theTweet)){
                        while (resultSet.next()){
                            if(resultSet.getString("id").equals(theUser.getId())){
                                if(!resultSet.getString("blacklist").contains(theTweet.getUserId())){
                                res.add(theTweet);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            out.writeObject(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void like(User theUser,Tweet theTweet) {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSetTweet = null;
        try {
            resultSetTweet = statement.executeQuery("SELECT * FROM Tweet");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String respond;
        System.out.println("injaaaaaa" + resultSetTweet);
        while (true){
            try {
                if (!resultSetTweet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSetTweet.getString(1).equals(theTweet.getText()) && resultSetTweet.getString("likesIds").contains(theUser.getId())){
                    System.out.println("toff " + resultSetTweet.getString("likesIds"));
                    respond="already-liked";
                    out.writeObject(respond);
                }
                else if(resultSetTweet.getString(1).equals(theTweet.getText()) && ! resultSetTweet.getString("likesIds").contains(theUser.getId())){
                    respond="success";
                    PreparedStatement updateStatement = connection.prepareStatement("UPDATE Tweet SET likesIds = ? WHERE text = ? AND userid = ?");
                    updateStatement.setString(1,theTweet.getLikesIds()+"="+theUser.getId());
                    updateStatement.setString(2,theTweet.getText());
                    updateStatement.setString(3,theTweet.getUserId());
                    updateStatement.executeUpdate();
                    System.out.println("sos"+resultSetTweet.getString("likesIds"));

                    PreparedStatement updateStatement2 = connection.prepareStatement("UPDATE Tweet SET likes = ? WHERE text = ? AND userid = ?");
                    updateStatement2.setInt(1,resultSetTweet.getInt("likes")+1);
                    updateStatement2.setString(2,theTweet.getText());
                    updateStatement2.setString(3,theTweet.getUserId());
                    updateStatement2.executeUpdate();
                    if(resultSetTweet.getInt("likes")==10){
                        PreparedStatement updateStatement3 = connection.prepareStatement("UPDATE Tweet SET isFavStar = ? WHERE text = ? AND userid = ?");
                        updateStatement3.setInt(1,theTweet.getIsFavStar()+1);
                        updateStatement3.setString(2,theTweet.getText());
                        updateStatement3.setString(3,theTweet.getUserId());
                        updateStatement3.executeUpdate();
                    }
                    out.writeObject(respond);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void searchHashtag(String hashtag) {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM Tweet");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Tweet> res=new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString("text").contains("#"+hashtag)){
                    Tweet theTweet = new Tweet(resultSet.getString("text"),resultSet.getString("piclink"),
                            resultSet.getString("userId"),resultSet.getInt("likes"),resultSet.getInt("retweets"),
                            resultSet.getInt("comments"),resultSet.getString("date") ,resultSet.getInt("isFavStar"),
                            resultSet.getString("commentText"),resultSet.getString("retweetIds"));
                    res.add(theTweet);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            out.writeObject(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void block(User theUser,User block)  {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSetUser = null;
        try {
            resultSetUser = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSetBlock = null;
        try {
            resultSetBlock = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String respond ;
        while (true){
            try {
                if (!resultSetUser.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSetUser.getString("id").equals(theUser.getId()) &&
                        resultSetUser.getString("blacklist").contains(block.getId())){
                    respond="already-blocked";
                    out.writeObject(respond);
                    return;
                }
                else if(resultSetUser.getString("id").equals(theUser.getId())){
                    PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET blacklist = ? WHERE id = ?");
                    updateStatement.setString(1,theUser.getBlacklist()+"="+block.getId());
                    updateStatement.setString(2,theUser.getId());
                    updateStatement.executeUpdate();
                    respond="success";
                    out.writeObject(respond);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        while (true){
            try {
                if (!resultSetUser.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSetUser.getString("id").equals(theUser.getId()) && resultSetUser.getString("followers").contains(block.getId())){
                    //block is following the user
                    //block should be removed from the user followers
                    int i;
                    String[] follower=resultSetUser.getString("followers").split("=");
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(follower));
                    for( i=0;i<list.size();i++){
                        if(list.equals(block.getId())){
                            break;
                        }
                        i++;
                    }
                    list.remove(i);
                    PreparedStatement updateStatement2 = connection.prepareStatement("UPDATE user SET followers = ? WHERE id = ?");
                    updateStatement2.setString(1,list.toString());
                    updateStatement2.setString(2,theUser.getId());
                    updateStatement2.executeUpdate();

                    PreparedStatement updateStatement3 = connection.prepareStatement("UPDATE user SET follower = ? WHERE id = ?");
                    updateStatement3.setInt(1,theUser.getFollowerNum()-1);
                    updateStatement3.setString(2,theUser.getId());
                    updateStatement3.executeUpdate();

                    //the user should be removed from the block followings
                    while (resultSetBlock.next()){
                        if(resultSetBlock.getString("id").equals(block.getId())){
                            int j;
                            String[] followings=resultSetUser.getString("followings").split("=");
                            ArrayList<String> list2 = new ArrayList<String>(Arrays.asList(followings));
                            for( j=0;j<list2.size();j++){
                                if(list2.equals(theUser.getId())){
                                    break;
                                }
                                j++;
                            }
                            list2.remove(j);
                            statement.executeUpdate("UPDATE user SET followings = '" +list2+ "' WHERE id = " + block.getId());
                            statement.executeUpdate("UPDATE user SET followingNum = '" +(block.getFollowingNum()-1)+ "' WHERE id = " + block.getId());
                            PreparedStatement updateStatement4 = connection.prepareStatement("UPDATE user SET followings = ? WHERE id = ?");
                            updateStatement4.setString(1,list2.toString());
                            updateStatement4.setString(2,block.getId());
                            updateStatement4.executeUpdate();

                            PreparedStatement updateStatement5 = connection.prepareStatement("UPDATE user SET following = ? WHERE id = ?");
                            updateStatement5.setInt(1,theUser.getFollowingNum()-1);
                            updateStatement5.setString(2,block.getId());
                            updateStatement5.executeUpdate();
                        }
                    }
                }
                else if(resultSetUser.getString("id").equals(theUser.getId()) && resultSetUser.getString("followings").contains(block.getId())){
                    //user is following block
                    //user should be removed from the block followers
                    while (resultSetBlock.next()){
                        if(resultSetBlock.getString("id").equals(block.getId())){
                            int i;
                            String[] follower=resultSetBlock.getString("followers").split("=");
                            ArrayList<String> list = new ArrayList<String>(Arrays.asList(follower));
                            for( i=0;i<list.size();i++){
                                if(list.equals(theUser.getId())){
                                    break;
                                }
                                i++;
                            }
                            list.remove(i);
                            PreparedStatement updateStatement6 = connection.prepareStatement("UPDATE user SET followers = ? WHERE id = ?");
                            updateStatement6.setString(1,list.toString());
                            updateStatement6.setString(2,block.getId());
                            updateStatement6.executeUpdate();

                            PreparedStatement updateStatement7 = connection.prepareStatement("UPDATE user SET follower = ? WHERE id = ?");
                            updateStatement7.setInt(1,block.getFollowerNum()-1);
                            updateStatement7.setString(2,block.getId());
                            updateStatement7.executeUpdate();
                        }
                    }

                    //block should be removed from the user followings
                    while (resultSetUser.next()){
                        if(resultSetUser.getString("id").equals(theUser.getId())){
                            int j;
                            String[] followings=resultSetUser.getString("followings").split("=");
                            ArrayList<String> list2 = new ArrayList<String>(Arrays.asList(followings));
                            for( j=0;j<list2.size();j++){
                                if(list2.equals(block.getId())){
                                    break;
                                }
                                j++;
                            }
                            list2.remove(j);

                            PreparedStatement updateStatement8 = connection.prepareStatement("UPDATE user SET followings = ? WHERE id = ?");
                            updateStatement8.setString(1,list2.toString());
                            updateStatement8.setString(2,theUser.getId());
                            updateStatement8.executeUpdate();

                            PreparedStatement updateStatement9 = connection.prepareStatement("UPDATE user SET following = ? WHERE id = ?");
                            updateStatement9.setInt(1,theUser.getFollowingNum()-1);
                            updateStatement9.setString(2,theUser.getId());
                            updateStatement9.executeUpdate();
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void unblock(User theUser,User unblock) {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSetUser = null;
        try {
            resultSetUser = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String respond;
        while (true){
            try {
                if (!resultSetUser.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSetUser.getString("id").equals(theUser.getId()) && !resultSetUser.getString("blacklist").contains(unblock.getId())){
                    respond="have-not-blocked";
                    out.writeObject(respond);
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSetUser.getString("id").equals(theUser.getId())  && resultSetUser.getString("blacklist").contains(unblock.getId())){
                    int i;
                    String[] blacklist=resultSetUser.getString("blacklist").split("=");
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(blacklist));
                    for( i=0;i<list.size();i++){
                        if(list.equals(unblock.getId())){
                            break;
                        }
                        i++;
                    }
                    list.remove(i);
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE  user SET blacklist = ? WHERE id = ?");
                    preparedStatement.setString(1,list.toString());
                    preparedStatement.setString(2,theUser.getId());
                    preparedStatement.executeUpdate();
                    respond="success";
                    out.writeObject(respond);
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void retweet(User theUser,Tweet theTweet)  {
        java.sql.Connection connection = Server.getConnection();
        try {
            Statement statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO Tweet(text, piclink, userId, likes, retweets, comments, date) VALUES (?,?,?,?,?,?,?)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement.setString(1,theTweet.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement.setString(2,theTweet.getPicLink());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement.setString(3,theUser.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement.setInt(4,0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement.setInt(5,0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement.setInt(6,0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement.setString(7,new Date().toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        PreparedStatement preparedStatement1 = null;
        try {
            preparedStatement1 = connection.prepareStatement("UPDATE Tweet SET retweets = ? WHERE text = ? AND userId = ?");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement1.setInt(1,theTweet.getRetweet()+1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement1.setString(2, theTweet.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement1.setString(3,theTweet.getUserId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement1.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        PreparedStatement preparedStatement2 = null;
        try {
            preparedStatement2 = connection.prepareStatement("UPDATE Tweet SET retweetIds = ? WHERE text = ? AND userId = ?");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement2.setString(1,theUser.getId()+"@");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement2.setString(2,theTweet.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement2.setString(3,theTweet.getUserId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement2.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String respond="success";
        try {
            out.writeObject(respond);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void addComment(User theUser,Tweet theTweet,String comment)  {
        java.sql.Connection connection = Server.getConnection();
        try {
            Statement statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("UPDATE Tweet SET comment = ? WHERE text = ? AND userId = ?");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement.setInt(1,theTweet.getComment()+1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement.setString(2,theTweet.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement.setString(3,theTweet.getUserId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String respond;
        PreparedStatement preparedStatement1 = null;
        try {
            preparedStatement1 = connection.prepareStatement("UPDATE Tweet SET comment = ? WHERE text = ? AND userId = ?");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement1.setString(1,"@"+theUser.getId()+"="+comment);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement1.setString(2,theTweet.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement1.setString(3,theTweet.getUserId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            preparedStatement1.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //@id=comment
        respond="success";
        try {
            out.writeObject(respond);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void showComments(Tweet tweet)  {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet= null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM Tweet");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if(resultSet.equals(tweet)){
                String str= null;
                try {
                    str = resultSet.getString("commentText");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                String[] arr = str.split("@");
                ArrayList<String> list = new ArrayList<>(Arrays.asList(arr));
                try {
                    out.writeObject(list);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
    }
    public static void checkLike(Tweet theTweet,User theUser){
        Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM Tweet");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String respond;
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString(1).equals(theTweet.getText())&&resultSet.getString("likesIds").contains(theUser.getId())){
                    respond="true";
                    out.writeObject(respond);
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        respond="false";
        try {
            out.writeObject(respond);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private static void checkRetweet(Tweet theTweet,User theUser){
        Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM Tweet");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String respond;
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                System.out.println(resultSet.getString(12));
                if(resultSet.getString("text").equals(theTweet.getText())&&resultSet.getString(12).contains(theUser.getId())){
                    respond="true";
                    out.writeObject(respond);
                    return;
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        respond="false";
        try {
            out.writeObject(respond);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private static void followerList(User theUser){
        java.sql.Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ArrayList<User> res=new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString("followings").equals(theUser.getId())){
                    User user = new User(resultSet.getString("id"),resultSet.getString("firstName"),
                            resultSet.getString("lastName"),resultSet.getString("email"),resultSet.getString("phoneNumber"),
                            resultSet.getString("password"),resultSet.getString("country"),resultSet.getString("birthDate"),
                            resultSet.getString("registerDate"),resultSet.getString("header"),resultSet.getString("profile"),
                            resultSet.getString("bio"),resultSet.getString("location"),resultSet.getString("web"),resultSet.getString("followers"),
                            resultSet.getString("followings"),resultSet.getInt("follower"),resultSet
                            .getInt("following"),resultSet.getString("blacklist"));
                    res.add(user);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            out.writeObject(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void followingList(User theUser){
        java.sql.Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ArrayList<User> res=new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString("followers").contains(theUser.getId())){
                    User user = new User(resultSet.getString("id"),resultSet.getString("firstName"),
                            resultSet.getString("lastName"),resultSet.getString("email"),resultSet.getString("phoneNumber"),
                            resultSet.getString("password"),resultSet.getString("country"),resultSet.getString("birthDate"),
                            resultSet.getString("registerDate"),resultSet.getString("header"),resultSet.getString("profile"),
                            resultSet.getString("bio"),resultSet.getString("location"),resultSet.getString("web"),resultSet.getString("followers"),
                            resultSet.getString("followings"),resultSet.getInt("follower"),resultSet
                            .getInt("following"),resultSet.getString("blacklist"));
                    res.add(user);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            out.writeObject(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


}
    private static void checkFollow(User one,User two){
        java.sql.Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String respond;
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString(1).equals(one.getId())&&resultSet.getString(17).contains(two.getId())){
                    respond="true";
                    out.writeObject(respond);
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        respond="false";
        try {
            out.writeObject(respond);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private static void checkBlock(User one,User two){
        java.sql.Connection connection = Server.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String respond;
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString(1).equals(one.getId())&&resultSet.getString(20).contains(two.getId())){
                    respond="true";
                    out.writeObject(respond);
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        respond="false";
        try {
            out.writeObject(respond);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}