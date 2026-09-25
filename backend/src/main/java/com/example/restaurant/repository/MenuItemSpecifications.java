package com.example.restaurant.repository;

import com.example.restaurant.entity.MenuItem;
import org.springframework.data.jpa.domain.Specification;



public final class MenuItemSpecifications {

    private MenuItemSpecifications(){

    }

    public static Specification<MenuItem> available(){return (root, query, cb) -> cb.isTrue(root.get("available"));}
    public static Specification<MenuItem> nameContains(String search) {
        if(search == null || search.isBlank()){
            return null;
        }

        String pattern = "%" + search.toLowerCase() + "%";
        return(root,query,cb) -> cb.like(cb.lower(root.get("name")),pattern);

    }

    public static Specification<MenuItem> inCategory(Long categoryId){
        if(categoryId == null){
            return null;
        }

        return(root,query,cb) -> cb.equal(root.get("category").get("id"),categoryId);
    }
}









