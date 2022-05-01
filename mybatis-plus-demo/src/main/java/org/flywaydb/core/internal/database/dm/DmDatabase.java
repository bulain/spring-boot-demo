package org.flywaydb.core.internal.database.dm;


import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.database.base.Database;
import org.flywaydb.core.internal.database.base.Table;
import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
import org.flywaydb.core.internal.jdbc.StatementInterceptor;
import org.flywaydb.core.internal.util.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DmDatabase extends Database<DmConnection> {
    private static final String DM_NET_TNS_ADMIN = "DM.net.tns_admin";

    /**
     * If the TNS_ADMIN environment variable is set, enable tnsnames.ora support for the DM JDBC driver.
     * See http://www.orafaq.com/wiki/TNS_ADMIN
     */
    public static void enableTnsnamesOraSupport() {
        String tnsAdminEnvVar = System.getenv("TNS_ADMIN");
        String tnsAdminSysProp = System.getProperty(DM_NET_TNS_ADMIN);
        if (StringUtils.hasLength(tnsAdminEnvVar) && tnsAdminSysProp == null) {
            System.setProperty(DM_NET_TNS_ADMIN, tnsAdminEnvVar);
        }
    }

    public DmDatabase(Configuration configuration, JdbcConnectionFactory jdbcConnectionFactory, StatementInterceptor statementInterceptor) {
        super(configuration, jdbcConnectionFactory, statementInterceptor);
    }

    @Override
    protected DmConnection doGetConnection(Connection connection) {
        return new DmConnection(this, connection);
    }














    @Override
    public final void ensureSupported() {

    }

    @Override
    public String getRawCreateScript(Table table, boolean baseline) {
        String tablespace = configuration.getTablespace() == null
                ? ""
                : " TABLESPACE \"" + configuration.getTablespace() + "\"";

        return "CREATE TABLE " + table + " (\n" +
                "    \"installed_rank\" INT NOT NULL,\n" +
                "    \"version\" VARCHAR2(50),\n" +
                "    \"description\" VARCHAR2(200) NOT NULL,\n" +
                "    \"type\" VARCHAR2(20) NOT NULL,\n" +
                "    \"script\" VARCHAR2(1000) NOT NULL,\n" +
                "    \"checksum\" INT,\n" +
                "    \"installed_by\" VARCHAR2(100) NOT NULL,\n" +
                "    \"installed_on\" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,\n" +
                "    \"execution_time\" INT NOT NULL,\n" +
                "    \"success\" NUMBER(1) NOT NULL,\n" +
                "    CONSTRAINT \"" + table.getName() + "_pk\" PRIMARY KEY (\"installed_rank\")\n" +
                ")" + tablespace + ";\n" +
                (baseline ? getBaselineStatement(table) + ";\n" : "") +
                "CREATE INDEX \"" + table.getSchema().getName() + "\".\"" + table.getName() + "_s_idx\" ON " + table + " (\"success\");\n";
    }

    @Override
    public boolean supportsEmptyMigrationDescription() {
        // DM will convert the empty string to NULL implicitly, and throw an exception as the column is NOT NULL
        return false;
    }

    /*@Override
    protected String doGetCatalog() throws SQLException {
        // DM's JDBC driver returns a hard-coded NULL from getCatalog()
        return getMainConnection().getJdbcTemplate().queryForString("SELECT GLOBAL_NAME FROM GLOBAL_NAME");
    }*/

    @Override
    protected String doGetCurrentUser() throws SQLException {
        return getMainConnection().getJdbcTemplate().queryForString("SELECT USER FROM DUAL");
    }

    @Override
    public boolean supportsDdlTransactions() {
        return false;
    }

    @Override
    public boolean supportsChangingCurrentSchema() {
        return true;
    }

    @Override
    public String getBooleanTrue() {
        return "1";
    }

    @Override
    public String getBooleanFalse() {
        return "0";
    }

    @Override
    public String doQuote(String identifier) {
        return "\"" + identifier + "\"";
    }

    @Override
    public boolean catalogIsSchema() {
        return false;
    }

    /**
     * Checks whether the specified query returns rows or not. Wraps the query in EXISTS() SQL function and executes it.
     * This is more preferable to opening a cursor for the original query, because a query inside EXISTS() is implicitly
     * optimized to return the first row and because the client never fetches more than 1 row despite the fetch size
     * value.
     *
     * @param query  The query to check.
     * @param params The query parameters.
     * @return {@code true} if the query returns rows, {@code false} if not.
     * @throws SQLException when the query execution failed.
     */
    boolean queryReturnsRows(String query, String... params) throws SQLException {
        return getMainConnection().getJdbcTemplate().queryForBoolean("SELECT CASE WHEN EXISTS(" + query + ") THEN 1 ELSE 0 END FROM DUAL", params);
    }

    /**
     * Checks whether the specified privilege or role is granted to the current user.
     *
     * @return {@code true} if it is granted, {@code false} if not.
     * @throws SQLException if the check failed.
     */
    boolean isPrivOrRoleGranted(String name) throws SQLException {
        return queryReturnsRows("SELECT 1 FROM SESSION_PRIVS WHERE PRIVILEGE = ? UNION ALL " +
                "SELECT 1 FROM SESSION_ROLES WHERE ROLE = ?", name, name);
    }

    /**
     * Checks whether the specified data dictionary view in the specified system schema is accessible (directly or
     * through a role) or not.
     *
     * @param owner the schema name, unquoted case-sensitive.
     * @param name  the data dictionary view name to check, unquoted case-sensitive.
     * @return {@code true} if it is accessible, {@code false} if not.
     * @throws SQLException if the check failed.
     */
    private boolean isDataDictViewAccessible(String owner, String name) throws SQLException {
        return queryReturnsRows("SELECT * FROM ALL_TAB_PRIVS WHERE OWNER = ? AND TABLE_NAME = ?" +
                " AND PRIVILEGE = 'SELECT'", owner, name);
    }

    /**
     * Checks whether the specified SYS view is accessible (directly or through a role) or not.
     *
     * @param name the data dictionary view name to check, unquoted case-sensitive.
     * @return {@code true} if it is accessible, {@code false} if not.
     * @throws SQLException if the check failed.
     */
    boolean isDataDictViewAccessible(String name) throws SQLException {
        return isDataDictViewAccessible("SYS", name);
    }

    /**
     * Returns the specified data dictionary view name prefixed with DBA_ or ALL_ depending on its accessibility.
     *
     * @param baseName the data dictionary view base name, unquoted case-sensitive, e.g. OBJECTS, TABLES.
     * @return the full name of the view with the proper prefix.
     * @throws SQLException if the check failed.
     */
    String dbaOrAll(String baseName) throws SQLException {
        return isPrivOrRoleGranted("SELECT ANY DICTIONARY") || isDataDictViewAccessible("DBA_" + baseName)
                ? "DBA_" + baseName
                : "ALL_" + baseName;
    }

    /**
     * Returns the set of DM options available on the target database.
     *
     * @return the set of option titles.
     * @throws SQLException if retrieving of options failed.
     */
    private Set<String> getAvailableOptions() throws SQLException {
        return new HashSet<>(getMainConnection().getJdbcTemplate()
                .queryForStringList("SELECT PARAMETER FROM V$OPTION WHERE VALUE = 'TRUE'"));
    }

    /**
     * Checks whether Flashback Data Archive option is available or not.
     *
     * @return {@code true} if it is available, {@code false} if not.
     * @throws SQLException when checking availability of the feature failed.
     */
    boolean isFlashbackDataArchiveAvailable() throws SQLException {
        return getAvailableOptions().contains("Flashback Data Archive");
    }

    /**
     * Checks whether XDB component is available or not.
     *
     * @return {@code true} if it is available, {@code false} if not.
     * @throws SQLException when checking availability of the component failed.
     */
    boolean isXmlDbAvailable() throws SQLException {
        return isDataDictViewAccessible("ALL_XML_TABLES");
    }

    /**
     * Checks whether Data Mining option is available or not.
     *
     * @return {@code true} if it is available, {@code false} if not.
     * @throws SQLException when checking availability of the feature failed.
     */
    boolean isDataMiningAvailable() throws SQLException {
        return getAvailableOptions().contains("Data Mining");
    }

    /**
     * Checks whether DM Locator component is available or not.
     *
     * @return {@code true} if it is available, {@code false} if not.
     * @throws SQLException when checking availability of the component failed.
     */
    boolean isLocatorAvailable() throws SQLException {
        return isDataDictViewAccessible("MDSYS", "ALL_SDO_GEOM_METADATA");
    }

    /**
     * Returns the list of schemas that were created and are maintained by DM-supplied scripts and must not be
     * changed in any other way. The list is composed of default schemas mentioned in the official documentation for
     * DM Database versions from 10.1 to 12.2, and is dynamically extended with schemas from DBA_REGISTRY and
     * ALL_USERS (marked with DM_MAINTAINED = 'Y' in DM 12c).
     *
     * @return the set of system schema names
     */
    Set<String> getSystemSchemas() throws SQLException {

        // The list of known default system schemas
        Set<String> result = new HashSet<>(Arrays.asList(
                "SYS", "SYSTEM", // Standard system accounts
                "SYSBACKUP", "SYSDG", "SYSKM", "SYSRAC", "SYS$UMF", // Auxiliary system accounts
                "DBSNMP", "MGMT_VIEW", "SYSMAN", // Enterprise Manager accounts
                "OUTLN", // Stored outlines
                "AUDSYS", // Unified auditing
                "DM_OCM", // DM Configuration Manager
                "APPQOSSYS", // DM Database QoS Management
                "OJVMSYS", // DM JavaVM
                "DVF", "DVSYS", // DM Database Vault
                "DBSFWUSER", // Database Service Firewall
                "REMOTE_SCHEDULER_AGENT", // Remote scheduler agent
                "DIP", // DM Directory Integration Platform
                "APEX_PUBLIC_USER", "FLOWS_FILES", /*"APEX_######", "FLOWS_######",*/ // DM Application Express
                "ANONYMOUS", "XDB", "XS$NULL", // DM XML Database
                "CTXSYS", // DM Text
                "LBACSYS", // DM Label Security
                "EXFSYS", // DM Rules Manager and Expression Filter
                "MDDATA", "MDSYS", "SPATIAL_CSW_ADMIN_USR", "SPATIAL_WFS_ADMIN_USR", // DM Locator and Spatial
                "ORDDATA", "ORDPLUGINS", "ORDSYS", "SI_INFORMTN_SCHEMA", // DM Multimedia
                "WMSYS", // DM Workspace Manager
                "OLAPSYS", // DM OLAP catalogs
                "OWBSYS", "OWBSYS_AUDIT", // DM Warehouse Builder
                "GSMADMIN_INTERNAL", "GSMCATUSER", "GSMUSER", // Global Data Services
                "GGSYS", // DM GoldenGate
                "WK_TEST", "WKSYS", "WKPROXY", // DM Ultra Search
                "ODM", "ODM_MTR", "DMSYS", // DM Data Mining
                "TSMSYS" // Transparent Session Migration
        ));






        result.addAll(getMainConnection().getJdbcTemplate().queryForStringList("SELECT USERNAME FROM ALL_USERS " +
                "WHERE REGEXP_LIKE(USERNAME, '^(APEX|FLOWS)_\\d+$')" +



                " OR DM_MAINTAINED = 'Y'"



        ));
























        return result;
    }
}