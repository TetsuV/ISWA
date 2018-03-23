package com.dbcommand;

import com.usr.info.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Connect
{
    private static String err_msg;

    static private Connection Connexion()
    {
        Connection con = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("JDBC driver est introuvable");
        }
        try
        {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbiswa" ,"root", "root");
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return (con);
    }

    public static List<String[]> getCours_to_Database(String id)
    {
        List<String[]> mat_pls = new ArrayList<String[]>();
        try
        {
            Connection con = Connexion();
            User as = new User();
            id = id.replace("id=", "");

            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE UserID=" + id);
            if(rs.next())
            {
                as.ID = Integer.parseInt(rs.getString("UserID"));
                as.Classe = rs.getString("Classe");
            }
            rs = statement.executeQuery("SELECT * FROM user_level_per_subject WHERE UserID=" + id);
            while (rs.next())
            {
                if (Integer.parseInt(rs.getString("UserLevel")) < 65)
                {
                    String[] mylist = new String[2];
                    mylist[1] = rs.getString("topicID");
                    mylist[0] = rs.getString("subjectID");
                    mat_pls.add(mylist);
                    System.out.println("tet " + mat_pls.get(0)[0]);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Err in getCours -> Connect.java. @" + e.toString());
        }
        return mat_pls;
    }

    public static String Connect_to_Database(String mat, String cla, String sub)
    {
     /*   try
        {
            Class.forName("com.mysql.jdbc.Driver");



            Connection con = DriverManager.getConnection(host, uName, uPass);

            Statement statement = con.createStatement();
            statement.execute("USE cours");
            ResultSet resultat = statement.executeQuery("SELECT n FROM cours WHERE Matiere=" + mat + " AND Sujet=" + cla + ";");
            resultat.next();
            int index = resultat.getInt("n");

            return ("I found this one : " + sub);
        }
        catch (Exception e)
        {
            err_msg = "Error while connecting to DB : " + e.getMessage(); // + " Param was : " + mat + " && " + cla;
            System.out.println(err_msg);
            return (err_msg);
        }*/
     return "";
    }


}
