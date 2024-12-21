package dev.marvin.category;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class CategoryRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();

        Category furniture = Category.builder()
                .name("Furniture")
                .build();
        categoryRepository.save(furniture);

        Category electronics = Category.builder()
                .name("Electronics")
                .build();
        categoryRepository.save(electronics);
    }

    @DisplayName("Test to fetch paginated categories")
    @Test
    void testGetCategories() {
        //when
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        Page<Category> actual = categoryRepository.getCategories(pageable);

        log.info("actual: {}", actual.getContent());

        //then
        assertEquals(2, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
        assertEquals("Electronics", actual.getContent().get(0).getName());
    }
}