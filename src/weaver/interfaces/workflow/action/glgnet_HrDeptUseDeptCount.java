package weaver.interfaces.workflow.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.basehelper.flowHelper;
import weaver.soa.workflow.request.*;

public class glgnet_HrDeptUseDeptCount extends weaver.interfaces.workflow.action.BaseAction{
    private String info = "用人需求流程";
    Log log = LogFactory.getLog(glgnet_HrDeptUseDeptCount.class);
    RecordSet rs = new RecordSet();
    BaseBean baseLog = new BaseBean();
    @Override
    public String execute(RequestInfo request) {

        if (request == null || request.getMainTableInfo() == null || request.getMainTableInfo().getPropertyCount() == 0) {
            request.getRequestManager().setMessageid("glgnet_HrDeptUseDeptCount");
            request.getRequestManager().setMessagecontent(info + "：未获取到表单信息！");
            return "0";
        }
        String flag = "0";
        MainTableInfo main = request.getMainTableInfo();//主表
        Property[] property = main.getProperty();//主表字段



        String OAID=request.getRequestid();//请求ID

        String xqbm= Util.null2String(flowHelper.getPropertyByName(property, "xqbmnew"));//申请部门
        String sqrq= Util.null2String(flowHelper.getPropertyByName(property, "sqrq"));//申请日期


        DetailTableInfo detail = request.getDetailTableInfo();//所有明细表
        Row[] rows = null;
        Cell[] cells = null;




        try {
            if (detail != null && detail.getDetailTableCount() > 0) {
                DetailTable d_table = detail.getDetailTable(0);//第一个明细表
                rows = d_table.getRow();
                d_table = null;
                /*******循环明细表的所有行*******/
                //请购单详情
                for (Row row : rows) {
                    cells = row.getCell();//明细每一列的值
                    //人数
                    int rs = Integer.parseInt(flowHelper.getCellByName(cells, "rs"));
                    //需求日期
                    String xqrq = flowHelper.getCellByName(cells, "xqrq");
                    String zd=flowHelper.getCellByName(cells, "zd");
                    String xqgw = flowHelper.getCellByName(cells, "xqgw");
                    //写入Hr记录表
                    RecordSetDataSource ds = new RecordSetDataSource("HRSystem");

                    ds.executeSql(" insert into glg_OADeptUseEmpRy (sqrq,dept,xqrq,rs,zd,xggw)   values('" +sqrq+"',"+
                            "'" +xqbm+"',"+
                            "'" +xqrq+"',"+
                            "'" +rs+"',"+
                            "'" +zd+"',"+
                            "'" +xqgw+"'"+
                            " )" );

                }
                flag = "1";
            }


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






}
