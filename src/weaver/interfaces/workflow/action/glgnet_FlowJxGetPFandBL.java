package weaver.interfaces.workflow.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.basehelper.flowHelper;
import weaver.monitor.cache.CacheFactory;
import weaver.soa.workflow.request.MainTableInfo;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

public class glgnet_FlowJxGetPFandBL extends weaver.interfaces.workflow.action.BaseAction {
    private String info = "请假单";
    Log log = LogFactory.getLog(glgnet_FlowJxGetPFandBL.class);
    RecordSet rs = new RecordSet();
    BaseBean baseLog = new BaseBean();

    @Override
    public String execute(RequestInfo request) {

        if (request == null || request.getMainTableInfo() == null || request.getMainTableInfo().getPropertyCount() == 0) {
            request.getRequestManager().setMessageid("glgnet_FlowJxGetPFandBL");
            request.getRequestManager().setMessagecontent(info + "：未获取到表单信息！");
            return "0";
        }
        String flag = "0";
        MainTableInfo main = request.getMainTableInfo();//主表
        Property[] property = main.getProperty();//主表字段

        String OAID=request.getRequestid();//请求ID
        String 	sjpfhz = Util.null2String(flowHelper.getPropertyByName(property, "sjpfhz"));//总评分
        String  pfgz  =Util.null2String(flowHelper.getPropertyByName(property, "gzdm"));//规则代码
        int  pftype=-1;
        if(pfgz.equals("common"))
        {
            pftype=1 ;

        }
        if(pfgz.equals("0102010201"))
        {
            pftype=2;

        }
        try {


        RecordSet rs = new RecordSet();
        rs.executeSql(" select  * from   " +
                "[dbo].[fn_JX_getUserPf]("+pftype+","+sjpfhz+")");
        if (rs.getCounts() > 0) {
            while (rs.next()) {
                String  pf= rs.getString("pf");
                String  bl= rs.getString("bl");

                rs.executeSql(" update formtable_main_144 set sfjxjjbl='"+bl+"'" +
                        ",zzpj='" +pf+"' " +
                        "  where requestid ='"+OAID+"'"+
                        " ");

            }

            //清除缓存
            CacheFactory sintance = CacheFactory.getInstance();



            String[] tablename ={"formtable_main_144"};

            sintance.removeCache(tablename);

        }

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
            //释放资源
            main = null;

            property = null;


        }
        flag = "1";
        return flag;
    }

}