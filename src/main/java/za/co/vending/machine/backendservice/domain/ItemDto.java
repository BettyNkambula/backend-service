package za.co.vending.machine.backendservice.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private int id;
    private String product;
    private double price;
}
