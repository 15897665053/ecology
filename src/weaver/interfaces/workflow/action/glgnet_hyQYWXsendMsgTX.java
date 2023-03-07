package weaver.interfaces.workflow.action;

import com.ibm.icu.text.SimpleDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;
import weaver.interfaces.workflow.action.basehelper.flowHelper;
import weaver.soa.workflow.request.MainTableInfo;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

import java.util.Calendar;

public class glgnet_hyQYWXsendMsgTX extends weaver.interfaces.workflow.action.BaseAction{
    private String info = "会议申请流程";
    Log log = LogFactory.getLog(glgnet_hyQYWXsendMsgTX.class);
    RecordSet rs = new RecordSet();
    BaseBean baseLog = new BaseBean();
    @Override
    public String execute(RequestInfo request) {

        if (request == null || request.getMainTableInfo() == null || request.getMainTableInfo().getPropertyCount() == 0) {
            request.getRequestManager().setMessageid("glgnet_hyQYWXsendMsgTX");
            request.getRequestManager().setMessagecontent(info + "：未获取到表单信息！");
            return "0";
        }
        String flag = "0";
        MainTableInfo main = request.getMainTableInfo();//主表
        Property[] property = main.getProperty();//主表字段





        String meetingtype= Util.null2String(flowHelper.getPropertyByName(property, "meetingtype"));//会议类型
        String name=Util.null2String(flowHelper.getPropertyByName(property, "name"));//会议名称
        String address=Util.null2String(flowHelper.getPropertyByName(property, "address"));//会议地点
        String  begindate=Util.null2String(flowHelper.getPropertyByName(property, "begindate"));//开始日期
        String  enddate=Util.null2String(flowHelper.getPropertyByName(property, "enddate"));//结束日期
        String  begintime=Util.null2String(flowHelper.getPropertyByName(property, "begintime"));//开始时间
        String  endtime=Util.null2String(flowHelper.getPropertyByName(property, "endtime"));//结束时间
        String  hrmmembers=Util.null2String(flowHelper.getPropertyByName(property, "hrmmembers"));//参会人员




        String meetingtypeBane = getHyTypeName(meetingtype);
        String addressName = getHyroomName(address);
        String gh= "";
        String  token="";


        try {
            //获取参会人员
             RecordSet data = new RecordSet();
            data.executeSql("select workcode from  hrmresource  a " +

                    " where a.id in ("+hrmmembers +")" +
                    " ");
            if (data.getColCounts() > 0) {
                while (data.next()) {
                    if(gh !="") {
                        //4个字段
                        gh =gh+"|"+ data.getString("workcode");
                    }else{
                        gh=data.getString("workcode");
                    }



                }

            }

            //获取企业微信通token,采取定时刷新token,复用避免频繁调用获取token,导致接口请求失败
            String agentId = "1000032";
            RecordSet rs = new RecordSet();
            rs.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");
            if (rs.getCounts() > 0) {
                while (rs.next()) {


                    token = rs.getString("token");
                }
            }
            //调用企业微信发送消息

            String Content = "`您有一个会议邀请`\n>" +
                    "**会议情况** \n>" +
                    "会议类型：<font color = \"warning\">"+meetingtypeBane+"</font>\n>" +
                    "会议名称： <font color = \"comment\">" + name +"</font>  \n>" +
                    "会议地点：<font color = \"info\">"+addressName+"</font>\n>" +
                    "会议日期： <font color = \"comment\">" + begindate +"</font>  \n>" +
                    "开始时间：<font color = \"comment\">"+begintime+"</font>\n>" +

                    "结束时间： <font color = \"comment\">" + endtime + "</font>  \n>\n>" +
                    "请准时参加会议!";


            String response = QYWXCommon.SendQywxMesageMarkDownandgh(Content, agentId, token, gh);

            log.info("业务员工号："+gh +"推送成功，企业微信返回信息为" + response);

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
    public static String getHyTypeName (String meetingtype){
        RecordSet data = new RecordSet();
        //RecordSetDataSource data = new RecordSetDataSource("e9");
        data.executeSql("select * from  meeting_type  a " +

                " where a.id='" + meetingtype + "'" +
                " ");

        String  name="";
        //已经生成过部门绩效主表信息
        if (data.getColCounts() > 0) {
            while (data.next()) {

                //4个字段
                name = data.getString("name");



            }

        }
        return name;
    }

    @Deprecated
    public static String getHyroomName (String address){
        RecordSet data = new RecordSet();
        //RecordSetDataSource data = new RecordSetDataSource("e9");
        data.executeSql("select * from  [MeetingRoom]  a " +

                " where a.id in ("+address +")" +
                " ");
        String  name="";

        //已经生成过部门绩效主表信息
        if (data.getColCounts() > 0) {
            while (data.next()) {
                if(name !="") {
                    //4个字段
                    name =name+","+ data.getString("name");
                }else{
                    name=data.getString("name");
                }



            }

        }
        return name;
    }


}
