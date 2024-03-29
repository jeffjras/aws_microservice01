package br.com.challenge.aws_microservice01.controller;

import br.com.challenge.aws_microservice01.enums.EventType;
import br.com.challenge.aws_microservice01.mock.CreateProductRequest;
import br.com.challenge.aws_microservice01.model.Product;
import br.com.challenge.aws_microservice01.service.ProductPublisher;
import br.com.challenge.aws_microservice01.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @InjectMocks
    ProductController productController;

    @Mock
    private ProductService service;

    @Mock
    ProductPublisher productPublisher;

    private MockMvc mockMvc;

    private Product product;

    private MockMultipartFile mockMultipartFile;

    private final Product mockProduct = mock(Product.class);

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        /*this.mockMvc = MockMvcBuilders.webAppContextSetup
                (this.context).build();*/
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .alwaysDo(print())
                .build();

        product = new Product();
        product.setId(1001);
        product.setName("test");
        product.setCode("123");
        product.setApproved("color");
        product.setType("model");
        product.setPrice(10.0F);

        mockMultipartFile = new MockMultipartFile(
                "file",
                "testeProduct.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Test, Product!".getBytes()
        );

        objectMapper = new ObjectMapper();

        mockMultipartFile = new MockMultipartFile("Teste1", "Teste1.pdf", MediaType.TEXT_PLAIN_VALUE,"Venda Processada.".getBytes(StandardCharsets.UTF_8));
    }

    public void deve_Aceitar_Requisicao_Chamar_Service_Upload_ArquivoComSucesso() throws Exception {
        when(service.uploadDocument(mockMultipartFile)).thenReturn(ResponseEntity.ok("Documento Carregado Com Sucesso!"));

        mockMvc.perform(multipart("/teste-open-api")
                        .file(mockMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(service).uploadDocument(mockMultipartFile);
        verifyNoMoreInteractions(service);

    }

    @Test
    public void deve_Buscar_Dados_Product_PorNome_CodeComSucesso() throws Exception {
        when(service.buscaPrdoductPor(product.getName(), product.getCode())).thenReturn(Collections.singletonList(product));

        mockMvc.perform(get("/teste-open-api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("Name", product.getName())
                        .param("Code", product.getCode()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(service).buscaPrdoductPor(product.getName(), product.getCode());
        verifyNoMoreInteractions(service);

    }

    @Test
    public void deve_Retonrar_Erroxx_NaoSejaPassado_Parametro_Obrigatorios() throws Exception {

        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("Code", product.getCode()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        verifyNoInteractions(service);

    }

    @Test
    public void deve_Processar_requisicao_ChamarServiceComSucesso() throws Exception {
        // when anything happen / have been any happen / whitch we to want that happen
        when(service.saveProduct(product)).thenReturn(new ResponseEntity<Product>(mockProduct, HttpStatus.OK).getBody());
        //method void
        //doNothing().when(repository).delete(prodcutTest);
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        assertNotNull(service);
        verify(service.findByIdProduct(mockProduct.getId()));
        verify(service.saveProduct(mockProduct));
        verifyNoMoreInteractions(service);
    }

    @Test
    public void it_should_return_created_product() throws Exception{
        CreateProductRequest request = new CreateProductRequest();
        request.setId(1L);
        Product productMock = new Product();
        productMock.setId(request.getId());
        when(service.saveProduct(product)).thenReturn(mockProduct);
        mockMvc.perform(post("/product")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
                //.andExpect(jsonPath("$.name").value(request.getName()));
    }

    public void testHome() throws Exception {
        String URL1="/api";
        System.out.println(this.mockMvc.perform(get(URL1))
                .andDo(print()));
        this.mockMvc.perform(get(URL1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(containsString("products")));
    }

    @Test
    void whenfindAll_thenCorrect() {
        Iterable<Product> products =
                this.service.findAllProducts();
        assertNotNull(products);
    }

    @Test
    void findById() {
        Optional<Product> product = this.service.findByIdProduct(mockProduct.getId());
        assertNotNull(product);
    }

    void saveProduct() throws Exception {
        // when anything happen / have been any happen / whitch we to want that happen
        //Optional<Product> optProduct = repository.findById(productMock.getId());
        when(service.saveProduct(product)).thenReturn(new ResponseEntity<Product>(mockProduct, HttpStatus.OK).getBody());
        //method void
        //doNothing().when(repository).delete(prodcutTest);
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(service.findByIdProduct(mockProduct.getId()));
        verify(service.saveProduct(mockProduct));
        verifyNoMoreInteractions(service);
    }


    void updateProduct() {
        Optional<Product> product = this.service.findByIdProduct(mockProduct.getId());
        when(service.saveProduct(mockProduct)).thenReturn(product.get());
        //when(service.saveProduct(any())).thenReturn(productPublisher);
        productPublisher.publishProductEvent(mockProduct, EventType.PRODUCT_UPDATE, "jefferson");
        assertNotNull(product);
        assertNotNull(productPublisher);
    }

    void deleteProduct() throws Exception {
        when(service.saveProduct(product)).thenReturn(new ResponseEntity<Product>(mockProduct, HttpStatus.OK).getBody());
        //method void
        doNothing().when(service).delete(product);
        mockMvc.perform(delete("/api/products/{id}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        assertNotNull(mockMvc.perform(MockMvcRequestBuilders.delete("/delete", 10001L))
                .andExpect(status().isOk()));
    }

    @Test
    void findByCode() {
        assertNotNull(when(service.findByCode(mockProduct.getCode())).thenReturn(null));
    }
}