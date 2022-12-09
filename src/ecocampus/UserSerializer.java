package ecocampus;

import ecocampus.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;

public class UserSerializer {
    public static void saveUsers(ObservableList<User> users) throws IOException {


        try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.json"))) {
            for (User user : users) {
                String line = user.getId()+","+user.getName()+","+user.isAdmin()+",";
                for (int i = 0 ; i< user.getUpVotedPosts().size();i ++) {
                    line += String.valueOf(user.getUpVotedPosts().get(i));
                    if (i != user.getUpVotedPosts().size()-1) line += ";";
                }
                bw.write(line);
                bw.newLine();
            }
        }
    }

    public static ObservableList<User> loadUsers() throws IOException {
        //read json file data to String
        ArrayList<User> users = new ArrayList();

        try (BufferedReader bw = new BufferedReader(new FileReader("users.json"))) {
            String line = bw.readLine();
            while (line != null){

                String[] parts = line.split(",");
                ArrayList<Integer> ints = new ArrayList<>();
                if (parts.length == 4 ){
                String[] ez =  parts[3].split(";");
                for (int i = 0 ; i< ez.length;i++) {
                    ints.add(Integer.valueOf(ez[i]));
                }
                }
                User r = new User(Integer.valueOf(parts[0]),parts[1],parts[2].equals("true") ? true: false, ints);
                users.add(r);
                line = bw.readLine();

            }
        }
        return FXCollections.observableArrayList(users);
    }

}