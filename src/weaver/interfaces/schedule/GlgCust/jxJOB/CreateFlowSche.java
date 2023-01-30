package weaver.interfaces.schedule.GlgCust.jxJOB;

import com.ibm.icu.text.SimpleDateFormat;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;

import weaver.soa.workflow.request.RequestInfo;
import weaver.soa.workflow.request.RequestService;
import weaver.workflow.webservices.*;

import java.util.Calendar;

public class CreateFlowSche extends BaseCronJob {
    private Log log = LogFactory.getLog(CreateFlowSche.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("执行定时任务开始！");

        tiggerWorkflow();
        log.info("执行定时任务完成！");

    }
    //需要定时执行的代码块
    public void tiggerWorkflow(){
        String workflowId = "1186";// 流程id
        String workflowName = "绩效考核流程";//流程名称
        String userName ="";//申请人名称
        String applyDate="";//申请日期

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        applyDate= formatter.format(cal.getTime());

        //获取khr信息
        RecordSetDataSource data = new RecordSetDataSource("e9");
        data.executeSql("select * from  hrmresource " +
                " where loginid='11663' ");
        //获取年份，//月份

        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        while (data.next()) {
           //员工Id
           // String  sqr = data.getString("id");
            String  sqr = "888";
            //工号
            String  gh = data.getString("loginid");
            //分部
            String  fb = data.getString("subcompanyid1");
            //部门
            String  bm = data.getString("departmentid");
            //岗位
            String  gw = data.getString("jobtitle");
            //姓名
            userName = data.getString("lastname");
            RecordSetDataSource data1 = new RecordSetDataSource("e9");
            try {
                data1.executeSql("select id  from  uf_GlgNet_ryjxsj where khr='" + 888 + "' and nf='" + year + "'  and yf='" + month + "'");
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            //已有考核流程创建记录 不再推送流程
            if (data1.getColCounts()>0)
                continue;
            try {

                /****************流程创建*************/
                WorkflowBaseInfo workflowBaseInfo = new WorkflowBaseInfo();//工作流信息
                workflowBaseInfo.setWorkflowId(workflowId);//流程ID
                workflowBaseInfo.setWorkflowName(workflowName);//流程名称

                WorkflowRequestInfo workflowRequestInfo = new WorkflowRequestInfo();//工作流程请求信息
                workflowRequestInfo.setCanView(true);//显示
                workflowRequestInfo.setCanEdit(true);//可编辑
                workflowRequestInfo.setRequestName(workflowBaseInfo.getWorkflowName() + "-" + userName + "-" + applyDate);//请求标题
                workflowRequestInfo.setRequestLevel("0");//紧急程度 0：正常 1：重要 2：紧急
                workflowRequestInfo.setCreatorId(sqr);//创建者ID 创建流程时为必输项
                workflowRequestInfo.setWorkflowBaseInfo(workflowBaseInfo);//工作流信息

                /****************main table start*************/
                WorkflowMainTableInfo workflowMainTableInfo = new WorkflowMainTableInfo();//主表
                WorkflowRequestTableRecord[] workflowRequestTableRecord = new WorkflowRequestTableRecord[1];//主表字段只有一条记录
                WorkflowRequestTableField[] WorkflowRequestTableField = new WorkflowRequestTableField[8];//主表的3个字段

                WorkflowRequestTableField[0] = new WorkflowRequestTableField();
                WorkflowRequestTableField[0].setFieldName("sqr");//姓名
                WorkflowRequestTableField[0].setFieldValue(sqr);//userId
                WorkflowRequestTableField[0].setView(true);
                WorkflowRequestTableField[0].setEdit(true);

                WorkflowRequestTableField[1] = new WorkflowRequestTableField();
                WorkflowRequestTableField[1].setFieldName("szgs");//所属分部
                WorkflowRequestTableField[1].setFieldValue(fb);//分部Id
                WorkflowRequestTableField[1].setView(true);
                WorkflowRequestTableField[1].setEdit(false);

                WorkflowRequestTableField[2] = new WorkflowRequestTableField();
                WorkflowRequestTableField[2].setFieldName("sqbm");//部门
                WorkflowRequestTableField[2].setFieldValue(bm);//deptId
                WorkflowRequestTableField[2].setView(true);
                WorkflowRequestTableField[2].setEdit(false);

                WorkflowRequestTableField[3] = new WorkflowRequestTableField();
                WorkflowRequestTableField[3].setFieldName("gh");//员工编号
                WorkflowRequestTableField[3].setFieldValue(gh);//员工编号
                WorkflowRequestTableField[3].setView(true);//字段是否可见
                WorkflowRequestTableField[3].setEdit(false);//字段是否可编辑

                WorkflowRequestTableField[4] = new WorkflowRequestTableField();
                WorkflowRequestTableField[4].setFieldName("gw");//岗位
                WorkflowRequestTableField[4].setFieldValue(gw);//
                WorkflowRequestTableField[4].setView(true);//字段是否可见
                WorkflowRequestTableField[4].setEdit(false);//字段是否可编辑

                WorkflowRequestTableField[5] = new WorkflowRequestTableField();
                WorkflowRequestTableField[5].setFieldName("nf");//年份
                WorkflowRequestTableField[5].setFieldValue(Integer.toString(year));//
                WorkflowRequestTableField[5].setView(true);//字段是否可见
                WorkflowRequestTableField[5].setEdit(false);//字段是否可编辑

                WorkflowRequestTableField[6] = new WorkflowRequestTableField();
                WorkflowRequestTableField[6].setFieldName("yf");//月份
                WorkflowRequestTableField[6].setFieldValue(Integer.toString(month));//
                WorkflowRequestTableField[6].setView(true);//字段是否可见
                WorkflowRequestTableField[6].setEdit(false);//字段是否可编辑

                WorkflowRequestTableField[7] = new WorkflowRequestTableField();
                WorkflowRequestTableField[7].setFieldName("sqrq");//申请日期
                WorkflowRequestTableField[7].setFieldValue(applyDate);//
                WorkflowRequestTableField[7].setView(true);//字段是否可见
                WorkflowRequestTableField[7].setEdit(false);//字段是否可编辑



                workflowRequestTableRecord[0] = new WorkflowRequestTableRecord();
                workflowRequestTableRecord[0].setWorkflowRequestTableFields(WorkflowRequestTableField);
                workflowMainTableInfo.setRequestRecords(workflowRequestTableRecord);

                workflowRequestInfo.setWorkflowMainTableInfo(workflowMainTableInfo);
                /****************main table end*************/

                /**********明细表开始**********/

                //获取个人方案
                //获取申请人方案
                RecordSetDataSource data2 = new RecordSetDataSource("e9");
                data2.executeSql("select b.* from uf_GlgNet_jxkhlb a inner join " +
                        "uf_GlgNet_jxkhlb_dt1 b on a.id=b.mainid " +
                        " where a.khr='" + 1117 + "' ");
                if (data2.getColCounts() > 0) {
                    WorkflowDetailTableInfo[] workflowDetailTableInfo = new WorkflowDetailTableInfo[1];//1个明细表
                    workflowDetailTableInfo[0] = new WorkflowDetailTableInfo();
                    workflowRequestTableRecord = new WorkflowRequestTableRecord[data2.getColCounts()];//两行数据（两条记录）
                    WorkflowRequestTableField = new WorkflowRequestTableField[4];//每行4个字段
                    int flag = 0;
                    while (data2.next()) {
                        //获取信息
                        String  khzb = data2.getString("khzb");
                        //工号
                        String  zbdy = data2.getString("zbdy");
                        //获取信息
                        String  qz = data2.getString("qz");
                        log.info("----考核指标："+khzb);
                        //工号
                        String  qzfz = data2.getString("mbfz");

                        /****第一行开始****/
                        WorkflowRequestTableField[0] = new WorkflowRequestTableField();
                        WorkflowRequestTableField[0].setFieldName("khzb");//考核指标
                        WorkflowRequestTableField[0].setFieldValue(khzb);
                        WorkflowRequestTableField[0].setView(true);
                        WorkflowRequestTableField[0].setEdit(true);

                        WorkflowRequestTableField[1] = new WorkflowRequestTableField();
                        WorkflowRequestTableField[1].setFieldName("zbdy");//指标定义
                        WorkflowRequestTableField[1].setFieldValue(zbdy);
                        WorkflowRequestTableField[1].setView(true);
                        WorkflowRequestTableField[1].setEdit(true);

                        WorkflowRequestTableField[2] = new WorkflowRequestTableField();
                        WorkflowRequestTableField[2].setFieldName("qz");//权重
                        WorkflowRequestTableField[2].setFieldValue(qz);
                        WorkflowRequestTableField[2].setView(true);
                        WorkflowRequestTableField[2].setEdit(true);

                        WorkflowRequestTableField[3] = new WorkflowRequestTableField();
                        WorkflowRequestTableField[3].setFieldName("qzfz");//权重分值
                        WorkflowRequestTableField[3].setFieldValue(qzfz);
                        WorkflowRequestTableField[3].setView(true);
                        WorkflowRequestTableField[3].setEdit(true);


                        workflowRequestTableRecord[flag] = new WorkflowRequestTableRecord();
                        workflowRequestTableRecord[flag].setWorkflowRequestTableFields(WorkflowRequestTableField);
                        /****第一行结束****/


                       flag = flag+1;

                    }

                    workflowDetailTableInfo[0].setWorkflowRequestTableRecords(workflowRequestTableRecord);
                    /**********第一张明细表结束**********/
                    //workflowRequestInfo.setWorkflowDetailTableInfos(workflowDetailTableInfo);
                }
                RequestInfo requestinfo =new WorkflowServiceImpl().toRequestInfo(workflowRequestInfo);
                RequestService rservice = new RequestService();
                String rs = rservice.createRequest(requestinfo);
                /*
                String requestId = new WorkflowServiceImpl().doCreateWorkflowRequest(workflowRequestInfo, Integer.parseInt("888"));
                if (Integer.parseInt(requestId) > 0) {
                    log.info("流程触发成功  requestId:" + requestId);
                } else {
                    log.info("流程触发失败  姓名:" + userName + "，工号：" + gh);
                }
                *
                 */
                log.info("创建测试流程成功！" + "--------" + rs);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }

}
