package com.example.asus.mapstations;

/**
 * Created by Asus on 29/10/2016.
 */

public class Vehicule {
    String marque;
    String carburant;
    int cylindre;
    String nom;
    double consomation;

    public Vehicule() {
    }

    public Vehicule(String marque, String carburant, int cylindre, String nom, double consomation) {
        this.marque = marque;
        this.carburant = carburant;
        this.cylindre = cylindre;
        this.nom = nom;
        this.consomation = consomation;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getCarburant() {
        return carburant;
    }

    public void setCarburant(String carburant) {
        this.carburant = carburant;
    }

    public int getCylindre() {
        return cylindre;
    }

    public void setCylindre(int cylindre) {
        this.cylindre = cylindre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getConsomation() {
        return consomation;
    }

    public void setConsomation(double consomation) {
        this.consomation = consomation;
    }
}
