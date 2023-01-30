package weaver.interfaces.schedule.mes.job;

import com.ibm.icu.text.SimpleDateFormat;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;

import java.util.Calendar;


public class GLGCustSendQYWXKuCun extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustSendQYWXKuCun.class.getName());


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
            String agentId="1000014";
            RecordSet data = new RecordSet();

            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");

            String  token ="";
            String deptName = "塑胶部";
            String linkUrl = "http://wx.glgnet.cn:10023//view/MES/Mod/MESModBJNeedSend.aspx?dept=S";
            String  user = "";

            String  Qty="";
            String title = "塑胶部模具备件库存水位预警";

            if (data.getCounts() > 0) {
                while (data.next()) {


                    token = data.getString("token");
                    //获取信息
                    RecordSetDataSource ds = new RecordSetDataSource("U9_75");
                    ds.executeSql("select    *  from GLG_QYWX_ItemKuCun where StoreQty<Qty " +
                            " and itemcode like'%S%'  " );
                    if (data.getCounts() > 0) {
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String applyDate= formatter.format(cal.getTime());
                        Qty = Integer.toString(ds.getCounts());

                        String description = "<div class=\"gray\">"+applyDate+"</div> " +
                                "<div class=\"normal\">部门名称："+deptName+"</div>" +
                                "<div class=\"normal\">待保养模具数量："+Qty+"</div>" +
                                "<div class=\"highlight\">" +deptName+"共有"+Qty+"套零件低于水位先，请尽快补充库存！"+
                                "</div>";
                        //获取推送人员信息
                        RecordSet userdata = new RecordSet();
                        userdata.executeSql("select    *  from uf_MES_qywxUser a  where a.bmbm='300101' " +
                                "and a.state=0 and a.lx=2 " );
                        while (userdata.next()) {
                            if(user.equals(""))
                            {
                                user = userdata.getString("gh");
                            }else{
                                user = user+"|"+userdata.getString("gh");
                            }
                        }





                        //推送消息.
                        String response=QYWXCommon.MESSendQywxMesageURlandMessage(linkUrl,description,title,agentId,token,user);
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

    public void OA_purta1() {



        try {
            log.info("请求企业微信生成Token！");
            String agentId="1000014";
            RecordSet data = new RecordSet();

            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");

            String  token ="";
            String deptName = "五金部";
            String linkUrl = "http://wx.glgnet.cn:10023//view/MES/Mod/MESModBJNeedSend.aspx?dept=W";
            String  user = "";

            String  Qty="";
            String title = "五金部模具备件库存水位预警";

            if (data.getCounts() > 0) {
                while (data.next()) {


                    token = data.getString("token");
                    //获取信息
                    RecordSetDataSource ds = new RecordSetDataSource("U9_75");
                    ds.executeSql("select    *  from GLG_QYWX_ItemKuCun where StoreQty<Qty " +
                            " and itemcode like'%W%'  " );
                    if (data.getCounts() > 0) {
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String applyDate= formatter.format(cal.getTime());
                        Qty = Integer.toString(ds.getCounts());

                        String description = "<div class=\"gray\">"+applyDate+"</div> " +
                                "<div class=\"normal\">部门名称："+deptName+"</div>" +
                                "<div class=\"normal\">待保养模具数量："+Qty+"</div>" +
                                "<div class=\"highlight\">" +deptName+"共有"+Qty+"套零件低于水位先，请尽快补充库存！"+
                                "</div>";
                        //获取推送人员信息
                        RecordSet userdata = new RecordSet();
                        userdata.executeSql("select    *  from uf_MES_qywxUser a  where a.bmbm='300201' " +
                                "and a.state=0 and a.lx=2 " );
                        while (userdata.next()) {
                            if(user.equals(""))
                            {
                                user = userdata.getString("gh");
                            }else{
                                user = user+"|"+userdata.getString("gh");
                            }
                        }





                        //推送消息.
                        String response=QYWXCommon.MESSendQywxMesageURlandMessage(linkUrl,description,title,agentId,token,user);
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