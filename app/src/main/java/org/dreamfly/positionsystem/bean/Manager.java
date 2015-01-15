package org.dreamfly.positionsystem.bean;

/**
 * Created by lzw on 2015/1/15.
 * 被管理者类的信息
 */
public class Manager {

            private int dataBaseID;//数据库中的主键

            private String  deviceName;//设备名

            private String  lastDateTouch;//最近一次联系的时间

            private String   mangerMarks;//备注

            private boolean  isOnLine;//是否在线

            private String  lastLocation;//最近一次定位信息

            public void setDataBaseID(int dataBaseID)
            {
                this.dataBaseID=dataBaseID;
            }

            public int getDataBaseID()
            {
                return (this.dataBaseID);
            }

            public void setDeviceNma(String deviceName)
            {
                this.deviceName=deviceName;
            }

            public String getDeviceName()
            {
               return(this.deviceName);
            }

            public void  setLastDateTouch(String lastDateTouch)
            {
                this.lastDateTouch=lastDateTouch;
            }

            public String getLastDateTouch()
            {
                return(this.lastDateTouch);
            }

            public void setMangerMarks(String mangerMarks)
            {
                this.mangerMarks=mangerMarks;
            }

            public String getMangerMarks()
            {
                return(this.mangerMarks);
            }

            public  void setLastLocation(String lastLocation)
            {
                this.lastLocation=lastLocation;
            }

            public String getLastLocation()
            {
                return(this.lastLocation);
            }

            public void setIsOnLine(boolean isOnLine)
            {
                this.isOnLine=isOnLine;
            }
            public boolean getOnLine()
            {
                return(this.isOnLine);
            }

}
