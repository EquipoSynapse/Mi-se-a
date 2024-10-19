package com.synapse.dactilogo.E;

public class Paquete {
    private String nombre;
    private String autor;
    private double peso;
    private int valor;
    private int cantidadSenias;

    public Paquete(String nombre, String autor, double peso, int valor, int cantidadSenias) {
        this.nombre = nombre;
        this.autor = autor;
        this.peso = peso;
        this.valor = valor;
        this.cantidadSenias = cantidadSenias;

    }

    public String getNombre() {
        return nombre;
    }

    public String getAutor() {
        return autor;
    }

    public double getPeso() {
        return peso;
    }

    public int getValor() {
        return valor;
    }

    public int getCantidadSenias() {
        return cantidadSenias;
    }

}
