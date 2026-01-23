package com.gachigage.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gachigage.global.WithMockCustomUser;
import com.gachigage.global.config.JwtProvider;
import com.gachigage.global.config.SecurityConfig;
import com.gachigage.global.login.service.CustomOAuth2UserService;
import com.gachigage.member.Member;
import com.gachigage.member.RoleType;
import com.gachigage.product.domain.Product;
import com.gachigage.product.domain.TradeType;
import com.gachigage.product.dto.ProductRegistrationRequestDto;
import com.gachigage.product.service.ProductService;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private AuthenticationSuccessHandler oAuth2SuccessHandler;

    @MockitoBean
    private CustomOAuth2UserService oAuth2UserService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                       .id(1L)
                       .email("test@gmail.com")
                       .name("테스터")
                       .roleType(RoleType.USER)
                       .birthDate(LocalDate.now())
                       .build();
    }

    @Test
    @DisplayName("상품 등록 컨트롤러 테스트")
    @WithMockCustomUser(id = 1L)
    void registerProductSuccess() throws Exception {
        // given
        ProductRegistrationRequestDto.ProductCategoryRegistrationDto categoryDto =
                new ProductRegistrationRequestDto.ProductCategoryRegistrationDto(1L, 2L);
        List<ProductRegistrationRequestDto.ProductPriceRegistrationDto> priceTable = List.of(
                new ProductRegistrationRequestDto.ProductPriceRegistrationDto(1, 10000),
                new ProductRegistrationRequestDto.ProductPriceRegistrationDto(5, 45000)
        );
        ProductRegistrationRequestDto.TradeLocationRegistrationDto tradeLocation =
                new ProductRegistrationRequestDto.TradeLocationRegistrationDto(37.497952, 127.027619, "서울 강남구 강남역");

        ProductRegistrationRequestDto requestDto = new ProductRegistrationRequestDto(
                categoryDto,
                "테스트 의자",
                "테스트 상세 설명",
                10L,
                priceTable,
                TradeType.DELIVERY,
                tradeLocation,
                List.of("image_url1.jpg", "image_url2.jpg")
        );

        Product returnedProduct = Product.builder()
                                         .id(1L)
                                         .build();

        given(productService.createProduct(
                anyLong(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
        )).willReturn(returnedProduct);


        // when & then
        mockMvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
               .andDo(print()) // Add this line to print the MvcResult
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.productId").value(1L))
               .andExpect(jsonPath("$.message").value("상품이 성공적으로 등록되었습니다."));
    }
}
