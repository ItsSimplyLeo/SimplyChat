package cx.leo.simplychat.data.impl.sqlite;

import cx.leo.simplychat.SimplyChat;
import cx.leo.simplychat.data.DataManager;
import cx.leo.simplychat.style.StyleManager;
import cx.leo.simplychat.user.ChatUser;
import cx.leo.simplychat.user.User;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Level;

public class SQLiteDataManager implements DataManager {

    private final static String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS chat_users (" +
            "`uuid` varchar(32) NOT NULL," +
            "`nickname_style` varchar(32)," +
            "`chat_style` varchar(32)," +
            "PRIMARY KEY (`uuid`)" +
            ");";

    private final SimplyChat plugin;
    private Connection connection;

    public SQLiteDataManager(SimplyChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        connection = getSQLConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_USER_TABLE);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getSQLConnection() {
        if (connection == null) connect();

        try {
            if (connection.isClosed()) connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }

    @Override
    public void connect() {
        File dataFolder = new File(plugin.getDataFolder(), "users.db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Error whilst creating users.db");
            }
        }
        try {
            if(connection != null && !connection.isClosed()) return;

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite Exception, please report.", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "SQLite JBDC library was not found. Please install it.");
        }
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reload() {
        connect();
    }

    @Override
    public @Nullable User loadUser(UUID uuid) {
        Connection connection = getSQLConnection();
        PreparedStatement statement = null;
        ResultSet result;

        try {
            statement = connection.prepareStatement("SELECT * FROM chat_users WHERE uuid = '" + uuid.toString() + "';");
            result = statement.executeQuery();

            while (result.next()) {
                if (result.getString("uuid").equals(uuid.toString())) {
                    User user = new ChatUser(uuid);

                    StyleManager styleManager = plugin.getStyleManager();
                    user.setNicknameStyle(styleManager.getStyle(result.getString("nickname_style")));
                    user.setChatStyle(styleManager.getStyle(result.getString("chat_style")));

                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                plugin.getLogger().severe("Error whilst closing statement & connection...");
            }
        }

        return null;
    }

    @Override
    public void updateUser(User user) {
        Connection connection = getSQLConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("REPLACE INTO player_ranks (uuid,nickname_style,chat_style) VALUES(?,?,?);");
            statement.setString(1, user.getUUID().toString());
            statement.setString(2, user.getNicknameStyle().getId());
            statement.setString(3, user.getChatStyle().getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().severe("Error whilst getting player...");
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                plugin.getLogger().severe("Error whilst closing statement & connection...");
            }
        }
    }
}
