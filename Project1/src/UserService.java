
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author windows_OS
 */
public class UserService {
  
    private final String FILE_PATH = "Users.txt";

    public void saveUser(User user) {
        try (FileWriter fw = new FileWriter(FILE_PATH, true)) {
            fw.write(user.getUsername() + "," + user.getPassword() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkLogin(String username, String password) {
    try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {

        String line;

        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");

            // إنشاء كائن User باستخدام التغليف
            User user = new User(data[0], data[1]);

            // التحقق من التطابق
            if (user.getUsername().equals(username) &&
                user.getPassword().equals(password)) {
                return true; // نجاح تسجيل الدخول
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false; // فشل تسجيل الدخول
}
}
