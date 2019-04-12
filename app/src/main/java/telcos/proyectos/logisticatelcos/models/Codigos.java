package telcos.proyectos.logisticatelcos.models;

public class Codigos {
    private String mCod;
    private String mDesc;
    private String mCant;
    private String mSerial;
    private int mIdSerial;

    public Codigos(){}
    public Codigos(String mCod,String mDesc,String mCant,String mSerial,int mIdSerial) {
        this.mCod = mCod;
        this.mDesc = mDesc;
        this.mCant = mCant;
        this.mSerial = mSerial;
        this.mIdSerial = mIdSerial;
    }

    public String getmCod() {
        return mCod;
    }

    public void setmCod(String mCod) {
        this.mCod = mCod;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmCant() {
        return mCant;
    }

    public void setmCant(String mCant) {
        this.mCant = mCant;
    }

    public String getmSerial() {
        return mSerial;
    }

    public void setmSerial(String mSerial) {
        this.mSerial = mSerial;
    }

    public int getmIdSerial() {
        return mIdSerial;
    }

    public void setmIdSerial(int mIdSerial) {
        this.mIdSerial = mIdSerial;
    }

    @Override
    public String toString() {
        return "Codigos{" +
                "mCod='" + mCod + '\'' +
                ", mDesc='" + mDesc + '\'' +
                ", mCant='" + mCant + '\'' +
                ", mSerial='" + mSerial + '\'' +
                ", mIdSerial=" + mIdSerial +
                '}';
    }

}
