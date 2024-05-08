<template>
  <v-main>
    <v-container>
      <v-row align="center">
        <!-- 상품 이미지 -->
        <v-col cols="12" sm="6" class="d-flex justify-center">
          <v-img :src="item.imagePath" alt="상품 이미지" max-height="400" />
        </v-col>
        <!-- 상품 정보 -->
        <v-col cols="12" sm="6">
          <div>
            <h1 style="font-size: 40px; margin-top: 30px">{{ item.name }}</h1>
              <p style="font-size: 20px; margin-top: 30px">
                가격: {{ item.price }}
              </p>
              <p style="font-size: 20px; margin-top: 30px">
                카테고리: {{ item.category }}
              </p>
            <v-btn text style="margin-top: 30px"
              >판매자 이름 : {{ seller.companyName }}</v-btn
            >
            &nbsp;&nbsp;&nbsp;
            <v-btn icon @click="toggleLove()">
              <v-icon>
                {{ item.isLove ? 'mdi-heart' : 'mdi-heart-outline' }}
              </v-icon>
            </v-btn>
            &nbsp;&nbsp;&nbsp;
            <v-btn icon @click="toggleFavorite(item.sellerId)">
            <v-icon>
              {{ item.isFavorited ? 'mdi-bookmark' : 'mdi-bookmark-outline' }}
            </v-icon>
          </v-btn>
            <br>
          <div style="display: flex; align-items: center; margin-top: 10px;">
          <p style="font-size: 20px;">평점 :</p>
          <v-rating
            v-model="totalScore" 
            :half-increments="true"
            :color="'red'"
            :background-color="'grey darken-3'"
            :size="'large'"
            readonly
          ></v-rating> ({{this.reviews.length}})
        </div>
          </div>
          <br>
          <v-card>
          <v-card-title>구매 옵션 및 수량 선택</v-card-title>
          <v-card-text>
            <v-select v-for="(option, index) in selectedOptions" :key="index" :items="option.value" :label="option.name" v-model="selectedValues[index].value">
          </v-select>
          <v-row align="center">
            <v-col cols="4">
              <p>수량:</p>
            </v-col>
            <v-col>
              <v-text-field
                v-model.number="quantity"
                type="number"
                min="1"
                max="10"
              />
             </v-col>
             </v-row>
            </v-card-text>
            <v-card-actions>
              <v-btn color="primary" @click="buyNow">바로 구매</v-btn>
              <v-btn color="secondary" @click="addToCart">장바구니 담기</v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
    <v-container class="text-center">
      <v-row>
      <!-- 첫 번째 열 -->
      <v-col cols="12">
      <v-btn large color="pink" @click="showFirstComeModal = true" class="aggressive-button">선착순 쿠폰받으러 가기</v-btn>
    </v-col>
      <v-col cols="12">
        <p class="grid-text">나와 취향이 비슷한 회원들이 구매한 상품</p>
      </v-col>
      </v-row>
      <v-row>
      <v-col v-for="(product, index) in recommendProducts" :key="index" cols="4">
        <img 
        :src="product.imagePath" 
        :alt="product.description" 
        style="max-width: 80%; max-height: 80%;" 
        @click="handleImageClick(product.itemId)"
        class="clickable-image"
        >
      </v-col>
    </v-row>
    </v-container>
    <v-container>
      <h1 style="text-align: center">상품 상세 설명</h1>
      <div v-html="item.detail" style="margin-top: 20px; text-align: center;"></div>
    </v-container>
    <v-container>
  <h1 style="text-align: center">리뷰</h1>
  <v-row>
    <v-col cols="12" v-for="(review, index) in reviews" :key="index">
      <!-- 리뷰 정보 -->
      <v-card>
        <v-card-title>
          <v-row align="center">
            <v-col cols="auto">
              <p>회원 이름: {{ review.name }}</p>
            </v-col>
            <v-col cols="100px">
              <!-- 점수(별표) 표시 -->
              평점 :
              <v-icon
                v-for="(icon, i) in 5"
                :key="i"
                :color="i < review.score ? 'yellow' : 'grey'"
              >
                mdi-star
              </v-icon>
            </v-col>
            <v-col cols="auto">
              <p>리뷰 날짜: {{ review.date }}</p>
            </v-col>
          </v-row>
        </v-card-title>
        <v-card-text>
        <v-row>
          <v-col cols="4" align="center">
            <!-- 리뷰 사진 -->
            <img :src="review.imagePath" alt="리뷰 사진"  style="max-width: 400px; max-height: 200px;">
          </v-col>
          
          <v-card-text >
            <v-head>내용</v-head>
          <v-col cols="auto" class="gray-background" style="height: 140px; border-radius: 10px;" >
            <!-- 리뷰 내용 -->
          <div class="gray-background" style="padding: 20px;">
            <!-- 리뷰 내용 -->
            <p>{{ review.content }}</p>
          </div>
          </v-col>
          </v-card-text>
        </v-row>
      </v-card-text>
      </v-card>
    </v-col>
  </v-row>
