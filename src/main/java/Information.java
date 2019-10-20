import config.Config;

import java.sql.Connection;

import org.telegram.telegrambots.api.objects.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Information {

    private static final String GET_SHEDULE = "SELECT * FROM shedule";
    private static final String INSERT_DATA = "INSERT INTO shedule(week, date, time, location, description) VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_BY_DATE = "SELECT week, date, time, location, description FROM shedule WHERE date like ?";
    private static final String FIND_BY_LOCATION = "SELECT date, time, location FROM shedule WHERE location like ?";
    private static final String GET_EDUCATION_SHEDULE1 = "SELECT ds.date, ds.day_name, lt.lesson_time, ds.lesson_num,  ds.lesson_name, ds.lesson_type, ds.room FROM day_shedule as ds, lessons_time as lt\n" +
            "WHERE ds.lesson_num = lt.lesson_num AND week_num = '1' AND day_name = ?";
    private static final String GET_EDUCATION_SHEDULE2 = "SELECT ds.date, ds.day_name, lt.lesson_time, ds.lesson_num,  ds.lesson_name, ds.lesson_type, ds.room FROM day_shedule as ds, lessons_time as lt\n" +
            "WHERE ds.lesson_num = lt.lesson_num AND week_num = '2' AND day_name = ?";

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

    public void showEducationShedule1(Message message, String pattern) {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(GET_EDUCATION_SHEDULE1)) {
            statement.setString(1, pattern);
            List<String> list = new ArrayList();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                list.add("Дата: " + resultSet.getString("date") + "\n" +
                        "День: " + resultSet.getString("day_name") + "\n" +
                        "Время: " + resultSet.getString("lesson_time") + "\n" +
                        "Пара: " + resultSet.getString("lesson_num") + "\n" +
                        "Название пары: " + resultSet.getString("lesson_name") + "\n" +
                        "Тип пары:  " + resultSet.getString("lesson_type") + "\n" +
                        "Аудитория: " + resultSet.getString("room") + "\n" +
                        "---------------------\n");
            }
            bot.sendMsg(message, list.toString(), false);
            System.out.println(list);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
        public void showEducationShedule2(Message message, String pattern){
            try (Connection connect = getConnection();
                 PreparedStatement statement = connect.prepareStatement(GET_EDUCATION_SHEDULE2)) {
                statement.setString(1, pattern);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    bot.sendMsg(message,
                            "Дата: " + resultSet.getString("date") + "\n" +
                                    "День: " + resultSet.getString("day_name") + "\n" +
                                    "Время: " + resultSet.getString("lesson_time ") + "\n" +
                                    "Пара: " + resultSet.getString("lesson_num") + "\n" +
                                    "Название пары: " + resultSet.getString("lesson_name") + "\n" +
                                    "Тип пары:  " + resultSet.getString("lesson_type") + "\n" +
                                    "Аудитория: " + resultSet.getString("room") + "\n",
                            false);
                }
            }catch(SQLException ex){
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getSheduleByDate(Message message, String pattern) {
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(FIND_BY_DATE)) {
            stmt.setString(1, pattern + "%");
            ResultSet resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    bot.sendMsg(message, "\uD83D\uDCCBWeek is " + resultSet.getString("week") + "\n" +
                            "\uD83D\uDCC6Date:" + resultSet.getString("date") + "\n" +
                            "\uD83D\uDD56Time: " + resultSet.getString("time") + "\n" +
                            "\uD83D\uDCCDLocation: " + resultSet.getString("location") + "\n" +
                            "\uD83D\uDCDDDescription: " + resultSet.getString("description"), false);
                }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void getSheduleByLocation(Message message, String pattern){
        try(Connection connect = getConnection(); PreparedStatement preparedStatement = connect.prepareStatement(FIND_BY_LOCATION)){
            preparedStatement.setString(1, pattern+"%");
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
            while(rs.next()){
                bot.sendMsg(message, "\uD83D\uDCC6Date: " + rs.getString("date") + "\n" +
                        "\uD83D\uDD56Time: " + rs.getString("time") + "\n" +
                        "\uD83D\uDCCDLocation: " + rs.getString("location"), false);
            }
            }else{
                bot.sendMsg(message, "This location is empty!", false);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    }