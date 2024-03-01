public class TV {

    private String id_number;

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public TV() {
        this.id_number = null;
    }

    public TV(String id_number) {
        this.id_number = id_number;
    }

    @Override
    public String toString(){
        return "The TV id number is: " + this.id_number;
    }
}
