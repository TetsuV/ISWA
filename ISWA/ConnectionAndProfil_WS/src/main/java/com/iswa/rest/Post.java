package com.iswa.rest;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

@Path("/post")
public class Post
{
    String e_id;

    private String getFileWithUtil(String fileName) {

        String result = "";

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }



    public Connection Connexion()
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

    public String readFile(String filename)
    {
        String content = null;
        File file = new File(filename); // For example, foo.txt
        FileReader reader = null;
        try
        {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return content;
    }

    @POST
    @Path("/GetInfo")
    public Response GetInfo(String Valus)
    {
        Connection con = Connexion();
        try
        {
            try
            {
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(Valus);
                String name = json.get("Name").toString();
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM users");
                while (rs.next())
                {
                    System.out.println(rs.getString("Name"));
                    if (name.equals(rs.getString("Name")) == true)
                    {
                        //con.close();
                        parsing();
                        String everything = getFileWithUtil("results/"+rs.getString("Path")+".txt");
                        return Response.status(200).entity(everything).build();
                    }
                }
                rs.close();
                statement.close();
                con.close();
            }
            catch(org.json.simple.parser.ParseException t)
            {
                System.out.println("Err in parserExc : + " + t.toString());
            }
        }
        catch (SQLException e)
        {
            System.out.println("Err in SQLExc : + " + e.toString());
        }
        return Response.status(401).entity("Music not found").build();
    }


    @POST
    @Path("/connexion")
    public Response connexion(String Valus)
    {
        Connection con = Connexion();
        try
        {
            try
            {
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(Valus);
                String Email = json.get("Email").toString();
                String Mdp = json.get("Mdp").toString();
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM users");
                while (rs.next())
                {
                    if (Email.equals(rs.getString("email_adresse")) == true && Mdp.equals(rs.getString("password")) == true)
                    {
                        //con.close();
                        return Response.status(200).entity(rs.getString("UserID")).build();
                    }
                }
                rs.close();
                statement.close();
                con.close();
            }
            catch(org.json.simple.parser.ParseException t)
            {
                System.out.println("Err in parserExc : + " + t.toString());
            }
        }
        catch (SQLException e)
        {
            System.out.println("Err in SQLExc : + " + e.toString());

        }
        return Response.status(401).entity("Bad users infos").build();
    }

    @POST
    @Path("/CreationProfil/eleve")
    public Response CreationProfil(String Param)
    {
        Connection con = Connexion();
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(Param);
            String Email = json.get("Email").toString();
            String Mdp = json.get("Mdp").toString();
            String UserName = json.get("UserName").toString();
            String UserLastName = json.get("UserLastName").toString();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM users");
            while (rs.next())
            {
                if (Email.equals(rs.getString("email_adresse")) == true)
                {
                    con.close();
                    return Response.status(401).entity("Email deja utilie").build();
                }
            }
            String query = "INSERT INTO `users`(`email_adresse`, `password`, `Nom`, `Prenom`) VALUES (";
            query += "'" + Email+"', '" + Mdp + "', '" + UserName + "', '" + UserLastName + "')";
            System.out.print(query);
            statement.executeUpdate(query);
            con.close();
        }
        catch(org.json.simple.parser.ParseException t){}catch (SQLException e) {System.out.print(e);}
        return Response.status(200).entity("ok").build();
    }

