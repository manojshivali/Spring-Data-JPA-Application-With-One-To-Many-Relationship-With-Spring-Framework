package com.lumen.springdatajpa;

/**
 * Hello world!
 *
 */
import com.lumen.springdatajpa.dao.CourseRepository;
import com.lumen.springdatajpa.dao.InstructorRepository;
import com.lumen.springdatajpa.entity.Course;
import com.lumen.springdatajpa.entity.Instructor;
import com.lumen.springdatajpa.entity.InstructorDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Configuration("mainBean")
@EnableJpaRepositories(basePackages = "com.lumen.springdatajpa")
@Import(JpaConfig.class)
public class App
{

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;


    @Autowired
    private DataSource dataSource;


    public static void main( String[] args )
    {

        System.out.println( "Hello World!" );
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(App.class);
        ctx.refresh();

        App s = (App) ctx.getBean("mainBean");

        //s.createInstructorWithCourses();
       // s.findInstructorWithCourses();  // change to EAGER Loading
        // s.findCoursesForInstructor();
       // s.findInstructorWithCoursesJoinFetch(); //Lazy load will not cause any issue as we are using JOIN Fetch DB way
        //s.updateInstructor();
       // s.updateCourse();
     //   s.deleteInstructor();  //change Instructor fetch type to EAGER then it will work, and cascadeType ALL is only working for this means it will delete associated object.
          s.deleteCourse();
        ctx.close();
    }


    private void deleteCourse() {
        int theId = 17;
        System.out.println("Deleting instructor id: " + theId);
        // find the course
        Optional<Course> result = courseRepository.findById(theId);
        Course tempCourse = null;
        if (result.isPresent()) {
            tempCourse = result.get();
        }
        else {
            // we didn't find the course
            throw new RuntimeException("Did not find course id - " + theId);
        }
        System.out.println("tempCourse: " + tempCourse);

        // delete the instructor
        courseRepository.deleteById(theId);

        System.out.println("Done!");


    }

    private void deleteInstructor() {
        int theId = 3;
        System.out.println("Deleting instructor id: " + theId);

        // find the instructor
        Optional<Instructor> result = instructorRepository.findById(theId);
        Instructor tempInstructor = null;
        if (result.isPresent()) {
            tempInstructor = result.get();
        }
        else {
            // we didn't find the course
            throw new RuntimeException("Did not find instructor id - " + theId);
        }
        System.out.println("tempInstructor: " + tempInstructor);

        List<Course> courses = tempInstructor.getCourses();
        // break associations of all courses for instructor
        for (Course tempCourse : courses) {
            tempCourse.setInstructor(null);
        }

        InstructorDetail instructorDetail = tempInstructor.getInstructorDetail();
        // break associations of all courses for instructorDetail
        instructorDetail.setInstructor(null);

        // delete the instructor
        instructorRepository.deleteById(theId);

        System.out.println("Done!");
    }

    private void updateCourse() {

        int theId = 10;

        // find the course
        System.out.println("Finding course id: " + theId);
        Optional<Course> result = courseRepository.findById(theId);
        Course tempCourse = null;
        if (result.isPresent()) {
            tempCourse = result.get();
        }
        else {
            // we didn't find the course
            throw new RuntimeException("Did not find instructor id - " + theId);
        }
        System.out.println("tempCourse: " + tempCourse);
        // update the course
        System.out.println("Updating course id: " + theId);
        tempCourse.setTitle("Enjoy the Simple Things");

        courseRepository.save(tempCourse);

        System.out.println("Done!");
    }
    private void updateInstructor() {

        int theId = 1;

        // find the instructor
        Optional<Instructor> result = instructorRepository.findById(theId);
        Instructor tempInstructor = null;
        if (result.isPresent()) {
            tempInstructor = result.get();
        }
        else {
            // we didn't find the instructor
            throw new RuntimeException("Did not find instructor id - " + theId);
        }
        //Does not load the courses since they are lazy loaded.
        System.out.println("tempInstructor: " + tempInstructor);

        // update the instructor
        System.out.println("Updating instructor id: " + theId);
        tempInstructor.setLastName("TESTER");

        instructorRepository.save(tempInstructor);

        System.out.println("Done!");
    }

    private void createInstructorWithCourses() {
        System.out.println("hi");
        // create the instructor
        Instructor tempInstructor =
                new Instructor("Susan3", "Shah", "susan.shah@lumen.com");

        // create the instructor detail
        InstructorDetail tempInstructorDetail =
                new InstructorDetail(
                        "http://www.youtube.com",
                        "Video Games3");

        // associate the objects
        tempInstructor.setInstructorDetail(tempInstructorDetail);

        // create some courses
        Course tempCourse1 = new Course("Java - Spring Boot3");
        Course tempCourse2 = new Course("Micro Service with Spring Boot3");

        // add courses to instructor
        tempInstructor.add(tempCourse1);
        tempInstructor.add(tempCourse2);

        // save the instructor
        //
        // NOTE: this will ALSO save the courses
        // because of CascadeType.PERSIST
        //
        System.out.println("Saving instructor: " + tempInstructor);
        System.out.println("The courses: " + tempInstructor.getCourses());
        instructorRepository.save(tempInstructor);

        System.out.println("Done!");
    }

    private void findInstructorWithCourses() {

        int theId = 1;
        System.out.println("Finding instructor id: " + theId);

        Optional<Instructor> result = instructorRepository.findById(theId);

        Instructor tempInstructor = null;

        if (result.isPresent()) {
            tempInstructor = result.get();
        }
        else {
            // we didn't find the instructor
            throw new RuntimeException("Did not find instructor id - " + theId);
        }
        //Does not load the courses since they are lazy loaded.

        System.out.println("tempInstructor: " + tempInstructor);

        //This execution will fail as hibernate session is closed and we are lazy initialized.
        // Quick solution is change fetch type to EAGER : (Instructor to Course)
        System.out.println("the associated courses: " + tempInstructor.getCourses());

        System.out.println("Done!");
    }

    private void findCoursesForInstructor() {

        int theId = 1;
        // find instructor
        System.out.println("Finding instructor id: " + theId);

        Optional<Instructor> result = instructorRepository.findById(theId);
        Instructor tempInstructor = null;
        if (result.isPresent()) {
            tempInstructor = result.get();
        }
        else {
            // we didn't find the employee
            throw new RuntimeException("Did not find instructor id - " + theId);
        }


        System.out.println("tempInstructor: " + tempInstructor);

        // find courses for instructor
        // findCoursesByInstructorId(theId) will fetch courses from DB.
        System.out.println("Finding courses for instructor id: " + theId);

        List<Course> courses = courseRepository.findCoursesByInstructorId(theId);

        // associate the objects
        tempInstructor.setCourses(courses);

        System.out.println("the associated courses: " + tempInstructor.getCourses());

        System.out.println("Done!");
    }

    // Even with Instructor @OneToMany(fetchType=LAZY), this code will still retrieve Instructor AND Courses, the JOIN FETCH is similar to EAGER loading
    private void findInstructorWithCoursesJoinFetch() {

        int theId = 1;

        // find the instructor
        System.out.println("Finding instructor id: " + theId);
        Instructor tempInstructor = instructorRepository.findInstructorByIdJoinFetch(theId);

        System.out.println("tempInstructor: " + tempInstructor);
        System.out.println("the associated courses: " + tempInstructor.getCourses());

        System.out.println("Done!");
    }
}


