package www.bookstore.com.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String imagePath;
    private String displayName;
    private String email;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Book> booksId=new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Author> authorsId=new ArrayList<>();
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @ColumnDefault(value = "1")
    private Integer active;
    @ManyToMany(fetch=FetchType.EAGER)
    private Collection<Role> roles=new ArrayList<>();


}
