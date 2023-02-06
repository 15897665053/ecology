package weaver.interfaces.workflow.action.basehelper;

import weaver.conn.RecordSet;

import weaver.general.Util;
import weaver.soa.workflow.request.Cell;
import weaver.soa.workflow.request.Property;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public  class flowHelper {

    public static Map<String, String> getPropertyMap (Property[]property){
        Map<String, String> m = new HashMap<String, String>();
        for (Property p : property) {
            m.put(p.getName(), Util.null2String(p.getValue()));
        }
        return m;
    }
    /**
     * Cell转到map（不建议使用，,Map消耗资源，注意回收）
     * @param cells
     * @return
     */
    @Deprecated
    public static Map<String, String> getCellMap (Cell[]cells){
        Map<String, String> m = new HashMap<String, String>();
        for (Cell c : cells) {
            m.put(c.getName(), Util.null2String(c.getValue()));
        }
        return m;
    }
    /**
     * 获取主表字段的值
     * @param property 主表Property数组
     * @param name 字段名
     * @return value 值
     */
    public static String getPropertyByName (Property[]property, String name){
        for (Property p : property) {
            if (Util.null2String(name).equalsIgnoreCase(p.getName()))
                return Util.null2String(p.getValue());
        }
        return "";
    }
    /**
     * 获取明细表字段的值
     * @param cells 明细某行中列的集合
     * @param name 字段名
     * @return value 值
     */
    public static String getCellByName (Cell[]cells, String name){
        for (Cell c : cells) {
            if (Util.null2String(name).equalsIgnoreCase(c.getName())) {
                return Util.null2String(c.getValue());
            }
        }
        return "";
    }


    /**
     * 根据request获取主表
     * @param requestid
     * @param rs
     * @return
     */
    public static String getRequestTableName (String requestid, RecordSet rs){
        //方法返回值
        String strTableName = "";
        String strSql = "SELECT A.TABLENAME FROM WORKFLOW_BILL A LEFT JOIN WORKFLOW_BASE B ON A.ID = B.FORMID WHERE B.ID= " +
                "(select r.workflowid from workflow_requestbase r where r.requestid= '" + requestid + "') ";
        rs.execute(strSql);
        while (rs.next()) {
            strTableName = Util.null2String(rs.getString("TABLENAME"));
        }
        return strTableName;
    }

    //获取部门
    public static String getDeptName (String deptId){
         RecordSet data = new RecordSet();
        //RecordSetDataSource data = new RecordSetDataSource("e9");
        data.executeSql("select * from  hrmdepartment  a " +

                " where a.id='" + deptId + "'" +
                " ");


        //已经生成过部门绩效主表信息
        if (data.getColCounts() > 0) {
            while (data.next()) {

                //4个字段
                String  departmentname = data.getString("departmentname");

                return departmentname;

            }
        }
        return "-1";
    }

    //获取分部名称
    public static String getCompanyName (String corpId){
        RecordSet data = new RecordSet();
        //RecordSetDataSource data = new RecordSetDataSource("e9");
        data.executeSql("select * from  hrmsubcompany  a " +

                " where a.id='" + corpId + "'" +
                " ");


        //已经生成过部门绩效主表信息
        if (data.getColCounts() > 0) {
            while (data.next()) {

                //4个字段
                String  subcompanyname = data.getString("subcompanyname");

                return subcompanyname;

            }
        }
        return "-1";
    }


    //文件解压并重命名
    public static Boolean unzip (String fileName, String unZipPath, String rename) throws Exception {
        boolean flag = false;
        File zipFile = new File(fileName);

        ZipFile zip = null;
        try {
            //指定编码，否则压缩包里面不能有中文目录
            zip = new ZipFile(zipFile, Charset.forName("GBK"));

            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = null;
                try {
                    entry = (ZipEntry) entries.nextElement();
                } catch (Exception e) {
                    return flag;
                }

                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                String[] split = rename.split("\n");
                for (int i = 0; i < split.length; i++) {
                    zipEntryName = zipEntryName.replace(split[i], " ");//这里可以替换原来的名字
                }
                String outPath = (unZipPath + zipEntryName).replace("/", File.separator); //解压重命名

                //判断路径是否存在,不存在则创建文件路径
                File outfilePath = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
                if (!outfilePath.exists()) {
                    outfilePath.mkdirs();
                }
                //判断文件全路径是否为文件夹
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                //保存文件路径信息
                //urlList.add(outPath);

                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[2048];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }
            flag = true;
            //必须关闭，否则无法删除该zip文件
            zip.close();
        } catch (IOException e) {
            flag = false;
        }

        return flag;

    }
}
