package model;

import java.time.LocalDate;

public record Payment8(String method, String status, LocalDate paidAt) {}
