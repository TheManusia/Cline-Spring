package xyz.themanusia.clineapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lesson_teacher")
public class LessonTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @Column(name = "lesson_id")
    private Lesson lesson;

    @OneToOne
    @Column(name = "teacher_id")
    private Teacher teacher;

    @OneToOne
    @Column(name = "class_id")
    private Classes classes;
}
