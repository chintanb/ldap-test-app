package com.entrust.ldapTestApp;

public class LdapTestApp {

    private static final String HOST = "--host";
    private static final String PORT = "--port";
    private static final String SSL = "--ssl";
    private static final String USERID = "--userId";
    private static final String PASSWORD = "--password";
    private static final String CONNECT_TIMEOUT = "--connectTimeout";
    private static final String READ_TIMEOUT = "--readTimeout";
    private static final String RUN_DURATION = "--runDuration";
    private static final String SLEEP_DURATION = "--sleepDuration";

    public static void main( String[] args ) {

        String host = getParam(args, HOST, true);
        String port = getParam(args, PORT, true);
        String isSsl = getParam(args, SSL, false);
        String userId = getParam(args, USERID, true);
        String password = getParam(args, PASSWORD, true);
        String runDuration = getParam(args, RUN_DURATION, true);

        String connectTimeout = getParam(args, CONNECT_TIMEOUT, false);
        String readTimeout = getParam(args, READ_TIMEOUT, false);
        String sleepDuration = getParam(args, SLEEP_DURATION, false);

        System.out.println("Running the LDAP authentication test and only errors will be logged.");

        DirectoryAuthenticator authenticator = new DirectoryAuthenticator(host, Integer.parseInt(port), isSsl, userId, password, connectTimeout,
                                                                            readTimeout, Integer.parseInt(runDuration), sleepDuration);

        authenticator.run();

        System.exit(0);
    }

    private static String getParam(String[] args, String param, boolean isRequired) {

        for(String arg : args) {
            if (arg.startsWith(param) && arg.contains("=")) {
                String value = arg.substring(arg.indexOf("=") + 1);
                if (value.equals("") && isRequired) {
                    throw new RuntimeException("Missing required param "+ param);
                }
                return value;
            }
        }

        if (isRequired) {
            throw new RuntimeException("Missing required param "+ param);
        } else {
            // ignore the param and return empty string
            return "";
        }
    }
}
