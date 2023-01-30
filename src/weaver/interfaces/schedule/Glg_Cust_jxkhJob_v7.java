package weaver.interfaces.schedule;

import com.ibm.icu.text.SimpleDateFormat;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.conn.RecordSetTrans;
import weaver.soa.workflow.request.MainTableInfo;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;
import weaver.soa.workflow.request.RequestService;

import java.util.Calendar;

public class Glg_Cust_jxkhJob_v7 extends BaseCronJob {
    private Log log = LogFactory.getLog(Glg_Cust_jxkhJob_v7.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        log.info("执行定时任务开始！");

        tiggerWorkflow();
        log.info("执行定时任务完成！");

    }
    //需要定时执行的代码块
    @Deprecated
    public void tiggerWorkflow(){
        String workflowId = "1348";// 流程id

        String userName ="";//申请人名称
        String applyDate="";//申请日期

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        applyDate= formatter.format(cal.getTime());
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        //获取khr信息
        RecordSet data = new RecordSet();
        data.executeSql("select a.*,c.ms from uf_GlgNet_jx_Emp a" +
                " inner join uf_bm_jx_Rule c on a.pfgz = c.bmbm "+
                " where  a.HRgh  not in (select gh from formtable_main_144  b where  b.nf="+year+" and yf= "+month+")  " +
                " and a.jxfa is not null    ");
        //获取年份，//月份


        while (data.next()) {
            //员工Id
            // String  sqr = data.getString("id");
            String  userid = "1";
            //工号
            String  gh = data.getString("HRgh");


            RecordSet userData = new RecordSet();
            userData.executeSql(" select  * from  hrmresource where workcode= '" + gh + "'   ");


            //分部
            String  fb = data.getString("szgs");
            //部门
            String  bm = data.getString("oabm");
            //部门编码
            String bmcode= data.getString("bmCode");
            //岗位
            String  gw = data.getString("gw");
            //姓名
            userName = data.getString("xm");
            //绩效基数
            String jxjs= data.getString("jxjjjs");
            //入职日期
            String rzrq= data.getString("rzrq");
            //员工状态
            String ygzt= data.getString("ygzt");
            //初评人
            String cpr  = data.getString("cpr");
            //方案Id
            String jxfaId  = data.getString("jxfa");
            //规则描述
            String pfgz  = data.getString("ms");
            String gzdm  = data.getString("pfgz");
            //获取评价人
            String pjr =  getPSRInfo(jxfaId);
            //获取部门对应的顶级部门用于部门考核信息提交
            String deptHostID= getDepthost(bmcode);
            //根据汇总部门获取对应的所属公司
           String fbHostID= getfbhost(deptHostID);
            String hasOAID = "";

            if (userData.getCounts() > 0) {

                while (userData.next()) {
                    userid = userData.getString("id");
                }
            }
            if(cpr == null || cpr .equals(""))
            {
                hasOAID="1";
            }else{
                hasOAID="0";
            }

            String workflowName =   "XZ14-个人绩效评审流程";

            try {
                log.info("评价人"+pjr);
                log.info("初评人ID"+cpr);

                log.info("生成"+userName+workflowName);


                RequestService rservice = new RequestService();
                RequestInfo requestinfo = new RequestInfo();

                MainTableInfo maintableinfo = new MainTableInfo();


                RecordSetTrans rst = new RecordSetTrans();


                //评分规则
                Property newpfgz = new Property();
                //property.setType("String");
                newpfgz.setName("pfgz");
                newpfgz.setValue(pfgz);
                maintableinfo.addProperty(newpfgz);

                Property newgzdm = new Property();
                //property.setType("String");
                newgzdm.setName("gzdm");
                newgzdm.setValue(gzdm);
                maintableinfo.addProperty(newgzdm);


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

                gangw.setName("hrgw");
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
                erpjxje.setValue(jxjs);
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


                //汇总分部
                Property hzfb = new Property();

                hzfb.setName("hzszgs");
                hzfb.setValue(fbHostID);
                maintableinfo.addProperty(hzfb);






                if(cpr != null && !cpr .equals("")) {


                    Property xzry = new Property();
                    xzry.setName("cpr");
                    xzry.setValue(cpr);
                    maintableinfo.addProperty(xzry);
                }

                //360评审
                Property ry = new Property();
                ry.setName("ry");
                ry.setValue(pjr);
                maintableinfo.addProperty(ry);



                //创建人
                requestinfo.setCreatorid(userid);

                requestinfo.setMainTableInfo(maintableinfo);


                /****************main table end*************/

                requestinfo.setRsTrans(rst);

                requestinfo.setWorkflowid(workflowId);

                requestinfo.setDescription("XZ14-个人绩效评审流程-"+ userName + "-" + formatter.format(cal.getTime()));



                String rs = rservice.createRequest(requestinfo);

                if (Integer.parseInt(rs) > 0) {
                    /**********明细表开始**********/

                    //获取个人方案
                    //获取申请人方案
                    String  khzb="";
                    String  zbdy="";
                    String  qz="";
                    String  qzfz="";
                    String  mbz="";
                    String  sjly="";
                    String  linepjr="";

                    RecordSetDataSource data2 = new RecordSetDataSource("e9");
                    data2.executeSql("select b.* from  " +
                            " uf_GlgNet_jxkhlb_dt1 b " +
                            " where b.mainid='" + jxfaId + "' ");
                    if (data2.getCounts() > 0) {

                        log.info("----数据行数："+data2.getCounts());






                        while (data2.next()) {

                            //获取信息
                            khzb = data2.getString("khzb");
                            //工号
                            zbdy = data2.getString("zbdy");
                            //获取信息
                            qz = data2.getString("qz");
                            log.info("----考核指标："+khzb);
                            //工号
                            qzfz = data2.getString("mbfz");
                            mbz = data2.getString("mbz");
                            sjly = data2.getString("sjly");
                            linepjr = data2.getString("pjr");



                            createDetail(rs,khzb,zbdy,qz,qzfz,mbz,sjly,linepjr);

                            log.info("创建明细表成功！");
                        }


                        /**********第一张明细表结束**********/





                    }
                    log.info("流程触发成功  requestId:" + rs);
                } else {
                    log.info("流程触发失败  姓名:" + userName + "，工号：" + gh);
                }




            } catch (Exception e) {
                // TODO Auto-generated catch block

                log.info(e.getMessage());
                e.printStackTrace();
            }
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

    //获取方案里面的评审人员转为多人力资源信息
    public String getPSRInfo(String id ){
        RecordSet rs = new RecordSet();
        rs.executeSql("select   min(id) minid,   pjr  from  uf_GlgNet_jxkhlb_dt1  " +
                " where mainid='" + id + "' " +
                " group by pjr " +
                " order  by min(id) asc " +
                " ");
        String Pjrid = "";
        while (rs.next()) {
            Pjrid += rs.getString("pjr") + ",";
        }

        return Pjrid;

    }


    //部门汇总处理，
    public String getDepthost(String code) {
        RecordSet rs = new RecordSet();
        String id="";
        rs.executeSql(" exec Glg_getOADeptIdByDeptCode " +
                "'"+code+"' ");
        while (rs.next()) {
            id=rs.getString("deptid");

        }
        if (id.equals("-1"))
        {
            return "229";
        }else{

            return id;
        }
    }

    public String getfbhost(String DeptId ){
        RecordSet rs = new RecordSet();
        rs.executeSql("select * from  hrmdepartment " +
                " where id='" + DeptId + "' ");
        String subcompanyid1 = "";
        String id = "";
        while (rs.next()) {
            subcompanyid1 = rs.getString("subcompanyid1");

        }
        return subcompanyid1;

    }


}
