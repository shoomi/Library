import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurentTime {
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getCurrentTime (){
        Date date = new Date();
        return df.format(date);
    }
}
