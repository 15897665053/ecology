package weaver.interfaces.workflow.action;

import weaver.interfaces.workflow.action.basehelper.flowHelper;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import weaver.conn.RecordSet;

import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.conn.RecordSetDataSource;

import weaver.soa.workflow.request.MainTableInfo;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

public class glgnet_FlowTomode extends weaver.interfaces.workflow.action.BaseAction{
    private String info = "请假单";
    Log log = LogFactory.getLog(glgnet_FlowTomode.class);
    RecordSet rs = new RecordSet();
    BaseBean baseLog = new BaseBean();
    @Override
    public String execute(RequestInfo request) {

        if (request == null || request.getMainTableInfo() == null || request.getMainTableInfo().getPropertyCount() == 0) {
            request.getRequestManager().setMessageid("glgnet_FlowTomode");
            request.getRequestManager().setMessagecontent(info + "：未获取到表单信息！");
            return "0";
        }
        String flag = "0";
        MainTableInfo main = request.getMainTableInfo();//主表
        Property[] property = main.getProperty();//主表字段
        //流程创建人;
        String workflowid = request.getCreatorid();

        String OAID=request.getRequestid();//请求ID

        String sqbm=Util.null2String(flowHelper.getPropertyByName(property, "sqbm"));//申请部门
        String nf=Util.null2String(flowHelper.getPropertyByName(property, "nf"));//年份
        String yf=Util.null2String(flowHelper.getPropertyByName(property, "yf"));//月份
        String  szgs=Util.null2String(flowHelper.getPropertyByName(property, "szgs"));//所属公司

        String title= nf+"年"+yf+"月"+szgs+"-"+sqbm+"绩效考核";
        //明细信息
        String khr=Util.null2String(flowHelper.getPropertyByName(property, "xm"));//考核人
        String gh=Util.null2String(flowHelper.getPropertyByName(property, "gh"));//工号
        String rzrq=Util.null2String(flowHelper.getPropertyByName(property, "rzrq"));//入职日期
        String ygzt=Util.null2String(flowHelper.getPropertyByName(property, "ygzt"));//员工状态
        String  zzpf=Util.null2String(flowHelper.getPropertyByName(property, "zzpf"));//最终评分
        String  zzpj=Util.null2String(flowHelper.getPropertyByName(property, "zzpj"));//最终评级
        String  jxjj=Util.null2String(flowHelper.getPropertyByName(property, "jxjj"));//绩效奖金

        try {
            //判断部门是否已经生成绩效考核汇总信息
           // RecordSet data = new RecordSet();
            String Id=getDetailId(sqbm,nf,yf,szgs);
            if (Id.equals("-1"))
            {
                //没有生成过部门绩效主表信息，先生成部门绩效主表信息
                CreateDeptMain(OAID,sqbm,nf,yf,szgs,title);
                Id=getDetailId(sqbm,nf,yf,szgs);
            }
            //加入部门明细信息
            CreateDeptEmpDetali(Id,khr,gh,rzrq,ygzt,zzpf,zzpj,jxjj);
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
    public String getDetailId(String sqbm, String nf,String yf,String szgs)
    {

        // RecordSet data = new RecordSet();
        RecordSetDataSource data = new RecordSetDataSource("e9");
        data.executeSql("select * from uf_deptEmp_jxsj  a " +

                " where a.bm='" + sqbm + "'  and  a.nf='" + nf + "' " +
                " and  a.yf='" + yf + "' " +
                "and  a.szgs='" + szgs + "' " +
                " ");


        //已经生成过部门绩效主表信息
        if (data.getColCounts() > 0) {
            while (data.next()) {

                //4个字段
                String  Id = data.getString("id");

                return Id;

            }
        }
        return "-1";

    }
    //添加部门主表信息
    @Deprecated
    public void CreateDeptMain(String OAID,String sqbm, String nf,String yf,String szgs,String bt)
    {

        // RecordSet data = new RecordSet();
        RecordSetDataSource data = new RecordSetDataSource("e9");
        data.executeSql(" insert into uf_deptEmp_jxsj (requestId,bm,nf,yf,szgs,bt,khzt,jxzje) values(" +
                " '"+OAID+"'," +
                "'" + sqbm + "','" + nf + "','" + yf + "','" + szgs + "' ,'" + bt + "'" +
                ",'" + 2 + "' ,'" + 0 + "')"
        );


    }
    //添加员工考核信息
    @Deprecated
    public void CreateDeptEmpDetali(String id ,String khr, String gh,String rzrq,String ygzt,String zzpf,
                                    String zzpj,String jxjj)
    {

        // RecordSet data = new RecordSet();
        RecordSetDataSource data = new RecordSetDataSource("e9");
        data.executeSql(" insert into  uf_deptEmp_jxsj_dt1 (mainid,khr,gh,rzrq,ygzt,zzpf,zzpj,jxjj) values(" +
                " '"+id+"'," +
                "'" + khr + "','" + gh + "','" + rzrq + "','" + ygzt + "' ,'" + zzpf + "'" +
                ",'" + zzpj + "' ,'" + jxjj + "')"
        );


    }




}
