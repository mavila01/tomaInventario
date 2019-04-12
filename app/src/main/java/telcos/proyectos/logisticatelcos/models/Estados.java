package telcos.proyectos.logisticatelcos.models;

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

    public String getDescEstado() {
        return descEstado;
    }

    @Override
    public String toString() {
        return "Estados{" +
                "idEstado='" + idEstado + '\'' +
                ", descEstado='" + descEstado + '\'' +
                '}';
    }
}
