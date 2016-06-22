package Model;

//课程模型
public class CourseModel {

    /**
     * @param id    主键
     * @param number    学号
     * @param courseInfo    课程信息
     * @param rowspan   连上几节课
     * @param row   第几节课
     * @param col   星期几
     * */
    private int id;
    private String number;
    private String courseInfo;
    private String rowspan;
    private int row;
    private int col;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getCourseInfo() {
        return courseInfo;
    }
    public void setCourseInfo(String courseInfo) {
        this.courseInfo = courseInfo;
    }
    public String getRowspan() {
        return rowspan;
    }
    public void setRowspan(String rowspan) {
        this.rowspan = rowspan;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
