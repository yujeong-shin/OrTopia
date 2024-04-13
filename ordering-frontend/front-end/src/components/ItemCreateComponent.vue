<template>
    <v-container>
      <v-form ref="form" @submit.prevent="submitItem" enctype="multipart/form-data">
        <v-text-field v-model="item.name" label="아이템 이름" required></v-text-field>
        <v-text-field v-model="item.stock" label="재고" type="number" required></v-text-field>
        <v-text-field v-model="item.price" label="가격" type="number" required></v-text-field>
        <v-select v-model="item.category" :items="categories" label="카테고리" required></v-select>
        <v-text-field v-model="item.detail" label="상세 설명" required></v-text-field>
        <v-file-input v-model="item.imagePath" label="이미지 파일" accept="image/*"></v-file-input>
        <v-text-field v-model="item.minimumStock" label="최소 재고" type="number" required></v-text-field>
        <!-- <v-checkbox v-model="item.delYN" label="삭제 여부"></v-checkbox>
        <v-checkbox v-model="item.isBaned" label="금지 여부"></v-checkbox> -->
        <!-- <v-text-field v-model="item.sellerId" label="판매자 ID" type="number" required></v-text-field> -->
        
        <v-btn type="submit" color="success">등록하기</v-btn>
      </v-form>
    </v-container>
  </template>
  
  <script>
 import axios from 'axios';
  export default {
    data() {
      return {
        item: {
          name: '',
          stock: 0,
          price: 0,
          category: '',
          detail: '',
          imagePath: null,
          minimumStock: 0,
          delYN: false,
          isBaned: false,
         // sellerId: null
        },
        categories: ['FOOD', 'LAPTOP', 'MOBILE'] // 실제 카테고리 목록으로 교체해야 합니다.
      };
    },
    methods: {
        async submitItem() {
  try {
    const formData = new FormData();
    Object.keys(this.item).forEach(key => {
      if (key !== 'imagePath') {
        formData.append(key, this.item[key]);
      } else if (this.item.imagePath && this.item.imagePath.length > 0) {
        // 파일이 있으면 첫 번째 파일만 formData에 추가
        formData.append('imagePath', this.item.imagePath[0], this.item.imagePath[0].name);
      }
    });
    const email = localStorage.getItem('email');
    const response = await axios.post(
      `${process.env.VUE_APP_API_BASE_URL}/item-service/item/create`, 
      formData, 
      { 
        headers: { 
          'Content-Type': 'multipart/form-data',
          'Authorization': `Bearer ${localStorage.getItem('accessToken')}`, 
          'X-Refresh-Token': `${localStorage.getItem('refreshToken')}`,
          myEmail : `${email}`
        }
      }
    );

    console.log('Item registered successfully', response.data);
    alert('Item registered successfully');
    window.location.href =  "/";
    // 등록 후의 로직 (예: 페이지 리다이렉션)
    // this.$router.push('/path-to-navigate-after-success');
  } catch (error) {
    console.error('Error registering the item', error);
    alert('Error registering the item');
  }
},


    }
  }
  </script>
  