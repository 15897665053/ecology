package weaver.interfaces.schedule.mes.job;

import com.ibm.icu.text.SimpleDateFormat;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;

import java.util.Calendar;

public class GLGCustSendjdCard extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustSendjdCard.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("执行定时任务开始！");
        OA_purta();
        OA_purta1();
        OA_purta2();
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

    //销售员销售情况信息推送
    public void OA_purta2() {


        try {
            log.info("请求企业微信生成Token！");
            String agentId = "1000025";
            RecordSet data = new RecordSet();


            //采取markDown样式展示

            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");
            String   gh = "";
            String token = "";
            String inYearJe = "";
            String outYearJe = "";
            String inMonJe = "";
            String outMonJe = "";
            String inLastMonJe = "";
            String outLastMonJe = "";
            String UserName = "";



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
                        inYearJe = ds.getString("inYearJe");
                        outYearJe = ds.getString("outYearJe");
                        inMonJe = ds.getString("inMonJe");
                        outMonJe = ds.getString("outMonJe");
                        inLastMonJe = ds.getString("inLastMonJe");
                        outLastMonJe = ds.getString("outLastMonJe");
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
                        String applyDate = formatter.format(cal.getTime());
                        String Content = "`您的接单数据请查收`\n>" +
                                "**接单情况** \n>" +
                                "业务员：<font color = \"info\">"+UserName+"万</font>\n>" +
                                "本年接单金额：<font color = \"info\">"+inYearJe+"万</font>\n>" +
                                "本年出货金额： <font color = \"warning\">" + outYearJe +"万</font>  \n>" +
                                "本月接单金额：<font color = \"info\">"+inMonJe+"万</font>\n>" +
                                "本月出货金额： <font color = \"warning\">" + outMonJe +"万</font>  \n>" +
                                "上月接单金额：<font color = \"info\">"+inLastMonJe+"万</font>\n>" +
                                "上月出货金额： <font color = \"warning\">" + outLastMonJe + "万</font>  \n>" +
                                "统计日期： <font color = \"warning\">" + applyDate + "</font>  \n>\n>" +
                                "深圳市方向电子股份有限公司";


                        String response = QYWXCommon.SendQywxMesageMarkDownandgh(Content, agentId, token, gh);

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
