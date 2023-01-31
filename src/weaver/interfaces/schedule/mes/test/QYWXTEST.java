package weaver.interfaces.schedule.mes.test;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.mes.helper.QYWXCommon;

import java.util.Calendar;
import java.util.Date;

public class QYWXTEST {

    public static void main(String args[]) throws Exception {

        //String corpid="ww1088ebca78fcd50e";
        //String secret="1h_p57LO1Qw-cF5HtUpTM-CgLw1M5-pLWaZVjzQ0jac";
        //String ConverUrl = "https://image-pub.lexiang-asset.com/common/assets/wish/birthday_02_cover_img_1068x455.png";
        //String LinkUrl="http://fx.glgnet.cn:8081/solist";
        //String title = "公司接单金额汇总";

        //String token= "SvWOVXiXjeKSSZgsIJQFcARdkd2JcaUkmlfrlTUP0FmpS7PBTZ9lAP7naOuKJfgUqQvDlPITyaeNJb4aBPRBEd38hJT-YTqkhYuP3tlELSZhRidKrjrVKdnjKhSQN6m5XnW5BtiVEg3bQzImrVA7ra_Y4kNEHNv-fu9mek10KsxD1hT2fHel-iUZpwFRC-zZ5xG_yWFEmkd7tEtBoerAGw";
        //System.out.println(QYWXCommon.SendQywxMesageCardaddUrl(LinkUrl,title,"1000014",token));
        String   gh = "FX11663|FX10324|FX04567";
        Calendar cal = Calendar.getInstance();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(isLastDayOfMonth(dateFormat.parse("2023-01-31")))
        {
            gh=gh+"|FX00001|FX00002|";
        }


        int month = cal.get(Calendar.MONTH) ;
        System.out.println(month);
        System.out.println(gh);
        //OA_purta();
        //System.out.println(prid);

    }


    public static void OA_purta() {


        try {

            String agentId = "1000022";
            RecordSet data = new RecordSet();
            String title = "";
            String linkUlr = "http://fx.glgnet.cn:10012/view/XZ/QZ/GZdetail.aspx";

            data.executeSql("select  * from uf_wxqy_tokenRecord a " +

                    " where a.agentId='" + agentId + "' ");
            String   gh = "";
            String token = "";
            String  Sessionid = "";
            String  EmpId = "";
            String  empName = "";

            String  Memo = "";
            String  DtName = "";

            if (data.getCounts() > 0) {
                while (data.next()) {


                    token = data.getString("token");
                    //获取信息
                    RecordSetDataSource ds = new RecordSetDataSource("HRSystem");
                    ds.executeSql("  select b.Code gh,a.SessionID ,a.EmpId,b.Name empName,s.Memo+'工资' as bt," +
                            "s.Memo,D.name DtName from Gz_Total a  " +
                            " inner  join ZlEmployee   b  " +
                            " on a.EmpID =b.ID " +
                            "left join zldept D on a.dept=D.code AND (ISNULL(D.IFSEE,0)=0) " +
                            " left join S_Session S On a.SessionID=S.ID " +
                            " where  a.qrState =0   " +


                            " " );
                    while (ds.next()) {
                        gh = ds.getString("gh");

                        Sessionid = ds.getString("SessionID");

                        EmpId =   ds.getString("EmpId");
                        title =   ds.getString("bt");
                        Memo  =   ds.getString("Memo");
                        empName  =   ds.getString("empName");
                        DtName  =   ds.getString("DtName");

                        linkUlr = linkUlr+"?Memo="+Memo;

                        //推送消息.
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String applyDate= formatter.format(cal.getTime());

                        String description = "<div class=\"gray\">"+applyDate+"</div> " +
                                "<div class=\"normal\">部门："+DtName+"</div>" +
                                "<div class=\"normal\">姓名："+empName+"</div>" +
                                "<div class=\"normal\">工号："+gh+"</div>" +




                                "<div class=\"highlight\">" +
                                "您有一份工资单待确认，请尽快确认!</div>";
                        String response = QYWXCommon.SendQywxMesageCardaddUrlandgh(linkUlr, title, description,agentId, token, gh);
                        RecordSetDataSource hr = new RecordSetDataSource("HRSystem");
                        hr.executeSql(" update Gz_Total set qywxSendTime ='" +
                                "'  " +
                                "where  EmpId  '" +EmpId+"' and Sessionid='"+Sessionid+
                                "' " );


                    }


                }
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }
    public static boolean isLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            return true;
        }
        return false;
    }

}
