package weaver.interfaces.schedule.HR.QyWX;

import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.httpPost;



public class GlgCust_qywx_Ry extends BaseCronJob {

    private Log log = LogFactory.getLog(GlgCust_qywx_Ry.class.getName());


    @Override
    public void execute() {
        //同步企业微信
        // TODO Auto-generated method stub
        log.info("执行定时任务开始！");
        log.info("开始添加用户！");
        OA_addUser();

        log.info("开始同步用户列表！");
        OA_getUser();

        log.info("开始同步删除离职用户！");
        OA_delUser();


    }

    @Deprecated
    public void OA_getUser() {


        try {

            String linkUlr = "http://192.168.0.33:10022/api/wxApi/getUser.api";

            String response= httpPost.doGet(linkUlr);
            log.info("开始同步用户列表完成！");






        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("获取企业微信用户列表失败");
        }

    }

    @Deprecated
    public void OA_addUser() {


        try {

            String linkUlr = "http://192.168.0.33:10022/api/wxApi/addUser.api";

            String response= httpPost.doGet(linkUlr);
            log.info("添加用户完成！");






        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("添加企业微信用户列表失败");
        }

    }



    @Deprecated
    public void OA_delUser() {


        try {

            String linkUlr = "http://192.168.0.33:10022/api/wxApi/delUser.api";

            String response= httpPost.doGet(linkUlr);
            log.info("删除离职微信用户完成！");






        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("删除企业微信用户列表失败");
        }

    }


}
