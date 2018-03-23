package com.usr.info;

import java.util.ArrayList;
import java.util.List;

public class User
{
    public int ID = 0;
    public String Classe = null;
    public int[][] lvl = new int[50][50]; // int[SubjectID][TopicID] = UserLvL

    public User()
    {
        int ID = 0;
        String Classe = null;
        int[][] lvl = new int[50][50];
    }
}
