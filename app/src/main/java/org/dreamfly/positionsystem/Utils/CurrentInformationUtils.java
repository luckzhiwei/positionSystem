package org.dreamfly.positionsystem.Utils;

import java.util.Calendar;

/**
 * 该类用于加载一些初始化信息
 * Created by zhengyl on 15-1-18.
 */
public class CurrentInformationUtils {

    private String getCurrentTime;
    private int month, day, hour, minute;
    Calendar c = Calendar.getInstance();

    public String getCurrentTime() {

        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        if (minute < 10) {
            getCurrentTime = month + "-" + day + "  " + hour + ":" + "0" + minute;
        } else {
            getCurrentTime = month + "-" + day + "  " + hour + ":" + minute;
        }

        return getCurrentTime;

    }

    public String setFirstLocation(int i) {

        String[] position = new String[]{"上次的位置:北京天安门", "上次的位置:长江三峡大坝",
                "上次的位置:电子科技大学", "上次的位置:西安长安街", "上次的位置:山东青岛", "上次的位置:天津廊坊",
                "上次的位置:夏威夷群岛"};
        return position[i];
    }

    public String setFirstDeviceName(int i) {

        String[] device = new String[]{"Samsung i9260", "Samsung s4",
                "iPhone6 plus", "Mi2S_note", "LG G3 D859", "Lenovo S2", "Meizu MX4"};
        return device[i];
    }


}
