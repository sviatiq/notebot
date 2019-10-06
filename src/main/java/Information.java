import config.Config;
import org.telegram.telegrambots.api.objects.Message;

import java.sql.*;

public class Information {

    private static final String GET_SHEDULE = "SELECT * FROM shedule";
    private static final String INSERT_DATA = "INSERT INTO shedule(week, date, time, location, description) VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_BY_DATE = "SELECT week, time, location, description FROM shedule WHERE date like ?";
    private static Bot bot = new Bot();

    private Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(Config.getProperty(Config.DB_URL),
                Config.getProperty(Config.DB_LOGIN), Config.getProperty(Config.DB_PASSWORD));
        return connection;
    }

    public void addInformation(Shedule shedule) {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(INSERT_DATA)) {
            statement.setString(1, shedule.getWeek());
            statement.setString(2, shedule.getDate());
            statement.setString(3, shedule.getTime());
            statement.setString(4, shedule.getLocation());
            statement.setString(5, shedule.getDesc());
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void showFullShedule(Message message) {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(GET_SHEDULE)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                bot.sendMsg(message,
                        "\uD83D\uDCCBWeek: " + rs.getString("week") + "\n" +
                                "\uD83D\uDD56Time: " + rs.getString("time") + "\n" +
                                "\uD83D\uDCC6Date: " + rs.getString("date") + "\n" +
                                "\uD83D\uDCCDLocation: " + rs.getString("location") + "\n" +
                                "\uD83D\uDCDDDescription: " + rs.getString("description"), false);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getSheduleByDate(Message message, String pattern) {
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(FIND_BY_DATE)) {

            stmt.setString(1,  pattern + "%");
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                if (resultSet.next()) {
                    bot.sendMsg(message, "This day is free!", false);
                } else {
                    bot.sendMsg(message, "\uD83D\uDCCBWeek is " + resultSet.getString("week") + "\n" +
                            "\uD83D\uDD56Time: " + resultSet.getString("time") + "\n" +
                            "\uD83D\uDCCDLocation: " + resultSet.getString("location") + "\n" +
                            "\uD83D\uDCDDDescription: " + resultSet.getString("description"), false);
                }
            }
            resultSet.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}