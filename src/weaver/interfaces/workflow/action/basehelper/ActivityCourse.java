package weaver.interfaces.workflow.action.basehelper;
import lombok.Data;

public class ActivityCourse {
    //原文件名
    public String fileName;
    //文件的外网路径
    public String filePath;

    public ActivityCourse() {
    }

    public ActivityCourse(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

}