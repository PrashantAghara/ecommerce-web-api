package api.ecommerce.startapp.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer product_id;

    private String productName;

    private Integer price;

    private String description;

    @ManyToOne(targetEntity = Category.class,cascade = CascadeType.PERSIST)
    @JoinColumn( name = "category_id"  , referencedColumnName =  "category_id")
    private Category category;

}
