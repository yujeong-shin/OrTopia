<template>
  <v-container class="my-5 py-4">
    <v-card
      :class="{ 'gray-scale': noticeDeleted }"
      elevation="10"
      class="mx-auto"
      max-width="800"
    >
    <v-card-title class="headline grey lighten-2 text-center">
      {{ notice.name }}
    </v-card-title>
      <v-card-text>
        <v-row justify="center">
          <v-col cols="12" sm="6" class="text-center">
          <br>
            <v-subheader>시작일:  </v-subheader>{{ notice.startDate }}
          </v-col>
          <v-col cols="12" sm="6" class="text-center">
            <br>
            <v-subheader>종료일:  </v-subheader>{{ notice.endDate }}
          </v-col>
        </v-row>
        <v-divider class="my-4"></v-divider>
        <div v-html="notice.contents" class="body-1 text-center py-2"></div>
        <!-- 메시지 표시 -->
        <div v-if="noticeDeleted" class="text-center caption">
          종료된 이벤트입니다.
        </div>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn 
          color="red" 
          @click="deleteNotice" 
          v-if="isAdmin && !noticeDeleted"
          >삭제하기</v-btn
        >
        <v-btn color="primary" @click="goToNotifications">닫기</v-btn>
      </v-card-actions>
      <v-dialog v-model="dialog" persistent max-width="300px">
        <v-card>
          <v-card-title class="headline">삭제 확인</v-card-title>
          <v-card-text>정말로 이 공지사항을 삭제하시겠습니까?</v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="closeDialog">취소</v-btn>
            <v-btn color="red" text @click="confirmDelete">삭제</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-card>
  </v-container>
</template>

<script>
import axios from "axios";
import { useRouter } from "vue-router";

export default {
  setup() {
    const router = useRouter();
    const goToNotifications = () => {
      router.push("/notice");
    };
    return {
      goToNotifications,
    };
  },
  data() {
    return {
      notice: {},
      noticeDeleted: false,
      dialog: false,
      isAdmin: false,
    };
  },
  created() {
    this.fetchNotice();
    this.checkAdminRole();
  },
  methods: {
    deleteNotice() {
      this.dialog = true;
    },
    closeDialog() {
      this.dialog = false;
    },
    confirmDelete() {
      const config = {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          "X-Refresh-Token": localStorage.getItem("refreshToken"),
        },
      };
      axios
        .patch(
          `${process.env.VUE_APP_API_BASE_URL}/ortopia-notice-service/delete/${this.$route.params.id}`,
          null,
          config
        )
        .then(() => {
          this.noticeDeleted = true;
          this.dialog = false;
        })
        .catch((error) => {
          console.error("공지사항 삭제 중 에러 발생:", error);
        });
    },
    fetchNotice() {
      const config = {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          "X-Refresh-Token": localStorage.getItem("refreshToken"),
        },
      };
      axios
        .get(
          `${process.env.VUE_APP_API_BASE_URL}/ortopia-notice-service/notice/${this.$route.params.id}`,
          config
        )
        .then((response) => {
          this.notice = response.data.result;
          this.noticeDeleted = this.notice.delYN === false;
        })
        .catch((error) => {
          console.error("공지사항 상세 정보를 불러오는 중 에러 발생:", error);
        });
    },
    checkAdminRole() {
      // 로컬 스토리지에서 'role'을 가져와서 'ADMIN'인지 확인
      const role = localStorage.getItem("role");
      this.isAdmin = role === "ADMIN";
    },
  },
};
</script>
