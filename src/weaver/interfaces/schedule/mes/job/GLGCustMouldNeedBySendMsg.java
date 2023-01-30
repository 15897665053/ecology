package weaver.interfaces.schedule.mes.job;

import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;

public class GLGCustMouldNeedBySendMsg extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustMouldNeedBySendMsg.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("执行定时任务开始！");
        log.info("塑胶模具未保养推送开始！");
        OA_purta();
        log.info("五金模具未保养推送开始！");
        OA_purta1();
        log.info("执行定时任务完成！");

    }

    //注塑车间
    @Deprecated
    public void OA_purta() {



        try {
            log.info("请求企业微信生成Token！");
            String agentId="1000014";
            RecordSet data = new RecordSet();

            data.executeSql("select  * from uf_wxqy_tokenRecord a  " +

                    " where a.agentId='" + agentId + "' ");

            String  token ="";

            String  deptName="塑胶部";

            String  Qty="";
            String  user="";
            String title = "模具待保养清单";
            String linkUrl="http://wx.glgnet.cn:10023/view/MES/Mod/MESModeNeedBY.aspx?dept=300101";

            if (data.getCounts() > 0) {
                while (data.next()) {


                    token = data.getString("token");
                    //获取信息
                    RecordSetDataSource ds = new RecordSetDataSource("MES");
                    ds.executeSql("select    *  from GLG_Mould_NeedWGBy a  where a.WorkshopCode='300101' " );
                    if(ds.getCounts()>0)
                    {

                        log.info("塑胶部未保养模具不存在！");
                        //获取推送人员信息
                        RecordSet userdata = new RecordSet();
                        userdata.executeSql("select    *  from uf_MES_qywxUser a  where a.bmbm='300101' " +
                                "and a.state=0 and a.lx=0 " );
                        while (userdata.next()) {
                            if(user.equals(""))
                            {
                                user = userdata.getString("gh");
                            }else{
                                user = user+"|"+userdata.getString("gh");
                            }
                        }
                        Qty = Integer.toString(ds.getCounts());
                        String response= QYWXCommon.MESSendQywxMesageURl(linkUrl,title,agentId,token,deptName,Qty,user);
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


    //五金车间
    @Deprecated
    public void OA_purta1() {



        try {
            log.info("请求企业微信生成Token！");
            String agentId="1000014";
            RecordSet data = new RecordSet();

            data.executeSql("select  * from uf_wxqy_tokenRecord a  " +

                    " where a.agentId='" + agentId + "' ");

            String  token ="";

            String  deptName="五金部";

            String  Qty="";
            String  user="";
            String title = "模具待保养清单";
            String linkUrl="http://wx.glgnet.cn:10023/view/MES/Mod/MESModeNeedBY.aspx?dept=300201";

            if (data.getCounts() > 0) {
                while (data.next()) {


                    token = data.getString("token");
                    //获取信息
                    RecordSetDataSource ds = new RecordSetDataSource("MES");
                    ds.executeSql("select    *  from GLG_Mould_NeedWGBy a  where a.WorkshopCode='300201' " );
                    if(ds.getCounts()>0)
                    {
                        log.info("五金部未保养模具不存在！");

                        //获取推送人员信息
                        RecordSet userdata = new RecordSet();
                        userdata.executeSql("select    *  from uf_MES_qywxUser a  where a.bmbm='300201' " +
                                "and a.state=0 and a.lx=0 " );
                        while (userdata.next()) {
                            if(user.equals(""))
                            {
                                user = userdata.getString("gh");
                            }else{
                                user = user+"|"+userdata.getString("gh");
                            }
                        }
                        Qty = Integer.toString(ds.getCounts());
                        String response= QYWXCommon.MESSendQywxMesageURl(linkUrl,title,agentId,token,deptName,Qty,user);
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