package weaver.interfaces.schedule.JD.SendCard.Job;

import com.ibm.icu.text.SimpleDateFormat;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;

import java.util.Calendar;

public class GLGCustSendjdCard_YC extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustSendjdCard_YC.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("预测订单推送执行定时任务开始！");

        OA_purta1();

        log.info("执行定时任务完成！");

    }

    @Deprecated



    public void OA_purta1() {


        try {
            log.info("请求企业微信生成Token！");
            String agentId = "1000025";
            RecordSet data = new RecordSet();
            String title = "";
            String linkUlr = "http://wx.glgnet.cn:10023/view/JD/ForecastOrder.aspx";

            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");
            String   gh = "";
            String token = "";




            if (data.getCounts() > 0) {
                while (data.next()) {


                    token = data.getString("token");
                    //获取信息


                    gh ="FX00001|FX00002|FX10324|FX00002|FX02607|FX00055|FX04340";
                    log.info("工号" + gh);


                    title =   "预测订单";




                    //推送消息.
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String applyDate= formatter.format(cal.getTime());

                    String description = "<div class=\"gray\">"+applyDate+"</div> " +
                            "<div class=\"normal\">"+"预测订单转化情况"+"</div>" +






                            "<div class=\"highlight\">" +
                            "方向电子预测订单转换情况，请您查收!</div>";
                    String response = QYWXCommon.SendQywxMesageCardaddUrlandgh(linkUlr, title, description,agentId, token, gh);

                    log.info("企业微信返回信息" + response);

                }



            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新Token失败");
        }

    }



}
