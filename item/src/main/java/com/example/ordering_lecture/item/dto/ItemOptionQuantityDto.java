package com.example.ordering_lecture.item.dto;

import com.example.ordering_lecture.item.entity.Item;
import com.example.ordering_lecture.item.entity.ItemOptionQuantity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ItemOptionQuantityDto {
    private Long id;
    private String value1;
    private String value2;
    private String value3;
    private int quantity;

    public ItemOptionQuantityDto(){
        value1 = "NONE";
        value2 = "NONE";
        value3 = "NONE";
    }
    public void setValue(int index,String value){
        if(index==0){
            value1 = value;
        }else if(index ==1){
            value2 = value;
        }else{
            value3 = value;
        }
    }

    public ItemOptionQuantity toEntity(Item item) {
        return ItemOptionQuantity.builder()
                .quantity(item.getStock())
                .item(item)
                .value1(value1)
                .value2(value2)
                .value3(value3)
                .build();
    }
}
