package weaver.interfaces.schedule.mes.job;

import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;

public class GLGCustJXryOAHRTB extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustJXryOAHRTB.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("执行定时任务开始！");
       HR_Emppurta();

        log.info("执行定时任务完成！");

    }

    @Deprecated
    public void HR_Emppurta() {

        try {

            RecordSet data = new RecordSet();

            data.executeSql("select  * from uf_GlgNet_jx_Emp ");





            String  EmpNo="";
            String  zjName="";
            String  zwName="";
            String  deptCode="";
            String  G_ifsy="";

            if (data.getCounts() > 0) {
                while (data.next()) {


                    EmpNo = data.getString("HRgh");
                    //获取信息
                    RecordSetDataSource ds = new RecordSetDataSource("HRSystem");
                    ds.executeSql("select    *  from GlgHr_OAEmp a  where a.EmpNo='" +EmpNo+
                            "' " );
                    while (ds.next()) {
                        zjName = ds.getString("zjName");
                        zwName = ds.getString("zwName");
                        deptCode = ds.getString("deptCode");
                        G_ifsy = ds.getString("G_ifsy");

                        RecordSet rs = new RecordSet();
                        rs.executeSql(" update  uf_GlgNet_jx_Emp " +
                                " set bmCode='" +deptCode+"' "+
                                "," +
                                "gw='"+zwName+ "'," +
                                "zj='"+zjName+"'," +
                                "ygzt='"+G_ifsy+"'," +
                                "oabm= (select id from HrmDepartment  where departmentcode='"+deptCode+"'    and canceled is null)" +
                                " " +
                                "where HRgh='"+EmpNo+"'");



                    }


                }
            }

            log.info("同步绩效人员信息完成");






        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("同步绩效人员数据完成");
        }

    }














}
