package com.yangzl.spring.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author yangzl
 * @Date: 2020/9/3 14:13
 * @Desc:
 */

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "ingredients")
public class Ingredient {
    @Id
    private final String id;
    private final String name;
    private final Type type;
    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
