package com.ti.homeautomation;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    public static Connection connectionclass()
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
