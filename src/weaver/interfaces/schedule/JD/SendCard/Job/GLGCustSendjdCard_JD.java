package weaver.interfaces.schedule.JD.SendCard.Job;

import com.ibm.icu.text.SimpleDateFormat;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;
import weaver.interfaces.schedule.mes.job.GLGCustSendjdCard;

import java.util.Calendar;

public class GLGCustSendjdCard_JD extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustSendjdCard_JD.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("接单消息推送执行定时任务开始！");
        OA_purta();

        log.info("执行定时任务完成！");

    }

    @Deprecated
    public void OA_purta() {


        try {
            log.info("请求企业微信生成Token！");
            String agentId = "1000025";
            RecordSet data = new RecordSet();
            String title = "接单金额汇总";
            String linkUlr="http://wx.glgnet.cn:10023/view/JD/index.aspx" ;


            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");

            String token = "";

            if (data.getCounts() > 0) {
                while (data.next()) {


                    token = data.getString("token");
                    //获取信息
                    RecordSetDataSource ds = new RecordSetDataSource("U9_75");

                    //推送消息.
                    String response = QYWXCommon.SendQywxMesageCardaddUrl(linkUlr,title, agentId, token);
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
