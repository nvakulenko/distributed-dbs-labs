import java.sql.*;

public class TransactionManager {

    private static Connection flyBookingConn;
    private static Connection hotelBookingConn;
    private static Connection accountConn;

    // ===== SQL's =====

    // Insert into FLY_BOOKING
    private static String PREPARE_FLY_BOOKING_SQL =
            "BEGIN;\n" +
                    "insert into fly_booking(client_name, fly_number, from_city, to_city, from_date) " +
                    "values ('Nik', 'KL888', 'IEV', 'AMS', CURRENT_DATE);\n" +
                    "PREPARE TRANSACTION 'fly_booking_tr';\n";
    private static String ROLLBACK_FLY_BOOKING_SQL = "ROLLBACK PREPARED 'fly_booking_tr';";
    private static String COMMIT_FLY_BOOKING_SQL = "COMMIT PREPARED 'fly_booking_tr';";

    // Insert into HOTEL_BOOKING
    private static String PREPARE_HOTEL_BOOKING_SQL =
            "BEGIN;\n" +
                    "insert into hotel_booking(client_name, hotel_name, arrival, departure) " +
                    "values ('Nik', 'Hilton', CURRENT_DATE, CURRENT_DATE + INTERVAL '1 day');\n" +
                    "PREPARE TRANSACTION 'hotel_booking_tr';\n";
    private static String ROLLBACK_HOTEL_BOOKING_SQL = "ROLLBACK PREPARED 'hotel_booking_tr';";
    private static String COMMIT_HOTEL_BOOKING_SQL = "COMMIT PREPARED 'hotel_booking_tr';";

    // Insert into ACCOUNT
    private static String PREPARE_ACCOUNT_SQL =
            "BEGIN;\n" +
                    "update account set amount = amount - 120 where account_id = 1;\n" +
                    "PREPARE TRANSACTION 'account_tr';\n";
    private static String ROLLBACK_ACCOUNT_SQL = "ROLLBACK PREPARED 'account_tr';";
    private static String COMMIT_ACCOUNT_SQL = "COMMIT PREPARED 'account_tr';";

    public static void main(String[] args) throws SQLException {
        initDbConnections();

        // === START TRANSACTION ===

        // === 1st PHASE ===
        boolean isFlyPrepared = executeSql(flyBookingConn, PREPARE_FLY_BOOKING_SQL);
        boolean isHotelPrepared = executeSql(hotelBookingConn, PREPARE_HOTEL_BOOKING_SQL);
        boolean isAccountPrepared = executeSql(accountConn, PREPARE_ACCOUNT_SQL);

        // === 2nd PHASE ===

        // COMMIT or ROLLBACK
        if (isFlyPrepared && isHotelPrepared && isAccountPrepared) {
            executeSql(flyBookingConn, COMMIT_FLY_BOOKING_SQL);
            executeSql(hotelBookingConn, COMMIT_HOTEL_BOOKING_SQL);
            executeSql(accountConn, COMMIT_ACCOUNT_SQL);
        } else {
            executeSql(flyBookingConn, ROLLBACK_FLY_BOOKING_SQL);
            executeSql(hotelBookingConn, ROLLBACK_HOTEL_BOOKING_SQL);
            executeSql(accountConn, ROLLBACK_ACCOUNT_SQL);
        }
    }

    private static boolean executeSql(Connection connection, String sql) {
        try {
            PreparedStatement prepareFlyStatement = connection.prepareStatement(sql);
            prepareFlyStatement.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    private static void initDbConnections() throws SQLException {
        // JDBC connection FLY_BOOKING
        String flyBookingUrl = "jdbc:postgresql://localhost:5433/fly_booking?user=postgres&password=postgres";
        flyBookingConn = DriverManager.getConnection(flyBookingUrl);

        // JDBC connection HOTEL_BOOKING
        String hotelBookingUrl = "jdbc:postgresql://localhost:5433/hotel_booking?user=postgres&password=postgres";
        hotelBookingConn = DriverManager.getConnection(hotelBookingUrl);

        // JDBC connection ACCOUNT
        String accountUrl = "jdbc:postgresql://localhost:5433/account?user=postgres&password=postgres";
        accountConn = DriverManager.getConnection(accountUrl);
    }
}

