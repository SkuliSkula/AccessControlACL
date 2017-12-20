import client.Client;
import server.Server;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

public class TestMain {

    public static void main(String[] args) {

        try{
            Server server = new Server();
            Registry registry = LocateRegistry.createRegistry(1099, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null, null, true));
            registry.bind("server", server);
            Client client = new Client(registry);

            Scanner sc = new Scanner(System.in);
            int operation;
            do {
                if (!client.isLoggedIn())
                    logInMenu();

                operation = sc.nextInt();
                handleOperation(client,operation);

            }while (operation != 0);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void handleOperation(Client client, int operation) throws Exception {
        ArrayList<String> operationsList = null;
        Scanner sc = new Scanner(System.in);
        try {
            if(!client.isLoggedIn()) {
                String userName;
                String password;
                switch (operation) {
                    case 0:
                        System.out.println("Good bye...");
                        System.out.println("Session terminated...");
                        break;
                    case 1:
                        System.out.println("Enter your user name");
                        userName = sc.nextLine();
                        System.out.println("Enter your password");
                        password = sc.nextLine();
                        operationsList= client.handleLogIn(userName, password);
                        if(!operationsList.isEmpty()){
                            operationsList.add(0, "Quit");
                            client.setClientName(userName);
                            client.logClientIn(true);
                            for(int i = 0; i < operationsList.size(); i++){
                                System.out.println(i + ") " + operationsList.get(i));
                            }
                        }
                        else
                            System.out.println("Client could NOT be logged in");
                        break;
                    case 2:
                        break;
                }
            }else {
                int selectedOperation = sc.nextInt();
                String op = "Quit";
                if(operationsList!= null)
                    op = operationsList.get(selectedOperation);

                switch (op){
                    case "Quit":
                        client.logClientIn(false);
                        System.out.println(client.getClientName() + " logged out.");
                        break;
                    case "print":
                        client.print("MyFileName", "My printer");
                        break;
                    case "queue":
                        client.queue();
                        break;
                    case "topQueue":
                        client.topQueue(10);
                        break;
                    case "start":
                        client.start();
                        break;
                    case "stop":
                        client.stop();
                        break;
                    case "restart":
                        client.restart();
                        break;
                    case "status":
                        client.serverStatus();
                        break;
                    case "readConfig":
                        client.readConfig("Some parameter");
                        break;
                    case "setConfig":
                        client.setConfig("Some other parameter");
                        break;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void logInMenu() {
        System.out.println();
        System.out.println("******Print server*******");
        System.out.println("Select an operation");
        System.out.println("0) Quit - terminate");
        System.out.println("1) Login");
        System.out.println();
    }
}
