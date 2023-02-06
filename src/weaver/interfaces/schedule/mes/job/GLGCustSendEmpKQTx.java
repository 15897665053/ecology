package weaver.interfaces.schedule.mes.job;
import com.ibm.icu.text.SimpleDateFormat;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;

import java.util.Calendar;
public class GLGCustSendEmpKQTx  extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustSendEmpKQTx.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("执行定时任务开始！");
        OA_purta();
        log.info("执行定时任务完成！");

    }

    @Deprecated
    public void OA_purta() {


        try {
            log.info("请求企业微信生成Token！");
            String agentId = "1000022";
            RecordSet data = new RecordSet();
            String title = "";
            // 跳转地址
            String linkUlr = "http://wx.glgnet.cn:10023/view/XZ/QZ/kqqr.aspx";

            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");
            String   gh = "";
            String token = "";
            String  Sessionid = "";
            String  EmpId = "";
            String  empName = "";

            String  Memo = "";
            String  DtName = "";

            if (data.getCounts() > 0) {
                while (data.next()) {


                    token = data.getString("token");
                    //获取信息
                    RecordSetDataSource ds = new RecordSetDataSource("HRSystem");
                    ds.executeSql("  exec Glg_Qywx_GetEmpKQNeedSend " +


                            " " );
                    while (ds.next()) {
                        gh = ds.getString("gh");
                        log.info("工号" + gh);

                        EmpId =   ds.getString("EmpId");
                        title =   ds.getString("bt");
                        Memo  =   ds.getString("Memo");
                        empName  =   ds.getString("empName");
                        DtName  =   ds.getString("DtName");



                        //推送消息.
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String applyDate= formatter.format(cal.getTime());

                        String description = "<div class=\"gray\">"+applyDate+"</div> " +
                                "<div class=\"normal\">部门："+DtName+"</div>" +
                                "<div class=\"normal\">姓名："+empName+"</div>" +
                                "<div class=\"normal\">工号："+gh+"</div>" +




                                "<div class=\"highlight\">" +
                                "您的"+Memo+"待确认，请尽快确认!</div>";
                        String response = QYWXCommon.SendQywxMesageCardaddUrlandgh(linkUlr, title, description,agentId, token, gh);

                        log.info("企业微信返回信息" + response);

                    }


                }
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新Token失败");
        }

    }

}
