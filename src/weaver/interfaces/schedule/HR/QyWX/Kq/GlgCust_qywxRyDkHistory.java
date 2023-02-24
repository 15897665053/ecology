package weaver.interfaces.schedule.HR.QyWX.Kq;

import com.alibaba.fastjson.JSONArray;
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

public class GlgCust_qywxRyDkHistory extends BaseCronJob {

    private Log log = LogFactory.getLog(GlgCust_qywxRyDkHistory.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("开始获取企业微信人员考勤信息");
        //
        OA_purta2("11","2");
        log.info("执行定时任务完成！");

    }

    @Deprecated


    //TagID  企业微信标签ID, 为配置的同步规则ID
    public void OA_purta2(String tagId,String id) {

    //获取任务执行时间戳
        try {
            RecordSet data = new RecordSet();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            //同步截至时间
            String applyDate= formatter.format(cal.getTime());
            //获取上一次同步时间记录
            data.executeSql(" select sckssj,scjssj from uf_qywxTb_sj  " +
                    " where id = '" +id+
                    "' " );
            String sckssj="";
            while (data.next()) {

                sckssj = data.getString("scjssj");

            }

            //获取打卡token
            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + 3010011 + "' ");

            String token = "";

            if (data.getCounts() > 0) {
                while (data.next()) {
                    token = data.getString("token");
                }
            }
            //获取需要取出数据的名单集合
            JSONArray userList= JSONArray.parseArray(GLGNet_QYWXCommon.getRyBybqName(token, tagId));

            //时间转换为标准时间戳
            String sckssjsjj=GLGNet_QYWXCommon.dateToStamp(sckssj);
            String scjssjsjj=GLGNet_QYWXCommon.dateToStamp(applyDate);

            // 获取用户集合的打卡数据
            try {

                String checkindata =GLGNet_QYWXCommon.getUsrtDkByUserList(token,"1",sckssjsjj,scjssjsjj,userList);
                //获取打卡数据保存到系统中间表

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                log.info("获取信息失败");
            }

            //修改时间戳

            data.executeSql(" update uf_qywxTb_sj set sckssj= '" +sckssj+"',scjssj='"+applyDate+"'"+
                    " where id = '" +id+"' "+
                    "" );



            //清除缓存
            CacheFactory sintance = CacheFactory.getInstance();



            String[] tablename ={"uf_qywxTb_sj"};

            sintance.removeCache(tablename);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("发送消息失败");
        }

    }

}