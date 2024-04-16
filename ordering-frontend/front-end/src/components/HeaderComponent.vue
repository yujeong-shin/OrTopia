<template>
  <v-app-bar app color="orange" dark>
    <!-- 로고 이미지 -->
    <v-hover v-slot:default="{ hover }">
      <v-img
        :class="['my-3', { 'logo-hover': hover }]"
        src="@/assets/logo.svg"
        contain
        height="40"
        @click="redirectToHome"
        style="cursor: pointer;"
      ></v-img>
    </v-hover>

    <v-spacer></v-spacer>

    <!-- 로그인 상태에 따라 다른 내용 표시 -->
    <template v-if="isLoggedIn">
      <!-- 로그인했을 때 보이는 아이콘들 -->
      <v-btn icon>
        <v-icon color="white">mdi-bell</v-icon>
      </v-btn>

      <!-- 판매자인 경우에만 판매 아이콘 보이기 -->
      <v-btn icon v-if="isSeller">
        <v-icon color="white" @click="goToSellPage">mdi-cash-register</v-icon> <!-- 판매 아이콘 -->
      </v-btn>

      <!-- 판매자가 아닌 경우 판매자 등록 버튼 보이기 -->
      <v-btn text v-else @click="goToSellerRegistration">
        <v-icon left color="white" size="24">mdi-account-check</v-icon>
      </v-btn>

      <v-btn icon>
        <v-icon color="white" @click="goToMyCart">mdi-cart </v-icon>
        <span class="v-badge__badge v-theme--dark bg-error" aria-atomic="true" aria-label="Badge" aria-live="polite" role="status" style="bottom: calc(100% - 12px); left: calc(100% - 12px);"> {{totalQuantity}} </span>
      </v-btn>

      <v-btn icon @click="goToMypageComponent">
        <v-icon color="white">mdi-account-circle</v-icon>
      </v-btn>

      <v-btn icon @click="logout">
        <v-icon color="white">mdi-logout</v-icon> <!-- 로그아웃 아이콘 -->
      </v-btn>
    </template>
    
    <template v-else>
      <!-- 로그인하지 않았을 때 보이는 아이콘 버튼들 -->
      <v-btn icon @click="goToLogin">
        <v-icon color="white">mdi-login</v-icon> <!-- 로그인 아이콘 -->
      </v-btn>
      <v-btn icon @click="goToRegister">
        <v-icon color="white">mdi-account-plus</v-icon> <!-- 회원가입 아이콘 -->
      </v-btn>
    </template>
  </v-app-bar>
</template>


<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex'

const router = useRouter();
const store = useStore();
const totalQuantity = computed(() => store.getters.getTotalQuantity);
const isLoggedIn = ref(false);
const isSeller = ref(false); // 판매자인지 확인하는 ref 추가

onMounted(() => {
  const token = localStorage.getItem('accessToken');
  const role = localStorage.getItem('role'); // localStorage에서 사용자 역할 가져오기
  isLoggedIn.value = !!token;
  isSeller.value = role === 'SELLER'; // 사용자 역할이 판매자인 경우 true 설정
});

const logout = () => {
  localStorage.clear();
  isLoggedIn.value = false;
  isSeller.value = false; // 로그아웃 시 isSeller도 초기화
  router.push('/login');
};

const redirectToHome = () => {
  router.push('/');
};

const goToMypageComponent = () => {
  router.push('/mypage');
};

const goToMyCart = () => {
  router.push('/mycart');
};

const goToLogin = () => {
  router.push('/login');
};

const goToRegister = () => {
  router.push('/signup');
};

const goToSellPage = () => {
  router.push('/sell');
};

const goToSellerRegistration = async () => {
  try {
    await router.push('/sellerCreate'); // 판매자 등록 페이지로 리다이렉트
    // 판매자 등록 후 사용자 역할을 다시 확인하여 isSeller 값 업데이트
    const role = localStorage.getItem('role'); 
    isSeller.value = role === 'SELLER';
  } catch (error) {
    console.error('Error navigating to seller registration page:', error);
  }
};


</script>



<style scoped>
/* 로고 호버 효과가 적용되지 않는 문제를 해결하기 위해 :deep 선택자를 사용 */
:deep(.logo-hover) {
  box-shadow: 0 2px 12px rgba(0,0,0,0.2); /* 그림자 효과 */
  transition: box-shadow 0.3s ease-in-out, transform 0.3s ease-in-out;
}

:deep(.logo-hover:hover) {
  transform: scale(1.05); /* 호버 시 이미지 확대 */
}
</style>
