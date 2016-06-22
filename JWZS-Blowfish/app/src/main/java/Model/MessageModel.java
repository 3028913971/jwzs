package Model;

import cn.bmob.v3.BmobObject;

//留言内容模型
public class MessageModel extends BmobObject{

    /**
     * @param xsxm  学生姓名
     * @param xsxh  学生学号
     * @param message   留言内容
     * */
    private String xsxm;
    private String xsxh;
    private String message;


    public String getXsxm() {
        return xsxm;
    }

    public void setXsxm(String xsxm) {
        this.xsxm = xsxm;
    }

    public String getXsxh() {
        return xsxh;
    }

    public void setXsxh(String xsxh) {
        this.xsxh = xsxh;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
