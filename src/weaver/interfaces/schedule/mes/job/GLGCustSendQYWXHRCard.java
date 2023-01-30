package weaver.interfaces.schedule.mes.job;

import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;

public class GLGCustSendQYWXHRCard extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustSendQYWXHRCard.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("执行定时任务开始！");
        QYWX_Birthdaypurta();
        emp_PYdaypurta();
        log.info("执行定时任务完成！");

    }

    @Deprecated
    public void QYWX_Birthdaypurta() {



        try {
            log.info("请求企业微信生成Token！");
            String agentId="1000022";
            RecordSet data = new RecordSet();

            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");

            String  token ="";
            String EmpName="";

            String Sex="";
            String  EmpNo="";
            String StoreQty="";
            String  Count="";
            String title = "";

            if (data.getCounts() > 0) {
                while (data.next()) {


                    token = data.getString("token");
                    //获取信息
                    RecordSetDataSource ds = new RecordSetDataSource("HRSystem");
                    ds.executeSql("select    *  from GlgNet_QYWX_BirthdayCard a " );
                    while (ds.next()) {
                        EmpName = ds.getString("Name");
                        Count = ds.getString("Count");
                        log.info("姓名"+EmpName);

                        Sex = ds.getString("Sex");

                        log.info("性别"+Sex);
                        EmpNo = ds.getString("Code");
                        log.info("工号"+EmpNo);
                        String ConverUrl = "https://image-pub.lexiang-asset.com/common/assets/wish/birthday_02_cover_img_1068x455.png";
                        String LinkUrl="http://wx.glgnet.cn:10023/view/sendCard/BirthdayCard.aspx?Name="+EmpName+"&Count="+Count+"";
                         title = EmpName+Sex;
                        String desc="祝您生日快乐";
                        //推送消息.
                        String response= QYWXCommon.SendQywxBirthdayMesage(LinkUrl,ConverUrl,desc,agentId,token,title,EmpNo);
                        log.info("企业微信返回信息"+response);

                    }


                }
            }






        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新Token失败");
        }

    }

    public void emp_PYdaypurta() {



        try {
            log.info("请求企业微信生成Token！");
            String agentId="1000022";
            RecordSet data = new RecordSet();

            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");

            String  token ="";
            String EmpName="";

            String Sex="";
            String  EmpNo="";

            String  Qty="";
            String title = "";

            if (data.getCounts() > 0) {
                while (data.next()) {


                    token = data.getString("token");
                    //获取信息
                    RecordSetDataSource ds = new RecordSetDataSource("HRSystem");
                    ds.executeSql("select    *  from GlgNet_QYWX_empPydayCard a " );
                    while (ds.next()) {
                        EmpName = ds.getString("Name");
                        log.info("姓名"+EmpName);
                        Qty = ds.getString("PydateCount");

                        Sex = ds.getString("sex");
                        log.info("性别"+Sex);

                        EmpNo = ds.getString("Code");
                        log.info("工号"+EmpNo);
                        String ConverUrl = "https://image-pub.lexiang-asset.com/common/assets/wish/entryday_01_cover_img_1068x455.png";
                        String LinkUrl="http://wx.glgnet.cn:10023/view/sendCard/Card.aspx?Name="+EmpName+"&Count="+Qty+"";
                        title = EmpName+Sex;
                        String desc="祝您"+Qty+"周年快乐!";
                        //推送消息.
                        String response= QYWXCommon.SendQywxBirthdayMesage(LinkUrl,ConverUrl,desc,agentId,token,title,EmpNo);
                        log.info("企业微信返回信息"+response);

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
