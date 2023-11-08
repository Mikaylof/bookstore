package www.bookstore.com.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import www.bookstore.com.dto.response.RespBook;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    private Author authorId;
    @ManyToOne
    @JoinColumn(name = "userInfo_id")
    private UserInfo userInfo;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;
    @ColumnDefault(value = "1")
    private Integer active;

}
