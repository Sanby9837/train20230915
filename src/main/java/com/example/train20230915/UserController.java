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
    //http://localhost:8080/users
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
        // 根據Id篩選顯示姓名
        List<String> names = users.stream()
                .filter(user -> user.getId() == id)
                .map(User::getName)
                .collect(Collectors.toList());

        return ResponseEntity.ok(names);
    }

    @GetMapping("/findUserByRequestBody")
    //http://localhost:8080/users/findUserByRequestBody
    //    {
    //        "age":20
    //    }
    public ResponseEntity<List<User>> findUserByRequestBody(@RequestBody Map<String, Integer> requestBody) {
        //根據年齡顯示符合的使用者姓名
        int age = requestBody.get("age");

        List<User> findUsers = users.stream()
                .filter(user -> user.getAge() == age)
                .collect(Collectors.toList());

        return ResponseEntity.ok(findUsers);
    }

    @PostMapping
    //http://localhost:8080/users
    //    {
    //        "id":1,
    //            "age":20,
    //            "name":"test-post"
    //    }
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
    //http://localhost:8080/users
    //    {
    //        "id":1,
    //            "age":20,
    //            "name":"test-put"
    //    }
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
    //http://localhost:8080/users/1
    public ResponseEntity<List<User>> delete(@PathVariable int id) {
        // 根據ID刪除user
        users = users.stream()
                .filter(user -> user.getId() != id)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/DeleteUserByRequestBody")
    //http://localhost:8080/users/DeleteUserByRequestBody
    //    {
    //        "id":3,
    //    }
    public ResponseEntity<List<User>> DeleteMapping(@RequestBody User request) {
        // 根據ID刪除user
        users = users.stream()
                .filter(user -> user.getId() != request.getId())
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}/age/{age}")
    //http://localhost:8080/users/4/age/33
    public ResponseEntity<List<User>> updateUserAge(@PathVariable("id") int id, @PathVariable("age") int age) {
        //根據id改年齡
        for (User user : users) {
            if (user.getId() == id) {
                user.setAge(age);
                break;
            }
        }
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/updateUserNameByRequestBody")
    //http://localhost:8080/users/updateUserNameByRequestBody
    //    {
    //        "id":4,
    //            "name":"test-Patch"
    //    }
    public ResponseEntity<List<User>> updateUserAgeByRequestBody(@RequestBody Map<String, Object> requestBody) {
        //根據id改姓名
        int findId = (int)requestBody.get("id");
        String newName = (String)requestBody.get("name");

        for (User user : users) {
            if (user.getId() == findId) {
                user.setName(newName);
                break;
            }
        }

        List<User> showUsers = users.stream()
                .filter(user -> user.getId() == findId)
                .collect(Collectors.toList());

        return ResponseEntity.ok(showUsers);
    }
}

