import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegUserInDb {

    String date = null, telephone = null;

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public void addNewUser() {

        String login = null, f_name = null, l_name = null;

        System.out.println("\nEnter your login");
        try {

            login = br.readLine();
            if (!loginIsBusy(login)) {
                System.out.println("Please. Enter your firs name");
                f_name = br.readLine();
                System.out.println("Please. Enter your last name");
                l_name = br.readLine();
                dateEnteringStage();
                telephoneEnteringStage();

                User user = new User();
                user.setLogin(login);
                user.setFirst_name(f_name);
                user.setLast_name(l_name);
                user.setDate_of_birth(date);
                user.setTelephone(telephone);

                LibWorker libWarker = new LibWorker();
                libWarker.addNewUserToDbUsers(user);
                System.out.println("\nYou were successfully added to the database!");

            } else {
                System.out.println("\nSory, but this login is already in use! Try again with another one.");
                addNewUser();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dateEnteringStage() throws IOException {

        System.out.println("Please. Enter date of birth in format: yyyy-MM-dd");
        date = br.readLine();
        if (dateIsValide(date)) {

        } else {
            System.out.println("You entered date in wrong format");
            dateEnteringStage();
        }
    }

    private void telephoneEnteringStage() throws IOException {

        System.out.println("Please. Enter your phone number");
        telephone = br.readLine();
        if (telephoneIsValid(telephone)) {

        } else {
            System.out.println("You entered telephone in wrong format");
            telephoneEnteringStage();
        }
    }

    private boolean loginIsBusy(String login) throws SQLException {
        DBWarker dbWarker = new DBWarker();
        Statement statement = dbWarker.getConnection().createStatement();
        String busyLogin = "";
        String query = "SELECT login FROM mylibrary.users where login='" + login + "'";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            busyLogin = resultSet.getString("login");
        }
        return login.equals(busyLogin.trim());
    }

    private boolean dateIsValide(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    private boolean telephoneIsValid(String tel) {
        Pattern pattern = Pattern.compile("\\d{3}-\\d{7}");
        Matcher matcher = pattern.matcher(tel);
        if (matcher.matches()) {
            return true;
        } else return false;
    }
}
