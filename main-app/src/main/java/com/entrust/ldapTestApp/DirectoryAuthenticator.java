package com.entrust.ldapTestApp;

import javax.naming.Context;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class DirectoryAuthenticator {

    private static final String SOCKET_FACTORY = "java.naming.ldap.factory.socket";

    private final String host;
    private final int port;
    private final boolean isSsl;
    private final String username;
    private final String password;
    private final String connectTimeout;
    private final String readTimeout;
    private final int duration;
    private final long sleepDuration;

    private final Hashtable env = new Hashtable();

    public DirectoryAuthenticator(String host, int port, String ssl, String username, String password,
                                  String connectTimeout, String readTimeout, int duration, String sleepDuration) {

        this.host = host;
        this.port = port;
        this.isSsl = isEmpty(ssl) ? false : Boolean.parseBoolean(ssl);
        this.username = username;
        this.password = password;
        this.connectTimeout = isEmpty(connectTimeout) ? "5000" : connectTimeout;
        this.readTimeout = isEmpty(readTimeout) ? "5000" : readTimeout;
        this.duration = duration;
        this.sleepDuration = isEmpty(sleepDuration) ? 1000 : Long.parseLong(sleepDuration);

        loadEnv();
    }

    private void loadEnv() {
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        // only wait 5 seconds for LDAP to respond
        env.put("com.sun.jndi.ldap.connect.timeout", connectTimeout);
        env.put("com.sun.jndi.ldap.read.timeout", readTimeout);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "{" + username + "}");
        env.put(Context.SECURITY_CREDENTIALS, password);

        env.remove(Context.SECURITY_PROTOCOL);
        env.remove(SOCKET_FACTORY);

        env.put(Context.PROVIDER_URL, buildUrl(host, port, isSsl));

        System.out.println(String.format("Initializing LDAP test runner with host %s, port %s, SSL %s, username %s " +
                        "connect-timeout %s ms read-timeout %s ms, duration %s minutes, sleep-interval %s ms. ",
                        host, port, isSsl, username, connectTimeout, readTimeout, duration, sleepDuration));
    }

    private String buildUrl(String hostname, int port, boolean useSsl) {
        String protocol = useSsl ? "ldaps://" : "ldap://";
        return protocol + hostname + ":" + port;
    }

    private void authenticate() throws Exception {
        InitialDirContext ctx;

        try {
            ctx = new InitialDirContext(env);
            ctx.close();
        } catch(Exception e) {
            System.out.println("Failed to authenticate user "+ this.username);
            e.printStackTrace();
            throw e;
        }
    }

    void run() {

        long endTime = System.currentTimeMillis() + duration * 60 * 1000;

        long successAttempts = 0;
        long failedAttempts = 0;

        while (System.currentTimeMillis() <= endTime) {
            try {
                authenticate();
                successAttempts++;
                Thread.sleep(sleepDuration);
            } catch (Exception e) {
                failedAttempts++;
            }
        }

        System.out.println("Authentications ran for "+ duration + " minutes.");
        System.out.println("Successful authentications "+ successAttempts);
        System.out.println("Failed authentications "+ failedAttempts);
    }

    private boolean isEmpty(String value) {
        return value == null || "".equals(value);
    }
}
