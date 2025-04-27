package template.domain.models;

import lombok.*;

/** A product listed. */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Product {
    private Long id;
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
