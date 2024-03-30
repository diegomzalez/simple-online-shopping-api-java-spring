package com.diegomzalez.productservice;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.diegomzalez.productservice.dto.ProductRequest;
import com.diegomzalez.productservice.dto.ProductResponse;
import com.diegomzalez.productservice.repository.ProductRepository;
import com.diegomzalez.productservice.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.assertions.Assertions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Slf4j
class ProductServiceApplicationTests {
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductService productService;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(status().isCreated());
	}

	@Test
	void shouldGetAllProducts() throws Exception {
		mockMvc
				.perform(MockMvcRequestBuilders
						.get("/api/v1/products", ""))
				.andExpect(status()
						.isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	void shouldGetProductById() throws Exception {
		List<ProductResponse> products = productService.getAllProducts();
		ProductResponse product = products.get(0);
		mockMvc
				.perform(MockMvcRequestBuilders
						.get("/api/v1/products/" + product.getId(), ""))
				.andExpect(status()
						.isOk())
				.andExpect(jsonPath("$.id").value(product.getId()));
	}

	@Test
	void shouldUpdateProductById() throws Exception {
		List<ProductResponse> products = productService.getAllProducts();
		ProductResponse product = products.get(0);
		String initialName = product.getName();
		String updatedName = initialName.concat(" Updated!");
		JSONObject body = new JSONObject();
		body.put("name", updatedName);
		String bodyString = objectMapper.writeValueAsString(body);
		log.info(bodyString);
		mockMvc
				.perform(MockMvcRequestBuilders
						.put("/api/v1/products/" + product.getId(), "").contentType(MediaType.APPLICATION_JSON)
						.content(bodyString))
				.andExpect(status().isOk());
	}

	@Test
	void shouldDeleteProductById() throws Exception {
		List<ProductResponse> products = productService.getAllProducts();
		String id = products.get(0).getId();
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/".concat(id), "null"))
				.andExpect(status().isNoContent());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder().name("RTX 3080").description("NVIDIA RTX 3080 16GB")
				.price(BigDecimal.valueOf(460))
				.build();
	}
}
