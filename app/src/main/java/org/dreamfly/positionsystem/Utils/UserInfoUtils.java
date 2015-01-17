package org.dreamfly.positionsystem.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by asus on 2015/1/17.
 * 管理个人信息的文件工具类
 */
public class UserInfoUtils {


    private File userInfoFile;

    private FileUitls mFileUitls;

    private FileOutputStream writeUserInfo;

    private FileInputStream readUserInfo;

    private String userInfoTag = "unlogin#";//初始化的个人信息

    private String[] userInfoArray;

    /**
     * 构造函数，如果本地缓存的文件不存在就先创建
     */
    public UserInfoUtils() {
        this.mFileUitls = new FileUitls();
        this.userInfoFile = new File(this.mFileUitls.getCacheFileDir(), "userinfo.txt");
        if (!this.userInfoFile.exists()) {
            try {
                this.userInfoFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.initialUserInfo();//没有文件的时候才写入未登录状态
        }else{//存在文件就不会改动文件中的内容

        }

    }

    /**
     * 这个函数是在默认没有本地文件的时候写入的文件是状态是没有登录的
     */
    private void initialUserInfo() {
        try {
            this.writeUserInfo = new FileOutputStream(this.userInfoFile);
            this.writeUserInfo.write(this.userInfoTag.getBytes());
            this.writeUserInfo.flush();
            this.writeUserInfo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 去读本地的文件,返回一个字符串,如果文件不存在,或者读取失误,直接返回空引用
     * @return
     */
    private String getUserInfo() {
        StringBuffer mStrBuf = new StringBuffer();
        String tmpStr = new String();
        try {
            this.readUserInfo = new FileInputStream(this.userInfoFile);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(this.readUserInfo));
            while ((tmpStr = bufReader.readLine()) != null) {
                mStrBuf.append(tmpStr);
            }
            this.readUserInfo.close();
            bufReader.close();
            return (mStrBuf.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 通过传递个HASHMAP来遍历得到MAP的所有键值对，并且写入文件,以“#”区分
     * @param userInfo
     */
    public void updateUserInfo(Map<String, String> userInfo) {
        StringBuffer strBuf = new StringBuffer();
        Iterator iterator = userInfo.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            strBuf.append(entry.getValue() + "#");
        }
        try {
            this.writeUserInfo = new FileOutputStream(this.userInfoFile);
            this.writeUserInfo.write(strBuf.toString().getBytes());
            this.writeUserInfo.flush();
            this.writeUserInfo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 验证是否登录的函数,如果并且将文件的字符串切割开来，作为成员空间的字符串数组
     * @return
     */
    public boolean isLogin() {
        String userinfo = this.getUserInfo();
        this.userInfoArray = userinfo.split("#");
        if (this.userInfoArray[0].equals("unlogin")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 返回类的成员的字符串数组,如果数组为空，则返回空引用
     * @return
     */
    public String[] getUserInfoArray() {
        if (this.userInfoArray != null) {
            return (this.userInfoArray);
        }else{
            return null;
        }
    }

    /**
     * 注销登录状态,将文件改成未登录状态
     */
    public void clearUserInfo() {
        this.initialUserInfo();
        this.userInfoArray = null;
    }

}
