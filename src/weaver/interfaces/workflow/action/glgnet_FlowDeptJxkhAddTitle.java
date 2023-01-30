package weaver.interfaces.workflow.action;

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

public class glgnet_FlowDeptJxkhAddTitle extends weaver.interfaces.workflow.action.BaseAction{
    private String info = "个人绩效评审流程";
    Log log = LogFactory.getLog(glgnet_FlowDeptJxkhAddTitle.class);
    RecordSet rs = new RecordSet();
    BaseBean baseLog = new BaseBean();
    @Override
    public String execute(RequestInfo request) {

        if (request == null || request.getMainTableInfo() == null || request.getMainTableInfo().getPropertyCount() == 0) {
            request.getRequestManager().setMessageid("glgnet_FlowDeptJxkhAddTitle");
            request.getRequestManager().setMessagecontent(info + "：未获取到表单信息！");
            return "0";
        }
        String flag = "0";
        MainTableInfo main = request.getMainTableInfo();//主表
        Property[] property = main.getProperty();//主表字段



        String OAID=request.getRequestid();//请求ID

        String sqbm= Util.null2String(flowHelper.getPropertyByName(property, "djbm"));//申请部门
        String nf=Util.null2String(flowHelper.getPropertyByName(property, "nf"));//年份
        String yf=Util.null2String(flowHelper.getPropertyByName(property, "yf"));//月份
        String  szgs=Util.null2String(flowHelper.getPropertyByName(property, "hzszgs"));//所属公司



        String deptName= flowHelper.getDeptName(sqbm);
        String corpName= flowHelper.getCompanyName(szgs);
        String title= nf+"年"+yf+"月-"+corpName+"-"+deptName+"-绩效考核";


        try {
            //判断部门是否已经生成绩效考核汇总信息
            // RecordSet data = new RecordSet();
            updateDeptmainTitle(OAID,title);



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
    public void updateDeptmainTitle(String OAID,String title)
    {

        // RecordSet data = new RecordSet();
        RecordSetDataSource data = new RecordSetDataSource("e9");
        data.executeSql("update formtable_main_144 set bt=  " +
                " '"+title+"' " +
                " where requestid='" + OAID + "' " +

                " ");

    }




}
