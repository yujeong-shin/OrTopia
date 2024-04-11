<template>
  <v-dialog v-model="dialog" persistent max-width="600px">
    <v-card>
      <v-card-title class="justify-space-between">
        <span>글쓰기</span>
      </v-card-title>
      <v-card-text>
        <!-- 공지 이름 입력 -->
        <v-text-field
          v-model="notice.name"
          label="공지 이름"
          outlined
          dense
        ></v-text-field>
        
        <!-- 시작 날짜 선택 -->
        <v-menu
          v-model="startMenu"
          :close-on-content-click="false"
          :nudge-right="40"
          transition="scale-transition"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="notice.startDate"
              label="시작 날짜"
              prepend-icon="mdi-calendar"
              v-bind="attrs"
              v-on="on"
              outlined
              dense
            ></v-text-field>
          </template>
          <v-date-picker v-model="notice.startDate" @input="startMenu = false"></v-date-picker>
        </v-menu>

        <!-- 종료 날짜 선택 -->
        <v-menu
          v-model="endMenu"
          :close-on-content-click="false"
          :nudge-right="40"
          transition="scale-transition"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="notice.endDate"
              label="종료 날짜"
              prepend-icon="mdi-calendar"
              v-bind="attrs"
              v-on="on"
              outlined
              dense
            ></v-text-field>
          </template>
          <v-date-picker v-model="notice.endDate" @input="endMenu = false"></v-date-picker>
        </v-menu>

        <!-- 텍스트 에디터 -->
        <text-editor v-model="content" />
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="saveContent">저장하기</v-btn>
        <v-btn color="primary" @click="close">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
  
<script>
import TextEditor from "@/components/TextEditor.vue";
import axios from "axios";

export default {
  components: {
    TextEditor,
  },
  props: {
    value: Boolean,
  },
  emits: ['update:value'],
  data() {
    return {
      dialog: this.value,
      notice: {
        name: '',
        startDate: '',
        endDate: '',
      },
      selectedFile: null,
      content: '', // 'content'를 위한 초기 값 설정
      startMenu: false,
      endMenu: false,
    };
  },
  watch: {
    value(val) {
      this.dialog = val;
    },
    dialog(val) {
      this.$emit('update:value', val);
    }
  },
  methods: {
    close() {
      this.dialog = false;
    },
    handleFileChange(file) {
      if (file) {
        this.notice.imagePath = file.name;
      }
    },
    saveContent() {
      const formData = new FormData();
      formData.append("name", this.notice.name);
      formData.append("startDate", this.notice.startDate);
      formData.append("endDate", this.notice.endDate);
      formData.append("content", this.content); // 'content'도 서버로 전송
      if (this.selectedFile) {
        formData.append("file", this.selectedFile);
      }
      
      // 토큰 사용 예시, 실제 구현에서는 적절한 인증 헤더를 설정해야 합니다.
      const token = localStorage.getItem('accessToken');
      const refreshToken = localStorage.getItem('refreshToken');
      
      axios.post(`${process.env.VUE_APP_API_BASE_URL}/notice-service/create`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${token}`,
          "X-Refresh-Token": refreshToken,
        },
      })
      .then(response => {
        console.log(response.data);
        this.close();
      })
      .catch(error => {
        console.error("저장 중 에러 발생: ", error);
        if (error.response && error.response.data && error.response.data.error_message) {
          alert(error.response.data.error_message);
        }
      });
    },
  },
};
</script>