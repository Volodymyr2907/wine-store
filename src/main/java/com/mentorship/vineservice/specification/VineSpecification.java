package com.mentorship.vineservice.specification;


import com.mentorship.vineservice.domain.Vine;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor
public class VineSpecification {

    public static Specification<Vine> equalsName(String name) {
        return (root, query, builder) ->
            name == null || name.isEmpty()
                ? builder.conjunction() :
                builder.equal(root.get("name"), name.trim());
    }

    public static Specification<Vine> equalsSugar(String sugar) {
        return (root, query, builder) ->
            sugar == null || sugar.isEmpty()
                ? builder.conjunction() :
                builder.equal(root.get("sugar"), sugar.trim());
    }

    public static Specification<Vine> equalsColor(String color) {
        return (root, query, builder) ->
            color == null || color.isEmpty()
                ? builder.conjunction() :
                builder.equal(root.get("color"), color.trim());
    }

    public static Specification<Vine> equalsGrape(String grape) {
        return (root, query, builder) ->
            grape == null || grape.isEmpty()
                ? builder.conjunction() :
                builder.equal(root.get("grapeName"), grape.trim());
    }

    public static Specification<Vine> equalsYear(Integer year) {
        return (root, query, builder) ->
            year == null || year.toString().isEmpty()
                ? builder.conjunction() :
                builder.equal(root.get("year"), year);
    }

}
