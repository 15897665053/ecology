package weaver.interfaces.workflow.action;

import com.ibm.icu.text.SimpleDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.basehelper.flowHelper;
import weaver.soa.workflow.request.MainTableInfo;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

import java.util.Calendar;

public class glgnet_flowUpdatedeptJx extends weaver.interfaces.workflow.action.BaseAction{
    private String info = "部门绩效评审流程";
    Log log = LogFactory.getLog(glgnet_flowUpdatedeptJx.class);
    RecordSet rs = new RecordSet();
    BaseBean baseLog = new BaseBean();
    @Override
    public String execute(RequestInfo request) {

        if (request == null || request.getMainTableInfo() == null || request.getMainTableInfo().getPropertyCount() == 0) {
            request.getRequestManager().setMessageid("glgnet_flowUpdatedeptJx");
            request.getRequestManager().setMessagecontent(info + "：未获取到表单信息！");
            return "0";
        }
        String flag = "0";
        MainTableInfo main = request.getMainTableInfo();//主表
        Property[] property = main.getProperty();//主表字段



        String OAID=request.getRequestid();//请求ID
        log.info("------流程Id:"+OAID);
        //创建人
        String userId=request.getCreatorid();
        log.info("------创建人Id:"+userId);
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String applyDate= formatter.format(cal.getTime());
        String sqbm= Util.null2String(flowHelper.getPropertyByName(property, "szbm"));//申请部门
        log.info("申请部门"+sqbm);








        try {

            //判断部门是否已经生成绩效考核汇总信息
            // RecordSet data = new RecordSet();
            updateDeptmainTitle(OAID,userId,applyDate,sqbm);



            flag = "1";


        }catch (Exception e) {
            flag = "0";
            request.getRequestManager().setMessageid(info);
            request.getRequestManager().setMessagecontent("执行节点附加操作失败!" + e.getLocalizedMessage());
            log.info(info + ",附加操作失败：");
            log.error(info + ">>", e);
        }
        finally {
            //释放资源
            main = null;

            property = null;

        }


        return flag;
    }


    @Deprecated
    public void updateDeptmainTitle(String OAID,String Id,String applyDate,String Dept)
    {
        //RecordSet userData = new RecordSet();
        RecordSetDataSource userData = new RecordSetDataSource("e9");

        userData.executeSql(" select  * from  hrmresource where lastname='"+Id+"' " +

                "  and departmentid='"+Dept+"' ");
        String userId="";
        String UserCode="";


        while (userData.next()) {
                UserCode = userData.getString("workcode");
                userId = userData.getString("id");
                log.info("------UserId:"+userId);
                log.info("------工号:"+UserCode);
        }

        // RecordSet data = new RecordSet();
        RecordSetDataSource data = new RecordSetDataSource("e9");
        data.executeSql("update formtable_main_161 set sqr=  " +
                " '"+userId+"', " +
                " gh= '"+UserCode+"', " +
                " sqrq= '"+applyDate+"' " +
                " where requestid='" + OAID + "' " +

                " ");

    }




}

