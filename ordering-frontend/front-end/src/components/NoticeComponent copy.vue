<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <h1>공지사항</h1>
        <v-list>
          <v-list-item v-for="(notice, index) in paginatedNotices" :key="index" @click="goToNoticeDetail(notice.id)">
            <v-list-item-content>
              <v-list-item-title>{{ notice.name }} {{ notice.start_date }}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <v-btn @click="showModal = true">글쓰기</v-btn>
          <EditorModal v-model="showModal" />
        </v-list>
        <v-pagination v-model="page" :length="totalPages" circle></v-pagination>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import EditorModal from "@/components/EditorModal.vue";
import { useRouter } from 'vue-router';

export default {
  components: {
    EditorModal,
  },
  setup() {
    const router = useRouter();
    const showModal = ref(false);
    const notices = ref([]);
    const page = ref(1);
    const noticesPerPage = 5;

    const goToNoticeDetail = (id) => {
      router.push({ name: 'NoticeDetail', params: { id } });
    };

    // 서버에서 공지사항 데이터를 불러오는 함수
    const fetchNotices = async () => {
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/notice-service/notices`);
        notices.value = response.data;
      } catch (error) {
        console.error("공지사항을 불러오는 중 에러 발생:", error);
      }
    };

    onMounted(fetchNotices);

    const totalPages = computed(() => {
      return Math.ceil(notices.value.length / noticesPerPage);
    });

    const paginatedNotices = computed(() => {
      const start = (page.value - 1) * noticesPerPage;
      const end = start + noticesPerPage;
      return notices.value.slice(start, end);
    });

    // 모달을 닫는 메소드
    const close = () => {
      showModal.value = false;
    };

    return {
      showModal,
      notices,
      page,
      totalPages,
      paginatedNotices,
      goToNoticeDetail,
      close,
    };
  },
};
</script>

<style scoped>
/* 여기에 스타일을 추가하세요 */
</style>