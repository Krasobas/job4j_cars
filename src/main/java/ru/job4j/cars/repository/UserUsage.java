package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.model.User;

import java.util.List;

public class UserUsage {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try (SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory()) {
            var crudRepository = new CrudRepository(sf);
            var userRepository = new UserRepository(crudRepository);
            var postRepository = new PostRepository(crudRepository);
            var user = new User();
            user.setLogin("admin");
            user.setPassword("admin");
            userRepository.create(user);
            userRepository.findAllOrderById()
                    .forEach(System.out::println);
            userRepository.findByLikeLogin("e")
                    .forEach(System.out::println);
            var found = userRepository.findById(user.getId());
            found.ifPresent(System.out::println);
            found.ifPresent(u -> testPosts(u, postRepository));
            userRepository.findByLogin("admin")
                    .ifPresent(System.out::println);
            user.setPassword("password");
            userRepository.update(user);
            userRepository.findById(user.getId())
                    .ifPresent(System.out::println);
            userRepository.delete(user.getId());
            userRepository.findAllOrderById()
                    .forEach(System.out::println);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static void testPosts(User user, PostRepository postRepository) {
        var post = new Post();
        post.setUser(user);
        post.setDescription("toto");
        var price = new PriceHistory();
        price.setBefore(100L);
        price.setAfter(200L);
        post.setPriceHistories(List.of(price));
        postRepository.create(post);
        System.out.println("POSTS: ");
        postRepository.findAllOrderById().forEach(System.out::println);
    }
}