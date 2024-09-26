package uzlitiscada.production.springdataaccess;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import uzlitiscada.production.springdataaccess.dao.AuthorDao;
import uzlitiscada.production.springdataaccess.model.Author;

import javax.sql.DataSource;
import java.time.LocalDate;

@SpringBootApplication
public class SpringDataAccessApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataAccessApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner(@Qualifier("jdbc-template-author-dao") AuthorDao authorDao) {
        return args -> {
            Author author = new Author("Petr", "Petrov", LocalDate.now());
            Author author1 = new Author("Oleg", "Olegovich", LocalDate.now());
            Author author2 = new Author("Ivan", "Ivanov", LocalDate.now());

            authorDao.insert(author);
            authorDao.insert(author1);
            authorDao.insert(author2);

            var authors = authorDao.findAll();
            authors.forEach(System.out::println);

            authorDao.deleteById(1L);
            System.out.println(authorDao.findById(3L));
        };

    }
}
