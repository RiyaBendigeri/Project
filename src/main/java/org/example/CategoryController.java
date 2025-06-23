package org.example;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryRepository repo;

    @GetMapping("/categories")
    public ResponseEntity<List<Categories>> getStudents() {
        try {
            List<Categories> categories = repo.findAll();
            if (categories.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(categories);
        }catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/categories/{id}")
    public ResponseEntity<Categories>getCategory(@PathVariable int id)
    {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/categories")
    @ResponseStatus(code= HttpStatus.CREATED)
    public ResponseEntity<Categories>postStudent(@RequestBody Categories category){
        Categories obj = repo.save(category);
        return ResponseEntity.ok(obj);
    }
    @DeleteMapping("/categories/{id}")
    public void Remove(@PathVariable int id)
    {
        Categories category = repo.findById(id).get();
        repo.delete(category);
    }
    @PatchMapping("/categories/{id}")
    public ResponseEntity<Categories>updateCategory(@PathVariable int id, @RequestBody Map<String,Object> updates)
    {
        return repo.findById(id)
                .map(categories -> {
                    if(updates.containsKey("name")){
                        categories.setName((String) updates.get("name"));
                    }
                    return ResponseEntity.ok(repo.save(categories));
                })
                .orElse(ResponseEntity.notFound().build());
    }


}
