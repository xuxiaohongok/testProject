package com.zhidian3g.dsp.adPostback.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by cjl on 2016/3/8.
 */
public class PropertiesUtil {

    public static final String MD5_SIGN_KEY = "MD5.sign.key";

    public static final String EMAIL_SMTP = "email.smtp";

    public static final String EMAIL_ACCOUNT = "email.account";

    public static final String EMAIL_PASSWORD = "email.password";

    public static final String EMAIL_RECEIVER = "email.receiver";

    public static final String ADVIEW_ENCKEYSTR = "adView.encKeyStr";

    public static final String ADVIEW_SIGNKEYSTR = "adView.signKeyStr";

    private final static Properties properties = new Properties();

    static {
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        System.out.println(PropertiesUtil.getValue("connection.url"));
    }
}
