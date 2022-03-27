package api.ecommerce.startapp.controller;

import api.ecommerce.startapp.entities.Admin;
import api.ecommerce.startapp.entities.Category;
import api.ecommerce.startapp.entities.Product;
import api.ecommerce.startapp.entities.User;
import api.ecommerce.startapp.exceptions.UnauthorizedAccessException;
import api.ecommerce.startapp.services.AdminService;
import api.ecommerce.startapp.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin user) {
        Admin loginUser = adminService.validate(user.getUserName(), user.getPassWord());
        if (loginUser != null) {
            AdminResponse adminResponse = new AdminResponse(loginUser.getAdmin_id(), loginUser.getUserName(), 200, "Login Success");
            return ResponseEntity.ok().body(adminResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse(404, "Invalid Username or password");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @GetMapping("/{adminid}/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Integer id, @PathVariable("adminid") Integer adminId) {
        try {
            Product product = adminService.getProduct(id, adminId);
            if (product != null) {
                ProductResponse productResponse = new ProductResponse(product, 200, "Success");
                return ResponseEntity.ok().body(productResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(404, "Product Not Found");
                return ResponseEntity.status(404).body(errorResponse);
            }
        } catch (UnauthorizedAccessException unauthorizedAccessException) {
            unauthorizedAccessException.printStackTrace();
            ErrorResponse response = new ErrorResponse(403, unauthorizedAccessException.getMessage());
            return ResponseEntity.status(403).body(response);
        } catch (NoSuchElementException noSuchElementException){
            ErrorResponse errorResponse = new ErrorResponse(404, "Product Not Found");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @PostMapping("/{adminid}/product")
    public ResponseEntity<?> saveProduct(@RequestBody Product product, @PathVariable("adminid") Integer adminId) {
        try {
            if (adminService.saveProduct(product, adminId) != null) {
                SuccessResponse successResponse = new SuccessResponse(200, "Inserted Product");
                return ResponseEntity.ok().body(successResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(500, "Internal Server Error");
                return ResponseEntity.status(500).body(errorResponse);
            }
        } catch (UnauthorizedAccessException unauthorizedAccessException) {
            unauthorizedAccessException.printStackTrace();
            ErrorResponse response = new ErrorResponse(403, unauthorizedAccessException.getMessage());
            return ResponseEntity.status(403).body(response);
        } catch (IllegalArgumentException illegalArgumentException) {
            illegalArgumentException.printStackTrace();
            ErrorResponse response = new ErrorResponse(400, illegalArgumentException.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    @DeleteMapping("/{adminid}/product")
    public ResponseEntity<?> deleteProduct(@RequestParam Integer id, @PathVariable("adminid") Integer adminId) {
        try {
            if (adminService.deleteProduct(id, adminId)) {
                SuccessResponse successResponse = new SuccessResponse(200, "Product Deleted");
                return ResponseEntity.ok().body(successResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(500, "Internal Server Error");
                return ResponseEntity.status(500).body(errorResponse);
            }
        } catch (UnauthorizedAccessException unauthorizedAccessException) {
            unauthorizedAccessException.printStackTrace();
            ErrorResponse response = new ErrorResponse(403, unauthorizedAccessException.getMessage());
            return ResponseEntity.status(403).body(response);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException){
            ErrorResponse response = new ErrorResponse(404, "No Product With ID "+ id + " exists" );
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping("/{adminid}/product")
    public ResponseEntity<?> getAllProducts(@PathVariable("adminid") Integer adminId) {
        try {
            List<Product> products = adminService.getAllProducts(adminId);
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

    @GetMapping("/{adminid}/category")
    public ResponseEntity<?> getAllCategories(@PathVariable("adminid") Integer adminId) {
        try {
            List<Category> categories = adminService.getAllCategories(adminId);
            if (categories != null) {
                ListResponse listResponse = new ListResponse(categories, 200, "Success");
                return ResponseEntity.ok().body(listResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(404, "No Categories found");
                return ResponseEntity.status(404).body(errorResponse);
            }
        } catch (UnauthorizedAccessException unauthorizedAccessException) {
            unauthorizedAccessException.printStackTrace();
            ErrorResponse response = new ErrorResponse(403, unauthorizedAccessException.getMessage());
            return ResponseEntity.status(403).body(response);
        }
    }

    @PostMapping("/{adminid}/category")
    public ResponseEntity<?> saveCategory(@RequestBody Category category, @PathVariable("adminid") Integer adminId) {
        try {
            Category savedCategory = adminService.insertCategory(category, adminId);
            if (savedCategory != null) {
                CategoryResponse categoryResponse = new CategoryResponse(savedCategory, 200, "Inserted Category");
                return ResponseEntity.ok().body(categoryResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(500, "Internal Server Error");
                return ResponseEntity.status(500).body(errorResponse);
            }
        } catch (UnauthorizedAccessException unauthorizedAccessException) {
            unauthorizedAccessException.printStackTrace();
            ErrorResponse response = new ErrorResponse(403, unauthorizedAccessException.getMessage());
            return ResponseEntity.status(403).body(response);
        }
    }

    @GetMapping("/{adminid}/user")
    public ResponseEntity<?> getAllUsers(@PathVariable("adminid") Integer adminId) {
        try {
            List<User> users = adminService.findAllUsers(adminId);
            if (users != null) {
                ListResponse listResponse = new ListResponse(users, 200, "Success");
                return ResponseEntity.ok().body(listResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(404, "No Users found");
                return ResponseEntity.status(404).body(errorResponse);
            }
        } catch (UnauthorizedAccessException unauthorizedAccessException) {
            unauthorizedAccessException.printStackTrace();
            ErrorResponse response = new ErrorResponse(403, unauthorizedAccessException.getMessage());
            return ResponseEntity.status(403).body(response);
        }
    }

    @GetMapping("/{adminid}/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Integer userId, @PathVariable("adminid") Integer adminId) {
        try {
            User user = adminService.findUserById(userId, adminId);
            if (user != null) {
                UserResponse userResponse = new UserResponse(user.getUser_id(), 200, user.getUserName(), user.getEmail(), "Success");
                return ResponseEntity.ok().body(userResponse);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(404, "User Not found");
                return ResponseEntity.status(404).body(errorResponse);
            }
        } catch (UnauthorizedAccessException unauthorizedAccessException) {
            unauthorizedAccessException.printStackTrace();
            ErrorResponse response = new ErrorResponse(403, unauthorizedAccessException.getMessage());
            return ResponseEntity.status(403).body(response);
        }
    }

    @GetMapping("/{adminid}/logout")
    public ResponseEntity<?> logOut(@PathVariable("adminid") Integer user_id) {
        if (adminService.logout(user_id)) {
            SuccessResponse successResponse = new SuccessResponse(200, "LogOut Success");
            return ResponseEntity.ok().body(successResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse(500, "Internal Server Error");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

}
