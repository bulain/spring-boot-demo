package org.flywaydb.core.internal.database.dm;


import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.ResourceProvider;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.callback.CallbackExecutor;
import org.flywaydb.core.internal.database.DatabaseType;
import org.flywaydb.core.internal.database.base.Database;
import org.flywaydb.core.internal.database.base.BaseDatabaseType;

import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.flywaydb.core.internal.jdbc.StatementInterceptor;
import org.flywaydb.core.internal.parser.Parser;
import org.flywaydb.core.internal.parser.ParsingContext;
import org.flywaydb.core.internal.sqlscript.SqlScriptExecutor;
import org.flywaydb.core.internal.sqlscript.SqlScriptExecutorFactory;
import org.flywaydb.core.internal.util.ClassUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

@Slf4j
public class DmDatabaseType extends BaseDatabaseType {
    // DM usernames/passwords can be 1-30 chars, can only contain alphanumerics and # _ $
    // The first (and only) capture group represents the password
    private static final Pattern usernamePasswordPattern = Pattern.compile("^jdbc:dm:[a-zA-Z0-9#_$]+/([a-zA-Z0-9#_$]+)@.*");

    @Override
    public String getName() {
        return "DM DBMS";
    }

    @Override
    public int getNullType() {
        return Types.VARCHAR;
    }

    @Override
    public boolean handlesJDBCUrl(String url) {
        if (url.startsWith("jdbc-secretsmanager:dm:")) {




            throw new org.flywaydb.core.internal.license.FlywayTeamsUpgradeRequiredException("jdbc-secretsmanager");

        }
        return url.startsWith("jdbc:dm") || url.startsWith("jdbc:p6spy:dm");
    }

    @Override
    public Pattern getJDBCCredentialsPattern() {
        return usernamePasswordPattern;
    }

    @Override
    public String getDriverClass(String url, ClassLoader classLoader) {





        if (url.startsWith("jdbc:p6spy:dm:")) {
            return "com.p6spy.engine.spy.P6SpyDriver";
        }
        return "dm.jdbc.driver.DmDriver";
    }

    @Override
    public boolean handlesDatabaseProductNameAndVersion(String databaseProductName, String databaseProductVersion, Connection connection) {
        return databaseProductName.startsWith("DM");
    }

    @Override
    public Database createDatabase(Configuration configuration, JdbcConnectionFactory jdbcConnectionFactory, StatementInterceptor statementInterceptor) {
        DmDatabase.enableTnsnamesOraSupport();

        return new DmDatabase(configuration, jdbcConnectionFactory, statementInterceptor);
    }

    @Override
    public Parser createParser(Configuration configuration, ResourceProvider resourceProvider, ParsingContext parsingContext) {




        return new DmParser(configuration










                , parsingContext
        );
    }

    @Override
    public SqlScriptExecutorFactory createSqlScriptExecutorFactory(JdbcConnectionFactory jdbcConnectionFactory,
                                                                   final CallbackExecutor callbackExecutor,
                                                                   final StatementInterceptor statementInterceptor
    ) {




        final DatabaseType thisRef = this;

        return new SqlScriptExecutorFactory() {
            @Override
            public SqlScriptExecutor createSqlScriptExecutor(Connection connection , boolean undo, boolean batch, boolean outputQueryResults) {






                return new DmSqlScriptExecutor(new JdbcTemplate(connection, thisRef)
                        , callbackExecutor, undo, batch, outputQueryResults, statementInterceptor
                );
            }
        };
    }

    @Override
    public void setDefaultConnectionProps(String url, Properties props, ClassLoader classLoader) {
        String osUser = System.getProperty("user.name");
        props.put("v$session.osuser", osUser.substring(0, Math.min(osUser.length(), 30)));
        props.put("v$session.program", APPLICATION_NAME);
        props.put("DM.net.keepAlive", "true");

        String oobb = ClassUtils.getStaticFieldValue("dm.jdbc.driver.DmdbConnection", "CONNECTION_PROPERTY_THIN_NET_DISABLE_OUT_OF_BAND_BREAK", classLoader);
        props.put(oobb, "true");
    }

    @Override
    public void setConfigConnectionProps(Configuration config, Properties props, ClassLoader classLoader) {






    }






















    @Override
    public boolean detectUserRequiredByUrl(String url) {
        return !usernamePasswordPattern.matcher(url).matches();
    }

    @Override
    public boolean detectPasswordRequiredByUrl(String url) {






        return !usernamePasswordPattern.matcher(url).matches();
    }

    @Override
    public Connection alterConnectionAsNeeded(Connection connection, Configuration configuration) {
        Map<String, String> jdbcProperties = configuration.getJdbcProperties();

        if (jdbcProperties != null /*&& jdbcProperties.containsKey(DmConnection.PROXY_USER_NAME)*/) {
            try {
                DmConnection dmConnection;

                try {
                    if (connection instanceof DmConnection) {
                        dmConnection = (DmConnection) connection;
                    } else if (connection.isWrapperFor(DmConnection.class)) {
                        // This includes com.zaxxer.HikariCP.HikariProxyConnection, potentially other unknown wrapper types
                        dmConnection = connection.unwrap(DmConnection.class);
                    } else {
                        throw new FlywayException("Unable to extract DM connection type from '" + connection.getClass().getName() + "'");
                    }
                } catch (SQLException e) {
                    throw new FlywayException("Unable to unwrap connection type '" + connection.getClass().getName() + "'", e);
                }

                /*if (!dmConnection.isProxySession()) {
                    Properties props = new Properties();
                    props.putAll(configuration.getJdbcProperties());
                    dmConnection.openProxySession(DmConnection.PROXYTYPE_USER_NAME, props);
                }*/
            } catch (FlywayException e) {
                log.warn(e.getMessage());
            /*} catch (SQLException e) {
                throw new FlywayException("Unable to open proxy session: " + e.getMessage(), e);*/
            }
        }

        return super.alterConnectionAsNeeded(connection, configuration);
    }

}