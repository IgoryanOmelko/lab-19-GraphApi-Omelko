package com.example.lab19_graphapiomelko.helper;

import com.example.lab19_graphapiomelko.database.GraphDB;

public final class StaticData {
    public static GraphDB DB;
    public static int ff=0;
    public static RequestCode LoginToMenuCode = new RequestCode();
    public static RequestCode LoginToRegisterCode = new RequestCode();
    public static void SetCodes() {
        StaticData.LoginToMenuCode.setCode(001);
        StaticData.LoginToRegisterCode.setCode(002);
    }
}

