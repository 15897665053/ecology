package weaver.interfaces.schedule.HR.QyWX.Common;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ibm.icu.text.SimpleDateFormat;
import weaver.interfaces.schedule.mes.helper.httpPost;

import java.text.ParseException;
import java.util.Date;

public class GLGNet_QYWXCommon {

    //获取企业微信标签人员集合信息
    public static String getRyBybqName ( String token,String tagId){

        //获取标签里面的人员集合信息
        String Url="https://qyapi.weixin.qq.com/cgi-bin/tag/get?access_token="+token+"&tagid="+tagId+"";
        String response=httpPost.doGet(Url);

        return response;




    }
    /*
    *获取员工打卡记录
    *opencheckindatatype （打卡类型。1：上下班打卡；2：外出打卡；3：全部打卡）
    *starttime （获取打卡记录的开始时间。Unix时间戳）
    *endtime （	获取打卡记录的结束时间。Unix时间戳）
    *useridlist （需要获取打卡记录的用户列表）
    * access_token（打卡应用的Secret获取access_token）
    * */
    public static String getUsrtDkByUserList ( String token,String opencheckindatatype,String starttime,String endtime,JSONArray useridlist) throws Exception {

        //获取标签里面的人员集合信息
        String Url="https://qyapi.weixin.qq.com/cgi-bin/checkin/getcheckindata?access_token="+token;
        JSONObject josn = new JSONObject();
        josn.put("opencheckindatatype",opencheckindatatype);
        josn.put("starttime",starttime);

        josn.put("endtime",endtime);
        josn.put("useridlist",useridlist);



        String response=httpPost.postJson(Url,josn).toString();



        return response;

    }


    /**
     * 时间转换成时间戳,参数和返回值都是字符串
     * @param  s
     * @return res
     * @throws ParseException
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        //设置时间模版
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime()/1000;
        res = String.valueOf(ts);
        return res;
    }


    /**
     * 将时间戳转换为时间,参数和返回值都是字符串
     * @param s
     * @return res
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt*1000);
        res = simpleDateFormat.format(date);
        return res;
    }



}
