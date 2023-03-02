package weaver.interfaces.schedule.HR.QyWX.Kq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ibm.icu.text.SimpleDateFormat;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;

import weaver.interfaces.schedule.mes.helper.QYWXCommon;
import weaver.monitor.cache.CacheFactory;
import  weaver.interfaces.schedule.HR.QyWX.Common.GLGNet_QYWXCommon;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class GlgCust_qywxRyDkHistory extends BaseCronJob {

    private Log log = LogFactory.getLog(GlgCust_qywxRyDkHistory.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("开始获取企业微信人员考勤信息");
        //山门标签打卡
        OA_purta2("11");
        //浙江方向
        OA_purta2("17");
        //山门无指纹排版打卡
        OA_purta2("18");
        log.info("执行定时任务完成！");

    }

    @Deprecated


    //TagID  企业微信标签ID, 为配置的同步规则ID
    public void OA_purta2(String tagId) {

    //获取任务执行时间戳
        try {
            RecordSet data = new RecordSet();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            //同步截至时间
            String applyDate= formatter.format(cal.getTime());
            Date date = new Date();
            cal.setTime(date);
            String sckssj="";
            //开始时间未当前时间的前10天()
            cal.add(Calendar.DATE,-10);
            System.out.println(formatter.format(cal.getTime()));
            sckssj = formatter.format(cal.getTime());



            //获取打卡token
            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + 3010011 + "' ");

            String token = "";

            if (data.getCounts() > 0) {
                while (data.next()) {
                    token = data.getString("token");
                }
            }

            //获取打卡token
            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + 1 + "' ");

            String txtoken = "";

            if (data.getCounts() > 0) {
                while (data.next()) {
                    txtoken = data.getString("token");
                }
            }
            //获取需要取出数据的名单集合
           // log.info(GLGNet_QYWXCommon.getRyBybqName(txtoken, tagId));

            JSONObject jsonObject= (JSONObject) JSONObject.parse(GLGNet_QYWXCommon.getRyBybqName(txtoken, tagId));
            String userlistjson = jsonObject.getString("userlist");

            JSONArray userList= JSONArray.parseArray(userlistjson);
            JSONArray Ulist = new JSONArray();
            for(int i=0;i<userList.size();i++) {
                Ulist.add(userList.getJSONObject(i).get("userid").toString());

            }
          //  log.info(Ulist);
            //时间转换为标准时间戳
            String sckssjsjj=GLGNet_QYWXCommon.dateToStamp(sckssj);
          //  log.info(sckssjsjj);
            String scjssjsjj=GLGNet_QYWXCommon.dateToStamp(applyDate);
           // log.info(scjssjsjj);
            // 获取用户集合的打卡数据


            String response =GLGNet_QYWXCommon.getUsrtDkByUserList(token,"1",sckssjsjj,scjssjsjj,Ulist);
            //log.info(response);
            JSONObject checkindataobj= (JSONObject) JSONObject.parse(response);
            String checkindata = checkindataobj.getString("checkindata");
           // log.info(checkindata);
                //获取打卡数据保存到系统中间表
            JSONArray checkindataList= JSONArray.parseArray(checkindata);
            //log.info(checkindataList);
            String userid= "";
            String empId="";
            String CardNO="";
            String checkin_time= "";
            String exception_type= "";
                //循环取出记录写入系统中

            for(int i=0;i<checkindataList.size();i++) {
                    //打卡用户
                userid= "";
                checkin_time= "";
                exception_type= "";
                empId="";
                CardNO="";
                userid= checkindataList.getJSONObject(i).get("userid").toString();
                     //获取HR 系统人员ID

                empId= getUserId(userid);
                CardNO = getUserCardNO(userid);
                    //获取HR 系统指纹No
                    //打卡时间
                checkin_time= checkindataList.getJSONObject(i).get("checkin_time").toString();
                    //打卡状态
                exception_type= checkindataList.getJSONObject(i).get("exception_type").toString();
                    //如打卡状态为未打卡 则不写入系统
                if(exception_type.equals("未打卡"))
                {
                        return;
                }
                else{
                        //根据用户，打卡时间组合判断此条记录是否在中间表是否已有同步记录，如果存在则跳过

                    if(ifHashistory(empId,GLGNet_QYWXCommon.stampToDate(checkin_time)))
                    {
                        return;
                    }else {
                        //将打卡记录数据写入记录表中
                        //企业微信使用虚拟打卡机进行记录数据 卡机编码28
                        RecordSetDataSource Hrdata = new RecordSetDataSource("HRSystem");
                        Hrdata.executeSql("  insert into WXKq_Source(FDateTime,CardNo,MachNo,EmpID,FType,card,wid) values(" +

                                "'" + GLGNet_QYWXCommon.stampToDate(checkin_time) + "'," +
                                "'" + CardNO + "'," +
                                "'" + 28 + "'," +

                                "'" + empId + "'," +
                                "'1'," +
                                "'" + tagId + "'," +
                                "'" + "企业微信打卡" + "' " +

                                ")");
                       }

                    }
                }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("发送消息失败");
        }

    }

    public String  getUserId(String EmpNO)
    {
        String userId="";
        RecordSetDataSource Hrdata = new RecordSetDataSource("HRSystem");
        Hrdata.executeSql("select  ID from  ZlEmployee  where Code='"+EmpNO+"'");
        while (Hrdata.next())
        {
            userId=Hrdata.getString("id");
        }
        return  userId;
    }

    public String  getUserCardNO(String EmpNO)
    {
        String CardNo="";
        RecordSetDataSource Hrdata = new RecordSetDataSource("HRSystem");
        Hrdata.executeSql("select  CardNo from  ZlEmployee  where Code='"+EmpNO+"'");
        while (Hrdata.next())
        {
            CardNo=Hrdata.getString("CardNo");
        }
        return  CardNo;
    }

    public  boolean ifHashistory(String userId,String dKTime)
    {

        RecordSetDataSource Hrdata = new RecordSetDataSource("HRSystem");
        Hrdata.executeSql("select  * from  WXKq_Source  where EmpID='"+userId+"'" +
                " and FDateTime ='" +dKTime+"'"+
                "");
        if (Hrdata.getCounts() > 0) {
           return  true;
        }
        return  false;

    }

}