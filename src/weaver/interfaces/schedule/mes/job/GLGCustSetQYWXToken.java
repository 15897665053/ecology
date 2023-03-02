package weaver.interfaces.schedule.mes.job;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;

import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.interfaces.schedule.BaseCronJob;


public class GLGCustSetQYWXToken extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustSetQYWXToken.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("执行定时任务开始！");
        //token刷新
        OA_purta5();
        //每日自动统计人员信息
        OA_purtanew();


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
            String  corpid ="";
            String  secret ="";
            if (data.getCounts() > 0) {
                while (data.next()) {

                    //4个字段
                    corpid = data.getString("corpid");
                    //工号
                    secret = data.getString("secret");
                    //获取信息




                }
            }
            //更新token
          String token = QYWXCommon.ChangeToken(corpid,secret);

            RecordSet rs = new RecordSet();
            rs.executeSql(" update a set a.token='" +token+"' from uf_wxqy_tokenRecord a "+

                    " where a.agentId='" + agentId + "' ");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新Token失败");
        }

    }
    @Deprecated
    public void OA_purta1() {



        try {
            log.info("请求企业微信生成Token！");
            String agentId="1000022";
            RecordSet data = new RecordSet();

            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");
            String  corpid ="";
            String  secret ="";
            if (data.getCounts() > 0) {
                while (data.next()) {

                    //4个字段
                    corpid = data.getString("corpid");
                    //工号
                    secret = data.getString("secret");
                    //获取信息




                }
            }
            //更新token
            String token = QYWXCommon.ChangeToken(corpid,secret);

            RecordSet rs = new RecordSet();
            rs.executeSql(" update a set a.token='" +token+"' from uf_wxqy_tokenRecord a "+

                    " where a.agentId='" + agentId + "' ");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新Token失败");
        }

    }
    @Deprecated
    public void OA_purta2() {



        try {
            log.info("请求企业微信生成Token！");
            String agentId="1";
            RecordSet data = new RecordSet();

            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");
            String  corpid ="";
            String  secret ="";
            if (data.getCounts() > 0) {
                while (data.next()) {

                    //4个字段
                    corpid = data.getString("corpid");
                    //工号
                    secret = data.getString("secret");
                    //获取信息




                }
            }
            //更新token
            String token = QYWXCommon.ChangeToken(corpid,secret);

            RecordSet rs = new RecordSet();
            rs.executeSql(" update a set a.token='" +token+"' from uf_wxqy_tokenRecord a "+

                    " where a.agentId='" + agentId + "' ");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新Token失败");
        }

    }

    @Deprecated
    public void OA_purta3() {



        try {
            log.info("请求企业微信生成Token！");
            String agentId="1000025";
            RecordSet data = new RecordSet();

            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");
            String  corpid ="";
            String  secret ="";
            if (data.getCounts() > 0) {
                while (data.next()) {

                    //4个字段
                    corpid = data.getString("corpid");
                    //工号
                    secret = data.getString("secret");
                    //获取信息




                }
            }
            //更新token
            String token = QYWXCommon.ChangeToken(corpid,secret);

            RecordSet rs = new RecordSet();
            rs.executeSql(" update a set a.token='" +token+"' from uf_wxqy_tokenRecord a "+

                    " where a.agentId='" + agentId + "' ");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新Token失败");
        }

    }

    @Deprecated
    public void OA_purta4() {



        try {
            log.info("请求企业微信生成Token！");
            String agentId="1000032";
            RecordSet data = new RecordSet();

            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");
            String  corpid ="";
            String  secret ="";
            if (data.getCounts() > 0) {
                while (data.next()) {

                    //4个字段
                    corpid = data.getString("corpid");
                    //工号
                    secret = data.getString("secret");
                    //获取信息




                }
            }
            //更新token
            String token = QYWXCommon.ChangeToken(corpid,secret);

            RecordSet rs = new RecordSet();
            rs.executeSql(" update a set a.token='" +token+"' from uf_wxqy_tokenRecord a "+

                    " where a.agentId='" + agentId + "' ");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新Token失败");
        }

    }

    @Deprecated
    public void OA_purta5() {



        try {
            RecordSetDataSource ds = new RecordSetDataSource("HRSystem");
            ds.executeSql( " exec  [Glg_getErpjoinOrleaveByHr_MOproc] " );


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新失败");
        }

    }

    @Deprecated
    public void OA_purtanew() {


        try {
            log.info("请求企业微信生成Token！");
            String agentId="";
            RecordSet data = new RecordSet();

            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " ");
            String  corpid ="";
            String  secret ="";
            if (data.getCounts() > 0) {
                while (data.next()) {

                    //4个字段
                    corpid = data.getString("corpid");
                    //工号
                    secret = data.getString("secret");
                    //获取信息
                    agentId= data.getString("agentId");

                    //更新token
                    String token = QYWXCommon.ChangeToken(corpid,secret);

                    RecordSet rs = new RecordSet();
                    rs.executeSql(" update a set a.token='" +token+"' from uf_wxqy_tokenRecord a "+

                            " where a.agentId='" + agentId + "' ");

                }
            }



        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新Token失败");
        }

    }


}

