package uzlitiscada.production.springdataaccess.dao;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import uzlitiscada.production.springdataaccess.model.Author;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("dad-das")
public class JdbcAuthorDao implements AuthorDao, InitializingBean {

    private final DataSource dataSource;
    private final static String INSERT_QUERY = "insert into author(name, surname, birth_date) VALUES(?, ?, ?)";
    private final static String REMOVE_QUERY = "delete from author where id = ?";
    private final static String FIND_ALL_QUERY = "SELECT id, name, surname, birth_date FROM author";
    private final static String FIND_BY_ID_QUERY = "SELECT id, name, surname, birth_date FROM author WHERE id=?";

    public JdbcAuthorDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Author author) {
        try (var connection = dataSource.getConnection();
        var statement = connection.prepareStatement(INSERT_QUERY)){
            statement.setString(1, author.getName());
            statement.setString(2, author.getSurname());
            statement.setDate(3, Date.valueOf(author.getBirthDate()));
            statement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(REMOVE_QUERY)){
            statement.setLong(1, id);
            statement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Author> findAll() {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(FIND_ALL_QUERY)){
            var result = statement.executeQuery();
            List<Author> authors = new ArrayList<>();
            while (result.next()) {
                authors.add(
                        new Author(
                                result.getLong("id"),
                                result.getString("name"),
                                result.getString("surname"),
                                result.getDate("birth_date").toLocalDate()
                        )
                );

            }
            return authors;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Author> findById(Long id) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(FIND_BY_ID_QUERY)){
            statement.setLong(1, id);
            var result = statement.executeQuery();
            if (result.next()) {
                Author author = new  Author(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("surname"),
                        result.getDate("birth_date").toLocalDate()
                );
                return Optional.of(author);
            }else {
                return Optional.empty();
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (dataSource == null) {
            throw new BeanCreationException("DataSource is null");
        }
    }
}
