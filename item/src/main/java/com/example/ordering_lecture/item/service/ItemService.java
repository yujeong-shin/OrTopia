package com.example.ordering_lecture.item.service;

import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.item.dto.ItemRequestDto;
import com.example.ordering_lecture.item.dto.ItemResponseDto;
import com.example.ordering_lecture.item.dto.ItemUpdateDto;
import com.example.ordering_lecture.item.entity.Item;
import com.example.ordering_lecture.item.repository.ItemRepository;
import com.example.ordering_lecture.redis.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final RedisService redisService;
    public ItemService(ItemRepository itemRepository, RedisService redisService) {
        this.itemRepository = itemRepository;
        this.redisService = redisService;
    }

    public ItemResponseDto createItem(ItemRequestDto itemRequestDto)throws OrTopiaException {
        Item item = itemRequestDto.toEntity();
        itemRepository.save(item);
        return ItemResponseDto.toDto(item);
    }

    public List<ItemResponseDto> showAllItem(){
        return itemRepository.findAll().stream()
                .map(ItemResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemResponseDto updateItem(Long id, ItemUpdateDto itemUpdateDto) {
        Item item = itemRepository.findById(id).orElseThrow(()->new OrTopiaException(ErrorCode.NOT_FOUND_ITEM));
        item = itemUpdateDto.toUpdate(item);
        return ItemResponseDto.toDto(item);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
//        Item item = itemRepository.findById(id).orElseThrow();
//        item.deleteItem();
    }

    public List<ItemResponseDto> banItem(Long sellerId)throws OrTopiaException{
        List<Item> items = itemRepository.findAllBySellerId(sellerId);
        if(items.isEmpty()){
            throw new OrTopiaException(ErrorCode.EMPTY_ITEMS);
        }
        for(Item item : items){
            item.banItem();
        }
        return items.stream()
                .map(ItemResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public List<ItemResponseDto>  releaseBanItem(Long sellerId)throws OrTopiaException{
        List<Item> items = itemRepository.findAllBySellerId(sellerId);
        if(items.isEmpty()){
            throw new OrTopiaException(ErrorCode.EMPTY_ITEMS);
        }
        for(Item item : items){
            item.releaseBanItem();
        }
        return items.stream()
                .map(ItemResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public List<ItemResponseDto> findItemByEmail(Long sellerId)throws OrTopiaException {
        List<Item> items = itemRepository.findAllBySellerId(sellerId);
        if(items.isEmpty()){
            throw new OrTopiaException(ErrorCode.EMPTY_ITEMS);
        }
        return items.stream()
                .map(ItemResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public ItemResponseDto readItem(Long id, String email) {
        Item item = itemRepository.findById(id).orElseThrow(
                ()->new OrTopiaException(ErrorCode.NOT_FOUND_ITEM)
                );
        ItemResponseDto itemResponseDto = ItemResponseDto.toDto(item);
        redisService.setValues(email,itemResponseDto);
        return itemResponseDto;
    }

    public List<ItemResponseDto> readRecentItems(String email) {
        Set<String> set = redisService.getValues(email);
        List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for(String str: set){
            ItemResponseDto  itemResponseDto = null;
            try {
                itemResponseDto = objectMapper.readValue(str, ItemResponseDto.class);
            } catch (JsonProcessingException e) {
                throw new OrTopiaException(ErrorCode.JSON_PARSE_ERROR);
            }
            itemResponseDtos.add(itemResponseDto);
        }
        return itemResponseDtos;
    }
}
