package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IPrintServer extends Remote{
    void print(String userName, String fileName, String printer) throws RemoteException;
    void queue(String userName) throws RemoteException;
    void topQueue(String userName, int job) throws RemoteException;
    void start(String userName) throws RemoteException;
    void stop(String userName) throws RemoteException;
    void restart(String userName) throws RemoteException;
    void status(String userName) throws RemoteException;
    void readConfig(String userName, String parameter) throws RemoteException;
    void setConfig(String userName, String parameter) throws RemoteException;
    ArrayList<String> handleLogIn(String userName, String password) throws RemoteException;
}
