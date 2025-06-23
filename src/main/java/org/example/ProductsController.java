package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductsController {
    @Autowired
    private ProductsRepository repo;

    @GetMapping("/products")
    public ResponseEntity<List<Products>>getProducts(){
        List<Products>products=repo.findAll();
        return ResponseEntity.ok(products);

    }
    @GetMapping("/products/{id}")
    public ResponseEntity<Products>getProduct(@PathVariable int id)
    {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
    @PostMapping("/products")
    @ResponseStatus(code= HttpStatus.CREATED)
    public ResponseEntity<Products>addProduct(@RequestBody Products product){
         Products obj=repo.save(product);
         return ResponseEntity.ok(obj);
    }
    @DeleteMapping("/products/{id}")
    public void Remove(@PathVariable int id)
    {
        Products product = repo.findById(id).get();
        repo.delete(product);
    }
    @PatchMapping("/products/{id}")
    public ResponseEntity<Products>updateProduct(@PathVariable int id, @RequestBody Map<String,Object> updates)
    {
        return repo.findById(id)
                .map(products -> {
                    if(updates.containsKey("name")){
                        products.setName((String) updates.get("name"));
                    }
                    if(updates.containsKey("price")){
                        products.setPrice((int) updates.get("price"));
                    }
                    return ResponseEntity.ok(repo.save(products));
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
