package weaver.interfaces.schedule.mes.job;

import com.ibm.icu.text.SimpleDateFormat;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;

import java.util.Calendar;
import java.util.Date;

public class GLGCustHRryLZorJoininfoTx extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustHRryLZorJoininfoTx.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub




        log.info("发送消息！");
        OA_purta();
        log.info("发送消息完成！");

    }

    //获取统计信息


    @Deprecated
    public void OA_purta() {


        try {
            log.info("统计当日数据");
            RecordSetDataSource ds = new RecordSetDataSource("HRSystem");
            ds.executeSql(" exec [Glg_getErpjoinOrleaveByHr_MOproc] ");
            log.info("统计数据完成");
            log.info("请求企业微信生成Token！");
            String agentId = "1000022";
            RecordSet data = new RecordSet();
            String title = "员工入职离职信息汇总";


            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");
            String   gh = "FX11663|FX10324|FX04567|FX00153";
            String token = "";


            String linkUlr = "http://wx.glgnet.cn:10023/view/JD/getHrInfo.aspx";


            if (data.getCounts() > 0) {
                while (data.next()) {


                    token = data.getString("token");




                        //推送消息.
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String applyDate= formatter.format(cal.getTime());
                        //判断是否为月末。 月末记录通知总经理，董事长
                        if(isLastDayOfMonth(cal.getTime()))
                        {
                            gh=gh+"|FX00001|FX00002";
                        }
                        //int year = cal.get(Calendar.YEAR);
                        //int month = cal.get(Calendar.MONTH) + 1;
                        String sendUlr = linkUlr+"?date="+applyDate;

                        String description = "<div class=\"gray\">"+applyDate+"</div> " +

                                "<div class=\"highlight\">" +
                                "本月员工入职离职情况汇总，请查看</div>";
                        String response = QYWXCommon.SendQywxMesageCardaddUrlandgh(sendUlr, title, description,agentId, token, gh);

                        log.info("企业微信返回信息" + response);




                }
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新Token失败");
        }

    }

    public static boolean isLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            return true;
        }
        return false;
    }
}