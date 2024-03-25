package br.com.challenge.aws_microservice01.controller;

import br.com.challenge.aws_microservice01.enums.EventType;
import br.com.challenge.aws_microservice01.model.Product;
import br.com.challenge.aws_microservice01.repository.ProductRepository;
import br.com.challenge.aws_microservice01.service.ProductEventProducer;
import br.com.challenge.aws_microservice01.service.ProductNotifier;
import br.com.challenge.aws_microservice01.service.ProductPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Nested
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    ProductEventProducer productEventProducer;

    @MockBean
    ProductNotifier productNotifier;

    @MockBean
    ProductPublisher productPublisher;

    @Autowired
    ProductRepository repository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Mock
    Product productMock;

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup
                (this.context).build();
        /*productMock = new Product();
        productMock.setId(1L);
        productMock.setName("test");
        productMock.setCode("123");
        productMock.setApproved("color");
        productMock.setType("model");
        productMock.setPrice(10.0F);*/
    }

    @Test
    public void it_should_return_created_product() throws Exception{
        /*CreateProductRequest request = new CreateProductRequest();
        request.setId(1L);
        Product productMock = new Product();
        productMock.setId(request.getId());
        when(repository.save(Product.class)).thenReturn(productMock);
        mvc.perform(post("/product")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(HttpStatus.OK)
                .andExpect(jsonPath("$.name").value(request.getName()));*/
    }

    @Test
    public void testHome() throws Exception {
        /*String URL1="/api";
        System.out.println(this.mvc.perform(get(URL1))
                .andDo(print()));
        this.mvc.perform(get(URL1))
                .andExpect(HttpStatus.OK)
                .andExpect(content().string(containsString("products")));*/
    }

    @Test
    void whenfindAll_thenCorrect() {
        Iterable<Product> products =
                this.repository.findAll();
        assertNotNull(products);
    }

    @Test
    void findById() {
        Optional<Product> product = this.repository.findById(productMock.getId());
        assertNotNull(product);
    }

        void saveProduct() {
        Product product = new Product();
        product.setId(1L);
        when(repository.save(productMock)).thenReturn(product);
        Product retorno = repository.save(product);
        assertNotNull(retorno);
    }


    void updateProduct() {
        Optional<Product> product = this.repository.findById(productMock.getId());
        when(repository.save(productMock)).thenReturn(product.get());
        when(repository.save(any())).thenReturn(productPublisher);
        productPublisher.publishProductEvent(productMock, EventType.PRODUCT_UPDATE, "jefferson");
        assertNotNull(product);
        assertNotNull(productPublisher);
    }

    void deleteProduct() throws Exception {
        assertNotNull(mvc.perform(MockMvcRequestBuilders.delete("/delete", 10001L))
                .andExpect(status().isOk()));
    }

    @Test
    void findByCode() {
        assertNotNull(when(repository.findByCode(productMock.getCode())).thenReturn(null));
    }
}