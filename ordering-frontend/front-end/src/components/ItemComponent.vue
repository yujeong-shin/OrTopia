<template>
  <v-main>
    <v-container>
      <v-row>
        <!-- 상품 이미지 -->
        <v-col cols="12" sm="6">
          <v-img :src="item.imagePath" alt="상품 이미지" max-height="400" />
        </v-col>
        <!-- 상품 정보 -->
        <v-col cols="12" sm="6">
          <div>
            <h1 style="font-size: 40px; margin-top: 30px;">{{ item.name }}</h1>
            <p style="font-size: 20px; margin-top: 30px;">가격: {{ item.price }}</p>
            <p style="font-size: 20px; margin-top: 30px;">카테고리 {{ item.category }}</p>
            <v-btn text style="margin-top: 30px;">판매자 이름 : {{ item.sellerId }}</v-btn>
            <v-btn color="primary" text style="margin-top: 30px;">즐겨찾기</v-btn>
            <p style="font-size: 20px; margin-top: 30px;">점수</p>
          </div>
        </v-col>
      </v-row>
      <v-card>
        <v-card-title>구매 옵션 및 수량 선택</v-card-title>
        <v-card-text>
          <v-select v-model="selectedOption" :items="options" label="옵션" />
          <v-row align="center">
            <v-col cols="4">
              <p>수량:</p>
            </v-col>
            <v-col>
              <v-text-field v-model.number="quantity" type="number" min="1" max="10" />
            </v-col>
          </v-row>
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" @click="buyNow">바로 구매</v-btn>
          <v-btn color="secondary" @click="addToCart">장바구니 담기</v-btn>
        </v-card-actions>
      </v-card>
    </v-container>
    <v-container>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <h1 style="text-align: center">상품 상세 설명</h1>
      
    </v-container>
    <v-container>
      <h1 style="text-align: center">리뷰</h1>
    </v-container>
  </v-main>
  
  <!-- 스티키 사이드바 -->
  <div class="sticky-sidebar" :style="{ top: stickyTop + 'px' }">
    <h5 style="text-align: center">최근본상품</h5>
    <v-card
      v-for="product in recentProducts"
      :key="product.id"
      class="mb-2"
      outlined
    >
      <v-img :src="product.imagePath" height="100px"></v-img> 
    </v-card>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      itemId: null,
      item : [],
      selectedOption: null,
      options: ['옵션 1', '옵션 2', '옵션 3'],
      quantity: 1,
      recentProducts: [],
      stickyTop: 0,
    };
  },
  created(){
    this.itemId = this.$route.params.id;
    this.getItemInfo();
  },
  methods: {
    buyNow() {
      // 바로 구매 동작 구현
    },
    addToCart() {
      // 장바구니 담기 동작 구현
    },
    handleScroll() {
      const offsetTop = window.pageYOffset || document.documentElement.scrollTop;
      this.stickyTop = offsetTop > 100 ? 100 : offsetTop;
    },
    async getItemInfo(){
      const token = localStorage.getItem('accessToken');
      const refreshToken = localStorage.getItem('refreshToken')
      const email = localStorage.getItem('email');
        try{
          const data = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/item-service/item/read/${this.itemId}`,{
            headers: {
              myEmail : `${email}`
            }
          });
          this.item = data.data.result; 
          console.log(this.item);
        }catch(error){
          alert(error.response.data.error_message);
          console.log(error);
        }
        // 최근 본 상품 불러오기
        
        if(token != null){
          try{
            const data = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/item-service/item/recent_items`,{
            headers: {
              Authorization: `Bearer ${token}`,
              "X-Refresh-Token" : `${refreshToken}`
            }
          });
          console.log(data)
          this.recentProducts = data.data.result;
          console.log(this.recentProducts);
          }catch(error){
            console.log(error);
          }
        }
      }
    
  },
  mounted() {
    window.addEventListener('scroll', this.handleScroll);
  },
  beforeUnmount() { // beforeDestroy가 아닌 beforeUnmount를 사용합니다.
    window.removeEventListener('scroll', this.handleScroll);
  },
};
</script>

<style scoped>
.sticky-sidebar {
  position: fixed;
  right: 20px;
  width: 120px; 
  max-height: calc(50vh - 50px); 
  overflow-y: hi;
}
</style>