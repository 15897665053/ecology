package weaver.interfaces.schedule.JD.SendCard.Job;

import com.ibm.icu.text.SimpleDateFormat;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;

import java.util.Calendar;

public class GLGCustSendjdCard_So extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustSendjdCard_So.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("销售订单消息执行定时任务开始！");

        OA_purta2();
        log.info("执行定时任务完成！");

    }

    @Deprecated


    //销售员销售情况信息推送
    public void OA_purta2() {


        try {
            log.info("请求企业微信生成Token！");
            String agentId = "1000025";
            RecordSet data = new RecordSet();
            String title = "业务员接单情况";

            //采取markDown样式展示

            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");
            String   gh = "";
            String token = "";

            String UserName = "";

            String linkUlr = "http://wx.glgnet.cn:10023/view/JD/getYwInfo.html";

            if (data.getCounts() > 0) {
                while (data.next()) {
                    gh = "";

                    token = data.getString("token");
                    //获取信息
                    RecordSetDataSource ds = new RecordSetDataSource("U9_75");
                    ds.executeSql("SELECT 业务员 username,isnull(本年接单金额,0) inYearJe" +
                            ",isnull(本年出货金额,0) outYearJe,isnull(本月接单金额,0) inMonJe," +
                            "isnull(本月出货金额,0) outMonJe,isnull(上月接单金额,0) inLastMonJe," +
                            "isnull(上月出货金额,0) outLastMonJe FROM V_ZZQ_SO_SALER1 " +

                            " ");

                    while (ds.next()) {

                        UserName = ds.getString("username");
                        //根据用户名从HR获取对应工号信息
                        RecordSetDataSource hr = new RecordSetDataSource("HRSystem");
                        hr.executeSql("select top 1  code from ZlEmployee a " +

                                " where a.name='" + UserName + "' and  a.State=0 ");
                        while (hr.next()) {

                            gh =hr.getString("code");
                        }

                        //推送消息.
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String applyDate= formatter.format(cal.getTime());
                        String sendUrl= "";
                        sendUrl = linkUlr +"?date="+applyDate+"&name="+UserName;

                        String description = "<div class=\"gray\">"+applyDate+"</div> " +
                                "<div class=\"normal\">"+UserName+"，您好！"+"</div>" +
                                "<div class=\"highlight\">" +
                                "今日接单情况，请您查收!</div>";
                        String response = QYWXCommon.SendQywxMesageCardaddUrlandgh(sendUrl, title, description,agentId, token, gh);

                        log.info("业务员工号："+gh +"推送成功，企业微信返回信息为" + response);
                    }

                }



            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("发送消息失败");
        }

    }

}
