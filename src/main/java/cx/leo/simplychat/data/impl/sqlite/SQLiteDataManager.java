package cx.leo.simplychat.data.impl.sqlite;

import cx.leo.simplychat.SimplyChatPlugin;
import cx.leo.simplychat.data.DataManager;
import cx.leo.simplychat.style.StyleManager;
import cx.leo.simplychat.user.ChatUser;
import cx.leo.simplychat.user.User;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;
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

    private final SimplyChatPlugin plugin;
    private Connection connection;

    public SQLiteDataManager(SimplyChatPlugin plugin) {
        this.plugin = plugin;
        this.init();
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
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();

        File dataFolder = new File(plugin.getDataFolder(), "users.db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: users.db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
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
    public @NotNull CompletableFuture<Optional<User>> loadUser(UUID uuid) {
        CompletableFuture<Optional<User>> completableFuture = new CompletableFuture<>();
        Connection connection = getSQLConnection();
        PreparedStatement statement = null;
        ResultSet result;

        try {
            statement = connection.prepareStatement("SELECT * FROM chat_users WHERE uuid = '" + uuid.toString() + "';");
            result = statement.executeQuery();

            while (result.next()) {
                if (UUID.fromString(result.getString("uuid")).equals(uuid)) {
                    User user = new ChatUser(uuid);

                    StyleManager styleManager = plugin.getStyleManager();
                    user.setNicknameStyle(styleManager.getStyle(result.getString("nickname_style")));
                    user.setChatStyle(styleManager.getStyle(result.getString("chat_style")));

                    completableFuture.complete(Optional.of(user));
                    return completableFuture;
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

        completableFuture.complete(Optional.empty());
        return completableFuture;
    }

    @Override
    public void updateUser(User user) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("REPLACE INTO chat_users (uuid,nickname_style,chat_style) VALUES(?,?,?)");
            ps.setString(1, user.getUUID().toString());
            ps.setString(2, user.getNicknameStyle().getId());
            ps.setString(3, user.getChatStyle().getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}