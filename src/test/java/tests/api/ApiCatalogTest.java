package tests.api;


import api.endpoints.ApiCatalog;
import api.models.Phone;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pro.learnup.ext.ApiTest;
import pro.learnup.testdata.DBTestHelper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("/api/catalog")
@ApiTest
public class ApiCatalogTest {
    List<Phone> phoneDtoList;

    @BeforeEach
    void setUp() {
        phoneDtoList = DBTestHelper.getAllPhones();
    }

    @Test
    @DisplayName("/api/catalog: 200, получение телефонов без авторизации")
    void getCatalogTest() {
        assertThat(new ApiCatalog().getAllPhones())
                .containsExactlyInAnyOrderElementsOf(phoneDtoList);
    }
}