package telcos.proyectos.logisticatelcos;

public class Estados {

    String idEstado;
    String descEstado;

    public Estados(String idEstado,String descEstado) {
        this.idEstado = idEstado;
        this.descEstado = descEstado;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
    }

    public String getDescEstado() {
        return descEstado;
    }

    public void setDescEstado(String descEstado) {
        this.descEstado = descEstado;
    }

    @Override
    public String toString() {
        return "Estados{" +
                "idEstado='" + idEstado + '\'' +
                ", descEstado='" + descEstado + '\'' +
                '}';
    }
}
