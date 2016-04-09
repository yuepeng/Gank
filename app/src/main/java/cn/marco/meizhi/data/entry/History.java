package cn.marco.meizhi.data.entry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class History {

    public boolean error;
    public List<String> results;

    public int[] getNearDate() {
        int[] date = new int[3];
        if (results != null && results.size() > 0) {
            String dateStr = results.get(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                Date parse = sdf.parse(dateStr);
                calendar.setTime(parse);
                accessDate(calendar, date);
            } catch (ParseException e) {
                accessDate(calendar, date);
            }
        }
        return date;
    }

    private void accessDate(Calendar calendar, int[] date){
        date[0] = calendar.get(Calendar.YEAR);
        date[1] = calendar.get(Calendar.MONTH) + 1;
        date[2] = calendar.get(Calendar.DAY_OF_MONTH);
    }

}
