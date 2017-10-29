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

    BufferedReader br;
    String date = null, telephone = null;

    public RegUserInDb() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    public void addNewUser() {
        User user = new User();
        String login = null;

        System.out.println("\nEnter your login");

        try {
            login = br.readLine();

            while (loginIsBusy(login)) {
                System.out.println("\nSory, but this login is already in use! Try again with another one.");
                login = br.readLine();
            }

            user.setLogin(login);

            System.out.println("Please. Enter your firs name");
            user.setFirst_name(br.readLine());
            System.out.println("Please. Enter your last name");
            user.setLast_name(br.readLine());

            enteringDate();
            enteringTelephone();

            user.setDate_of_birth(date);
            user.setTelephone(telephone);

            new LibWorker().addNewUserToDbUsers(user);
            System.out.println("\nYou were successfully added to the database!");

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void enteringDate() throws IOException {

        System.out.println("Please. Enter date of birth in format: yyyy-MM-dd");
        date = br.readLine();
        while (!dateIsValide(date)) {
            System.out.println("You entered date in wrong format. Try again");
            date = br.readLine();
        }
    }

    private void enteringTelephone() throws IOException {

        System.out.println("Please. Enter your phone number in format 0**-*******");
        telephone = br.readLine();
        while (!telephoneIsValid(telephone)) {
            System.out.println("You entered telephone in wrong format. Try again");
            telephone = br.readLine();
        }
    }

    private boolean loginIsBusy(String login) throws SQLException {
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();
        String query = "SELECT login FROM mylibrary.users where login='" + login + "'";
        ResultSet rs = statement.executeQuery(query);
        if (rs.next()) {
            return true;
        }
        return false;
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
        }
        return false;
    }
}
