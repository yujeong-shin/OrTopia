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
      <v-btn icon @click="goToSellPage">
        <v-icon color="white">mdi-cash-register</v-icon> <!-- 판매 아이콘 -->
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
import { ref, onMounted,computed } from 'vue';
import { useRouter } from 'vue-router';
// import {mapGetters} from 'vuex'
import { useStore } from 'vuex'

const router = useRouter();

const store = useStore();
const totalQuantity = computed(() => store.getters.getTotalQuantity);

const isLoggedIn = ref(false);

onMounted(() => {
  const token = localStorage.getItem('accessToken');
  isLoggedIn.value = !!token;
});

const logout = () => {
  localStorage.clear();
  isLoggedIn.value = false;
  router.push('/login'); // 로그아웃 후 로그인 페이지로 리다이렉트
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
  router.push('/sell'); // '/sell'은 판매 페이지의 경로로 가정합니다. 실제 경로에 맞게 조정해주세요.
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