    @POST
    @Path("/CreationProfil/Parent")
    public Response CreationProfilParent(String Param)
    {
        Connection con = Connexion();
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(Param);
            String Email_e = json.get("Email_e").toString();
            String Mdp_e = json.get("Mdp_e").toString();
            String UserName_e = json.get("UserName_e").toString();
            String UserLastName_e = json.get("UserLastName_e").toString();

            String Email = json.get("Email").toString();
            String Mdp = json.get("Mdp").toString();
            String UserName = json.get("UserName").toString();
            String UserLastName = json.get("UserLastName").toString();

            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM parent_user");
            while (rs.next())
            {
                if (Email_e.equals(rs.getString("email_adresse")) == true)
                {
                    con.close();
                    return Response.status(401).entity("Email deja utilie").build();
                }
            }
            String query = "INSERT INTO `parent_user`(`email_adresse`, `password`, `Nom`, `Prenom`, `Id_enfant`) VALUES (";

            System.out.println("TEST : 1");
            rs = statement.executeQuery("SELECT * from users ORDER BY UserID DESC LIMIT 1");
            System.out.println("TEST : 2");
            while (rs.next())
            {
                e_id = rs.getString("UserID");
            }
            System.out.println("TEST : 3" + e_id);

            int id = Integer.parseInt(e_id);
            id = id + 1;
            e_id = String.valueOf(id);

            query += "'" + Email_e+"', '" + Mdp_e + "', '" + UserName_e + "', '" + UserLastName_e + "', '" + e_id + "')";
            System.out.print(query);
            statement.executeUpdate(query);

            rs = statement.executeQuery("SELECT * FROM users");
            while (rs.next())
            {
                if (Email.equals(rs.getString("email_adresse")) == true)
                {
                    con.close();
                    return Response.status(401).entity("Email deja utilie").build();
                }
            }

            query = "INSERT INTO `users`(`email_adresse`, `password`, `Nom`, `Prenom`) VALUES (";
            query += "'" + Email+"', '" + Mdp + "', '" + UserName + "', '" + UserLastName + "')";
            System.out.print(query);
            statement.executeUpdate(query);

            con.close();
        }
        catch(org.json.simple.parser.ParseException t){}catch (SQLException e) {System.out.print(e);}
        return Response.status(200).entity("ok").build();
    }

    @POST
    @Path("/connexion_parent")
    public Response connexionParent(String Valus)
    {
        Connection con = Connexion();
        try
        {
            try
            {
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(Valus);
                String Email = json.get("Email").toString();
                String Mdp = json.get("Mdp").toString();
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM parent_user");
                while (rs.next())
                {
                    if (Email.equals(rs.getString("email_adresse")) == true && Mdp.equals(rs.getString("password")) == true)
                    {
                        //con.close();
                        return Response.status(200).entity(rs.getString("Id")).build();
                    }
                }
                rs.close();
                statement.close();
                con.close();
            }
            catch(org.json.simple.parser.ParseException t)
            {
                System.out.println("Err in parserExc : + " + t.toString());
            }
        }
        catch (SQLException e)
        {
            System.out.println("Err in SQLExc : + " + e.toString());

        }
        return Response.status(401).entity("Bad users infos").build();
    }

    @POST
    @Path("/CreationProfil/Prof")
    public Response CreationProfilProf(String Param)
    {
        Connection con = Connexion();
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(Param);
            String Email = json.get("Email").toString();
            String Mdp = json.get("Mdp").toString();
            String UserName = json.get("UserName").toString();
            String UserLastName = json.get("UserLastName").toString();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM prof_user");
            while (rs.next())
            {
                if (Email.equals(rs.getString("email_adresse")) == true)
                {
                    con.close();
                    return Response.status(401).entity("ko").build();
                }
            }
            String query = "INSERT INTO `prof_user`(`email_adresse`, `password`, `nom`, `prenom`) VALUES (";
            query += "'" + Email+"', '" + Mdp + "', '" + UserName + "', '" + UserLastName + "')";
            System.out.print(query);
            statement.executeUpdate(query);
            con.close();
        }
        catch(org.json.simple.parser.ParseException t){}catch (SQLException e) {System.out.print(e);}
        return Response.status(200).entity("ok").build();
    }

    @POST
    @Path("/connexion_prof")
    public Response connexionProf(String Valus)
    {
        Connection con = Connexion();
        try
        {
            try
            {
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(Valus);
                String Email = json.get("Email").toString();
                String Mdp = json.get("Mdp").toString();
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM prof_user");
                while (rs.next())
                {
                    if (Email.equals(rs.getString("email_adresse")) == true && Mdp.equals(rs.getString("password")) == true)
                    {
                        //con.close();
                        return Response.status(200).entity(rs.getString("Id")).build();
                    }
                }
                rs.close();
                statement.close();
                con.close();
            }
            catch(org.json.simple.parser.ParseException t)
            {
                System.out.println("Err in parserExc : + " + t.toString());
            }
        }
        catch (SQLException e)
        {
            System.out.println("Err in SQLExc : + " + e.toString());

        }
        return Response.status(401).entity("Bad users infos").build();
    }

    public String parsing()
    {
        try
        {
            Thread.sleep(7000);
        }
        catch (java.lang.InterruptedException t){}
        return "ok";
    }
}
