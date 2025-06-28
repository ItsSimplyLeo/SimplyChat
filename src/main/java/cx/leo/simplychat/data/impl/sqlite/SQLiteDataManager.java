package cx.leo.simplychat.data.impl.sqlite;

import cx.leo.simplychat.SimplyChatPlugin;
import cx.leo.simplychat.data.DataManager;
import cx.leo.simplychat.style.StyleManager;
import cx.leo.simplychat.user.ChatUser;
import cx.leo.simplychat.user.User;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class SQLiteDataManager implements DataManager {

    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS chat_users (" +
            "`uuid` varchar(32) NOT NULL," +
            "`nickname_style` varchar(32)," +
            "`chat_style` varchar(32)," +
            "PRIMARY KEY (`uuid`)" +
            ");";

    private final SimplyChatPlugin plugin;
    private Connection connection;

    public SQLiteDataManager(SimplyChatPlugin plugin) {
        this.plugin = plugin;
        init();
    }

    @Override
    public void init() {
        try (Connection conn = getSQLConnection();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate(CREATE_USER_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getSQLConnection() {
        try {
            File dataFolder = new File(plugin.getDataFolder(), "users.db");
            if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
            if (!dataFolder.exists()) dataFolder.createNewFile();

            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (IOException | SQLException | ClassNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, "SQLite connection error", e);
            return null;
        }
    }

    @Override
    public void connect() {
        getSQLConnection();
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
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT * FROM chat_users WHERE uuid = ?";
            try (Connection conn = getSQLConnection();
                 PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, uuid.toString());
                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        User user = new ChatUser(uuid);
                        StyleManager styleManager = plugin.getStyleManager();
                        user.setNicknameStyle(styleManager.getStyle(result.getString("nickname_style")));
                        user.setChatStyle(styleManager.getStyle(result.getString("chat_style")));
                        return Optional.of(user);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return Optional.empty();
        });
    }

    @Override
    public CompletableFuture<Void> updateUser(User user) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "REPLACE INTO chat_users (uuid, nickname_style, chat_style) VALUES (?, ?, ?)";
            try (Connection conn = getSQLConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, user.getUUID().toString());
                ps.setString(2, user.getNicknameStyle().getId());
                ps.setString(3, user.getChatStyle().getId());
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        });
    }

}