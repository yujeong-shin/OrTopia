<template>
  <v-app-bar app color="white" dark elevation="0" class="sticky-header">
    <v-img
      src="@/assets/logo.png"
      contain
      height="30"
      class="logo-margin top-margin"
      @click="redirectToHome"
    ></v-img>
    <v-btn
      text-color="white"
      class="top-margin"
      :style="{ 'margin-left': '50px' }"
    >
      커뮤니티
    </v-btn>
    <!-- 판매자일 경우 판매 텍스트 표시 -->
    <v-btn
      text-color="white"
      class="top-margin"
      @click="goToItemSellComponent"
      v-if="isSeller"
    >
      판매
    </v-btn>
    <v-btn
      text-color="white"
      class="top-margin"
      @click="redirectToHome"
      v-if="!isSeller"
    >
      구매
    </v-btn>

    <v-row>
      <v-col cols="12" class="search-margin">
        <v-text-field
          v-model="search"
          append-icon="mdi-magnify"
          label="상품을 검색해보세요"
          single-line
          color="black"
          bg-color="transparent"
          label-color="black"
          :style="{
            'border-bottom': '1px solid #c6c6c6',
            'border-bottom-width': '1px',
            'border-bottom-style': 'solid',
            'border-bottom-color': '#c6c6c6',
          }"
          :append-icon-style="{
            'border-bottom': '1px solid #c6c6c6',
            'border-bottom-width': '1px',
            'border-bottom-style': 'solid',
            'border-bottom-color': '#c6c6c6',
          }"
          @keyup.enter="searchAndNavigate"
          @click:append="searchAndNavigate"
        ></v-text-field>
      </v-col>
    </v-row>

    <div class="d-flex align-center justify-end">
      <!-- 로그인 상태에 따라 다른 내용 표시 -->
      <template v-if="isLoggedIn">
        <!-- 로그인했을 때 보이는 텍스트들 -->
        <v-btn icon class="alarm-margin top-margin">
          <v-icon color="black" @click="goToNotifications">mdi-bell</v-icon>
        </v-btn>

        <v-btn icon class="top-margin">
          <v-badge color="error" :content="totalQuantity">
            <v-icon color="black" @click="goToMyCart">mdi-cart</v-icon>
          </v-badge>
        </v-btn>

        <v-btn
          text-color="white"
          class="top-margin"
          @click="goToMypageComponent"
        >
          마이페이지
        </v-btn>

        <v-btn
          text-color="white"
          class="right-margin top-margin end-margin"
          @click="logout"
        >
          로그아웃
        </v-btn>
      </template>

      <template v-else>
        <!-- 로그인하지 않았을 때 보이는 텍스트 버튼들 -->
        <v-btn
          text-color="white"
          class="top-margin end-margin"
          @click="goToLogin"
        >
          로그인
        </v-btn>
        <v-btn
          text-color="white"
          class="right-margin top-margin end-margin"
          @click="goToRegister"
        >
          회원가입
        </v-btn>
      </template>
    </div>
  </v-app-bar>
  <br />
  <div class="header-bottom-line"></div>

  <v-btn-toggle v-model="selectedCategory" mandatory class="category-buttons">
    <div class="d-flex">
    <v-btn @click="handleButtonClick('가구')" class="mx-2">가구</v-btn>
    <v-btn @click="handleButtonClick('도서')" class="mx-2">도서</v-btn>
    <v-btn @click="handleButtonClick('가전')" class="mx-2">가전</v-btn>
    <v-btn @click="handleButtonClick('생활')" class="mx-2">생활</v-btn>
    <v-btn @click="handleButtonClick('건강')" class="mx-2">건강</v-btn>
    <v-btn @click="handleButtonClick('스포츠')" class="mx-2">스포츠</v-btn>
    <v-btn @click="handleButtonClick('식품')" class="mx-2">식품</v-btn>
    <v-btn @click="handleButtonClick('육아')" class="mx-2">육아</v-btn>
    <v-btn @click="handleButtonClick('의류')" class="mx-2">의류</v-btn>
    <v-btn @click="handleButtonClick('잡화')" class="mx-2">잡화</v-btn>
    <v-btn @click="handleButtonClick('화장품')" class="mx-2">화장품</v-btn>
  </div>
  </v-btn-toggle>
  <div class="header-bottom-line"></div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

const router = useRouter();
const store = useStore();
const totalQuantity = computed(() => store.getters.getTotalQuantity);
const isLoggedIn = ref(false);
const isSeller = ref(false); // 판매자인지 확인하는 ref 추가
const search = ref(""); // ref로 search 변수를 생성합니다.
const searchAndNavigate = () => {
  if (search.value.trim() !== "") {
    // 검색어가 비어있지 않은 경우에만 실행
    // 여기서 검색한 내용을 가지고 URL을 만들어서 router.push()를 호출하여 이동합니다.
    const url = `/search/${search.value}`;
    window.location.href = url;
  }
};
onMounted(() => {
  const token = localStorage.getItem("accessToken");
  const role = localStorage.getItem("role"); // localStorage에서 사용자 역할 가져오기
  isLoggedIn.value = !!token;
  isSeller.value = role === "SELLER"; // 사용자 역할이 판매자인 경우 true 설정
  
});
const handleButtonClick = (category) => {
  const url = `/search/${category}`;
    window.location.href = url;
};
const logout = () => {
  localStorage.clear();
  isLoggedIn.value = false;
  isSeller.value = false; // 로그아웃 시 isSeller도 초기화
  router.push("/login");
};

const redirectToHome = () => {
  router.push("/");
};

const goToMypageComponent = () => {
  router.push("/mypage");
};

const goToMyCart = () => {
  router.push("/mycart");
};

const goToNotifications = () => {
  router.push("/notifications");
};

const goToLogin = () => {
  router.push("/login");
};

const goToRegister = () => {
  router.push("/signup");
};

const goToItemSellComponent = () => {
  router.push("/seller");
};
</script>

<style scoped>
.logo-margin {
  margin-left: 20px;
}
.right-margin {
  margin-right: 180px;
}
.search-margin {
  margin-left: 50px;
  margin-right: -100px;
  margin-top: 30px;
}
.alarm-margin {
  margin-left: 70px;
}
.v-text-field {
  width: 600px !important;
}
.header-bottom-line {
  border-bottom: 1px solid #e0e0e0;
}
.category-buttons {
  display: flex;
  justify-content: center;
  margin-top: 5px;
}
.top-margin {
  margin-top: 30px;
}
.end-margin {
  margin-right: 30px;
}
.custom-outlined {
  border: 1px solid #c6c6c6;
  border-radius: 4px;
}
.sticky-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
}
</style>
