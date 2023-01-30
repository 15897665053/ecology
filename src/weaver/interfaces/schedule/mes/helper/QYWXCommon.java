package weaver.interfaces.schedule.mes.helper;


import java.util.Calendar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ibm.icu.text.SimpleDateFormat;


public class QYWXCommon {
    public static String ChangeToken ( String corpid,String secret){
        String agentId="1000014";
        //更新token
        String Url="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpid+"&corpsecret="+secret+"";
        String response=httpPost.doGet(Url);
        JSONObject jsonObject= (JSONObject) JSONObject.parse(response);
        String token = jsonObject.getString("access_token");


        return token;

    }
   //MES 推送
    public static String SendQywxMesage (String title,String agentId, String token,String itemcode,String itemName
            ,String whName,String StoreQty,String Qty) throws Exception {

        //更新token
        String Url="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token;
        Calendar cal = Calendar.getInstance();
        String chonseUser = itemcode.substring(0,1);
        String User="FX11663";
        if(chonseUser.equals("S"))
        {
            User +="|FX00015|FX00096";
        }else{

            User +="|FX00326";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
       String applyDate= formatter.format(cal.getTime());
        String description = "<div class=\"gray\">"+applyDate+"</div> " +
                "<div class=\"normal\">备件编码："+itemcode+"</div>" +
                "<div class=\"normal\">备件名称："+itemName+"</div>" +
                "<div class=\"normal\">存储地点："+whName+"</div>" +

                "<div class=\"normal\">&nbsp;&nbsp;&nbsp;水位值："+Qty+"</div>" +
                "<div class=\"normal\">当前库存："+StoreQty+"</div>" +



                "<div class=\"highlight\">" +
                "模具备件库存数量已低于水位值！</div>";
        JSONObject DataJson = new JSONObject();
        DataJson.put("title",title);
        DataJson.put("description",description);
        DataJson.put("url","#");
        DataJson.put("btntxt","更多");
        JSONObject josn = new JSONObject();
        josn.put("touser",User);
        josn.put("msgtype","textcard");
        josn.put("agentid",agentId);
        josn.put("textcard",DataJson);
        String response=httpPost.postJson(Url,josn).toString();



        return response;

    }


    //MES 推送
    public static String MESSendQywxMesageURl (String linkUrl ,String title,String agentId, String token,
            String deptname ,String Qty ,String User) throws Exception {

        //更新token
        String Url="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token;
        Calendar cal = Calendar.getInstance();


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String applyDate= formatter.format(cal.getTime());
        String description = "<div class=\"gray\">"+applyDate+"</div> " +
                "<div class=\"normal\">部门名称："+deptname+"</div>" +
                "<div class=\"normal\">待保养模具数量："+Qty+"</div>" +





                "<div class=\"highlight\">" +deptname+"共有"+Qty+"套模具需保养，请尽快保养！"+
                "</div>";
        JSONObject DataJson = new JSONObject();
        DataJson.put("title",title);
        DataJson.put("description",description);
        DataJson.put("url",linkUrl);
        DataJson.put("btntxt","详情");
        JSONObject josn = new JSONObject();
        josn.put("touser",User);
        josn.put("msgtype","textcard");
        josn.put("agentid",agentId);
        josn.put("textcard",DataJson);
        String response=httpPost.postJson(Url,josn).toString();



        return response;

    }


    public static String MESSendQywxMesageURlandMessage (String linkUrl ,String description,String title,String agentId, String token,
                                               String User) throws Exception {

        //更新token
        String Url="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token;






        JSONObject DataJson = new JSONObject();
        DataJson.put("title",title);
        DataJson.put("description",description);
        DataJson.put("url",linkUrl);
        DataJson.put("btntxt","详情");
        JSONObject josn = new JSONObject();
        josn.put("touser",User);
        josn.put("msgtype","textcard");
        josn.put("agentid",agentId);
        josn.put("textcard",DataJson);
        String response=httpPost.postJson(Url,josn).toString();



        return response;

    }

    //HR生日推送
    public static String SendQywxBirthdayMesage (String LinkUrl,String ConverUrl,String desc,String agentId, String token,String tltle,String EmpNo) throws Exception {

        //更新token
        String Url="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token;




        JSONObject DataJson = new JSONObject();
        DataJson.put("title",tltle);
        DataJson.put("description",desc);
        DataJson.put("url",LinkUrl);
        DataJson.put("picurl",ConverUrl);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(DataJson);
        JSONObject NewsJson = new JSONObject();
        NewsJson.put("articles",jsonArray);




        JSONObject josn = new JSONObject();
        josn.put("touser",EmpNo);
        josn.put("msgtype","news");
        josn.put("agentid",agentId);
        josn.put("news",NewsJson);
        String response=httpPost.postJson(Url,josn).toString();



        return response;

    }

    //推送企业微信带url
    public static String SendQywxMesageCardaddUrl(String linkUrl, String title,String agentId, String token
       ) throws Exception {

        //发送消息
        String Url="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token;
        Calendar cal = Calendar.getInstance();

        String User="FX00001|FX00002|FX10324|FX00153|FX03272";


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String applyDate= formatter.format(cal.getTime());
        String description = "<div class=\"gray\">"+applyDate+"</div> "
             ;
        JSONObject DataJson = new JSONObject();
        DataJson.put("title",title);
        DataJson.put("description",description);
        DataJson.put("url",linkUrl);
        DataJson.put("btntxt","详情");
        JSONObject josn = new JSONObject();
        josn.put("touser",User);
        josn.put("msgtype","textcard");
        josn.put("agentid",agentId);
        josn.put("textcard",DataJson);
        String response=httpPost.postJson(Url,josn).toString();



        return response;

    }



    //推送企业微信带url加工号
    public static String SendQywxMesageCardaddUrlandgh(String linkUrl, String title,String description, String agentId, String token,
                                                       String gh) throws Exception {

        //发送消息
        String Url="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token;







        JSONObject DataJson = new JSONObject();
        DataJson.put("title",title);
        DataJson.put("description",description);
        DataJson.put("url",linkUrl);
        DataJson.put("btntxt","详情");
        JSONObject josn = new JSONObject();
        josn.put("touser",gh);
        josn.put("msgtype","textcard");
        josn.put("agentid",agentId);
        josn.put("textcard",DataJson);
        String response=httpPost.postJson(Url,josn).toString();



        return response;

    }

    //推送MarkDown消息
    public static String SendQywxMesageMarkDownandgh( String content, String agentId, String token,
                                                       String gh) throws Exception {

        //发送消息
        String Url="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token;
        JSONObject DataJson = new JSONObject();

        DataJson.put("content",content);
        JSONObject josn = new JSONObject();
        josn.put("touser",gh);
        josn.put("msgtype","markdown");
        josn.put("agentid",agentId);
        josn.put("markdown",DataJson);
        josn.put("enable_duplicate_check",0);
        josn.put("duplicate_check_interval",1800);
        String response=httpPost.postJson(Url,josn).toString();



        return response;

    }




}
