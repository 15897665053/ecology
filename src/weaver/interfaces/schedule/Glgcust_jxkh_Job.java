package weaver.interfaces.schedule;

import com.ibm.icu.text.SimpleDateFormat;

import weaver.conn.RecordSet;

import weaver.conn.RecordSetTrans;

import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;


import weaver.soa.workflow.request.*;



import java.util.Calendar;

public class Glgcust_jxkh_Job extends BaseCronJob {

    private Log log = LogFactory.getLog(Glgcust_jxkh_Job.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("执行定时任务完成！");
        OA_purta();

    }

    @Deprecated
    public void OA_purta ()  {


        RecordSet data = new RecordSet();
        //RecordSetTrans rst = new RecordSetTrans();


        data.executeSql("select * from  uf_GlgNet_jxkhlb   " +
                "  ");
        //获取年份，//月份
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        while (data.next()) {
           String  sqr = data.getString("modedatacreater");
           // String  sqr = "888";
            //工号
            String  gh = data.getString("gh");
            //分部
            String  fb = data.getString("szgs");
            //部门
            String  bm = data.getString("bm");
            //获取部门对应的顶级部门用于部门考核信息提交
            String deptHostID= getDepthost(bm);
            //岗位
            String  gw = data.getString("gw");
            //姓名
            String userName = data.getString("xm");
            //绩效金额
            String jxje = data.getString("jxje");

            String id = data.getString("id");

            String rzrq = data.getString("rzrq");
            String ygzt = data.getString("ygzt");


            String sfdbmps = data.getString("sfdbmps");
            String xzpsr = data.getString("xzpsr");
            String fwbmpsr = data.getString("fwbmpsr");

            String  pjr = getPSRInfo(id,fwbmpsr);

            RecordSet userData = new RecordSet();
            //如果申请人拥有账号 使用自己账号进行创建流程， 否则使用方案创建人申请流程
            userData.executeSql(" select  * from  hrmresource where workcode='"+gh+"' " +
                    "  ");
            String hasOAID="1";

            if(userData.getColCounts()>0)
            {

                hasOAID="0";
                while (userData.next()) {
                    sqr = userData.getString("id");
                }
            }

            log.info("员工ID"+sqr);

            RecordSet data1 = new RecordSet();
            try {
                data1.executeSql("select id  from  uf_GlgNet_ryjxsj where khr='" + sqr + "' and nf='" + year + "'  and yf='" + month + "'");
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            /*

            //已有考核流程创建记录 不再推送流程
            if (data1.getColCounts()>0)
                continue;

             */

            try {
                CreateJxFlow(sqr, year, month,gh,fb,bm,gw,userName,jxje,id,rzrq,
                        ygzt,sfdbmps,xzpsr,pjr,hasOAID,deptHostID);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Deprecated
    public void CreateJxFlow (String userid,int year, int month,String gh,String  fb, String bm ,String gw,
                              String userName,String jxje,String id ,String rzrq, String ygzt ,
                              String sfdbmps ,String xzpsr ,String fwbmpsr,String hasOAID,String deptHostID){
        String workflowId = "1306";
        RequestService rservice = new RequestService();
        RequestInfo requestinfo = new RequestInfo();

        MainTableInfo maintableinfo = new MainTableInfo();


        RecordSetTrans rst = new RecordSetTrans();



        //是否拥有OA账号
        Property hasoazh = new Property();
        //property.setType("String");
        hasoazh.setName("hasoazh");
        hasoazh.setValue(hasOAID);
        maintableinfo.addProperty(hasoazh);
        //申请人
        Property sqr = new Property();
        //property.setType("String");
        sqr.setName("sqr");
        sqr.setValue(userid);
        maintableinfo.addProperty(sqr);

        Property djbm = new Property();
        //property.setType("String");
        djbm.setName("djbm");
        djbm.setValue(deptHostID);
        maintableinfo.addProperty(djbm);


        Calendar cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //申请日期
        Property sqrq = new Property();
        //property.setType("String");
        sqrq.setName("sqrq");
        sqrq.setValue(formatter.format(cal.getTime()));
        maintableinfo.addProperty(sqrq);

        //年份
        Property nf = new Property();

        nf.setName("nf");
        nf.setValue(Integer.toString(year));
        maintableinfo.addProperty(nf);
        //月份
        Property yf = new Property();

        yf.setName("yf");
        yf.setValue(Integer.toString(month));
        maintableinfo.addProperty(yf);

        //分部
        Property szgs = new Property();

        szgs.setName("szgs");
        szgs.setValue(fb);
        maintableinfo.addProperty(szgs);
        //部门
        Property sqbm = new Property();

        sqbm.setName("sqbm");
        sqbm.setValue(bm);
        maintableinfo.addProperty(sqbm);

        //岗位
        Property gangw = new Property();

        gangw.setName("gw");
        gangw.setValue(gw);
        maintableinfo.addProperty(gangw);

        //岗位
        Property empno = new Property();

        empno.setName("gh");
        empno.setValue(gh);
        maintableinfo.addProperty(empno);

        //姓名
        Property xm = new Property();

        xm.setName("xm");
        xm.setValue(userName);
        maintableinfo.addProperty(xm);
        //考核金额
        Property erpjxje = new Property();

        erpjxje.setName("jxje");
        erpjxje.setValue(jxje);
        maintableinfo.addProperty(erpjxje);
        //入职日期
        Property erprzrq = new Property();

        erprzrq.setName("rzrq");
        erprzrq.setValue(rzrq);
        maintableinfo.addProperty(erprzrq);

        //员工状态
        Property erpygzt = new Property();

        erpygzt.setName("ygzt");
        erpygzt.setValue(ygzt);
        maintableinfo.addProperty(erpygzt);


        //是否多部门审核

        Property dbmps = new Property();

        dbmps.setName("dbmps");
        dbmps.setValue(sfdbmps);
        maintableinfo.addProperty(dbmps);





            Property xzry = new Property();
            xzry.setName("xzry");
            xzry.setValue(xzpsr);
            maintableinfo.addProperty(xzry);

            //360评审
            Property ry = new Property();
            ry.setName("ry");
            ry.setValue(fwbmpsr);
            maintableinfo.addProperty(ry);



        //创建人
        requestinfo.setCreatorid(userid);

        requestinfo.setMainTableInfo(maintableinfo);

        
        /****************main table end*************/





        requestinfo.setRsTrans(rst);

        requestinfo.setWorkflowid(workflowId);

        requestinfo.setDescription("XZ14-个人绩效评审流程-"+ userName + "-" + formatter.format(cal.getTime()));



        try {
            String rs = rservice.createRequest(requestinfo);
            //创建成功
            if(Integer.parseInt(rs)>0)
            {

                //获取申请人方案
                  RecordSet data = new RecordSet();

                data.executeSql("select b.* from uf_GlgNet_jxkhlb a inner join " +
                        "uf_GlgNet_jxkhlb_dt1 b on a.id=b.mainid " +
                        " where a.id='" + id + "' ");

                if (data.getColCounts() > 0) {

                    DetailTableInfo detailTableInfo = new DetailTableInfo();

                    DetailTable[] d_table = new DetailTable[data.getColCounts()];

                    //创建行


                    while (data.next()) {

                        //4个字段
                        String  khzb = data.getString("khzb");
                        //工号
                        String  zbdy = data.getString("zbdy");
                        //获取信息
                        String  qz = data.getString("qz");

                        //工号
                        String  mbz = data.getString("mbz");
                        String  qzfz = data.getString("mbfz");

                        String  sjly = data.getString("sjly");
                        String  pjr = data.getString("pjr");
                        //考核指标
                        createDetail(rs,khzb,zbdy,qz,qzfz,mbz,sjly,pjr);

                    }






                }
                log.info("创建测试流程成功！" + "--------" + rs);
            }else {

                log.info("创建测试流程失败！");
            }



        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("创建测试流程失败！");
        }

    }

    @Deprecated
    public void createDetail(String mainid, String khzb,String zbdy,String qz,String qzfz,String mbz,String sjly,String pjr)
    {
        RecordSet rs = new RecordSet();
        rs.executeSql("select id from  formtable_main_144 " +
                " where requestid='"+mainid+"' ");
        String id="";
        while (rs.next()) {
            id=rs.getString("id");
        }

        RecordSet rs1 = new RecordSet();
        rs1.executeSql(" insert into formtable_main_144_dt1 (mainid,khzb,zbdy,qz,qzfz,mbz,sjly,pjr) values("+
                " '"+id+"'," +
        "'"+khzb+"','"+zbdy+"','"+qz+"','"+qzfz+"' ,'"+mbz+"','"+sjly+"','"+pjr+"')"
        );


    }

    //部门汇总处理，
    public String getDepthost(String DeptId)
    {
        //总经办下级部门由本部门进行绩效提报
        if(DeptId.equals("155") || DeptId.equals("135") || DeptId.equals("145")
                || DeptId.equals("186") || DeptId.equals("151")   || DeptId.equals("176")
                || DeptId.equals("156"))
        {
            return DeptId;
        }else {
            RecordSet rs = new RecordSet();
            rs.executeSql("select * from  hrmdepartment " +
                    " where id='" + DeptId + "' ");
            String DepthostId = "";
            String id = "";
            while (rs.next()) {
                id = rs.getString("supdepid");
                if (id.equals("0")) {
                    DepthostId = DeptId;
                } else {

                    DepthostId= getDepthost(id);
                }
            }
            return DepthostId;
        }
    }

    //获取方案里面的评审人员转为多人力资源信息
    public String getPSRInfo(String id,String pjr)
    {
        RecordSet rs = new RecordSet();
        rs.executeSql("select * from  uf_GlgNet_jxkhlb_dt1 " +
                " where mainid='"+id+"' ");
        String Pjrid="";
        while (rs.next()) {
            Pjrid += rs.getString("pjr")+",";
        }
        if(Pjrid.equals(""))
        {
            return pjr;
        }else{
            return  Pjrid;
        }
    }
}