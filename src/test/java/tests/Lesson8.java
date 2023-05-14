package tests;

import api.models.UserShort;
import org.sql2o.Sql2o;

import java.sql.*;

public class Lesson8 {
    public static void main(String[] args) {
        try {
            postgres();
            insertUserByJDBC(new UserShort(13, "Lily"));

            Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5438/postgres", "postgres", "postgres");
            getUserBySql2o(sql2o);
            insertUserBySql2o(sql2o, new UserShort(14, "Lily"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void postgres() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5438/postgres", "postgres", "postgres");
        PreparedStatement preparedStatement = connection.prepareStatement("select*fromuserswhereuser_id=?");
        preparedStatement.setInt(1, 7);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            System.out.println("Users:");
            System.out.println(resultSet.getString("name") + "withid=" + resultSet.getInt("user_id"));
        }
    }

    public static void insertUserByJDBC(UserShort user) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5438/postgres", "postgres", "postgres")) {
            PreparedStatement preparedStatement = connection.prepareStatement("insertintousers(user_id,name)values(?,?)");
            preparedStatement.setInt(1, user.getUser_id());
            preparedStatement.setString(2, user.getName());
            preparedStatement.executeUpdate();
        }
    }

    public static void getUserBySql2o(Sql2o sql2o) throws SQLException {

        org.sql2o.Connection connection = sql2o.open();

        System.out.println(connection.createQuery("select*fromuserswhereuser_id=:id").addParameter("id", 7).executeAndFetch(UserShort.class));


    }

    public static void insertUserBySql2o(Sql2o sql2o, UserShort user) throws SQLException {
        try (org.sql2o.Connection connection=sql2o.beginTransaction()){
            connection.createQuery("insertintousers(user_id,name)values(:user_id,:name)").bind(user).executeUpdate();
            connection.commit();
        }
    }

}
