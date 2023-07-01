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
                        connection = DriverManager.getConnection("jdbc:sqlite:H:\\New folder\\demo1\\jdbc.db");
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
                if (!!(command = (String) in.readObject()).equals("exit")) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (command.equals("sign-up")) {
                System.out.println("sign up done");
                try {
                    signUpServer((User)in.readObject());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (command.equals("sign-in")) {
                System.out.println("sign in done");
                try {
                    signInServer((User)in.readObject());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(command.equals("get-user")){
                try {
                    getUser((String) in.readObject());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(command.equals("get-profile")){
                User x = null;
                try {
                    x = (User)in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                User y = null;
                try {
                    y = (User)in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                getProfile(x,y);
            }
            else if(command.equals("edit-profile")){
                User x= null;
                try {
                    x = (User) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                String y= null;
                try {
                    y = (String) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                editProf(x,y,4);
            }
            else if(command.equals("edit-header")){
                User x= null;
                try {
                    x = (User) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                String y= null;
                try {
                    y = (String) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                editProf(x,y,5);
            }
            else if(command.equals("edit-bio")){
                User x= null;
                try {
                    x = (User) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                String y= null;
                try {
                    y = (String) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                editProf(x,y,1);
            }
            else if(command.equals("edit-location")){
                User x= null;
                try {
                    x = (User) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                String y= null;
                try {
                    y = (String) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                editProf(x,y,2);
            }
            else if(command.equals("edit-web")){
                User x= null;
                try {
                    x = (User) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                String y= null;
                try {
                    y = (String) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                editProf(x,y,3);
            }
            else if(command.equals("follow")){
                User x= null;
                try {
                    x = (User) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                String y= null;
                try {
                    y = (String) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                follow(x,y);
            }
            else if(command.equals("search")){
                String x= null;
                try {
                    x = (String) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                search(x);
            }
            else if (command.equals("unfollow")){
                User x= null;
                try {
                    x = (User) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                String y= null;
                try {
                    y = (String) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                unfollow(x,y);
            }
            else if ((command.equals("new-tweet"))){
                Tweet x= null;
                try {
                    x = (Tweet) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                newTweet(x);
            }
            else if(command.equals("timeline")){
                User x= null;
                try {
                    x = (User) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                timeline(x);
            }
            else if(command.equals("like")){
                User x= null;
                try {
                    x = (User) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Tweet y= null;
                try {
                    y = (Tweet) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                like(x,y);
            }
            else if(command.equals("hashtag")){
                String x= null;
                try {
                    x = (String) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                searchHashtag(x);
            }
            else if(command.equals("block")){
                User x= null;
                try {
                    x = (User) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                User y= null;
                try {
                    y = (User) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                try {
                    block(x,y);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(command.equals("unblock")){
                User x= null;
                try {
                    x = (User) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                User y= null;
                try {
                    y = (User) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                try {
                    unblock(x,y);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (command.equals("retweet")){
                User x= null;
                try {
                    x = (User) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Tweet y= null;
                try {
                    y = (Tweet) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                try {
                    retweet(x,y);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(command.equals("comment")){
                //reply
                User x= null;
                try {
                    x = (User) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Tweet y= null;
                try {
                    y = (Tweet) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                String z= null;
                try {
                    z = (String) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                try {
                    addComment(x,y,z);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(command.equals("show-comments")){
                Tweet x= null;
                try {
                    x = (Tweet) in.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                try {
                    showComments(x);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
                else if(resultSet.getString("email").equals(theUser.getEmail())){
                    respond = "duplicate-email";
                    out.writeObject(respond);
                    return;
                }
                else if(resultSet.getString("phoneNumber").equals(theUser.getPhoneNumber())){
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
            System.out.println(respond);
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
                    User theUser=new User(resultSet.getString("id"),resultSet.getString("firstName"),
                            resultSet.getString("lastName"),resultSet.getString("email"),resultSet.getString("phoneNumber")
                            ,resultSet.getString("password"),resultSet.getString("country"),resultSet.getString("birthDate"),
                            resultSet.getString("header"),resultSet.getString("profile"));
                    out.writeObject(theUser);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
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
                    System.out.println("user-find");
                    System.out.println(resultSet1.getString("blacklist"));
                    if(resultSet1.getString("blacklist")==null || !resultSet1.getString("blacklist").contains(wanted.getId())){
                        System.out.println("oomad");
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
                        out.writeObject(theProfile);
                        return;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
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
                        out.writeObject(theProfile);
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
                        out.writeObject(theProfile);
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

                        out.writeObject(theProfile);
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

                        out.writeObject(theProfile);
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
                pstmt = connection.prepareStatement("INSERT INTO Tweet(text,piclink,userId,likes,retweets,comments,date,isFavStar,likesIds) VALUES (?,?,?,?,?,?,?,?,?)");
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

        //check followings
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString("id").equals(theUser.getId())){
                    while (resultSetTweet.next()){
                        if(resultSet.getString("followings").contains(resultSetTweet.getString("userId"))){
                            Tweet theTweet = new Tweet(resultSetTweet.getString("text"),resultSet.getString("piclink"),
                                    resultSet.getString("userId"),resultSet.getInt("likes"),resultSet.getInt("retweets"),
                                  resultSet.getInt("comments"),format.parse( resultSet.getString("date")) ,resultSet.getInt("isFavStar") );
                            res.add(theTweet);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
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
                            resultSet.getInt("comments"),format.parse( resultSet.getString("date")) ,resultSet.getInt("isFavStar") );
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
            } catch (ParseException e) {
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
        while (true){
            try {
                if (!resultSetTweet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSetTweet.equals(theTweet) && resultSetTweet.getString("likesIds").contains(theUser.getId())){
                    respond="already-liked";
                    out.writeObject(respond);
                }
                else if(resultSetTweet.equals(theTweet) && ! resultSetTweet.getString("likesIds").contains(theUser.getId())){
                    respond="success";
                    PreparedStatement updateStatement = connection.prepareStatement("UPDATE Tweet SET likesIds = ? WHERE text = ? AND userid = ?");
                    updateStatement.setString(1,theTweet.getLikesIds()+"="+theUser.getId());
                    updateStatement.setString(2,theTweet.getText());
                    updateStatement.setString(3,theTweet.getUserId());
                    updateStatement.executeUpdate();

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
                            resultSet.getInt("comments"),format.parse( resultSet.getString("date")) ,resultSet.getInt("isFavStar") );
                    res.add(theTweet);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            out.writeObject(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void block(User theUser,User block) throws SQLException, IOException {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSetUser = statement.executeQuery("SELECT * FROM user");
        ResultSet resultSetBlock = statement.executeQuery("SELECT * FROM user");
        String respond ;
        while (resultSetUser.next()){
            if(resultSetUser.getString(1).equals(theUser.getId()) &&
                    resultSetUser.getString(20).contains(block.getId())){
                respond="already-blocked";
                out.writeObject(respond);
                return;
            }
            else if(resultSetUser.getString(1).equals(theUser.getId())){
                statement.executeUpdate("UPDATE user SET blacklist = '" +(theUser.getBlacklist())+"="+block.getId()+ "' WHERE id = " + theUser.getId());
                respond="success";
                out.writeObject(respond);
            }
        }
        while (resultSetUser.next()){
            if(resultSetUser.getString(1).equals(theUser.getId()) && resultSetUser.getString(16).contains(block.getId())){
                //block is following the user
                //block should be removed from the user followers
                int i;
                String[] follower=resultSetUser.getString(16).split("=");
                ArrayList<String> list = new ArrayList<String>(Arrays.asList(follower));
                for( i=0;i<list.size();i++){
                    if(list.equals(block.getId())){
                        break;
                    }
                    i++;
                }
                list.remove(i);
                statement.executeUpdate("UPDATE user SET followers = '" +list+ "' WHERE id = " + theUser.getId());
                statement.executeUpdate("UPDATE user SET followerNum = '" +(theUser.getFollowerNum()-1)+ "' WHERE id = " + theUser.getId());

                //the user should be removed from the block followings
                while (resultSetBlock.next()){
                    if(resultSetBlock.getString(1).equals(block.getId())){
                        int j;
                        String[] followings=resultSetUser.getString(17).split("=");
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
                    }
                }
            }
            else if(resultSetUser.getString(1).equals(theUser.getId()) && resultSetUser.getString(17).contains(block.getId())){
                //user is following block
                //user should be removed from the block followers
                while (resultSetBlock.next()){
                    if(resultSetBlock.getString(1).equals(block.getId())){
                        int i;
                        String[] follower=resultSetBlock.getString(16).split("=");
                        ArrayList<String> list = new ArrayList<String>(Arrays.asList(follower));
                        for( i=0;i<list.size();i++){
                            if(list.equals(theUser.getId())){
                                break;
                            }
                            i++;
                        }
                        list.remove(i);
                        statement.executeUpdate("UPDATE user SET followers = '" +list+ "' WHERE id = " + block.getId());
                        statement.executeUpdate("UPDATE user SET followerNum = '" +(block.getFollowerNum()-1)+ "' WHERE id = " + block.getId());
                    }
                }

                //block should be removed from the user followings
                while (resultSetUser.next()){
                    if(resultSetUser.getString(1).equals(theUser.getId())){
                        int j;
                        String[] followings=resultSetUser.getString(17).split("=");
                        ArrayList<String> list2 = new ArrayList<String>(Arrays.asList(followings));
                        for( j=0;j<list2.size();j++){
                            if(list2.equals(block.getId())){
                                break;
                            }
                            j++;
                        }
                        list2.remove(j);
                        statement.executeUpdate("UPDATE user SET followings = '" +list2+ "' WHERE id = " + theUser.getId());
                        statement.executeUpdate("UPDATE user SET followingNum = '" +(theUser.getFollowingNum()-1)+ "' WHERE id = " + theUser.getId());
                    }
                }
            }
        }
    }
    public static void unblock(User theUser,User unblock) throws SQLException, IOException {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSetUser = statement.executeQuery("SELECT * FROM user");
        String respond;
        while (resultSetUser.next()){
            if(resultSetUser.getString(1).equals(theUser.getId()) && !resultSetUser.getString(20).contains(unblock.getId())){
                respond="have-not-blocked";
                out.writeObject(respond);
                return;
            }
            if(resultSetUser.getString(1).equals(theUser.getId())  && resultSetUser.getString(20).contains(unblock.getId())){
                int i;
                String[] blacklist=resultSetUser.getString(20).split("=");
                ArrayList<String> list = new ArrayList<String>(Arrays.asList(blacklist));
                for( i=0;i<list.size();i++){
                    if(list.equals(unblock.getId())){
                        break;
                    }
                    i++;
                }
                list.remove(i);
                statement.executeUpdate("UPDATE user SET blacklist = '" +list+ "' WHERE id = " + theUser.getId());
                respond="success";
                out.writeObject(respond);
                return;
            }
        }
    }
    public static void retweet(User theUser,Tweet theTweet) throws SQLException, IOException {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO Tweet(text, picture, userid, like, retweet, comment, date) "+"VALUES "
                +theTweet.getText()+theTweet.getPicLink()+theUser.getId()+theTweet.getLikes()+theTweet.getRetweet()+theTweet.getComment()+new Date());
        statement.executeUpdate("UPDATE Tweet SET retweet = '" +(theTweet.getRetweet()+1)+ "'WHERE text = " + theTweet.getText()+ "' AND userid = '"+theTweet.getUserId());
        String respond="success";
        out.writeObject(respond);
    }
    public static void addComment(User theUser,Tweet theTweet,String comment) throws SQLException, IOException {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("UPDATE Tweet SET comment = '" +(theTweet.getComment()+1)+ "'WHERE text = " + theTweet.getText()+ "' AND userid = '"+theTweet.getUserId());
        String respond;
        statement.executeUpdate("UPDATE Tweet SET comments = '" +"@"+theUser.getId()+"="+comment+ "'WHERE text = " + theTweet.getText()+ "' AND userid = '"+theTweet.getUserId());
        //@id=comment
        statement.executeUpdate("UPDATE Tweet SET comment = '" +(theTweet.getComment()+1)+ "'WHERE text = " + theTweet.getText()+ "' AND userid = '"+theTweet.getUserId());
        respond="success";
        out.writeObject(respond);
    }
    public static void showComments(Tweet tweet) throws SQLException, IOException {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet= statement.executeQuery("SELECT * FROM Tweet");
        while (resultSet.next()){
            if(resultSet.equals(tweet)){
                String str=resultSet.getString(10);
                String[] arr = str.split("@");
                ArrayList<String> list = new ArrayList<>(Arrays.asList(arr));
                out.writeObject(list);
                return;
            }
        }
    }

}