package weaver.interfaces.schedule.mes.job;

import com.ibm.icu.text.SimpleDateFormat;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;

import java.util.Calendar;

public class GLGCustSendGzTx extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustSendGzTx.class.getName());


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


            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");
            String   gh = "";
            String token = "";
            String  Sessionid = "";
            String  EmpId = "";
            String  empName = "";
            String linkUlr = "http://wx.glgnet.cn:10023/view/XZ/QZ/GZdetail.aspx";
            String  Memo = "";
            String  DtName = "";

            if (data.getCounts() > 0) {
                while (data.next()) {


                    token = data.getString("token");
                    //获取信息
                    RecordSetDataSource ds = new RecordSetDataSource("HRSystem");
                    ds.executeSql("  select b.Code gh,a.SessionID ,a.EmpId,b.Name empName,s.Memo+'工资' as bt, " +
                            " s.Memo,D.name DtName from Gz_Total a  " +
                            " inner  join ZlEmployee   b  " +
                            " on a.EmpID =b.ID " +
                            " left join zldept D on a.dept=D.code AND (ISNULL(D.IFSEE,0)=0) " +
                            " left join S_Session S On a.SessionID=S.ID " +
                            " left join Glg_qywx_empQZHistory qr on  a.SessionId=qr.SessionId and a.EmpId =qr.EmpID " +
                            " where  a.qrState =0 and CONVERT(int ,b.ZhiJi)<=6 " +
                            " and qr.empid is null " +



                            " " );
                    while (ds.next()) {
                        gh = ds.getString("gh");
                        log.info("工号" + gh);
                        Sessionid = ds.getString("SessionID");
                        log.info("SessionID" + Sessionid);
                        EmpId =   ds.getString("EmpId");
                        title =   ds.getString("bt");
                        Memo  =   ds.getString("Memo");
                        empName  =   ds.getString("empName");
                        DtName  =   ds.getString("DtName");

                        String sendUlr = linkUlr+"?Memo="+Memo;
                        log.info("EmpId" + EmpId);
                        //推送消息.
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String applyDate= formatter.format(cal.getTime());

                        String description = "<div class=\"gray\">"+applyDate+"</div> " +
                                "<div class=\"normal\">部门："+DtName+"</div>" +
                                "<div class=\"normal\">姓名："+empName+"</div>" +
                                "<div class=\"normal\">工号："+gh+"</div>" +
                                "<div class=\"highlight\">" +
                                "您有一份工资单待确认，请尽快确认!</div>";
                        String response = QYWXCommon.SendQywxMesageCardaddUrlandgh(sendUlr, title, description,agentId, token, gh);
                        RecordSetDataSource hr = new RecordSetDataSource("HRSystem");
                        hr.executeSql(" update Gz_Total set qywxSendTime ='" +applyDate+
                                "'  " +
                                "where  EmpId  ='" +EmpId+"' and Sessionid='"+Sessionid+
                                "' " );
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
