package dev.marvin.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    CategoryServiceImpl underTest;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    CategoryUtils categoryUtils;

    @BeforeEach
    void setUp() {
        underTest = new CategoryServiceImpl(categoryRepository, categoryUtils);
        categoryRepository.deleteAll();

        Category furniture = Category.builder()
                .name("Electronics")
                .build();
        categoryRepository.save(furniture);
    }

    @DisplayName("Test happy path - adding a new category")
    @Test
    void add() {
        //given
        CategoryRequest request = new CategoryRequest("Furniture");
        //when
        underTest.add(request);

        //then
    }

    @Test
    void getAll() {
    }

    @Test
    void getAllPaginated() {
    }

    @Test
    void getOne() {
    }

    @Test
    void update() {
    }

    @Test
    void toggleStatus() {
    }
}