package weaver.interfaces.schedule.mes.job;
import com.aliyuncs.http.IHttpClient;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;
import weaver.interfaces.schedule.mes.helper.httpPost;

public class GLGCustQYWXtb extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustQYWXtb.class.getName());


    @Override
    public void execute() {
        //同步企业微信
        // TODO Auto-generated method stub
        log.info("执行定时任务开始！");
        log.info("开始添加用户！");
        OA_addUser();
        log.info("开始更新用户！");
        OA_updateUser();
        log.info("开始同步用户列表！");
        OA_getUser();

        log.info("开始同步删除离职用户！");
        OA_delUser();

        log.info("开始同步部门列表！");
        OA_getQywiDept();
        log.info("开始同步部门信息！");
        OA_tbDept();
        log.info("执行定时任务完成！");

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

            String linkUlr = "" +
                    "";

            String response= httpPost.doGet(linkUlr);
            log.info("添加用户完成！");






        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("添加企业微信用户列表失败");
        }

    }

    @Deprecated
    public void OA_updateUser() {


        try {

            String linkUlr = "http://192.168.0.33:10022/api/wxApi/updateUser.api";

            String response= httpPost.doGet(linkUlr);
            log.info("更新企业微信用户完成！");






        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新企业微信用户列表失败");
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

    @Deprecated
    public void OA_getQywiDept() {


        try {

            String linkUlr = "http://192.168.0.33:10022/api/wxApi/getQywiDept.api";

            String response= httpPost.doGet(linkUlr);
            log.info("同步部门列表完成！");






        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("同步部门列表失败");
        }

    }

    @Deprecated
    public void OA_tbDept() {


        try {

            String linkUlr = "http://192.168.0.33:10022/api/wxApi/tbDept.api";

            String response= httpPost.doGet(linkUlr);
            log.info("同步部门信息完成！");






        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("同步部门信息失败");
        }

    }
}
