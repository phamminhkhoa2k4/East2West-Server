package com.east2west.models.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;


@Entity
@Table(name = "Models")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "modelid")
    private int modelId;

    public int getModelId() {
        return this.modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    @Column(name = "modelname")
    private String modelName;

    public String getModelName() {
        return this.modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    // Constructors, getters, setters, etc.
}