package com.example.lab19_graphapiomelko.model;

import com.example.lab19_graphapiomelko.helper.StaticData;

public final class User {
    static String name;
    static String password;
    static String token;
    static String cstr = "http://labs-api.spbcoit.ru/lab/graph/api";

    /**
     * Method return name of user
     *
     * @return name of user, what stored in object of class
     */
    public static String getName() {
        return name;
    }

    /**
     * Method set new name for user
     *
     * @param name name, what need to set
     */
    public static void setName(String name) {
        User.name = name;
    }

    /**
     * Method return password of user
     *
     * @return password of user, what stored in object of class
     */
    public static String getPassword() {
        return password;
    }

    /**
     * Method set new password for user
     *
     * @param password password, what need to set
     */
    public static void setPassword(String password) {
        User.password = password;
    }

    /**
     * Method return token of user
     *
     * @return token of user, what stored in object of class
     */
    public static String getToken() {
        return token;
    }

    /**
     * Method set new token for user
     *
     * @param token token, what need to set
     */
    public static void setToken(String token) {
        User.token = token;
    }
    /**
     * Method return connection string of user
     *
     * @return connection string of user, what stored in object of class
     */
    public static String getCSTR() {
        return cstr;
    }

    /**
     * Method set new connection string for user
     *
     * @param cstr connection string, what need to set
     */
    public static void setCSTR(String cstr) {
        User.cstr = cstr;
    }

    /**
     * Method writing user data in the database
     */
    public static void setSettings(){
        StaticData.DB.SetSettingsName(name);
        StaticData.DB.SetSettingsPassword(password);
        StaticData.DB.SetSettingsToken(token);
        StaticData.DB.SetSettingsCSTR(cstr);
    }

    /**
     * Method reading user data from database and set properties
     */
    public static void getSettings(){
        User.name=StaticData.DB.GetSettingsName();
        User.password=StaticData.DB.GetSettingsPassword();
        User.token=StaticData.DB.GetSettingsToken();
        User.cstr=StaticData.DB.GetSettingsCSTR();
    }
}
