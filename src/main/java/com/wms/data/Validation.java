package com.wms.data;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.*;

class HashedPassword {
    public static String getHashedPassword(String password) throws Exception{
        int iterations=1000;
        char[] chars=password.toCharArray();
        byte[] salt=getSalt();

        PBEKeySpec spec=new PBEKeySpec(chars,salt,iterations,64*8);
        SecretKeyFactory skf=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }
    public static boolean validatePassword(String originalPassword, String storedPassword)
            throws Exception
    {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);

        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(),
                salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }
    private static byte[] getSalt() throws Exception{
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
    private static String toHex(byte[] array) throws Exception
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
    private static byte[] fromHex(String hex) throws Exception
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

}



public class Validation {
    static class DBConnection{
        final static String URL = "jdbc:mysql://localhost:3306/wms";
        final static String USER = "wms_admin";
        final static String PASS = "P@ssword12";
        public static Connection connect() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASS);
        }
    }
    public static User getUserFromDB(String username) throws Exception{
        String SQL="SELECT * FROM wms.users WHERE username=?";
        User user=null;
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                user = new User(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("display_name"),
                        Position.valueOf(rs.getString("position")));
            }
        }catch(Exception e){
            throw new Exception("Something Went Wrong");
        }
        return user;
    }
    public static void loginValid(String username,String password) throws Exception{
        if(username.isEmpty()) throw new Exception("Username Field Mustn't be Empty");
        if(password.isEmpty()) throw new Exception("Password Field Mustn't be Empty");
        String SQL = "SELECT * FROM wms.users WHERE username=?";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery() ;
            if(rs.next()){
                if(!HashedPassword.validatePassword(password,rs.getNString("password"))){
                    throw new Exception("Incorrect Password");
                }
                if(rs.getString("status").equals("PENDING")){
                    throw new Exception("Account needs Admin Approval");
                }
                else if(rs.getString("status").equals("DENIED")){
                    throw new Exception("This Account was Denied By Admin");
                }
            }else{
                throw new Exception("This Username Doesn't Exist");
            }
        }catch(SQLException e){
            throw new Exception("Something Went Wrong");
        }
        catch(Exception e){
            throw new Exception(e.getMessage());
        }

    }
    private static void registerToDB(String username,String hashedPassword,
                                     String displayName,Position position) throws Exception{
        String SQL = "INSERT INTO `wms`.`users` (`username`,`password`,`display_name`,`position`) VALUES (?,?,?,?);";
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1,username);
            ps.setString(2,hashedPassword);
            ps.setString(3,displayName);
            ps.setInt(4,position.ordinal()+1);
            ps.execute();
            ps.close();
        }catch(Exception e){
            throw new Exception("Something Went Wrong!");
        }
    }
    public static void registerValid(String username,String password,
                                     String confirmPassword,String displayName,
                                     Position position) throws Exception{
        if(username.isEmpty()) throw new Exception("Username Field is Empty");
        if(password.isEmpty()) throw new Exception("Password Field is Empty");
        if(confirmPassword.isEmpty()) throw new Exception("Confirm Password Field is Empty");
        if(displayName.isEmpty()) throw new Exception("Name Field is Empty");
        if(username.length()<6 || username.length()>50) throw new Exception("Username Must be Between 6 and 50 Characters");
        if(password.length()<6 || password.length()>50) throw new Exception("Password Must be Between 6 and 50 Characters");
        if(!password.equals(confirmPassword)) throw new Exception("Password and Confirm Password Fields Don't Match");
        if(usernameAlreadyExists(username)) throw new Exception("This Username Already Exists");
        registerToDB(username,HashedPassword.getHashedPassword(password),displayName,position);
    }

    private static boolean usernameAlreadyExists(String username) {
        String SQL="SELECT * FROM wms.users WHERE username=?";
        boolean userExists=false;
        try(Connection conn = DBConnection.connect()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                userExists=true;
            }
        }catch(Exception e){
            userExists=true;
        }
        return userExists;
    }
}
