package com.example.train20230915;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public record UserController() {

    public UserController() {
        users.add(new User(1, "Jason", 20));
        users.add(new User(2, "Alan", 22));
        users.add(new User(3, "David", 21));
        users.add(new User(4, "Monika", 20));
    }

    static List<User> users = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(users);
    }

    @GetMapping("/findByAge")
    // 網址後加上?參數名稱=值
    // http://localhost:8080/users/findByAge?age=20
    public ResponseEntity<List<String>> findByAge(@RequestParam int age) {
        // 根据age篩選顯示姓名
        List<String> names = users.stream()
                .filter(user -> user.getAge() == age)
                .map(User::getName)
                .collect(Collectors.toList());

        return ResponseEntity.ok(names);
    }

    @GetMapping("/findById/{id}")
    // 網址後加上/，再加上值
    // http://localhost:8080/users/findById/1
    public ResponseEntity<List<String>> findById(@PathVariable int id) {
        // 根据Id篩選顯示姓名
        List<String> names = users.stream()
                .filter(user -> user.getId() == id)
                .map(User::getName)
                .collect(Collectors.toList());

        return ResponseEntity.ok(names);
    }

    @PostMapping
    public ResponseEntity<List<User>> create(@RequestBody User request) {
        //想讓ID是最大號塞進去，所以不管request ID是多少，都重新確認最大號ID+1
        int newId = users.stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;

        User newUser = new User(newId, request.getName(), request.getAge());
        users.add(newUser);
        return ResponseEntity.ok(users);
    }

    @PutMapping
    public ResponseEntity<List<User>> update(@RequestBody User request) {
        // 根據ID去update user的 name 和 age
        for (User user : users) {
            if (user.getId() == request.getId()) {
                user.setName(request.getName());
                user.setAge(request.getAge());
                break;
            }
        }
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<User>> delete(@PathVariable int id) {
        // 根據ID刪除user
        users = users.stream()
                .filter(user -> user.getId() != id)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @PatchMapping("/users/{id}/age/{age}")
    public ResponseEntity<List<User>> updateUserAge(@PathVariable int id, @PathVariable int age) {
        for (User user : users) {
            if (user.getId() == id) {
                user.setAge(age);
                break;
            }
        }
        return ResponseEntity.ok(users);
        
    }
}

