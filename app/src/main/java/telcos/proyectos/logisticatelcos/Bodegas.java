package telcos.proyectos.logisticatelcos;

public class Bodegas {
    String idbodega;
    String descbodega;

    public Bodegas(String idbodega,String descbodega) {
        this.idbodega = idbodega;
        this.descbodega = descbodega;
    }

    public String getIdbodega() {
        return idbodega;
    }

    public void setIdbodega(String idbodega) {
        this.idbodega = idbodega;
    }

    public String getDescbodega() {
        return descbodega;
    }

    public void setDescbodega(String descbodega) {
        this.descbodega = descbodega;
    }

    @Override
    public String toString() {
        return "Bodegas{" +
                "idbodega='" + idbodega + '\'' +
                ", descbodega='" + descbodega + '\'' +
                '}';
    }
}
