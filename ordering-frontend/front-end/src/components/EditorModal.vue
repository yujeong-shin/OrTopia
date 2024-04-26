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

        <v-select v-model="notice.category" :items="category" label="카테고리" required></v-select>
        
        <!-- 썸내일 올리기 -->
        <v-file-input label="썸네일 업로드" v-model="selectedFile" @change="handleFileUpload"></v-file-input>

        <!-- 시작 날짜 입력 -->
        <v-col cols="12">
          <input type="date" v-model="notice.startDate" class="date-input">
        </v-col>

        <!-- 종료 날짜 입력 -->
        <v-col cols="12">
          <input type="date" v-model="notice.endDate" class="date-input">
        </v-col>

        <!-- 텍스트 에디터 -->
        <text-editor v-model="contents" />
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
        category: '',
        startDate: '',
        endDate: '',
        contents: '',
      },
      category: ['EVENT','NOTICE','POPUP'],
      selectedFile: null,
      contents: '',
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
      window.location.reload();
    },
    handleFileUpload(event) {
        this.selectedFile = event.target.files[0];
    },
    saveContent() {
      const formData = new FormData();
      formData.append('imagePath', this.selectedFile);
      formData.append('name', this.notice.name);
      formData.append('category', this.notice.category);
      formData.append('startDate', this.notice.startDate);
      formData.append('endDate', this.notice.endDate);
      formData.append('contents', this.contents);

      const config = {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
          "X-Refresh-Token": localStorage.getItem('refreshToken'),
        },
      };

      axios.post(`${process.env.VUE_APP_API_BASE_URL}/notice-service/create`, formData, config)
        .then(response => {
          console.log(response.data);
          window.location.reload();
        })
        .catch(error => {
          console.error("저장 중 에러 발생: ", error);
        });
    },
  },
}
</script>

<style scoped>
.date-input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}
</style>