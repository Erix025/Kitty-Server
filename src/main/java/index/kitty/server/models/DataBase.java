package index.kitty.server.models;

import index.kitty.server.Main;

import java.io.*;
import java.util.ArrayList;

public class DataBase {
    public final ArrayList<User> UserDataBase = new ArrayList<>();

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
            Main.mainServer.logger.severe("File \"Users.data\" not found.");
        } catch (IOException e) {
            Main.mainServer.logger.severe("IOException: Cannot read the file \"Users.data\".");
        }
    }

    private void UsersSave() {
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
            Main.mainServer.logger.severe("IOException: Cannot save the file \"Users.data\".");
        }
    }
}