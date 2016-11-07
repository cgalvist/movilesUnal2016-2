package cgt.SQLite.model;

public class Contacto {

    private long id;
    private String nombre;
    private String url;
    private int telefono;
    private String email;
    private String productosYServicios;
    private String departamento;

    public Contacto(long id, String nombre, String url, int telefono, String email, String productosYServicios, String departamento){
        this.id = id;
        this.nombre = nombre;
        this.url = url;
        this.telefono = telefono;
        this.email = email;
        this.productosYServicios = productosYServicios;
        this.departamento = departamento;

    }

    public Contacto(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String toString(){
        return "Id contacto: "+ getId()+ "\n" +
                "Nombre: "+ getNombre() + "\n" +
                "Url: "+ getUrl() + "\n" +
                "Tel: "+ getTelefono() + "\n" +
                "Email: "+ getEmail() + "\n" +
                "PyS: "+ getProductosYServicios() + "\n" +
                "Departmento : "+ getDepartamento();


    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProductosYServicios() {
        return productosYServicios;
    }

    public void setProductosYServicios(String productosYServicios) {
        this.productosYServicios = productosYServicios;
    }
}
