package authentication;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JSONHandler {
    private final String LOGIN_FILE_PATH = "C:\\Git\\AccessControlACL\\login\\logins.json";
    private final String SERVICE_LOG_FILE_PATH = "C:\\Git\\AccessControlACL\\login\\servicelog.json";
    private final String OPERATIONS_FILE_PATH = "C:\\Git\\AccessControlACL\\acl\\operations.json";
    private final String PERMISSIONS_FILE_PATH = "C:\\Git\\AccessControlACL\\acl\\permissions.json";
    public JSONHandler() {
        try {
            //createJSONLoginFile();
            createJSONServiceLogFile();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean writeJSON(String userName, String password,String salt, ArrayList<String> operations) throws IOException {
        JSONObject mainObject = readJSON(LOGIN_FILE_PATH);
        JSONArray arr = (JSONArray) mainObject.get("logins");

        if(checkIfUserNameExists(arr,userName)) {
            System.out.println("That user name is taken, please choose another one");
            return false;
        }
        JSONObject valueObj = new JSONObject();
        valueObj.put("UserName", userName);
        valueObj.put("Password", password);
        valueObj.put("Salt", salt);
        valueObj.put("operations", operations);
        arr.add(valueObj);

        try(FileWriter file = new FileWriter(LOGIN_FILE_PATH)) {
            file.write(mainObject.toJSONString());
            System.out.println("User added successfully...");
        }
        return true;
    }

    public void writeJSONServiceLog(String userName, String operation) throws IOException {
        JSONObject mainObject = readJSON(SERVICE_LOG_FILE_PATH);
        JSONArray arr = (JSONArray) mainObject.get("operations");
        JSONObject values = new JSONObject();
        values.put("userName", userName);
        values.put("operation", operation);
        values.put("timestamp", System.currentTimeMillis());
        arr.add(values);
        try(FileWriter file = new FileWriter(SERVICE_LOG_FILE_PATH)) {
            file.write(mainObject.toJSONString());
            System.out.println("Service logged successfully...");
        }
    }
    public JSONObject readJSON(String filePath) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try{
            Object obj = parser.parse(new FileReader(filePath));
            jsonObject = (JSONObject) obj;
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public void createJSONLoginFile() throws IOException{
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        obj.put("logins", list);
        try(FileWriter file = new FileWriter(LOGIN_FILE_PATH)) {
            file.write(obj.toJSONString());
            System.out.println("Login file created successfully...");
        }
    }
    public void createJSONServiceLogFile() throws IOException{
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        obj.put("operations", list);
        try(FileWriter file = new FileWriter(SERVICE_LOG_FILE_PATH)) {
            file.write(obj.toJSONString());
            System.out.println("Service log file created successfully...");
        }
    }
    public boolean checkIfUserNameExists(JSONArray passwordKeeper, String userName) {
        boolean userFound = false;
        for(int i = 0; i < passwordKeeper.size(); i++) {
            JSONObject obj = (JSONObject) passwordKeeper.get(i);
            if(userName.equals(obj.get("UserName"))){
                userFound = true;
                break;
            }
        }
        return userFound;
    }

    public String getLoginFilePath() {
        return LOGIN_FILE_PATH;
    }

    public String getServiceLogFilePath() {
        return SERVICE_LOG_FILE_PATH;
    }

    public String getOPERATIONS_FILE_PATH() {
        return OPERATIONS_FILE_PATH;
    }

    public String getPERMISSIONS_FILE_PATH() {
        return PERMISSIONS_FILE_PATH;
    }
}
