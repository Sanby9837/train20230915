package com.example.train20230915;

public interface UserMapper {
    User getUserById(int id);
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
}
