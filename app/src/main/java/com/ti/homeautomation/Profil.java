package com.ti.homeautomation;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Profil {

    private static Profil profil;

    public static synchronized Profil getInstance()
    {
        if(profil==null)
        {
            profil = new Profil();
        }
        return profil;
    }

//    public void updateProfilDataBase() throws SQLException {
//
//        String SQL = "UPDATE [dbo].[UserProfil]" +
//                " SET numeUtilizator=?, prenumeUtilizator=?, adresa=?, adresaEmail=?, nrTelefon=?, locDeMunca=?" +
//                " WHERE id=?";
//
//        Log.e("updateUserProfil","updateUserProfil");
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        if(imagineProfil!=null) {
//            imagineProfil.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//            Log.e("bitmap","bitmap size: "+ imagineProfil.getByteCount()/1000000 + " MB");
//        }
//        byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
//
//        Log.e("byteArray","byteArray size: "+ byteArrayImage.length + " Bytes");
//
//
//        PreparedStatement statement = conectionclass().prepareStatement(SQL);
//        statement.setString(1,numeUtilizator);
//        Log.e("UPDATE","PASS Nume");
//
//        statement.setString(2,prenumeUtilizator);
//        Log.e("UPDATE","PASS Prenume");
//
//        statement.setString(3,adresa);
//        Log.e("UPDATE","PASS Adresa");
//
//        statement.setString(4,adresaEmail);
//        Log.e("UPDATE","PASS Email");
//
//        statement.setString(5,nrTelefon);
//        Log.e("UPDATE","PASS Telefon");
//
//        statement.setString(6,locDeMunca);
//        Log.e("UPDATE","PASS Munca");
//
////        statement.setBytes(7,byteArrayImage);
////        Log.e("UPDATE","PASS IMAGINE");
//
//        statement.setString(7,ID);
//        Log.e("UPDATE","PASS ID");
//
//
//        int rowUpdated = statement.executeUpdate();
//        if(rowUpdated>0)
//        {
//            Log.e("UPDATE","UPDATE EFECTUAT CU SUCCES");
//        }
//        conectionclass().close();
//
//
//    }

    public boolean Login(String userID,String password) throws SQLException {
        Statement sql;
        sql = connectionclass().createStatement();

        ResultSet rs;
        rs = sql.executeQuery("select * from [dbo].[user1]");

        while(rs.next()){
            if(rs.getString("UserId").equals(userID) && rs.getString("Parola").equals(password))
                return true;
        }

        return false;

    }




    public Connection connectionclass()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        String USERNAME ;
        String PASSWORD ;
        //String URL = "jdbc:jtds:sqlserver://mobileapp-testserver.database.windows.net:1433;DatabaseName=ProfilData;user="+USERNAME+";password="+PASSWORD+";encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
         String URL = "jdbc:jtds:sqlserver://ssshouse.database.windows.net:1433;DatabaseName=sssdb;user=user;password=Ssshouse123!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Log.e("CONNECT","PASS 1");

            ConnectionURL=URL;
            connection = DriverManager.getConnection(ConnectionURL);
            Log.e("CONNECT","PASS 2");

        }
        catch (SQLException e)
        {
            Log.e("error here 1 : ", e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2: ", e.getLocalizedMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3: ", e.getLocalizedMessage());
        }

        return connection;
    }
}
