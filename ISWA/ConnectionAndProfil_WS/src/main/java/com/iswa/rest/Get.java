package com.iswa.rest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.sql.*;

@Path("/get")
public class Get
{
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

    @GET
    @Path("InfoProfil/{param}")
    public Response InfoProfil(@PathParam("param") String msg)
    {
        String Name = new String();
        String Email = new String();
        String LastName = new String();
        Connection con = Connexion();
        try
        {
            Statement statement = con.createStatement();
            String query = "SELECT * FROM users WHERE UserID =";
            query += msg;
            ResultSet rs = statement.executeQuery(query);
            System.out.print(rs);
            if (rs.next())
            {
                Email = rs.getString("email_adresse");
                Name = rs.getString("Prenom");
                LastName = rs.getString("Nom");
            }
            else
            {
                return Response.status(500).entity("ko").build();
            }
            JSONObject obj = new JSONObject();
            obj.put("name", Name);
            obj.put("Email", Email);
            obj.put("LastName", LastName);
            return Response.status(200).entity(obj.toJSONString()).build();
        }
        catch (SQLException e) {}
        return Response.status(400).entity("ko").build();
    }

    @GET
    @Path("GetInfoeleve/{param}")
    public Response GetInfoeleve(@PathParam("param") String msg)
    {
        Connection con = Connexion();
        String SubjectID = new String();
        String TopicID = new String();
        String UserLevel = new String();
        int i = 0;
        try
        {
            Statement statement = con.createStatement();
            String query = "SELECT * FROM user_level_per_subject WHERE UserId =";
            query += msg;
            ResultSet rs = statement.executeQuery(query);
            JSONObject obj = new JSONObject();
            while (rs.next())
            {
                JSONArray list = new JSONArray();
                SubjectID = "SubjectID : ";
                SubjectID += rs.getString("SubjectID");
                TopicID = "TopicID : ";
                TopicID += rs.getString("TopicID");
                UserLevel = "UserLevel : ";
                UserLevel += rs.getString("UserLevel");
                list.add(SubjectID);
                list.add(TopicID);
                list.add(UserLevel);
                obj.put("UserLvl_"+i, list);
                i++;
            }
            return Response.status(200).entity(obj.toJSONString()).build();
        }
        catch (SQLException e) {}
        return Response.status(400).entity("ko").build();
    }
}
