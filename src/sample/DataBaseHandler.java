package sample;

import java.sql.*;

// В этом классе мы подключ. БД и работаем с методами в которых мы можем получать инфу из БД.
// Исспользуя селекты и тд.
public class DataBaseHandler extends Configs{
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
     String connectionString = "jdbc:postgresql://localhost:5432/postgres";

     Class.forName("org.postgresql.Driver"); // сервер обязательно должен быть установлен!

     dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
     return dbConnection;

    }

    public void singUpUser(User user) {
        String insert = "INSERT INTO users (" +
                Const.USER_FIRSTNAME + "," + Const.USER_LASTNAME + "," +
                Const.USER_USERNAME + "," + Const.USER_PASSWORD + "," +
                Const.USER_DOLZNOST + ")" + "VALUES(?,?,?,?,?)";
try {
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.setString(1, user.getFirstName());
        prSt.setString(2, user.getLastName());
        prSt.setString(3, user.getUserName());
        prSt.setString(4, user.getPassword());
        prSt.setString(5, user.getDolznost());
        prSt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
    e.printStackTrace();
}
    }
    public ResultSet getUser(User user) {
        ResultSet resSet = null;
        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " +
                Const.USER_USERNAME + "=? AND " + Const.USER_PASSWORD + "=?";
        try {
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, user.getUserName());
        prSt.setString(2, user.getPassword());
       resSet =  prSt.executeQuery();
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
        return resSet;
    }
}
