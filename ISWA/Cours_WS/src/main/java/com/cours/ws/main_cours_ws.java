package com.cours.ws;

import com.dbcommand.Connect;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@Path("/cours")
public class main_cours_ws
{
    private String err_msg = "";
   static String[][] linkSample ={
        { "../tmpfiles/6/nbrEntier.pdf", "1.3.json"  },
        { "../ressources/1.2" }
   };

    @POST
    @Path("/gc")
    public Response getMsg(String id)
    {
        List<String[]> rep = Connect.getCours_to_Database(id);
        System.out.println("length : " + rep.size());

        for (String[] a: rep)
        {
            System.out.println("mat in pls : " + a[0]);
            System.out.println("mat in pls1 : " + a[1]);
        }
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        return Response.status(200).entity(rep).build();
    }
}
