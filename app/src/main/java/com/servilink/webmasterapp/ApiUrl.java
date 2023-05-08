package com.servilink.webmasterapp;

public class ApiUrl {
    public static String commanapi = "http://192.168.1.9:8080/";

   // public static String commanapi = "http://192.168.1.4:8080/webAppServ/";
    //public static String commanapi = "http://192.168.0.11:8080/";
    public static String generateToken = commanapi + "generate-token";
    public static String getuserdetail = commanapi + "user/";
    public static String getImageDetail=commanapi+"user/getImage";
    public static String getDeviceList = commanapi + "plc/getPlcOfCreatedBy";
    public static String getPlcList = commanapi + "form/getFormPlcId/";
    public static String putManualFloatdata = commanapi + "manual/addManualFloatData";
    public static String putManualStringdata = commanapi + "manual/addManualStringData";
    public static String updateManualFloatdata = commanapi + "manual/updateManualFloatData";
    public static String getManualFloatdata = commanapi + "data/manualFloatDataUpdate";
    public static String getManualStringdata = commanapi + "data/manualStringDataUpdate";
    public static String updateManualStringdata = commanapi + "manual/updateManualStringData";
    public static String getPlcOfCreatedBy=commanapi+"form/getPlcOfCreatedBy";
    public static String getallplc=commanapi+"plc/getAllPlcs";
    public static String getdashboardName=commanapi+"dashboard/getDashboardUsingPLCId/";
    public static String getDashboardLayout=commanapi+"dashboardLayout/getAllDashboardLayout/";

}
