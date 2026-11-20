package model;

public record Supplier8(int id, String name, String phone, String email, String address, String contactPerson) {
    public String code() { return "NCC%03d".formatted(id); }
}
