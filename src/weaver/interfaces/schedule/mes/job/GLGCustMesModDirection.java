package weaver.interfaces.schedule.mes.job;

import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;

public class GLGCustMesModDirection extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustMouldNeedBySendMsg.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub



        log.info("冲压自动绑定工作流！");
        OA_purta();
        log.info("注塑自动绑定工作流！");
        OA_purta1();
        log.info("执行定时任务完成！");

    }

    //冲压车间模具失效发送消息提醒并更新模具状态

    @Deprecated

    public void OA_purta() {



        try {
            //五金模具到达15亿模次或使用15年系统自动进行模具报废
            RecordSetDataSource ds = new RecordSetDataSource("MES");
            ds.executeSql(" update ModMould set State='MouldState_Scrapped',IsScrap=1 from  ModMould" +
                    " where (DATEDIFF(YEAR, OpenMouldDate, GETDATE()) >= 15 or CurrentLife>=Life)" +
                    " and State='MouldState_InWareHouse' and MouldCode  like 'GLG-W%' " );
            //初始化 五金模具寿命为15亿次

            ds.executeSql(" update ModMould set life='1500000000' where MouldCode  like 'GLG-W%'  and life ='0' " );
            //冲压一课车间绑定默认工作流


            ds.executeSql(" update MesMO  set WorkflowId=521 where OrganizationId=1762 " );

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新Token失败");
        }

    }


    public void OA_purta1() {



        try {
            //注塑模具到达15亿模次或使用15年系统自动进行模具报废
            RecordSetDataSource ds = new RecordSetDataSource("MES");
            ds.executeSql(" update ModMould set State='MouldState_Scrapped',IsScrap=1 from  ModMould" +
                    " where (DATEDIFF(YEAR, OpenMouldDate, GETDATE()) >= 20 or CurrentLife>=Life)" +
                    " and State='MouldState_InWareHouse' and MouldCode  like 'GLG-S%' " );
            //初始化 注塑模具寿命为20亿次

            ds.executeSql(" update ModMould set life='2000000000' where MouldCode  like 'GLG-S%'  and life ='0' " );
            //注塑一课车间绑定默认工作流


            ds.executeSql(" update MesMO  set WorkflowId=522 where OrganizationId=1759 " );

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新Token失败");
        }

    }















}
