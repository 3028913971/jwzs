package Model;

//成绩模型
public class GradeModel {

    /**
     * @param id    主键
     * @param kcmc  课程名称
     * @param xf    学分
     * @param jd    绩点
     * @param number    学号
     * @param xn    学年
     * @param xq    学期
     * @param kcdm  课程代码
     * @param kcxz  课程性质
     * @param kcgs  课程归属
     * @param cj    成绩
     * @param bkcj  补考成绩
     * @param cxcj  重修成绩
     * @param kkxy  开课学院
     * */
    private int id;
    private String kcmc;
    private String xf;
    private String jd;
    private String number;
    private String xn;
    private String xq;
    private String kcdm;
    private String kcxz;
    private String kcgs;
    private String cj;
    private String bkcj;
    private String cxcj;
    private String kkxy;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKcmc() {
        return kcmc;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

    public String getXf() {
        return xf;
    }

    public void setXf(String xf) {
        this.xf = xf;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getKkxy() {
        return kkxy;
    }

    public void setKkxy(String kkxy) {
        this.kkxy = kkxy;
    }

    public String getXn() {
        return xn;
    }

    public void setXn(String xn) {
        this.xn = xn;
    }

    public String getXq() {
        return xq;
    }

    public void setXq(String xq) {
        this.xq = xq;
    }

    public String getKcdm() {
        return kcdm;
    }

    public void setKcdm(String kcdm) {
        this.kcdm = kcdm;
    }

    public String getKcxz() {
        return kcxz;
    }

    public void setKcxz(String kcxz) {
        this.kcxz = kcxz;
    }

    public String getKcgs() {
        return kcgs;
    }

    public void setKcgs(String kcgs) {
        this.kcgs = kcgs;
    }

    public String getCj() {
        return cj;
    }

    public void setCj(String cj) {
        this.cj = cj;
    }

    public String getBkcj() {
        return bkcj;
    }

    public void setBkcj(String bkcj) {
        this.bkcj = bkcj;
    }

    public String getCxcj() {
        return cxcj;
    }

    public void setCxcj(String cxcj) {
        this.cxcj = cxcj;
    }
}
