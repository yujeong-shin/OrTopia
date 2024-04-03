<template>
  <v-main>
    <v-container>
      <v-row>
        <!-- 상품 이미지 -->
        <v-col cols="12" sm="6">
          <v-img src="https://thumbnail9.coupangcdn.com/thumbnails/remote/230x230ex/image/vendor_inventory/c817/00cc68d197d40ea6130488bd10bc2626eccb524e2b1abd0d744fe9479f2e.png" alt="상품 이미지" max-height="400" />
        </v-col>
        <!-- 상품 정보 -->
        <v-col cols="12" sm="6">
          <div>
            <h1 style="font-size: 40px; margin-top: 30px;">짱 멋진 운동화</h1>
            <p style="font-size: 20px; margin-top: 30px;">가격: 100원</p>
            <p style="font-size: 20px; margin-top: 30px;">카테고리</p>
            <v-btn text style="margin-top: 30px;">판매자 이름</v-btn>
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
      <v-img :src="product.img" height="100px"></v-img> 
    </v-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      selectedOption: null,
      options: ['옵션 1', '옵션 2', '옵션 3'],
      quantity: 1,
      recentProducts: [
        { id: 1, img: 'https://thumbnail8.coupangcdn.com/thumbnails/remote/230x230ex/image/retail/images/1341027894628655-7c08d5a4-d0d3-4560-9803-113e75b97fee.jpg' },
        { id: 2, img: 'https://thumbnail8.coupangcdn.com/thumbnails/remote/230x230ex/image/retail/images/29838384141585-d25eea29-955b-4845-bf3e-729c2ad57018.jpg' },
        { id: 3, img: 'https://thumbnail9.coupangcdn.com/thumbnails/remote/230x230ex/image/retail/images/1708806820802792-81731057-7274-4038-b4ab-887a3890ad74.jpg' },
        { id: 4, img: 'https://thumbnail9.coupangcdn.com/thumbnails/remote/230x230ex/image/retail/images/1708806820802792-81731057-7274-4038-b4ab-887a3890ad74.jpg' },
      ],
      stickyTop: 0,
    };
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