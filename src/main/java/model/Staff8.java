package model;

public abstract class Staff8 extends Member8 {
    private final String position;

    protected Staff8(int id, String fullname, String phone, String address, String position) {
        super(id, fullname, phone, address);
        this.position = position;
    }

    public String position() { return position; }
}
