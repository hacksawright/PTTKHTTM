package model;

public abstract class Member8 {
    private final int id;
    private final String fullname;
    private final String phone;
    private final String address;

    protected Member8(int id, String fullname, String phone, String address) {
        this.id = id;
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
    }

    public int id() { return id; }
    public String fullname() { return fullname; }
    public String phone() { return phone; }
    public String address() { return address; }
}
