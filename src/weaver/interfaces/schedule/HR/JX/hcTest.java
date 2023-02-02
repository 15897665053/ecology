package weaver.interfaces.schedule.HR.JX;

import com.ibm.icu.text.SimpleDateFormat;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.JD.SendCard.Job.GLGCustSendjdCard_So;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;
import weaver.monitor.cache.CacheFactory;

import java.util.Calendar;

public class hcTest extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustSendjdCard_So.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("销售订单消息执行定时任务开始！");

        OA_purta2();
        log.info("执行定时任务完成！");

    }

    @Deprecated


    //销售员销售情况信息推送
    public void OA_purta2() {


        try {
            RecordSet data = new RecordSet();
            data.executeSql(" update uf_Empts set gh=123456 ");

            data.executeSql(" insert uf_Empts(gh,xm) values('987654','李四') ");

            //清除缓存
            CacheFactory sintance = CacheFactory.getInstance();



            String[] tablename ={"uf_Empts"};

            sintance.removeCache(tablename);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("发送消息失败");
        }

    }

}
