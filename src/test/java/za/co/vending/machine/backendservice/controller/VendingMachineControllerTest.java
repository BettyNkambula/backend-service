package za.co.vending.machine.backendservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import za.co.vending.machine.backendservice.domain.ItemDto;

import java.util.Arrays;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VendingMachineControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testVendingItems_success() throws Exception {
        final MvcResult mvcResult = this.mockMvc.perform(
                get("/vending/items")
                        .content(objectMapper.writeValueAsString(Arrays.asList(new ItemDto())))
        ).andDo(
                print()
        ).andExpect(
                status().isOk()
        ).andReturn();
    }

    @Test
    void testVendingItems_Bad_Request() throws Exception {
        final MvcResult mvcResult = this.mockMvc.perform(
                get("/vending/item")
                        .content(objectMapper.writeValueAsString(""))
        ).andDo(
                print()
        ).andExpect(
                status().isNotFound()
        ).andReturn();
    }

    @Test
    void testPurchase_Bad_Request() throws Exception {
        final MvcResult mvcResult = this.mockMvc.perform(
                post("/vending/purchase/2")
                        .content(objectMapper.writeValueAsString(""))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
                print()
        ).andExpect(
                status().isBadRequest()
        ).andReturn();
    }


}
