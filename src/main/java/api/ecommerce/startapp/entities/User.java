package api.ecommerce.startapp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;
    private String userName;
    private String email;
    private String passWord;
    private boolean isLogin = false;

    @Override
    public String toString() {
        return "" +
                "username='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + passWord + '\'';
    }

    public String toAdmin(){
        return "username = " + userName + " email = " + email;
    }
}
