package com.example.appbansach.Service;

public class APIService {

    private static String base_url = "https://ntdntd1271272003.000webhostapp.com/Server/";
    //private static String base_url = "https://192.168.203.1/bansach/";
    //public static final String BASE_URL = "http://127.0.0.1:3333/";
    public static DataService getService(){
        return APIRetrofitClient.getClient(base_url).create(DataService.class);
    }
}
