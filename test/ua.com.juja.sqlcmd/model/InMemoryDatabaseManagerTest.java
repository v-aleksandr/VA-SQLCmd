package ua.com.juja.sqlcmd.model;

/**
 * Created by Александр on 16.05.17.
 */
public class InMemoryDatabaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDatabaseManager() {
        return new InMemoryDatabaseManager();
    }
}
