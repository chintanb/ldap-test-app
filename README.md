# Simple test application to test LDAP authentication using Java

1) Run `mvn package` to build a jar.

2) Run following command to test LDAP connectivity.
java -jar ldap-test-app-1.0-SNAPSHOT.jar --host=acs2016server.acs2016.dev.datacard.com --port=389 --userId=<user-GUID> --password=**** --runDuration=1

**Information about arguments:**

host - The AD server url
port - The port AD server is listening on
userId - GUID of the user you want to authenticate with
password - Password of the authenticating user
runDuration - Duration to run the application (in minutes)
connectTimeout - (optional) LDAP connect timeout - default is 5 seconds
connectTimeout - (optional) LDAP read timeout - default is 5 seconds
sleepDuration - (optional) time to sleep between two authentications - default is 1 second 

***Sample Run***

```
java -jar ldap-test-app-1.0-SNAPSHOT.jar
    --host=acs2016server.acs2016.dev.datacard.com 
    --port=389 --userId=67b163e2-1bb7-4213-b62a-ff76f823dd99 
    --password=Snapple2006 
    --runDuration=1
    
Running the LDAP authentication test and only errors will be logged.
Initializing LDAP test runner with host acs2016server.acs2016.dev.datacard.com, port 389, SSL false, username 67b163e2-1bb7-4213-b62a-ff76f823dd99 connect-timeout 5000 ms read-timeout 5000 ms, duration 1 minutes, sleep-interval 1000 ms.
Authentications ran for 1 minutes.
Successful authentications 51
Failed authentications 0
```

### Future work
Support LDAP secure.