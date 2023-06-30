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
                        connection = DriverManager.getConnection("jdbc:sqlite:H:\\New folder\\demo1\\jdbc2.db");
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
    public void run() {
        java.sql.Connection connection = Server.getConnection();
        try {
            
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

        } catch (  IOException e) {
            throw new RuntimeException(e);
        }
        try {
            String command;
            while(!(command = (String) in.readObject()).equals("exit")) {
                if (command.equals("sign-up")) {
                    System.out.println("sign up done");
                    signUpServer((User)in.readObject());
                }
                else if (command.equals("sign-in")) {
                    System.out.println("sign in done");
                    signInServer((User)in.readObject());
                }
                else if(command.equals("get-user")){
                    getUser((String) in.readObject());
                }
                else if(command.equals("get-profile")){
                    User x = (User)in.readObject();
                    User y = (User)in.readObject();
                    getProfile(x,y);
                }
                else if(command.equals("edit-profile")){
                    User x=(User) in.readObject();
                    String y=(String) in.readObject();
                    editProfile(x,y);
                }
                else if(command.equals("edit-header")){
                    User x=(User) in.readObject();
                    String y=(String) in.readObject();
                    editHeader(x,y);
                }
                else if(command.equals("edit-bio")){
                    User x=(User) in.readObject();
                    String y=(String) in.readObject();
                    editProf(x,y,1);
                }
                else if(command.equals("edit-location")){
                    User x=(User) in.readObject();
                    String y=(String) in.readObject();
                    editProf(x,y,2);
                }
                else if(command.equals("edit-web")){
                    User x=(User) in.readObject();
                    String y=(String) in.readObject();
                    editProf(x,y,3);
                }
                else if(command.equals("follow")){
                    User x=(User) in.readObject();
                    String y=(String) in.readObject();
                    follow(x,y);
                }
                else if(command.equals("search")){
                    String x=(String) in.readObject();
                    search(x);
                }
                else if (command.equals("unfollow")){
                    User x=(User) in.readObject();
                    String y=(String) in.readObject();
                    unfollow(x,y);
                }
                else if ((command.equals("new-tweet"))){
                    Tweet x=(Tweet) in.readObject();
                    newTweet(x);
                }
                else if(command.equals("timeline")){
                    User x=(User) in.readObject();
                    timeline(x);
                }
                else if(command.equals("like")){
                    User x=(User) in.readObject();
                    Tweet y=(Tweet) in.readObject();
                    like(x,y);
                }
                else if(command.equals("hashtag")){
                    String x=(String) in.readObject();
                    searchHashtag(x);
                }
                else if(command.equals("block")){
                    User x=(User) in.readObject();
                    User y=(User) in.readObject();
                    block(x,y);
                }
                else if(command.equals("unblock")){
                    User x=(User) in.readObject();
                    User y=(User) in.readObject();
                    unblock(x,y);
                }
                else if (command.equals("retweet")){
                    User x=(User) in.readObject();
                    Tweet y=(Tweet) in.readObject();
                    retweet(x,y);
                }
                else if(command.equals("comment")){
                    //reply
                    User x=(User) in.readObject();
                    Tweet y=(Tweet) in.readObject();
                    String z=(String) in.readObject();
                    addComment(x,y,z);
                }
                else if(command.equals("show-comments")){
                    Tweet x=(Tweet) in.readObject();
                    showComments(x);
                }
            }
        } catch (IOException | ClassNotFoundException | SQLException | InterruptedException | ParseException e) {
            System.out.println("client disconnected.");
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
        // try {
           // connection.close();
       // } catch (SQLException e) {
         //   System.out.println("nane data base jendas!!!!!!");
        //}
        try {
            statement.close();
            resultSet.close();

            //connection = DriverManager.getConnection("jdbc:sqlite:H:\\New folder\\demo1\\jdbc.db");
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO user(id,firstName,lastName,email,phoneNumber,password,country,birthDate,registerDate) VALUES (?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1,theUser.getId());
            pstmt.setString(2,theUser.getFirstName());
            pstmt.setString(3,theUser.getLastName());
            pstmt.setString(4,theUser.getEmail());
            pstmt.setString(5,theUser.getPhoneNumber());
            pstmt.setString(6,theUser.getPassword());
            pstmt.setString(7,theUser.getCountry());
            pstmt.setString(8,theUser.getBirthDate());
            pstmt.setString(9,theUser.getRegisterDate().toString());
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
            resultSet = statement.executeQuery("SELECT id,firstName,lastName,email,phoneNumber,password,country,birthDate,registerDate,lastUpdate,profile,header,bio,location,web,followers,followings,follower,following,blacklist from user");
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
                            ,resultSet.getString("password"),resultSet.getString("country"),resultSet.getString("birthDate"));
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
    public static void editProfile(User theUser, String prof) {
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

        while (true) {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if (resultSet.getString("id").equals(theUser.getId())) {
                    PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET profilePicture = ? WHERE id = ?");
                    updateStatement.setString(1, prof);
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
    public static void editHeader(User theUser,String header)  {
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
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(resultSet.getString("id").equals(theUser.getId())){
                    PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET header = ? WHERE id = ?");
                    updateStatement.setString(1,header);
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
    public static void editProf(User theUser,String text,int com) throws SQLException, IOException {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
        if(com==1){
            //bio
            while (resultSet.next()){
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
            }
        }
        else if(com==2){
            //loc
            while (resultSet.next()){
                if(resultSet.getString(1).equals(theUser.getId())){
                    PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET location = ? WHERE id = ?");
                    updateStatement.setString(1,text);
                    updateStatement.setString(2,theUser.getId());
                    updateStatement.executeUpdate();
                    Profile theProfile = new Profile(resultSet.getString("profile"), resultSet.getString("header"),
                            resultSet.getString("bio"), resultSet.getString("location"), resultSet.getString("web"),
                            resultSet.getInt("follower"), resultSet.getInt("following"));
                    out.writeObject(theProfile);
                    return;
                }
            }
        }
        else if(com==3){
            //web
            while (resultSet.next()){
                if(resultSet.getString(1).equals(theUser.getId())){
                    PreparedStatement updateStatement = connection.prepareStatement("UPDATE user SET web = ? WHERE id = ?");
                    updateStatement.setString(1,text);
                    updateStatement.setString(2,theUser.getId());
                    updateStatement.executeUpdate();
                    Profile theProfile = new Profile(resultSet.getString("profile"), resultSet.getString("header"),
                            resultSet.getString("bio"), resultSet.getString("location"), resultSet.getString("web"),
                            resultSet.getInt("follower"), resultSet.getInt("following"));
                    out.writeObject(theProfile);
                    return;
                }
            }
        }
    }
    public static void follow(User theUser,String followingId) throws SQLException, IOException {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
        String respond;
        while (resultSet.next()){
            if(resultSet.getString(1).equals(theUser.getId())){
                if(resultSet.getString(17).contains(followingId)){
                    respond="already-followed";
                    out.writeObject(respond);
                    return;
                }
            }
            else{
                statement.executeUpdate("UPDATE user SET followings = '" + theUser.getFollowing()+"="+followingId + "' WHERE id = " + theUser.getId());
                statement.executeUpdate("UPDATE user SET followingNum = '" + (theUser.getFollowingNum()+1) + "' WHERE id = " + theUser.getId());
                while (resultSet.next()){
                    if(resultSet.getString(1).equals(followingId)){
                        statement.executeUpdate("UPDATE user SET followers = '" + resultSet.getString(16)+"="+theUser.getId() + "' WHERE id = " + followingId);
                        statement.executeUpdate("UPDATE user SET followerNum = '" + (Integer.parseInt(resultSet.getString(18))+1)+ "' WHERE id = " + followingId);
                        respond="success";
                        out.writeObject(respond);
                        return;
                    }
                }
            }
        }
    }
    public static void search(String text) throws SQLException, IOException, InterruptedException {
        ArrayList <User> res=new ArrayList<>();
        java.sql.Connection connection = Server.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
        String respond;
        while (resultSet.next()){
            if(resultSet.getString(1).contains(text)||resultSet.getString(2).contains(text)
                    ||resultSet.getString(3).contains(text)){
                User newUser = new User(resultSet.getString(1),resultSet.getString(2),
                        resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),
                        resultSet.getString(6),resultSet.getString(7),resultSet.getString(8));
                res.add(newUser);
            }
        }
        if(res.isEmpty()){
            respond="not-found";
            out.writeObject(respond);
        }
        else{
            respond="found";
            out.writeObject(respond);
            Thread.sleep(50);
            out.writeObject(res);
        }
    }
    public static void unfollow(User theUser,String followingId) throws SQLException, IOException {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
        String respond;
        while (resultSet.next()){
            if(resultSet.getString(1).equals(theUser.getId())){
                if(!resultSet.getString(17).contains(followingId)){
                    respond="is not followed";
                    out.writeObject(respond);
                    return;
                }
                else {
                    int i;
                    String[] following=resultSet.getString(17).split("=");
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(following));
                    for( i=0;i<list.size();i++){
                        if(list.equals(followingId)){
                            break;
                        }
                        i++;
                    }
                    list.remove(i);
                    statement.executeUpdate("UPDATE user SET followings = '" +list+ "' WHERE id = " + theUser.getId());
                    statement.executeUpdate("UPDATE user SET followingNum = '" + (theUser.getFollowingNum()-1) + "' WHERE id = " + theUser.getId());
                }
            }
        }
        while (resultSet.next()){
            if(resultSet.getString(1).equals(followingId)){
                int i;
                String[] follower=resultSet.getString(16).split("=");
                ArrayList<String> list = new ArrayList<>(Arrays.asList(follower));
                for(i=0;i<list.size();i++){
                    if(list.equals(theUser.getId())){
                        break;
                    }
                    i++;
                }
                list.remove(i);
                statement.executeUpdate("UPDATE user SET followers = '" +list+ "' WHERE id = " + followingId);
                statement.executeUpdate("UPDATE user SET followerNum = '" +(Integer.parseInt(resultSet.getString(18))-1)+ "' WHERE id = " + followingId);
                respond="success";
                out.writeObject(respond);
                return;
            }
        }
    }
    public static void newTweet(Tweet tweet) throws SQLException, IOException {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO Tweet(text, picture, userid, like, retweet, comment, date) "+"VALUES "
        +tweet.getText()+tweet.getPicLink()+tweet.getUserId()+tweet.getLikes()+tweet.getRetweet()+tweet.getComment()+tweet.getDate());
        String respond="success";
        out.writeObject(respond);
    }
    public static void timeline(User theUser) throws SQLException, ParseException, IOException {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
        ResultSet resultSetTweet = statement.executeQuery("SELECT * FROM Tweet");
        ArrayList <Tweet> res=new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //check followings
        while (resultSet.next()){
            if(resultSet.getString(1).equals(theUser.getId())){
                while (resultSetTweet.next()){
                    if(resultSet.getString(17).contains(resultSetTweet.getString(3))){
                        Tweet theTweet = new Tweet(resultSetTweet.getString(1),resultSet.getString(2),
                                resultSet.getString(3),Integer.parseInt(resultSet.getString(4)),Integer.parseInt(resultSet.getString(5)),
                               Integer.parseInt( resultSet.getString(6)),format.parse( resultSet.getString(7)) ,Integer.parseInt(resultSet.getString(8)) );
                        res.add(theTweet);
                    }
                }
            }
        }

        //check favstars and blocks
        while (resultSetTweet.next()){
            if(resultSetTweet.getString(8).equals("1")){
                Tweet theTweet = new Tweet(resultSetTweet.getString(1),resultSet.getString(2),
                        resultSet.getString(3),Integer.parseInt(resultSet.getString(4)),Integer.parseInt(resultSet.getString(5)),
                        Integer.parseInt( resultSet.getString(6)),format.parse( resultSet.getString(7)) ,Integer.parseInt(resultSet.getString(8)) );
                if(!res.contains(theTweet)){
                    while (resultSet.next()){
                        if(resultSet.getString(1).equals(theUser.getId())){
                            if(!resultSet.getString(20).contains(theTweet.getUserId())){
                            res.add(theTweet);
                            }
                        }
                    }
                }
            }
        }
        out.writeObject(res);
    }
    public static void like(User theUser,Tweet theTweet) throws SQLException, IOException {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
        ResultSet resultSetTweet = statement.executeQuery("SELECT * FROM Tweet");
        String respond;
        while (resultSetTweet.next()){
            if(resultSetTweet.equals(theTweet) && resultSetTweet.getString(9).contains(theUser.getId())){
                respond="already-liked";
                out.writeObject(respond);
            }
            else if(resultSetTweet.equals(theTweet) && ! resultSetTweet.getString(9).contains(theUser.getId())){
                respond="success";
                statement.executeUpdate("UPDATE Tweet SET likedIds = '" +theTweet.getLikesIds()+"="+theUser.getId()+ "'WHERE text = " + theTweet.getText()+ "' AND userid = '"+theTweet.getUserId());
                statement.executeUpdate("UPDATE Tweet SET like = '" +(Integer.parseInt(resultSetTweet.getString(4))+1)+"'WHERE text = " + theTweet.getText()+ "' AND userid = '"+theTweet.getUserId());
                if(Integer.parseInt(resultSetTweet.getString(4))==10){
                    statement.executeUpdate("UPDATE Tweet SET isFavStar = '" +(theTweet.getIsFavStar()+1)+ "'WHERE text = " + theTweet.getText()+ "' AND userid = '"+theTweet.getUserId());
                }
                out.writeObject(respond);
            }
        }
    }
    public static void searchHashtag(String hashtag) throws SQLException, ParseException, IOException {
        java.sql.Connection connection = Server.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Tweet");
        ArrayList<Tweet> res=new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        while (resultSet.next()){
            if(resultSet.getString(1).contains("#"+hashtag)){
                Tweet theTweet = new Tweet(resultSet.getString(1),resultSet.getString(2),
                        resultSet.getString(3),Integer.parseInt(resultSet.getString(4)),
                        Integer.parseInt(resultSet.getString(5)),
                        Integer.parseInt( resultSet.getString(6)),format.parse( resultSet.getString(7))
                        ,Integer.parseInt(resultSet.getString(8)) );
                res.add(theTweet);
            }
        }
        out.writeObject(res);
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