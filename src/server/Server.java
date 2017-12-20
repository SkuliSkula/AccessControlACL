package server;

import authentication.Authentication;
import authentication.JSONHandler;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements IPrintServer {
    private Authentication authenticationService;
    private JSONHandler jsonHandler;
    private ServerManager serverManager;
    public Server() throws RemoteException {
        super(1099, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null, null, true));
        try {
            setSslSettings();
            jsonHandler = new JSONHandler();
            authenticationService = new Authentication(jsonHandler);
            serverManager = new ServerManager(jsonHandler,authenticationService);
            System.out.println("Starting server on port 1099...");
        } catch (Exception e) {
            System.out.println("Failed to construct server... " + e.getMessage());
        }
    }
    private void setSslSettings() {
        String password = "Password";
        System.setProperty("javax.net.ssl.debug", "all");
        System.setProperty("javax.net.ssl.keyStore", "C:\\ssl\\serverkeystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", password);
        System.setProperty("javax.net.ssl.trustStore", "C:\\ssl\\servertruststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", password);
    }
    @Override
    public void print(String userName,String fileName, String printer) throws RemoteException {
        try{
            jsonHandler.writeJSONServiceLog(userName,"Print");
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File name: " + fileName + ", Printer: " + printer);
    }
    @Override
    public void queue(String userName) throws RemoteException {
        try{
            jsonHandler.writeJSONServiceLog(userName,"queue");
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("There are 3 pending print jobs");
    }
    @Override
    public void topQueue(String userName, int job) throws RemoteException {
        try{
            jsonHandler.writeJSONServiceLog(userName,"topQueue");
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Print job number " + job + " has been added to the top of the queue");
    }
    @Override
    public void start(String userName) throws RemoteException {
        try{
            jsonHandler.writeJSONServiceLog(userName,"start");
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Printing started...");
    }
    @Override
    public void stop(String userName) throws RemoteException {
        try{
            jsonHandler.writeJSONServiceLog(userName,"stop");
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Printing stopped...");
    }
    @Override
    public void restart(String userName) throws RemoteException {
        try{
            jsonHandler.writeJSONServiceLog(userName,"restart");
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Restarting printer...");
    }
    @Override
    public void status(String userName) throws RemoteException{
        try{
            jsonHandler.writeJSONServiceLog(userName,"status");
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The server status is good...");
    }
    @Override
    public void readConfig(String userName,String parameter) throws RemoteException {
        try{
            jsonHandler.writeJSONServiceLog(userName,"readConfig");
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("readConfig with " + parameter + " parameter");
    }
    @Override
    public void setConfig(String userName,String parameter) throws RemoteException {
        try{
            jsonHandler.writeJSONServiceLog(userName,"setConfig");
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("setConfig with " + parameter + " parameter");
    }
    @Override
    public ArrayList<String> handleLogIn(String userName, String password) throws RemoteException {
       return authenticationService.handleLogIn(userName,password);
    }

    public boolean registerClient(String userName, String password, ArrayList<String> operations) throws IOException {
        return serverManager.registerClient(userName,password, operations);
    }

    public ArrayList<String> getUserACL(String accessRight) {
        return serverManager.getUserACL(accessRight);
    }
    /*Main to create the users*/
    public static void main(String[] args) throws Exception {
        Server server = new Server();

        server.registerClient("Alice", "Alice1234",
                server.getUserACL("Manager"));
        server.registerClient("BobBob", "BobBob1234",
                server.getUserACL("Technician"));
        server.registerClient("Cecilia", "Cecilia1234",
                server.getUserACL("PowerUser"));
        server.registerClient("Erica", "Erica1234",
                server.getUserACL("User"));

    }
}
