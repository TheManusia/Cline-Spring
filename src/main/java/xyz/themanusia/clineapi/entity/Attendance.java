package xyz.themanusia.clineapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.themanusia.clineapi.student.Student;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    private Student student;

    @Column(name = "time")
    private Timestamp time;

    @Column(name = "status")
    private int status;

    @Column(name = "description")
    private String description;
}
