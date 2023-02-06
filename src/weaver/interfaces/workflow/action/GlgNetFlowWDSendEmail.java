package weaver.interfaces.workflow.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.basehelper.ActivityCourse;
import weaver.interfaces.workflow.action.basehelper.flowHelper;
import weaver.interfaces.workflow.action.basehelper.sendMailHelper;
import weaver.soa.workflow.request.MainTableInfo;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;
import weaver.interfaces.workflow.action.basehelper.zip;

import java.util.ArrayList;
import java.util.List;

public class GlgNetFlowWDSendEmail extends weaver.interfaces.workflow.action.BaseAction{
    private String info = "文件发行流程" +
            "";
    Log log = LogFactory.getLog(GlgNetFlowWDSendEmail.class);
    RecordSet rs = new RecordSet();
    BaseBean baseLog = new BaseBean();
    @Override
    public String execute(RequestInfo request) {

        if (request == null || request.getMainTableInfo() == null || request.getMainTableInfo().getPropertyCount() == 0) {
            request.getRequestManager().setMessageid("GlgNetFlowWDSendEmail");
            request.getRequestManager().setMessagecontent(info + "：未获取到表单信息！");
            return "0";
        }
        String flag = "0";
        //获取流程请求id
        String Requestid=request.getRequestid();//请求ID

        MainTableInfo main = request.getMainTableInfo();//主表
        Property[] property = main.getProperty();//主表字段
        //流程单号
        String lcbh= Util.null2String(flowHelper.getPropertyByName(property, "lcbh"));//申请部门
        String fjsc= "";
        String filerealpath="";
        String imagefilename="";
        //获取当前主表名称（例如：formtable_main_122）
        String sqlTableName = request.getRequestManager().getBillTableName();
        List<ActivityCourse> courseList= new ArrayList<ActivityCourse>();
        String filePath= "D:/filerealpath/"+lcbh+"/";
        try {
           //获取附件信息
            rs.executeSql(" select  fjsc from   " +
                    "formtable_main_122 where requestid ="+Requestid);
            if (rs.getCounts() > 0) {
                while (rs.next()) {
                 fjsc= rs.getString("fjsc");
                //
                    RecordSet data = new RecordSet();
                    data.executeSql("select imagefilename,filerealpath from ImageFile where imagefileid in(select imagefileid from DocImageFile where    docid  in (" +
                            fjsc+
                            "))");
                    while (data.next()) {
                        log.info("开始解压文件");
                        filerealpath=data.getString("filerealpath");
                        imagefilename=data.getString("imagefilename");
                        log.info(filerealpath);


                        log.info(imagefilename);

                        //解压到临时文件夹
                        Boolean f =   zip.unzip1(filerealpath.replace("/","//"),filePath.replace("/","//"),filePath.replace("/","//")+imagefilename);
                        log.info(f);
                        //保存转换后的
                        ActivityCourse course= new ActivityCourse(imagefilename,filePath.replace("/","//")+imagefilename);
                        courseList.add(course);


                    }
                    log.info("发送邮件");
                    //发送邮件
                    /*
                     * Server 邮件服务器
                     * serndUid 发件人账号
                     * sendPwd 发件人密码
                     * ToUid 收件人邮箱账号
                     * title 邮件主题
                     * Content 邮件内容
                     * courseList 附件集合
                     */
                    sendMailHelper.sendEmail("smtp.qq.com","645266648@qq.com","nhihzhfxmjrtbbah","517363696@qq.com","研发图纸变更","测试发送邮件",courseList);

                    //清除临时文件
                    sendMailHelper.deleteFolders("D://filerealpath//"+lcbh);

                }
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


        return flag;
    }







}
