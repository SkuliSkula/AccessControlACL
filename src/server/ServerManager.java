package server;

import authentication.Authentication;
import authentication.JSONHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


/*Class to manage the server. Create users, change users, change user roles etc...*/
public class ServerManager {
    private JSONHandler jsonHandler;
    private Authentication authentication;
    private ArrayList<String> operations;
    public ServerManager(JSONHandler jsonHandler, Authentication authentication){
        this.jsonHandler = jsonHandler;
        this.authentication = authentication;
        this.operations = new ArrayList<>();
    }

    public boolean registerClient(String userName, String password, ArrayList<String> operations) throws IOException {
        if(!checkPasswordStrength(password)) {
            System.out.println("The password needs to contain upper case letter, characters and numbers and be 8 characters or longer");
            return false;
        }else if(!checkUserName(userName)) {
            System.out.println("The user name has to be longer than 5 characters");
            return false;
        }
        else {
            return authentication.registerUser(userName, password, operations);
        }
    }

    public ArrayList<String> getUserACL(String accessRight) {
        ArrayList<String> toReturn = new ArrayList<>();
        JSONObject permissions = jsonHandler.readJSON(jsonHandler.getPERMISSIONS_FILE_PATH());
        JSONArray permission = (JSONArray) permissions.get(accessRight);
        JSONObject operations = jsonHandler.readJSON(jsonHandler.getOPERATIONS_FILE_PATH());
        JSONArray operationList = (JSONArray) operations.get("operations");

        for(int i = 0; i< permission.size(); i++) {
            for(int j = 0; j < operationList.size(); j++) {
                if(permission.get(i).equals(operationList.get(j))){
                    toReturn.add((String) operationList.get(j));
                }
            }
        }
        return  toReturn;
    }
    private boolean checkPasswordStrength(String password){
        if(password.length() < 8){
            return false;
        }
        return password.matches("(([A-Z].*[0-9])|([0-9].*[A-Z]))");
    }
    private boolean checkUserName(String userName) {
        return userName.length() > 4;
    }
}
