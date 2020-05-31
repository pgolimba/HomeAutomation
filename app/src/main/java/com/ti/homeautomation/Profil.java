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

    public String username;
    public String password;

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
        Connection con = DbConnection.connectionclass();
        Statement sql;
        sql = con.createStatement();
        boolean ret = false;

        ResultSet rs;
        rs = sql.executeQuery("select * from [dbo].[user1]");

        while(rs.next()){
            if(rs.getString("UserId").equals(userID) && rs.getString("Parola").equals(password)) {
                ret = true;
                break;
            }
        }

        con.close();

        if(ret) {
            this.username = userID;
            this.password = password;
        }

        return ret;

    }

}
