<template>
  <v-container>
      <v-col cols="12">
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col cols="12">
                <h1>공지사항</h1>
                <v-table>
                  <thead>
                    <tr>
                      <th class="text-center">번호</th>
                      <th class="text-center">카테고리</th>
                      <th class="text-center">시작일</th>
                      <th class="text-center">종료일</th>
                      <th class="text-center">제목</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(notice, index) in paginatedNotices" :key="index" @click="goToNoticeDetail(notice.id)">
                      <td class="text-center">{{ notice.id }}</td>
                      <td class="text-center">{{ notice.category }}</td>
                      <td class="text-center">{{ notice.startDate }}</td>
                      <td class="text-center">{{ notice.endDate }}</td>
                      <td class="text-center">{{ notice.name }}</td>
                    </tr>
                  </tbody>
                </v-table>
              </v-col>
            </v-row>
            <v-list>
              <v-btn @click="showModal = true">글쓰기</v-btn>
              <EditorModal v-model="showModal" />
            </v-list>
          </v-container>
        </v-card-text>
        <v-pagination v-model="page" :length="totalPages" circle></v-pagination>
      </v-col>
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
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/ortopia-notice-service/notices`);
        if (Array.isArray(response.data.result)) {
          // 삭제 되지 않은 공지만 불러옴. 실패
          notices.value = response.data.result;
        } 
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
      return Array.isArray(notices.value) ? notices.value.slice(start, end) : [];
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