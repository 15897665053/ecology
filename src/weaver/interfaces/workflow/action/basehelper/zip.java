package weaver.interfaces.workflow.action.basehelper;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public  class zip {
    private static final int BUFFER_SIZE = 4096;

    public static  void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath, zipFilePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    private static void extractFile(ZipInputStream zipIn, String filePath, String zipFilePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));

        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        File oldName = new File(filePath);
        System.out.println(oldName);
        String str = zipFilePath.substring(zipFilePath.lastIndexOf("\\") + 1, zipFilePath.lastIndexOf("."));
        System.out.println(str);
        File zipPath = new File(zipFilePath);
        System.out.println(zipPath.getParent());
        File newName = new File(zipPath.getParent() + "\\" + str);
        System.out.println(newName);
        if (oldName.renameTo(newName)) {
            System.out.println("Renamed");
        } else {
            System.out.println("Not Renamed");
        }

        bos.close();
    }

    public static Boolean unzip1(String fileName, String unZipPath, String rename) throws Exception {
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

                            System.out.println(outPath);
                                OutputStream out = new FileOutputStream(outPath);
                                byte[] buf1 = new byte[2048];
                                 int len;
                                 while ((len = in.read(buf1)) > 0) {
                                         out.write(buf1, 0, len);
                                    }
                                 in.close();
                                 out.close();
                            File file = new File(outPath);
                            file.renameTo(new File(rename));
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


