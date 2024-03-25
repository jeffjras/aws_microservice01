package br.com.challenge.aws_microservice01.dto;


import br.com.challenge.aws_microservice01.model.Product;

public class ProductDTO {
    private long id;
    private String name;
    private String type;
    private String code;
    private float price;
    private String approved;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.type = product.getType();
        this.code = product.getType();
        this.price = product.getPrice();
        this.approved = product.getApproved();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }
}
