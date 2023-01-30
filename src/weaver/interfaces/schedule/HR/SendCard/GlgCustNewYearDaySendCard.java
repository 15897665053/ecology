package weaver.interfaces.schedule.HR.SendCard;

import com.ibm.icu.text.SimpleDateFormat;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;
import weaver.interfaces.schedule.mes.job.GLGCustSendQYWXHRCard;

import java.util.Calendar;

public class GlgCustNewYearDaySendCard extends BaseCronJob {

    private Log log = LogFactory.getLog(GlgCustNewYearDaySendCard.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("执行定时任务开始！");
        QYWX_NewYeardaypurta();

        log.info("执行定时任务完成！");

    }

    @Deprecated
    public void QYWX_NewYeardaypurta() {



        try {
            log.info("请求企业微信生成Token！");
            String agentId="1000022";
            RecordSet data = new RecordSet();

            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");

            String  token ="";



            String  EmpNo="FX11663";


            String title = "";

            if (data.getCounts() > 0) {
                while (data.next()) {


                    token = data.getString("token");
                    //获取信息

                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String applyDate= formatter.format(cal.getTime());








                        String ConverUrl = "https://image-pub.lexiang-asset.com/common/assets/wish/welcome_new_year_1068_455.jpg";
                        String LinkUrl="http://wx.glgnet.cn:10023/view/sendCard/NewYearDay/index.html";
                        title = "方向电子祝您元旦快乐";
                        String desc=applyDate;
                        //推送消息.
                        String response= QYWXCommon.SendQywxBirthdayMesage(LinkUrl,ConverUrl,desc,agentId,token,title,EmpNo);
                        log.info("企业微信返回信息"+response);




                }
            }






        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新Token失败");
        }

    }














}
