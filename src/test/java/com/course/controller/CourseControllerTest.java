package com.course.controller;

import com.course.model.CourseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;


// https://asbnotebook.com/spring-boot-database-integration-test-with-h2/
// https://www.vincenzoracca.com/blog/framework/spring/integration-test/
// https://levelup.gitconnected.com/unit-test-with-junit5-and-mockito-6935431f5d7c
// https://medium.com/@reachansari/rest-endpoint-testing-with-mockmvc-7b3da1f83fbb

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application.yml")
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = CourseApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CourseControllerTest {

//    private final ObjectMapper objectMapper = new ObjectMapper();

//    @Autowired
//    private CourseController courseController;

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";
    private static RestTemplate restTemplate;

//    @Autowired
//    private GetCoursesBaseCommand getCoursesBaseCommand;
//    //    @Mock
//    @Autowired
//    private CourseService courseService;
//    private MockMvc mockMvc;


    @BeforeAll
    static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    void setUp() {
        this.baseUrl = baseUrl.concat(":" + port).concat("/courses");
    }


//
//    @AfterEach
//    void tearDown() throws Exception {
//        MockitoAnnotations.openMocks(this).close();
//    }


    @Test
    @SqlGroup({@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/data.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:sql/delete-data.sql")})
    void getCourse() throws Exception {
//        when(courseService.findCourseById(any())).thenReturn(createCourseDto());

//        this.mockMvc.perform(get("courses/1"))
//                .andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(1)))
//                .andExpect(jsonPath("$[0].name", Matchers.equalTo("Javascript")));
        final CourseDto dto = restTemplate.getForObject(baseUrl.concat("/{id}"), CourseDto.class, 1);
        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals("Javascript", dto.getName())
        );

    }

    @Test
    void getPagingCourses() {
    }

    @Test
    void putRatingCourse() {
    }

    @Test
    void putRatingEventCourse() {
    }

    private CourseDto createCourseDto() {
        final CourseDto courseDto = new CourseDto();
        courseDto.setCourseId(1L);
        courseDto.setName("Javascript");
        courseDto.setCourseDescription("description");

        return courseDto;

    }
}