<template>
    <v-app-bar app color="orange" dark>
      <!-- 로고 이미지 -->
      <v-img
        src="@/assets/logo.svg"
        class="my-3"
        contain
        height="40"
      ></v-img>
  
      <v-spacer></v-spacer>
  
      <!-- 알람 종 아이콘 -->
      <v-btn icon>
        <v-icon color="white">mdi-bell</v-icon>
      </v-btn>
  
      <!-- 장바구니 아이콘 -->
      <v-btn icon>
        <v-icon color="white">mdi-cart</v-icon>
      </v-btn>
  
      <!-- 내 정보 아이콘 -->
      <v-btn icon>
        <v-icon color="white">mdi-account-circle</v-icon>
      </v-btn>
    </v-app-bar>
  </template>
  
  <script setup>
  // Vue 3 "setup" 스크립트 태그 사용
  </script>
  
  <style scoped>
  </style>