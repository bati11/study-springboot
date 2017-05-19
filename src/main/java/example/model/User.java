package example.model;


import lombok.Value;

@Value
public class User {
    private int id;
    private String name;
    private String email;
}
