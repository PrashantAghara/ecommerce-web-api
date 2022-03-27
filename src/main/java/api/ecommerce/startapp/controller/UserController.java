package api.ecommerce.startapp.controller;
import api.ecommerce.startapp.entities.Product;
import api.ecommerce.startapp.entities.User;
import api.ecommerce.startapp.exceptions.UnauthorizedAccessException;
import api.ecommerce.startapp.services.UserService;
import api.ecommerce.startapp.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User loginUser = userService.validate(user.getUserName(), user.getPassWord());
        if (loginUser != null) {
            UserResponse userResponse = new UserResponse(loginUser.getUser_id(), 200, loginUser.getUserName(), loginUser.getEmail(), "Login Success");
            return ResponseEntity.ok().body(userResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse(404, "Invalid Username or password");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            if (savedUser != null) {
                UserResponse userResponse = new UserResponse(savedUser.getUser_id(), 200, savedUser.getUserName(), savedUser.getEmail(), "Registration Successfully");
                return ResponseEntity.ok().body(userResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(500, "Internal Server Error");
                return ResponseEntity.status(500).body(errorResponse);
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            illegalArgumentException.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse(400, illegalArgumentException.getMessage());
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @GetMapping("/{user_id}/products")
    public ResponseEntity<?> getAllProducts(@PathVariable("user_id") Integer user_id) {
        try {
            List<Product> products = userService.getAllProducts(user_id);
            if (products != null) {
                ListResponse listResponse = new ListResponse(products, 200, "Success");
                return ResponseEntity.ok().body(listResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(404, "No Products found");
                return ResponseEntity.status(404).body(errorResponse);
            }
        } catch (UnauthorizedAccessException unauthorizedAccessException) {
            unauthorizedAccessException.printStackTrace();
            ErrorResponse response = new ErrorResponse(403, unauthorizedAccessException.getMessage());
            return ResponseEntity.status(403).body(response);
        }
    }

    @PostMapping("/{user_id}/cart/{product_id}")
    public ResponseEntity<?> addToCart(@PathVariable("user_id") Integer user_id, @PathVariable("product_id") Integer product_id) {
        try {
            if (userService.addProduct(user_id, product_id) != null) {
                SuccessResponse successResponse = new SuccessResponse(200, "Product Added to Cart");
                return ResponseEntity.ok().body(successResponse);
            } else {
                ErrorResponse response = new ErrorResponse(403, "Forbidden! Please Login");
                return ResponseEntity.status(403).body(response);
            }
        } catch (NoSuchElementException noSuchElementException) {
            noSuchElementException.printStackTrace();
            ErrorResponse response = new ErrorResponse(404, "Product Not Found");
            return ResponseEntity.status(404).body(response);
        } catch (UnauthorizedAccessException unauthorizedAccessException) {
            unauthorizedAccessException.printStackTrace();
            ErrorResponse response = new ErrorResponse(403, unauthorizedAccessException.getMessage());
            return ResponseEntity.status(403).body(response);
        }
    }

    @GetMapping("/{userid}/cart")
    public ResponseEntity<?> getCartProducts(@PathVariable("userid") Integer user_id) {
        try {
            CartItemsResponse cartProducts = userService.getProductsInCart(user_id);
            if (!cartProducts.getData().isEmpty()) {
                cartProducts.setStatusCode(200);
                cartProducts.setMessage("Success");
                return ResponseEntity.ok().body(cartProducts);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(404, "No Products In Cart");
                return ResponseEntity.status(404).body(errorResponse);
            }
        } catch (UnauthorizedAccessException unauthorizedAccessException) {
            unauthorizedAccessException.printStackTrace();
            ErrorResponse response = new ErrorResponse(403, unauthorizedAccessException.getMessage());
            return ResponseEntity.status(403).body(response);
        }
    }

    @GetMapping("/{userid}/logout")
    public ResponseEntity<?> logOut(@PathVariable("userid") Integer user_id) {
        if (userService.logout(user_id)) {
            SuccessResponse successResponse = new SuccessResponse(200, "LogOut Success");
            return ResponseEntity.ok().body(successResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse(500, "Internal Server Error");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @DeleteMapping("/{user_id}/cart/{product_id}")
    public ResponseEntity<?> removeProductFromCart(@PathVariable("user_id") Integer user_id, @PathVariable("product_id") Integer product_id) {
        try {
            if (userService.removeProduct(user_id, product_id) != null) {
                SuccessResponse successResponse = new SuccessResponse(200, "Product Removed from Cart");
                return ResponseEntity.ok().body(successResponse);
            } else {
                ErrorResponse response = new ErrorResponse(404, "Product Not In Cart");
                return ResponseEntity.status(404).body(response);
            }
        } catch (UnauthorizedAccessException unauthorizedAccessException) {
            unauthorizedAccessException.printStackTrace();
            ErrorResponse response = new ErrorResponse(403, unauthorizedAccessException.getMessage());
            return ResponseEntity.status(403).body(response);
        }
    }
}
