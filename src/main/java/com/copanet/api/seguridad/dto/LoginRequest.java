package com.copanet.api.seguridad.dto;

public class LoginRequest {
    private String email;
    private String password; // por ahora no lo validamos de verdad

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
