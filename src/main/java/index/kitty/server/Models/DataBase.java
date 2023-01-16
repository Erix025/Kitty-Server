package index.kitty.server.Models;

import java.io.*;
import java.util.ArrayList;

public class DataBase {
    public ArrayList<User> UserDataBase = new ArrayList<>();

    public DataBase() {
        LoadData();
    }
    public void LoadData(){
        UsersLoad();
    }
    public void SaveData(){
        UsersSave();
    }
    private void UsersLoad() {
        //final String PATH = DataBase.class.getClassLoader().getResource("DataBase/Users.data").getPath();
        final String PATH = "DataBase/Users.data";

        try (BufferedReader reader = new BufferedReader(new FileReader(PATH))) {
            String line = reader.readLine();
            User user = new User();
            while (line != null) {
                String key, value;
                //analysis
                key = line.substring(0, line.indexOf('='));
                value = line.substring(line.indexOf('=') + 1);
                //judge
                if (key.equals("UserID")) {
                    user.setID(value);
                } else if (key.equals("Password")) {
                    user.setPassword(value);
                    UserDataBase.add(user);
                    user = new User();
                }
                //update
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void UsersSave() {
        //final String PATH = DataBase.class.getClassLoader().getResource("DataBase/Users.data").getPath();
        final String PATH = "DataBase/Users.data";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH))) {
            String line;
            for (User user : UserDataBase) {
                line = "UserID=" + user.getID() + '\n';
                writer.write(line);
                line = "Password=" + user.getPassword() + '\n';
                writer.write(line);
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}