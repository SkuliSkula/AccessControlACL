package authentication;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Authentication {
    private JSONHandler jsonHandler;
    private String salt;
    public Authentication(JSONHandler jsonHandler) {
        this.jsonHandler = jsonHandler;
    }
    public ArrayList<String> handleLogIn(String userName, String password){
        return verifyLogIn(userName, password);

    }
    public boolean registerUser(String userName, String password, ArrayList<String> operations) throws IOException {
        return jsonHandler.writeJSON(userName,hashAndSaltPassword(password),salt, operations);
    }
    private ArrayList<String> verifyLogIn(String userName, String password) {
        ArrayList<String> operationList = new ArrayList<>();
        JSONObject jsonObject = jsonHandler.readJSON(jsonHandler.getLoginFilePath());
        JSONArray passwordKeeper = (JSONArray) jsonObject.get("logins");

        if(passwordKeeper == null) {
            System.out.println("Problem with login file");
        }else{
            for(int i = 0; i < passwordKeeper.size(); i++) {
                JSONObject obj = (JSONObject) passwordKeeper.get(i);
                String pass = (String) obj.get("Password");
                String salt = (String) obj.get("Salt");
                if(userName.equals(obj.get("UserName")) && pass.equals(unHashPassword(password,salt))){
                    JSONObject operations = (JSONObject) passwordKeeper.get(i);
                    JSONArray op = (JSONArray) operations.get("operations");
                    for(int j = 0; j < op.size(); j++)
                        operationList.add((String) op.get(j));
                    System.out.println(userName + " has been logged in...");
                    break;
                }
            }
        }
        return operationList;
    }
    private String hashAndSaltPassword(String password) {
        salt = RandomStringUtils.randomAscii(20);
        return DigestUtils.sha256Hex(password + salt);
    }

    private String unHashPassword(String password,String salt) {
        return DigestUtils.sha256Hex(password + salt);
    }
}