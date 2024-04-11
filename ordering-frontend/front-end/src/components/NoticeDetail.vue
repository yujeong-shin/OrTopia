<template>
    <div>
      <h1>{{ notice.name }}</h1>
      <img :src="notice.imagePath" alt="Notice Image" />
      <p>시작일: {{ notice.startDate }}</p>
      <p>종료일: {{ notice.endDate }}</p>
    </div>
  </template>
  
  <script setup>
  import { onMounted, ref } from 'vue';
  import { useRoute } from 'vue-router';
  import axios from 'axios';
  
  const route = useRoute();
  const notice = ref({});
  
  onMounted(async () => {
    try {
      const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/notice-service/notice/${route.params.id}`);
      notice.value = response.data;
    } catch (error) {
      console.error("공지사항 상세 정보를 불러오는 중 에러 발생:", error);
    }
  });
  </script>