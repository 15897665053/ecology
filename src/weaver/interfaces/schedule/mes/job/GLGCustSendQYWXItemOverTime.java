package weaver.interfaces.schedule.mes.job;

import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;

public class GLGCustSendQYWXItemOverTime  extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustSendQYWXItemOverTime.class.getName());


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
            String itemcode="";

            String itemName="";
            String  whName="";
            String StoreQty="";
            String  Qty="";
            String title = "模具备件库存水位预警";

            if (data.getCounts() > 0) {
                while (data.next()) {


                    token = data.getString("token");
                    //获取信息
                    RecordSetDataSource ds = new RecordSetDataSource("U9_75");
                    ds.executeSql("select    *  from GLG_QYWX_ItemKuCun a " );
                    while (ds.next()) {
                        itemcode = ds.getString("itemcode");
                        log.info("料号"+itemcode);

                        itemName = ds.getString("itemName");
                        log.info("品名"+itemName);
                        whName = ds.getString("whName");
                        log.info("仓库"+whName);
                        StoreQty = ds.getString("StoreQty");
                        log.info("库存数量"+StoreQty);
                        Qty = ds.getString("Qty");
                        log.info("水位线"+Qty);
                        //推送消息.
                        String response= QYWXCommon.SendQywxMesage(title,agentId,token,itemcode,itemName,whName,StoreQty,Qty);
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
