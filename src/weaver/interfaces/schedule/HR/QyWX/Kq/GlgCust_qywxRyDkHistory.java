package weaver.interfaces.schedule.HR.QyWX.Kq;

import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.interfaces.schedule.BaseCronJob;

import weaver.monitor.cache.CacheFactory;

public class GlgCust_qywxRyDkHistory extends BaseCronJob {

    private Log log = LogFactory.getLog(GlgCust_qywxRyDkHistory.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("开始获取企业微信人员考勤信息");

        OA_purta2();
        log.info("执行定时任务完成！");

    }

    @Deprecated



    public void OA_purta2() {

    //获取任务执行时间戳
        try {



            //修改时间戳
            RecordSet data = new RecordSet();
            data.executeSql(" update uf_qywxTb_sj set gh=123456 " +
                    " where id = 1 " +
                    "" );



            //清除缓存
            CacheFactory sintance = CacheFactory.getInstance();



            String[] tablename ={"uf_qywxTb_sj"};

            sintance.removeCache(tablename);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("发送消息失败");
        }

    }

}