</v-container>
  </v-main>
  <!-- 스티키 사이드바 -->
  <div class="sticky-sidebar" :style="{ top: stickyTop + 'px', 'z-index': 9999 }">
    <h5 style="text-align: center">최근본상품</h5>
    <v-card
      v-for="product in recentProducts"
      :key="product.id"
      class="mb-2"
      outlined
      @click="goToDetailPage(product.id)"
    >
      <v-img :src="product.imagePath" height="100px"></v-img>
    </v-card>
  </div>
  <first-come-modal v-model="showFirstComeModal" :item-id="itemId"></first-come-modal>
</template>
<script>
import axios from "axios";
import { mapActions } from "vuex";
import FirstComeModal from './FirstComeModal.vue';

export default {
  components: {
    FirstComeModal
  },
  data() {
    return {
      showFirstComeModal: false,
      itemId: null,
      item: [],
      selectedOptions:[],
      seller: {},
      options: [],
      quantity: 0,
      recentProducts: [],
      stickyTop: 0,
      selectedValues:[],
      reviews:[],
      totalScore:0,
      recommendProducts: [],
    };
  },
  created() {
    this.itemId = this.$route.params.id;
    this.getItemInfo(); 
    console.log("Item ID:", this.itemId);
    this.getRecommend();
    this.getReview();
},
  methods: {
    ...mapActions("addToCart"),
    async checkFavoriteStatus(sellerId) {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");
      const url = `${process.env.VUE_APP_API_BASE_URL}/ortopia-member-service/member/checkLiked/${sellerId}`;

      try {
        const response = await axios.get(url, {
          headers: {
            myEmail: email,
            Authorization: `Bearer ${token}`,
            "X-Refresh-Token": `${refreshToken}`,
          }
        });
        this.item.isFavorited = response.data.result;
      } catch (error) {
        console.error("즐겨찾기 상태 확인 실패:", error);
      }
    },
    // 아이템 좋아요 표시
    async toggleLove(){
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      if(token == null){
        alert("로그인 후 사용해 주세요.")
        this.$router.push(`/login`);
      }
      try{
        const response = await axios.post(
          `${process.env.VUE_APP_API_BASE_URL}/ortopia-item-service/item/love/${this.itemId}`,null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "X-Refresh-Token": `${refreshToken}`,
            },
          }
        );
        console.log(response);
        if(response.data.result === "save success"){
          this.item.isLove = !this.item.isLove;
          alert("아이템 좋아요!");
        }else{
          this.item.isLove = !this.item.isLove;
          alert("아이템 좋아요! 취소");
        }
      }catch(e){
        console.log(e);
      }
    }
    ,
    // 아이템 즐겨 찾기
    async toggleFavorite(sellerId) {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const headers = token ? { Authorization: `Bearer ${token}`, 'X-Refresh-Token': `${refreshToken}` } : {};
      const baseApiUrl = `${process.env.VUE_APP_API_BASE_URL}/ortopia-member-service/member`;
      const action = this.item.isFavorited ? 'unlikeSeller' : 'likeSeller';
      const method = this.item.isFavorited ? 'delete' : 'post';
      const url = `${baseApiUrl}/${action}/${sellerId}`;

    try {
      await axios({
        method: method,
        url: url,
        headers: headers,
        data: null
      });
      this.item.isFavorited = !this.item.isFavorited;
      alert(`즐겨찾기가 ${this.item.isFavorited ? '추가' : '삭제'}되었습니다!`);
    } catch (error) {
      console.error('즐겨찾기 변경 실패:', error);
      alert('즐겨찾기 변경에 실패하였습니다.');
    }
  },
  // 추천 아이템 클릭 시 해당 상품으로 이동
  handleImageClick(itemId) {
    // 여기서 다른 함수를 호출하거나 필요한 작업을 수행할 수 있습니다.
    window.location.href = `/item/${itemId}`;
  },
    buyNow() {
      // 바로 구매 동작 구현
      const token = localStorage.getItem("accessToken");
      if(token == null){
        alert("로그인 후 사용해 주세요.")
        this.$router.push(`/login`);
        return;
      }
      if (this.quantity == 0) {
        alert("0개는 주문 할 수 없습니다.");
      } else {
        console.log(this.selectedValues);
        let orderItem = this.item;
        orderItem.count = this.quantity;
        orderItem.options = this.selectedValues;
        if (!Array.isArray(orderItem)) {
            orderItem = [orderItem];
        }
        console.log(orderItem);
        localStorage.setItem('buyItem', JSON.stringify(orderItem));
        this.$router.push("/buypage");
      }
    },
    addToCart() {
      if (this.quantity == 0) {
        alert("0개는 주문 할 수 없습니다.");
      } else {
        if (confirm("장바구니에 담습니까?")) {
          this.item.count = this.quantity;
          this.item.options = this.selectedValues;
          this.$store.dispatch("addToCart", this.item);
          if (confirm("장바구니로 이동하시겠습니까?")) {
            this.$router.push("/mycart");
          }
        }
      }
    },
    handleScroll() {
      const offsetTop =
        window.pageYOffset || document.documentElement.scrollTop;
      this.stickyTop = offsetTop > 100 ? 100 : offsetTop;
    },
    // 추천 아이템 받아오는 method
    async getRecommend(){
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      try{
        const data  = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/ortopia-item-service/item/recommendItems`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
                "X-Refresh-Token": `${refreshToken}`,
              },
            }
        );
        console.log(data.data.result);
        this.recommendProducts = data.data.result;
      }catch(e){
        console.log(e);
      }
    },
    async getReview(){
      try{
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/ortopia-item-service/review/show_item/${this.itemId}`);
        console.log(response);
        this.reviews = response.data.result;
        if(this.reviews.length != 0){
          this.totalScore = this.reviews.reduce((total, review) => total + review.score, 0);
          this.totalScore /= this.reviews.length;
        }
        console.log(this.totalScore);
      }catch(e){
        console.log(e);
      }
    },
    async getItemInfo() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      let email = localStorage.getItem("email");
      if(email == null){
        email = "noLogin";
      }
  try {
    const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/ortopia-item-service/item/read/${this.itemId}`, {
      headers: {
        myEmail: email,
      },
    });
    this.item = response.data.result;
    if (this.item && this.item.sellerId) {
      this.getSellerInfo(this.item.sellerId);
      this.checkFavoriteStatus(this.item.sellerId);
    } else {
      console.log("응답 데이터에 sellerId 또는 아이템이 누락되었습니다:", response.data);
    }
    this.selectedOptions = [...this.item.itemOptionResponseDtoList];
    this.selectedValues = this.selectedOptions.map(option => ({ name: option.name, value: null }));
    // console.log(this.options);
  } catch (error) {
    alert(error.response.data.error_message);
    console.error(error);
  }

      // 최근 본 상품 불러오기
      if (token != null) {
        try {
          const data = await axios.get(
            `${process.env.VUE_APP_API_BASE_URL}/ortopia-item-service/item/recent_items`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
                "X-Refresh-Token": `${refreshToken}`,
              },
            }
          );
          console.log(data);
          this.recentProducts = data.data.result;
          console.log(this.recentProducts);
        } catch (error) {
          console.log(error);
        }
      }
    },
  async getSellerInfo(sellerId) {
  const token = localStorage.getItem("accessToken");
  const refreshToken = localStorage.getItem("refreshToken");
  try {
    const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/ortopia-member-service/seller/noLogin/${sellerId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        "X-Refresh-Token": `${refreshToken}`,
      }
    });
    console.log(response);
    this.seller = response.data.result;
  } catch (error) {
    console.error("판매자 정보 가져오기 실패:", error);
  }
},
    // 최근 본 상품 사진을 클릭시 리다이렉트
    goToDetailPage(Id) {
      window.location.href = `/item/${Id}`;
    },
  },
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
  },
  beforeUnmount() {
    // beforeDestroy가 아닌 beforeUnmount를 사용합니다.
    window.removeEventListener("scroll", this.handleScroll);
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
.v-icon {
  color: red; /* 하트 색상 */
  font-size: 24px; /* 아이콘 크기 */
}
</style>
<style scoped>
.sticky-sidebar {
  position: fixed;
  right: 20px;
  width: 120px;
  max-height: calc(50vh - 50px);
  overflow-y: hi;
  margin-top: 100px;
}
.v-icon {
  color: red; /* 하트 색상 */
  font-size: 24px; /* 아이콘 크기 */
}
.gray-background {
  background-color: rgb(239, 232, 219);
}
.clickable-image {
  transition: filter 0.3s;
}

.clickable-image:hover {
  cursor: pointer;
  filter: brightness(85%);
}

</style>