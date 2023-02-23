package weaver.interfaces.schedule.mes.job;

import com.ibm.icu.text.SimpleDateFormat;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;
import weaver.conn.RecordSetDataSource;
import weaver.interfaces.schedule.BaseCronJob;

import java.util.Calendar;

public class GLGCustMesModBjTB  extends BaseCronJob {

    private Log log = LogFactory.getLog(GLGCustMesModBjTB.class.getName());


    @Override
    public void execute() {
        // TODO Auto-generated method stub


        log.info("同步冲压零件到模具！");
        OA_purta();
     
        log.info("执行定时任务完成！");

    }

    //冲压车间模具失效发送消息提醒并更新模具状态

    @Deprecated

    public void OA_purta() {


        try {
            String MODEcODE="";
            String Code="";
            String modType="";
            int modId = -1;
            int codeId = -1;
            int modCount = 0;
            int TableSequence = getTableSequence();

            RecordSetDataSource ds = new RecordSetDataSource("U9_75");
            ds.executeSql(" select  * from GlgNet_ModBom where datediff(day,ModifiedOn,getdate())<= 15 " );
            RecordSetDataSource data = new RecordSetDataSource("MES");

            while(ds.next())
            {
                modId = -1;
                 codeId = -1;
                MODEcODE="";
                Code="";
                modType = ds.getString("modType");
                MODEcODE = ds.getString("MODEcODE");
                Code = ds.getString("Code");
                //判断备件是否存在存在则不考虑
                if(ifhasCode(MODEcODE,Code)){
                    continue;
               }
                if(modType.equals("W"))
                {
                    modCount = 500000;
                }else{

                    modCount = 1000000;
                }
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String applyDate = formatter.format(cal.getTime());
                modId = getModID(MODEcODE);
                codeId= getCodeID(Code);
                //不存在，则添加到模具备件中
                //添加模具零件
                if(modId>0 && codeId>0) {
                    data.executeSql("insert into ModMould_KeyMaterials values" +
                            "(" + TableSequence + "," +
                            "" + modId + "," + codeId + "," + modCount + ",0," + modCount + ",1," +
                            "'" + applyDate + "',1," +
                            "'" + applyDate + "','')");
                    TableSequence = TableSequence + 1;
                }

            }

            //更新流水号

            data.executeSql(" update SysTableSequence set Sequence="+TableSequence+" " +
                    " TableName='ModMould_KeyMaterials' ");





        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("更新Token失败");
        }

    }




    //判断模具是否已经存在该备件

    public boolean ifhasCode(String modecode,String code)
    {
        int  modid = getModID(modecode);
        int codeid= getCodeID(code);
        RecordSetDataSource ds = new RecordSetDataSource("MES");
        ds.executeSql(" select * from ModMould_KeyMaterials where " +
                " ProductId='"+ codeid + "' " +
                "  and MouldId='"+ modid+"'");
        if (ds.getCounts() > 0) {
        return true;
        }
        return false;
    }

    public int getTableSequence()
    {
        int TableSequence =-1;
        RecordSetDataSource ds = new RecordSetDataSource("MES");
        ds.executeSql(" select * from SysTableSequence where TableName='ModMould_KeyMaterials' " );
        while(ds.next())
        {
            TableSequence = Integer.parseInt(ds.getString("Sequence"));
        }
        return TableSequence;
    }

    //获取模具ID
    public int getModID(String code)
    {
        int MouldId =-1;
        RecordSetDataSource ds = new RecordSetDataSource("MES");
        ds.executeSql(" select MouldId from ModMould where MouldCode='"+code+"'" );
        while(ds.next())
        {
            MouldId = Integer.parseInt(ds.getString("MouldId"));
        }
        return MouldId;
    }

    //获取产品Id
    public int getCodeID(String code)
    {
        int ProductId =-1;
        RecordSetDataSource ds = new RecordSetDataSource("MES");
        ds.executeSql(" select a.ProductId from BasProduct  a"
                +" inner join  BasProductRoot  b " +
                " on a.ErpId =b.ErpId "+
                " where b.ProductRootCode='"+code+"'" );
        while(ds.next())
        {
            ProductId = Integer.parseInt(ds.getString("ProductId"));
        }
        return ProductId;
    }




}
